package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.*;
import com.skidata.wimc.tracking.impl.CalibrationLPArea2Dist;
import com.skidata.wimc.tracking.impl.CalibrationPixel2Pos;
import com.skidata.wimc.tracking.impl.LinearPositionMapper;
import com.skidata.wimc.tracking.impl.MappingContext;
import com.skidata.wimc.tracking.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class TrackingService {

    private static final int INVALIDATE_RESULT_AFTER = 2500;

    private static final int BEST_PLATE_CONFIDENCE = 94;

    private static final int REAL_TIME_BEST_CONFIDENCE = 94;

    private static final int DISTANCE_TO_CONSIDER = 100;

    private static final int DISTANCE_TO_MOVE = 0;

    private static final Logger logger = LoggerFactory.getLogger(MainResponseController.class);

    private static final Map<String, Camera> cameras = new ConcurrentHashMap<>();

    private final PositionMapper mapper;

    public TrackingService() {
        addBrickcom();
        addRaspberryCamFenster();
        addRaspCam2();

        mapper = new LinearPositionMapper();
    }

    private void addRaspCam2() {
        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1055, 573), new Position(430, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(750, 573), new Position(430, 200)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(476, 573), new Position(430, 340)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1010, 500), new Position(530, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(780, 500), new Position(530, 200)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(550, 500), new Position(530, 340)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(988, 421), new Position(760, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(824, 421), new Position(760, 200)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(648, 421), new Position(760, 340)));


        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();
//        Camera newCam = new Camera("899804908", new Position(300, 160), pixel2pos, area2Dists, 180.0/Math.PI*(+75.0));
        Camera newCam = new Camera("899804908", new Position(0,0), pixel2pos, area2Dists, Math.PI/180.0*15.0);
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

       // Camera newCam = new Camera("2103694419", new Position(790, 1450), pixel2pos, area2Dists, 180.0);
        Camera newCam = new Camera("2103694419", new Position(0, 0), pixel2pos, area2Dists, 0.0);
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

//        Camera newCam = new Camera("732045809", new Position(790, 100), pixel2pos, area2Dists, 0.0); //brickcom
        Camera newCam = new Camera("732045809", new Position(0, 0), pixel2pos, area2Dists, 0.0); //brickcom
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
        int i = 0;
        ArrayList<UniqueCar> platesToRemove = new ArrayList();
        for (UniqueCar uniqueCar : SingletonDB.getInstance().getUniqueCars()) {
            i++;
            logger.info("Cleaning car no. " + i + "; " + uniqueCar.getPlate() + " - " + uniqueCar.getUuid());

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
                Position newBestPosition = null;
                if (size > 0) {
                    newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[size]));
                }

                if (newBestPosition == null) {
                    platesToRemove.add(uniqueCar);
                    msg.add(new RemoveVehicle(uniqueCar.getUuid()));
                } else if (!oldBestPosition.equals(newBestPosition)) {
                    uniqueCar.setBestPosition(newBestPosition);
                    msg.add(new MoveVehicle(uniqueCar.getUuid(), newBestPosition.getX(), newBestPosition.getY(), uniqueCar.getPlate(), uniqueCar.getBestColor()));
                }
            }

        }

        for (UniqueCar uniqueCar : platesToRemove) {
            SingletonDB.getInstance().getUniqueCars().remove(uniqueCar);
        }
        return msg;
    }


    public UniqueCar findUniqueCarByPlate(String plate) {
        for (UniqueCar uniqueCar : SingletonDB.getInstance().getUniqueCars()) {
            if (uniqueCar.getPlate().equalsIgnoreCase(plate)) {
                return uniqueCar;
            }
        }
        return null;
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

                logger.info("tracking realtime lp " + lp.getPlate() + " with confidence " + lp.getConfidence());

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                if (lp.getConfidence() >= REAL_TIME_BEST_CONFIDENCE || (findUniqueCarByPlate(lp.getPlate()) != null)) {
//                    if ("SL620NU".equals(lp.getPlate())) {
                        Pixel pix = getPixel(lp.getPlateCoordinates());

                        Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), pix);
                        logger.info("lp={}, conf={}, px=({},{}) pos={}", lp.getPlate(), lp.getConfidence(), pix.getX(), pix.getY(), pos);

                        addMsg(msg, lp.getPlate(), pos, lp.getConfidence() >= REAL_TIME_BEST_CONFIDENCE, camera.getId(), null, null, lp.getConfidence());
