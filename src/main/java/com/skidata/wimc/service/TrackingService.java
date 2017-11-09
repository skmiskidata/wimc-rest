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

    private static final int INVALIDATE_RESULT_AFTER = 2000;

    private static final int BEST_PLATE_CONFIDENCE = 94;

    private static final int REAL_TIME_BEST_CONFIDENCE = 94;

    private static final Logger logger = LoggerFactory.getLogger(MainResponseController.class);

    private Map<String, String> plateToUUID = new ConcurrentHashMap<>();

    private Map<String, String> bestPlateToUUID = new ConcurrentHashMap<>();

    private Map<String, Camera> cameras = new ConcurrentHashMap<>();

    private Map<String, UniqueCar> uniqueCars = new ConcurrentHashMap<>();

    private final PositionMapper mapper;

    public TrackingService() {
        addBrickcom();
        addRaspberryCamFenster();
        addRaspCam2();

        mapper = new LinearPositionMapper();
    }

    private void addRaspCam2() {
        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1066, 582), new Position(450, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(765, 578), new Position(450, 260)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(484, 548), new Position(450, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(417, 492), new Position(550, 580)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(532, 504), new Position(550, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(830, 421), new Position(810, 260)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(654, 420), new Position(810, 450)));


        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();
        Camera newCam = new Camera("899804908", new Position(790, 100), pixel2pos, area2Dists, 90.0); //brickcom
        cameras.put(newCam.getId(), newCam);
    }

    private void addRaspberryCamFenster() {
        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(300, 141), new Position(1050, 900)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(697, 160), new Position(800, 900)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1091, 201), new Position(550, 900)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1200, 212), new Position(490, 900)));

        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(138, 72), new Position(1300, 580)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(410, 73), new Position(1050, 580)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(704, 88), new Position(800, 580)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(997, 117), new Position(550, 580)));

        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(974, 102), new Position(550, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(702, 75), new Position(800, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(439, 60), new Position(1050, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(190, 58), new Position(1300, 450)));

        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();

        Camera newCam = new Camera("2103694419", new Position(790, 100), pixel2pos, area2Dists, 90.0); //brickcom
        cameras.put(newCam.getId(), newCam);

    }

    private void addBrickcom() {
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
//        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0, 0), 0, 5820, 340));
//        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0, 0), 0, 3810, 450));
//        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0, 0), 0, 2270, 560));
//        area2Dists.add(new CalibrationLPArea2Dist(new Pixel(0, 0), 0, 1310, 740));

        Camera newCam = new Camera("732045809", new Position(790, 100), pixel2pos, area2Dists, 90.0); //brickcom
        cameras.put(newCam.getId(), newCam);


        //newCam = new Camera("604916464", new Position(0, 0), pixel2pos, area2Dists, 90.0);
        //cameras.put(newCam.getId(), newCam);
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
                if ((systemTime - uniqueCar.getLastSeenAtPerCamera().get(cameraId)) > INVALIDATE_RESULT_AFTER) {
                    uniqueCar.getLastSeenAtPerCamera().remove(cameraId);
                    uniqueCar.getPositionPerCamera().remove(cameraId);
                    recalculate = true;
                }
            }

            if (recalculate) {
                Position oldBestPosition = uniqueCar.getBestPosition();
                int size = uniqueCar.getPositionPerCamera().values().size();
                Position newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[size]));

                if (newBestPosition == null) {
                    uniqueCars.remove(uuid);
                    bestPlateToUUID.remove(uniqueCar.getPlate());
                    plateToUUID.remove(uniqueCar.getPlate());
                    msg.add(new RemoveVehicle(uuid));
                } else if (!oldBestPosition.equals(newBestPosition)) {
                    uniqueCar.setBestPosition(newBestPosition);
                    msg.add(new MoveVehicle(uuid, newBestPosition.getX(), newBestPosition.getY(), uniqueCar.getPlate(), uniqueCar.getBestColor()));
                }
            }

        }
        return msg;
    }


    public List<Message> mapToRealWorld(AlprResult alpr) {
        List<Message> msg = new ArrayList<>();
        Camera camera = cameras.get(alpr.getCameraId());
        if (camera == null) {
            logger.warn("!!!!! Camera number " + alpr.getCameraId() + " not calibrated");
            return msg;
        }

        if (alpr.getResults() != null) {
            logger.info("processing live results");

            for (LicencePlate lp : alpr.getResults()) {

                logger.info("tracking lp " + lp.getPlate() + " with confidence " + lp.getConfidence());

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                if (lp.getConfidence() >= REAL_TIME_BEST_CONFIDENCE || (bestPlateToUUID.get(lp.getPlate()) != null)) {
                    Pixel pix = getPixel(lp.getPlateCoordinates());

                    Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), pix);
                    logger.info("lp={}, conf={}, px=({},{}) pos={}", lp.getPlate(), lp.getConfidence(), pix.getX(), pix.getY(), pos);

                    addMsg(msg, lp.getPlate(), pos, lp.getConfidence() >= REAL_TIME_BEST_CONFIDENCE, camera.getId(), null, null);
                }
            }

        }

        if (alpr.getBestPlate() != null) {
            if (alpr.getBestPlate().getConfidence() >= BEST_PLATE_CONFIDENCE) {

                logger.info("tracking best result for LP " + alpr.getBestPlate().getPlate() + " with confidence " + alpr.getBestPlate().getConfidence());

                LicencePlate lp = alpr.getBestPlate();

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                Pixel pix = getPixel(lp.getPlateCoordinates());
                Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), pix);
                logger.info("lp={}, conf={}, px=({},{}) pos={}", alpr.getBestPlate().getPlate(), alpr.getBestPlate().getConfidence(), pix.getX(), pix.getY(), pos);

                String color = null;
                Float colorConfidence = 0f;
                if (alpr.getVehicle() != null && alpr.getVehicle().getColors() != null && alpr.getVehicle().getColors().length > 0) {
                    color = alpr.getVehicle().getColors()[0].getName();
                    colorConfidence = alpr.getVehicle().getColors()[0].getConfidence();
                }

                addMsg(msg, alpr.getBestPlate().getPlate(), pos, true, camera.getId(), color, colorConfidence);
            }
        }

        return msg;
    }

    private Pixel getPixel(PlateCoordinate[] plateCoordinates) {
        int x = 0;
        int y = 0;
        for (PlateCoordinate c : plateCoordinates) {
            x = (int) (x + c.getX());
            y = (int) (y + c.getY());
        }
        return new Pixel(x / 4, y / 4);
    }

    private void addMsg(List<Message> msg, String plate, Position newPos, boolean bestPlate, String cameraId, String color, Float colorConfidence) {
        String uuid = plateToUUID.get(plate);
        if (null == uuid) {
            uuid = UUID.randomUUID().toString();
            plateToUUID.put(plate, uuid);
            logger.info("ADDMSG: new uuid created " + uuid + " LP: " + plate);
        }

        String bestUuid = bestPlateToUUID.get(plate);
        if (bestPlate && (bestUuid == null)) {
            bestPlateToUUID.put(plate, uuid);
            logger.info("ADDMSG: new best plate reported " + bestUuid + " LP: " + plate);
        }

        UniqueCar uniqueCar = uniqueCars.get(uuid);
        if (uniqueCar == null) {
            uniqueCar = new UniqueCar();
            uniqueCar.setPlate(plate);
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());
            uniqueCar.setBestPosition(newPos);
            uniqueCar.setBestColor(color);
            uniqueCar.setBestColorConfidence(colorConfidence == null ? 0f : colorConfidence);

            uniqueCars.put(uuid, uniqueCar);

            logger.info("ADDMSG: new unique car created " + uuid + " LP: " + plate);

            msg.add(new InitVehicle(uuid, newPos.getX(), newPos.getY(), plate, uniqueCar.getBestColor()));
        } else {
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());

            logger.info("ADDMSG: updating old unique car " + uuid + " LP: " + plate);

            if (colorConfidence != null && colorConfidence > uniqueCar.getBestColorConfidence()) {
                uniqueCar.setBestColor(color);
                uniqueCar.setBestColorConfidence(colorConfidence);

                logger.info("ADDMSG: updating old color with higher confidence " + uuid + " LP: " + plate + " color: " + color + " conf: " + colorConfidence);
            }

            Position oldBestPosition = uniqueCar.getBestPosition();
            int size = uniqueCar.getPositionPerCamera().values().size();
            Position newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[size]));

            if (!oldBestPosition.equals(newBestPosition)) {
                uniqueCar.setBestPosition(newBestPosition);
                logger.info("ADDMSG: moving vehicle with new best postition. LP: " + plate);
                msg.add(new MoveVehicle(uuid, newBestPosition.getX(), newBestPosition.getY(), plate, uniqueCar.getBestColor()));
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

    public static double distance(Position pos1, Position pos2) {
        return Math.hypot(pos1.getX() - pos2.getX(), pos1.getY() - pos2.getY());
    }

}
