package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.*;
import com.skidata.wimc.tracking.impl.*;
import com.skidata.wimc.tracking.messages.InitVehicle;
import com.skidata.wimc.tracking.messages.Message;
import com.skidata.wimc.tracking.messages.MoveVehicle;
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

    private final Camera brickcom;
    private final PositionMapper mapper;

    public TrackingService () {
        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(82, 325), new Position(300,700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(82, 364), new Position(300,600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(3, 424), new Position(300,450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(346, 325), new Position(550,700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(346, 369), new Position(550,600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(253, 443), new Position(550,450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(712, 325), new Position(800,700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(712, 373), new Position(800,600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(727, 464), new Position(800,450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1084, 325), new Position(1050,700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1084, 385), new Position(1050,600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1176, 471), new Position(1050,450)));

        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();
        area2Dists.add(new CalibrationLPArea2Dist(5820, 340));
        area2Dists.add(new CalibrationLPArea2Dist(3810, 450));
        area2Dists.add(new CalibrationLPArea2Dist(2270, 560));
        area2Dists.add(new CalibrationLPArea2Dist(1310, 740));

        brickcom = new Camera("1", new Position(0, 0), pixel2pos, area2Dists, 90.0);
        mapper = new LinearPositionMapper();
    }

    @GetMapping("/carpark")
    public CarPark getCarPark() {
        return new CarPark();
    }


    public List<Message> mapToRealWorld(AlprResult alpr) {
        List<Message> msg = new ArrayList<>();

        for (LicencePlate lp : alpr.getResults()) {
            long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
            long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
            long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

            //logger.info("lp={}, conf={}, lpw={}, lph={}, lparea={}", lp.getPlate(), lp.getConfidence(), w, h, w*h);
            if (lp.getConfidence() >= 89) {
                int x=0;
                int y=0;
                for (PlateCoordinate c : lp.getPlateCoordinates()) {
                    x = (int) (x + c.getX());
                    y = (int) (y + c.getY());
                }
                Pixel pix = new Pixel(x/4, y/4);
                if ("SL620NU".equals(lp.getPlate())) {
                    MappingContext ctx = new MappingContext(brickcom, w, h1, h2, lp.getPlate());
                    new LPAreaPositionMapper().mapPixelToRealWorld(ctx, pix);
                    Position pos = mapper.mapPixelToRealWorld(ctx, pix);
                    logger.info("lp={}, conf={}, px=({},{}) pos={}", lp.getPlate(), lp.getConfidence(), x/4, y/4, pos);

                    addMsg(msg, lp.getPlate(), pos);
                }
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
