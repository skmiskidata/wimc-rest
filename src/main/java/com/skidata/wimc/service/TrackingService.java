package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.*;
import com.skidata.wimc.tracking.impl.CalibrationLPArea2Dist;
import com.skidata.wimc.tracking.impl.CalibrationPixel2Pos;
import com.skidata.wimc.tracking.impl.LinearPositionMapper;
import com.skidata.wimc.tracking.impl.MappingContext;
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

    private Map<String, String> bestPlateToUUID = new ConcurrentHashMap<>();
    //private Map<String, Long> lastSeenAt = new ConcurrentHashMap<>();

    private Map<String, Camera> cameras = new ConcurrentHashMap<>();

    private Map<String, UniqueCar> uniqueCars = new ConcurrentHashMap<>();

    private final PositionMapper mapper;

    public TrackingService() {
        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(82, 325), new Position(300, 700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(82, 364), new Position(300, 600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(3, 424), new Position(300, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(346, 325), new Position(550, 700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(346, 369), new Position(550, 600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(253, 443), new Position(550, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(712, 325), new Position(800, 700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(712, 373), new Position(800, 600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(727, 464), new Position(800, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1084, 325), new Position(1050, 700)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1084, 385), new Position(1050, 600)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1176, 471), new Position(1050, 450)));

        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();
        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0,0), 0, 5820, 340));
        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0,0), 0, 3810, 450));
        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0,0),  0, 2270, 560));
        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0,0), 0, 1310, 740));

        Camera newCam = new Camera("732045809", new Position(790, 100), pixel2pos, area2Dists, 90.0); //brickcom
        cameras.put(newCam.getId(), newCam);

        newCam = new Camera("2103694419", new Position(0, 0), pixel2pos, area2Dists, 90.0);
        cameras.put(newCam.getId(), newCam);

        newCam = new Camera("604916464", new Position(0, 0), pixel2pos, area2Dists, 90.0);
        cameras.put(newCam.getId(), newCam);

        newCam = new Camera("899804908", new Position(0, 0), pixel2pos, area2Dists, 90.0);
        cameras.put(newCam.getId(), newCam);


        mapper = new LinearPositionMapper();
    }

    @GetMapping("/carpark")
    public CarPark getCarPark() {
        return new CarPark();
    }

    public List<Message> checkForVanished() {
        List<Message> msg = new ArrayList<>();
        long systemTime = System.currentTimeMillis();
        for (String uuid : uniqueCars.keySet()) {

            UniqueCar uniqueCar = uniqueCars.get(uuid);

            boolean recalculate = false;
            // remove all old licence plate recognitions
            for (String cameraId : uniqueCar.getLastSeenAtPerCamera().keySet()) {
                if ((systemTime - uniqueCar.getLastSeenAtPerCamera().get(cameraId)) > 1500) {
                    uniqueCar.getLastSeenAtPerCamera().remove(cameraId);
                    uniqueCar.getPositionPerCamera().remove(cameraId);
                    recalculate = true;
                }
            }

            if (recalculate) {
                Position oldBestPosition = uniqueCar.getBestPosition();
                Position newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[0]));

                if (newBestPosition == null) {
                    uniqueCars.remove(uuid);
                    bestPlateToUUID.remove(uniqueCar.getPlate());
                    plateToUUID.remove(uniqueCar.getPlate());
                    msg.add(new RemoveVehicle(uuid));
                } else if (!oldBestPosition.equals(newBestPosition)) {
                    uniqueCar.setBestPosition(newBestPosition);
                    msg.add(new MoveVehicle(uuid, newBestPosition.getX(), newBestPosition.getY(), uniqueCar.getPlate()));
                }
            }

        }
        return msg;
    }


    public List<Message> mapToRealWorld(AlprResult alpr) {
        List<Message> msg = new ArrayList<>();
        Camera camera = cameras.get(alpr.getCameraId());
        if (camera == null) {
            logger.warn("Camera number " + alpr.getCameraId() + " not calibrated");
            return msg;
        }

        if (alpr.getResults() != null) {

            for (LicencePlate lp : alpr.getResults()) {

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                //logger.info("lp={}, conf={}, lpw={}, lph={}, lparea={}", lp.getPlate(), lp.getConfidence(), w, h, w*h);
                if (lp.getConfidence() >= 95 || (bestPlateToUUID.get(lp.getPlate()) != null)) {

                    //lastSeenAt.put(lp.getPlate(), System.currentTimeMillis());

                    int x = 0;
                    int y = 0;
                    for (PlateCoordinate c : lp.getPlateCoordinates()) {
                        x = (int) (x + c.getX());
                        y = (int) (y + c.getY());
                    }

                    Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), new Pixel(x / 4, y / 4));
                    logger.info("lp={}, conf={}, px=({},{}) pos={}", lp.getPlate(), lp.getConfidence(), x / 4, y / 4, pos);

                    addMsg(msg, lp.getPlate(), pos, lp.getConfidence() >= 94, camera.getId());
                }
            }

        }

        if (alpr.getBestPlate() != null) {
            if (alpr.getBestPlate().getConfidence() >= 94) {

                LicencePlate lp = alpr.getBestPlate();

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                // lastSeenAt.put(alpr.getBestPlate().getPlate(), System.currentTimeMillis());

                int x = 0;
                int y = 0;
                for (PlateCoordinate c : alpr.getBestPlate().getPlateCoordinates()) {
                    x = (int) (x + c.getX());
                    y = (int) (y + c.getY());
                }

                Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), new Pixel(x / 4, y / 4));
                logger.info("lp={}, conf={}, px=({},{}) pos={}", alpr.getBestPlate().getPlate(), alpr.getBestPlate().getConfidence(), x / 4, y / 4, pos);

                addMsg(msg, alpr.getBestPlate().getPlate(), pos, true, camera.getId());
            }
        }

        return msg;
    }

    private void addMsg(List<Message> msg, String plate, Position newPos, boolean bestPlate, String cameraId) {
        String uuid = plateToUUID.get(plate);
        if (null == uuid) {
            uuid = UUID.randomUUID().toString();
            plateToUUID.put(plate, uuid);
        }

        String bestUuid = bestPlateToUUID.get(plate);
        if (bestPlate && (bestUuid == null)) {
            bestPlateToUUID.put(plate, uuid);
        }

        UniqueCar uniqueCar = uniqueCars.get(uuid);
        if (uniqueCar == null) {
            uniqueCar = new UniqueCar();
            uniqueCar.setPlate(plate);
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());
            uniqueCar.setBestPosition(newPos);

            uniqueCars.put(uuid, uniqueCar);

            msg.add(new InitVehicle(uuid, newPos.getX(), newPos.getY(), plate));
        } else {
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());

            Position oldBestPosition = uniqueCar.getBestPosition();
            Position newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[0]));

            if (!oldBestPosition.equals(newBestPosition)) {
                uniqueCar.setBestPosition(newBestPosition);
                msg.add(new MoveVehicle(uuid, newBestPosition.getX(), newBestPosition.getY(), plate));
            }

        }
    }


    public Position calculateCentroid(Position[] positions) {
        if (positions.length == 0) {
            return null;
        }
        if (positions.length == 1) {
            return positions[0];
        }
        int sumofx = 0, sumofy = 0;
        for (int i = 0; i < positions.length; i++) {
            sumofx = sumofx + positions[i].getX();
            sumofy = sumofy + positions[i].getY();
        }
        return new Position(sumofx / positions.length, sumofy / positions.length);
    }
}
