package com.skidata.wimc.sighthound.client;

import com.skidata.wimc.sighthound.client.domain.SightResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by Mihael on 6. 11. 2017.
 */
@Service
public class SighthoundProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(SighthoundProcessorService.class);

    @Autowired
    private SighthoundClient sighthoundClient;

    @Async
    public void initializeCameraStream(URL cameraUrl, ISighthoundProcessResult iSighthoundProcessResult) {
        int counter = 0;
        try {
            do {
                counter++;
                SightResult sightResult = sighthoundClient.recognize(cameraUrl);
                if (sightResult != null && iSighthoundProcessResult != null) {
                    iSighthoundProcessResult.processResult(sightResult);
                } else {
                    logger.warn("Null sight result or null process object");
                }
                logger.info("Camera " + cameraUrl.toString() + " recognition number " + counter + " finished");
                Thread.sleep(100);
            } while (counter < 100000);
        } catch (Exception ex) {
            logger.error("Fatal error at processing camera: " + cameraUrl.toString(), ex);
        }
    }


}
