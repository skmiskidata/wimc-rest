package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.*;
import com.skidata.wimc.tracking.impl.CalibrationValues;
import com.skidata.wimc.tracking.impl.LinearPositionMapper;
import com.skidata.wimc.tracking.messages.InitVehicle;
import com.skidata.wimc.tracking.messages.Message;
import com.skidata.wimc.tracking.messages.MoveVehicle;
import com.skidata.wimc.tracking.messages.RemoveVehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class TrackingService {

    private static final Logger logger = LoggerFactory.getLogger(MainResponseController.class);

    private Map<String, String> plateToUUID = new ConcurrentHashMap<>();
    private Map<String, Position> uuidToPos = new ConcurrentHashMap<>();

    private Map<String, String> bestPlateToUUID = new ConcurrentHashMap<>();
    private Map<String, Long> lastSeenAt = new ConcurrentHashMap<>();

    private final Camera brickcom;
    private final PositionMapper mapper;

    public TrackingService() {
        Set<CalibrationValues> cal = new HashSet<>();
        cal.add(new CalibrationValues(new Pixel(82, 325), new Position(300, 700), 2, 2));
        cal.add(new CalibrationValues(new Pixel(82, 364), new Position(300, 600), 2, 2));
        cal.add(new CalibrationValues(new Pixel(3, 424), new Position(300, 450), 2, 2));
        cal.add(new CalibrationValues(new Pixel(346, 325), new Position(550, 700), 2, 2));
        cal.add(new CalibrationValues(new Pixel(346, 369), new Position(550, 600), 2, 2));
        cal.add(new CalibrationValues(new Pixel(253, 443), new Position(550, 450), 2, 2));
        cal.add(new CalibrationValues(new Pixel(712, 325), new Position(800, 700), 2, 2));
        cal.add(new CalibrationValues(new Pixel(712, 373), new Position(800, 600), 2, 2));
        cal.add(new CalibrationValues(new Pixel(727, 464), new Position(800, 450), 2, 2));
        cal.add(new CalibrationValues(new Pixel(1084, 325), new Position(1050, 700), 2, 2));
        cal.add(new CalibrationValues(new Pixel(1084, 385), new Position(1050, 600), 2, 2));
        cal.add(new CalibrationValues(new Pixel(1176, 471), new Position(1050, 450), 2, 2));

        brickcom = new Camera("1", new Position(0, 0), cal);
        mapper = new LinearPositionMapper();
    }

    @GetMapping("/carpark")
    public CarPark getCarPark() {
        return new CarPark();
    }

    public List<Message> checkForVanished() {
        List<Message> msg = new ArrayList<>();
        long systemTime = System.currentTimeMillis();
        for (String plate : plateToUUID.keySet()) {
            if (!lastSeenAt.containsKey(plate) || (systemTime - lastSeenAt.get(plate) > 3000)) {
                // we do remove
                String uuid = plateToUUID.get(plate);
                lastSeenAt.remove(plate);
                bestPlateToUUID.remove(plate);
                plateToUUID.remove(plate);
                msg.add(new RemoveVehicle(uuid));
            }
        }
        return msg;
    }


    public List<Message> mapToRealWorld(AlprResult alpr) {
        List<Message> msg = new ArrayList<>();

        if (alpr.getResults() != null) {

            for (LicencePlate lp : alpr.getResults()) {
                long w = lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX();
                long h = lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY();

                //logger.info("lp={}, conf={}, lpw={}, lph={}, lparea={}", lp.getPlate(), lp.getConfidence(), w, h, w*h);
                if (lp.getConfidence() >= 94 || (bestPlateToUUID.get(lp.getPlate()) != null)) {

                    lastSeenAt.put(lp.getPlate(), System.currentTimeMillis());

                    int x = 0;
                    int y = 0;
                    for (PlateCoordinate c : lp.getPlateCoordinates()) {
                        x = (int) (x + c.getX());
                        y = (int) (y + c.getY());
                    }

                    Position pos = mapper.mapPixelToRealWorld(brickcom, new Pixel(x / 4, y / 4));
                    logger.info("lp={}, conf={}, px=({},{}) pos={}", lp.getPlate(), lp.getConfidence(), x / 4, y / 4, pos);

                    addMsg(msg, lp.getPlate(), pos, lp.getConfidence() >= 94);
                }
            }

        }

        if (alpr.getBestPlate() != null) {
            if (alpr.getBestPlate().getConfidence() >= 94) {

                lastSeenAt.put(alpr.getBestPlate().getPlate(), System.currentTimeMillis());

                int x = 0;
                int y = 0;
                for (PlateCoordinate c : alpr.getBestPlate().getPlateCoordinates()) {
                    x = (int) (x + c.getX());
                    y = (int) (y + c.getY());
                }

                Position pos = mapper.mapPixelToRealWorld(brickcom, new Pixel(x / 4, y / 4));
                logger.info("lp={}, conf={}, px=({},{}) pos={}", alpr.getBestPlate().getPlate(), alpr.getBestPlate().getConfidence(), x / 4, y / 4, pos);

                addMsg(msg, alpr.getBestPlate().getPlate(), pos, true);
            }
        }


        return msg;
    }

    private void addMsg(List<Message> msg, String plate, Position newPos, boolean bestPlate) {
        String uuid = plateToUUID.get(plate);
        if (null == uuid) {
            uuid = UUID.randomUUID().toString();
            plateToUUID.put(plate, uuid);
        }

        String bestUuid = bestPlateToUUID.get(plate);
        if (bestPlate && (bestUuid == null)) {
            bestPlateToUUID.put(plate, uuid);
        }

        Position oldPos = uuidToPos.get(uuid);
        if (oldPos == null) {
            msg.add(new InitVehicle(uuid, newPos.getX(), newPos.getY(), plate));
        } else {
            if (!oldPos.equals(newPos)) {
                msg.add(new MoveVehicle(uuid, newPos.getX(), newPos.getY(), plate));
            }

        }
        uuidToPos.put(uuid, newPos);
    }
}
