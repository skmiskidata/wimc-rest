package com.skidata.wimc.service;

import com.skidata.wimc.domain.Bounding;
import com.skidata.wimc.domain.CarPosition;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.sighthound.client.ISighthoundProcessResult;
import com.skidata.wimc.sighthound.client.SighthoundProcessorService;
import com.skidata.wimc.sighthound.client.domain.SightResult;
import com.skidata.wimc.sighthound.client.domain.Vertice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mihael on 4.11.2017.
 */
@ConfigurationProperties(prefix = "recognition.service")
@Service
public class ImageRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageRecognitionService.class);

    @NotNull
    private boolean sightEnabled;

    private List<String> webcams = new ArrayList<String>();

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SighthoundProcessorService sighthoundProcessorService;

    @PostConstruct
    public void pollCameras() {
        if (isSightEnabled()) {
            for (String cameraUrl : getWebcams()) {
                try {
                    logger.info("Polling Camera URL : " + cameraUrl);
                    sighthoundProcessorService.initializeCameraStream(new URL(cameraUrl), new SightHoundTransformAndProcess());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SightHoundTransformAndProcess implements ISighthoundProcessResult {

        @Override
        public void processResult(SightResult sightResult) {
            ArrayList carPositions = mapSightResults(sightResult);
            Iterator iterator = carPositions.iterator();
            while (iterator.hasNext()) {
                CarPosition carPosition = (CarPosition) iterator.next();
                template.convertAndSend("/topic/debugOut", carPosition);
                logger.info("Got object: \n" + carPosition.toString());
            }
        }

    }

    private static ArrayList mapSightResults(SightResult sightResult) {
        if (sightResult == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (com.skidata.wimc.sighthound.client.domain.Object object : sightResult.getObjects()) {

            if (object.getVehicleAnnotation() != null) {
                CarPosition carPosition = new CarPosition();
                carPosition.setDraw(true);
                carPosition.setUuid(object.getObjectId());
                if (object.getVehicleAnnotation().getLicenseplate() != null) {
                    if (object.getVehicleAnnotation().getLicenseplate().getAttributes() != null) {
                        carPosition.setLicencePlate(object.getVehicleAnnotation().getLicenseplate().getAttributes().getSystemLP().getString().getName());
                        carPosition.setLicencePlateConfidence(object.getVehicleAnnotation().getLicenseplate().getAttributes().getSystemLP().getString().getConfidence());
                    }
                    carPosition.setPlateCoordinates(mapPlateCoordinates(object.getVehicleAnnotation().getLicenseplate().getBounding()));
                    carPosition.setLicencePlateBounding(mapBounding(object.getVehicleAnnotation().getLicenseplate().getBounding()));
                }
                carPosition.setCameraSource(sightResult.getRequestId());
                carPosition.setCarMake(object.getVehicleAnnotation().getAttributes().getSystem().getMake().getName());
                carPosition.setCarMakeConfidence(object.getVehicleAnnotation().getAttributes().getSystem().getMake().getConfidence());
                carPosition.setCarColor(object.getVehicleAnnotation().getAttributes().getSystem().getColor().getName());
                carPosition.setCarColorConfidence(object.getVehicleAnnotation().getAttributes().getSystem().getColor().getConfidence());
                carPosition.setConfidence(object.getVehicleAnnotation().getRecognitionConfidence());
                carPosition.setCarBounding(mapBounding(object.getVehicleAnnotation().getBounding()));
                arrayList.add(carPosition);
            }

        }


        return arrayList;
    }

    public static Bounding mapBounding(com.skidata.wimc.sighthound.client.domain.Bounding boundingIn) {
        if (boundingIn != null) {
            Bounding bounding = new Bounding();
            bounding.setX1(boundingIn.getVertices()[0].getX());
            bounding.setY1(boundingIn.getVertices()[0].getY());
            bounding.setX2(boundingIn.getVertices()[1].getX());
            bounding.setY2(boundingIn.getVertices()[1].getY());
            bounding.setX3(boundingIn.getVertices()[2].getX());
            bounding.setY3(boundingIn.getVertices()[2].getY());
            bounding.setX4(boundingIn.getVertices()[3].getX());
            bounding.setY4(boundingIn.getVertices()[3].getY());
            return bounding;
        } else {
            return null;
        }
    }

    public static PlateCoordinate[] mapPlateCoordinates(com.skidata.wimc.sighthound.client.domain.Bounding boundingIn) {
        if (boundingIn != null) {
            ArrayList<PlateCoordinate> plates = new ArrayList();
            for (Vertice vertice : boundingIn.getVertices()) {
                PlateCoordinate plateCoordinate = new PlateCoordinate();
                plateCoordinate.setX(vertice.getX());
                plateCoordinate.setY(vertice.getY());
                plates.add(plateCoordinate);
            }
            PlateCoordinate[] plateCoordinatesOut = new PlateCoordinate[4];
            plateCoordinatesOut[0] = plates.get(0);
            plateCoordinatesOut[1] = plates.get(1);
            plateCoordinatesOut[2] = plates.get(2);
            plateCoordinatesOut[3] = plates.get(3);
            return plateCoordinatesOut;
        } else {
            return null;
        }
    }

    public boolean isSightEnabled() {
        return sightEnabled;
    }

    public void setSightEnabled(boolean sightEnabled) {
        this.sightEnabled = sightEnabled;
    }

    public List<String> getWebcams() {
        return webcams;
    }

    public void setWebcams(List<String> webcams) {
        this.webcams = webcams;
    }

}
