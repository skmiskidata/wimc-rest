package com.skidata.wimc.sighthound.client;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by skmi on 2. 11. 2017.
 */
@ConfigurationProperties(prefix = "sighthound.client")
@Component
public class SighthoundClient {

    private static final Logger logger = LoggerFactory.getLogger(SighthoundClient.class);

    private String recognizeUrl;
    private String detectUrl;
    private String accessToken;

    @Async
    private byte[] loadImage(URL url) {
        InputStream is = null;
        byte[] binaryData = null;
        try {
            is = url.openStream();
            binaryData = IOUtils.toByteArray(is);
        } catch (Exception ex) {
            logger.error("Error at recognition", ex);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return binaryData;
    }

    @Async
    public void recognize(URL url) {
        try {
            byte[] binaryData = loadImage(url);
            URL apiURL = new URL(recognizeUrl);
            long timeStart = System.currentTimeMillis();
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("X-Access-Token", accessToken);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(binaryData.length);
            OutputStream os = connection.getOutputStream();
            os.write(binaryData);
            os.flush();
            int httpCode = connection.getResponseCode();
            if (httpCode == 200) {
                JsonReader jReader = Json.createReader(connection.getInputStream());
                JsonObject jsonBody = jReader.readObject();
                logger.info(jsonBody.toString());
            } else {
                logger.error("Error code = " + httpCode);
                JsonReader jReader = Json.createReader(connection.getErrorStream());
                logger.error(jReader.toString());
                JsonObject jsonError = jReader.readObject();
                logger.error(jsonError.toString());
            }

            logger.info("Total time for recognition = " + (System.currentTimeMillis() - timeStart) + "ms");

        } catch (Exception ex) {
            logger.error("Error at recognition", ex);
        }
    }


    public void detect(URL url) {
        try {
            byte[] binaryData = loadImage(url);
            URL apiURL = new URL(detectUrl);
            long timeStart = System.currentTimeMillis();
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("X-Access-Token", accessToken);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(binaryData.length);
            OutputStream os = connection.getOutputStream();
            os.write(binaryData);
            os.flush();
            int httpCode = connection.getResponseCode();
            if (httpCode == 200) {
                JsonReader jReader = Json.createReader(connection.getInputStream());
                JsonObject jsonBody = jReader.readObject();
                logger.info(jsonBody.toString());
            } else {
                logger.error("Error code = " + httpCode);
                JsonReader jReader = Json.createReader(connection.getErrorStream());
                logger.error(jReader.toString());
                JsonObject jsonError = jReader.readObject();
                logger.error(jsonError.toString());
            }
            logger.info("Total time for recognition = " + (System.currentTimeMillis() - timeStart) + "ms");
        } catch (Exception ex) {
            logger.error("Error at detection ", ex);
        }
    }

    public String getRecognizeUrl() {
        return recognizeUrl;
    }

    public void setRecognizeUrl(String recognizeUrl) {
        this.recognizeUrl = recognizeUrl;
    }

    public String getDetectUrl() {
        return detectUrl;
    }

    public void setDetectUrl(String detectUrl) {
        this.detectUrl = detectUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // tests
//    public static void main(String[] args) throws IOException {
//        SighthoundClient sighthoundService = new SighthoundClient();
//        //String imageUrl = "http://172.27.20.183:8080/?action=snapshot";
//        String imageUrl = "http://192.168.1.106:8080/photoaf.jpg";
//        sighthoundService.recognize(new URL(imageUrl));
//        sighthoundService.detect(new URL(imageUrl));
//    }

}
