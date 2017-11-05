package com.skidata.wimc.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihael on 5.11.2017.
 */
@ConfigurationProperties(prefix = "local")
@Component
public class CameraConfig {

    @NotNull
    private boolean sightEnabled;

    private List<String> webcams = new ArrayList<String>();

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
