package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Model {

    private String name;

    private Float confidence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
