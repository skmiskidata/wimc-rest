package com.skidata.wimc.service;

import com.skidata.wimc.sighthound.client.ISighthoundProcessResult;
import com.skidata.wimc.sighthound.client.SighthoundProcessorService;
import com.skidata.wimc.sighthound.client.domain.SightResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @NotNull
    private boolean sightEnabled;

    private List<String> webcams = new ArrayList<String>();

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
            // TODO transform and post further
            logger.info("Got object: \n" + sightResult.toString());
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
