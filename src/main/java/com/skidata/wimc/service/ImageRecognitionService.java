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

    @Autowired
    private CameraConfig cameraConfig;

    @Scheduled(fixedDelay = 3000)
    public void pollCameras() {
        if (cameraConfig != null && cameraConfig.isSightEnabled()) {

            for (String cameraUrl : cameraConfig.getWebcams()) {
                try {
                    logger.info("Polling Camera URL : " + cameraUrl);
                    sighthoundService.recognize(new URL(cameraUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