//                    }
                }
            }

        }

        if (alpr.getBestPlate() != null) {
            String color = null;
            Float colorConfidence = 0f;
            // we update the color even the confidence for the group is lower as our treshold
            if (alpr.getVehicle() != null && alpr.getVehicle().getColors() != null && alpr.getVehicle().getColors().length > 0) {
                color = alpr.getVehicle().getColors()[0].getName();
                colorConfidence = alpr.getVehicle().getColors()[0].getConfidence();
                updateCarColor(alpr.getBestPlate().getPlate(), color, colorConfidence);
            }
            if (alpr.getBestPlate().getConfidence() >= BEST_PLATE_CONFIDENCE) {

                logger.info("tracking best result for LP " + alpr.getBestPlate().getPlate() + " with confidence " + alpr.getBestPlate().getConfidence());

                LicencePlate lp = alpr.getBestPlate();

                long w = Math.abs(lp.getPlateCoordinates()[1].getX() - lp.getPlateCoordinates()[0].getX());
                long h1 = Math.abs(lp.getPlateCoordinates()[2].getY() - lp.getPlateCoordinates()[1].getY());
                long h2 = Math.abs(lp.getPlateCoordinates()[3].getY() - lp.getPlateCoordinates()[0].getY());

                Pixel pix = getPixel(lp.getPlateCoordinates());
                Position pos = mapper.mapPixelToRealWorld(new MappingContext(camera, w, h1, h2, lp.getPlate()), pix);
                logger.info("lp={}, conf={}, px=({},{}) pos={}", alpr.getBestPlate().getPlate(), alpr.getBestPlate().getConfidence(), pix.getX(), pix.getY(), pos);

                addMsg(msg, alpr.getBestPlate().getPlate(), pos, true, camera.getId(), color, colorConfidence, alpr.getBestPlate().getConfidence());
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


    public UniqueCar findBestMatchingPositionHoweverNotForExistingPlate(String plate, Position position) {
        for (UniqueCar uniqueCar : SingletonDB.getInstance().getUniqueCars()) {
            double distance = distance(position, uniqueCar.getBestPosition());
            if (distance < DISTANCE_TO_CONSIDER && !uniqueCar.getPlate().equalsIgnoreCase(plate)) {
                return uniqueCar;
            }
        }
        return null;
    }


    private void updateCarColor(String plate, String color, Float colorConfidence) {
        UniqueCar uniqueCar = findUniqueCarByPlate(plate);
        if (uniqueCar != null) {
            if (colorConfidence != null && colorConfidence > uniqueCar.getBestColorConfidence()) {
                uniqueCar.setBestColor(color);
                uniqueCar.setBestColorConfidence(colorConfidence);
                logger.info("######  updating old color with higher confidence " + uniqueCar.getUuid() + " LP: " + plate + " color: " + color + " conf: " + colorConfidence);
            }
        }
    }

    private void addMsg(List<Message> msg, String foundPlate, Position newPos, boolean bestPlate, String cameraId, String color, Float colorConfidence, Float currentConfidence) {
        String plate = foundPlate;
        UniqueCar uniqueCar = null;
        if (bestPlate) {
            // merging plates if better comes along in the same area
            uniqueCar = findBestMatchingPositionHoweverNotForExistingPlate(plate, newPos);
            if (uniqueCar != null) {
                logger.info("check old plate to rename......");
                if (uniqueCar.getHighestConfidenceAchieved() < currentConfidence) {
                    // change licence plate number
                    uniqueCar.setPlate(plate);
                    logger.info("... changing plate to " + plate + " due to higher confidence on the same area in the 2D map");
                    msg.add(new ChangeLP(uniqueCar.getUuid(), plate));
                } else {
                    // its better to ignore this result as to have another licence plate in the same spot
                    logger.info("... using old plate " + uniqueCar.getPlate() + " due to position collision");
                    plate = uniqueCar.getPlate();
                }
            }
        }

        if (uniqueCar == null) {
            uniqueCar = findUniqueCarByPlate(plate);
        }

        if ((uniqueCar == null) && (bestPlate)) {
            logger.info("#################### I SEE NEW PLATE ######### " + plate);
            uniqueCar = new UniqueCar();
            uniqueCar.setPlate(plate);
            String uuid = UUID.randomUUID().toString();
            uniqueCar.setUuid(uuid);
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());
            uniqueCar.setBestPosition(newPos);
            uniqueCar.setBestColor(color);
            uniqueCar.setBestColorConfidence(colorConfidence == null ? 0f : colorConfidence);

            // we should never add the same licence plate twice... however it happens for some reason
            SingletonDB.getInstance().getUniqueCars().add(uniqueCar);

            logger.info("ADDMSG: new unique car created " + uniqueCar.getUuid() + " LP: " + plate);

            msg.add(new InitVehicle(uuid, newPos.getX(), newPos.getY(), plate, uniqueCar.getBestColor()));
        } else {
            uniqueCar.getPositionPerCamera().put(cameraId, newPos);
            uniqueCar.getLastSeenAtPerCamera().put(cameraId, System.currentTimeMillis());

            logger.info("ADDMSG: updating old unique car " + uniqueCar.getUuid() + " LP: " + plate);

            Position oldBestPosition = uniqueCar.getBestPosition();
            int size = uniqueCar.getPositionPerCamera().values().size();
            Position newBestPosition = calculateCentroid(uniqueCar.getPositionPerCamera().values().toArray(new Position[size]));

            if (!oldBestPosition.equals(newBestPosition)) {
                uniqueCar.setBestPosition(newBestPosition);
                logger.info("ADDMSG: moving vehicle with new best positition. LP: " + plate);
                msg.add(new MoveVehicle(uniqueCar.getUuid(), newBestPosition.getX(), newBestPosition.getY(), plate, uniqueCar.getBestColor()));
            }

        }
        if (bestPlate || uniqueCar.getHighestConfidenceAchieved() == null) {
            uniqueCar.setLastBestLPConfidence(currentConfidence);
            if (uniqueCar.getHighestConfidenceAchieved() == null || uniqueCar.getHighestConfidenceAchieved() < currentConfidence) {
                uniqueCar.setHighestConfidenceAchieved(currentConfidence);
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
