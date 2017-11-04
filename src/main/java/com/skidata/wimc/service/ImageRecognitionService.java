package com.skidata.wimc.service;

import com.skidata.wimc.sighthound.client.SighthoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mihael on 4.11.2017.
 */
@Component
public class ImageRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageRecognitionService.class);

    @Autowired
    private SighthoundService sighthoundService;

    @Scheduled(fixedRate = 1000)
    public void pollCameraOne() {
        try {
            logger.info("Polling Camera 1");
            sighthoundService.recognize(new URL("http://192.168.1.106:8080/photoaf.jpg"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000)
    public void pollCameraTwo() {
        try {
            logger.info("Polling Camera 2");
            sighthoundService.recognize(new URL("http://192.168.1.107:8080/photoaf.jpg"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
