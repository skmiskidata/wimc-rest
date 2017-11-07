package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by skmi on 7. 11. 2017.
 */
public class StringLP {

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
        return "StringLP{" +
                "name='" + name + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
