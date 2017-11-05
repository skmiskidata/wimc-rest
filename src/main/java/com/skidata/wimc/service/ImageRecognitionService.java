package com.skidata.wimc.service;

import com.skidata.wimc.sighthound.client.SighthoundClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihael on 4.11.2017.
 */
@ConfigurationProperties(prefix = "recognition.service")
@Service
public class ImageRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageRecognitionService.class);

    @Autowired
    private SimpMessagingTemplate template;

    @NotNull
    private boolean sightEnabled;

    private List<String> webcams = new ArrayList<String>();

    @Autowired
    private SighthoundClient sighthoundService;

    @Scheduled(fixedDelay = 3000)
    public void pollCameras() {
        if (isSightEnabled()) {

            for (String cameraUrl : getWebcams()) {
                try {
                    logger.info("Polling Camera URL : " + cameraUrl);
                    sighthoundService.recognize(new URL(cameraUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Scheduled(fixedRate = 5000)
    public void testStomp() {
        logger.info("Sending message...");
        template.convertAndSend("/topic/hello", "Hi message!");
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
