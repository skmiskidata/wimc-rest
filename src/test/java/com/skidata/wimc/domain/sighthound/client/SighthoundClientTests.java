package com.skidata.wimc.domain.sighthound.client;

import com.skidata.wimc.sighthound.client.SighthoundClient;
import com.skidata.wimc.sighthound.client.domain.SightResult;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.io.FileInputStream;

@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {SighthoundClient.class})
public class SighthoundClientTests {

    private static String SAMPLE_PICTURE_PATH = "target/test-classes/audi-sports-car-r8-marlene.jpg";

    private static final Logger logger = LoggerFactory.getLogger(SighthoundClientTests.class);

    @Autowired
    private SighthoundClient sighthoundClient;

    @Test
    public void testCarRecognition() throws Exception {
        byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(new File(SAMPLE_PICTURE_PATH)));
        Assert.assertNotNull(sighthoundClient.getRecognizeUrl());
        SightResult sightResult = sighthoundClient.recognize(imageBytes);
        Assert.assertNotNull(sightResult);
        logger.info(sightResult.toString());
    }

}
