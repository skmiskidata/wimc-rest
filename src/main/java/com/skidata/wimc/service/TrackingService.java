package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.*;
import com.skidata.wimc.tracking.impl.LinearPositionMapper;
import com.skidata.wimc.tracking.messages.InitVehicle;
import com.skidata.wimc.tracking.messages.Message;
import com.skidata.wimc.tracking.messages.MoveVehicle;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class TrackingService {

    private static final Logger logger = LoggerFactory.getLogger(MainResponseController.class);

    private Map<String, String> plateToUUID = new ConcurrentHashMap<>();
    private Map<String, Position> uuidToPos = new ConcurrentHashMap<>();

    private final Camera brickcom;
    private final PositionMapper mapper;

    public TrackingService () {
        Map<Pixel, Position> cal = new HashMap<>();
        cal.put(new Pixel(100, 420), new Position(300,560));
        cal.put(new Pixel(1080, 441), new Position(1050,560));
        cal.put(new Pixel(103, 618), new Position(300,350));
        cal.put(new Pixel(1190, 578), new Position(1050,410));

        brickcom = new Camera("1", new Position(0, 0), cal);
        mapper = new LinearPositionMapper();
    }

    @GetMapping("/carpark")
    public CarPark getCarPark() {
        return new CarPark();
    }


    public List<Message> mapToRealWorld(AlprResult alpr) {
        List<Message> msg = new ArrayList<>();

        for (LicencePlate lp : alpr.getResults()) {
            if (lp.getConfidence() >= 85) {
                logger.info("lp="+lp.getPlate());
                int x=0;
                int y=0;
                for (PlateCoordinate c : lp.getPlateCoordinates()) {
                    x = (int) (x + c.getX());
                    y = (int) (y + c.getY());
                }

                Position pos = mapper.mapPixelToRealWorld(brickcom, new Pixel(x/4, y/4));

                addMsg(msg, lp.getPlate(), pos);
            }
        }

        return msg;
    }

    private void addMsg(List<Message> msg, String plate, Position newPos) {
        String uuid = plateToUUID.get(plate);
        if (null == uuid) {
            uuid = UUID.randomUUID().toString();
            plateToUUID.put(plate, uuid);
        }

        Position oldPos = uuidToPos.get(uuid);
        if (oldPos == null) {
            msg.add(new InitVehicle(uuid, newPos.getX(), newPos.getY(), plate));
        }
        else {
            if (!oldPos.equals(newPos)) {
                msg.add(new MoveVehicle(uuid,  newPos.getX(), newPos.getY(), plate));
            }

        }
        uuidToPos.put(uuid, newPos);
    }
}
