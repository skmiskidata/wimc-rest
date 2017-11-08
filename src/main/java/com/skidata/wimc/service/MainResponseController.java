package com.skidata.wimc.service;

import com.skidata.wimc.domain.AlprResult;
import com.skidata.wimc.domain.CarPosition;
import com.skidata.wimc.domain.LicencePlate;
import com.skidata.wimc.domain.PlateCoordinate;
import com.skidata.wimc.tracking.impl.DummyCarDataGenerator;
import com.skidata.wimc.tracking.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class MainResponseController {

    private static final Logger logger = LoggerFactory.getLogger(MainResponseController.class);
    private static Thread dummyDataThread;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    TrackingService trackingService;

    public MainResponseController(SimpMessagingTemplate template) {
        this.template = template;
        dummyDataThread = new Thread(new Runnable() {
            private DummyCarDataGenerator gen = new DummyCarDataGenerator();
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Message m = gen.next();
                    logger.info("sending message " + m);
                    template.convertAndSend("/topic/track", gen.next());
                }
            }
        });
        //dummyDataThread.start();;
    }

    @GetMapping("/test")
    public String indexResponse() {
        return "wimc-rest-service, springboot-appengine-standard, running...";
    }

    @PostMapping(path = "/postAll", consumes = "application/json", produces = "application/json")
    public void postAll(@RequestBody AlprResult result) {
        StringBuffer sb = new StringBuffer();
        for (LicencePlate licencePlate : result.getResults()) {
            sb.append(" LP: ").append(licencePlate.getPlate()).append(" XY: ");
            toString(sb, licencePlate.getPlateCoordinates());
        }
        //logger.info("camId=" + result.getCameraId() + " result = " + sb.toString());

        List<Message> msgs = trackingService.mapToRealWorld(result);
        for (Message m : msgs) {
            logger.info("Sending {}", m);
            template.convertAndSend("/topic/track", m);
        }

    }

    public void sendOut(CarPosition carPosition) {
        template.convertAndSend("/topic/lp", carPosition);
    }

    // MAPPERS /////////////////////////////////////////////////////////////////////////////////////////////

    private static CarPosition mapCarPosition(LicencePlate licencePlate) {
        // we take only first one for tests
        CarPosition carPosition = new CarPosition();
        carPosition.setUuid(UUID.randomUUID().toString()); /// TESTING
        carPosition.setDraw(true); /// TESTING
        carPosition.setConfidence(licencePlate.getConfidence());
        carPosition.setLicencePlate(licencePlate.getPlate());
        carPosition.setPlateCoordinates(licencePlate.getPlateCoordinates());
        return carPosition;
    }

    private static void toString(StringBuffer sb, PlateCoordinate[] plateCoordinates) {
        for (PlateCoordinate plateCoordinate : plateCoordinates) {
            sb.append(plateCoordinate.getX()).append("-").append(plateCoordinate.getY()).append(";");
        }
    }

}