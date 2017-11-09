package com.skidata.wimc.domain;

/**
 * Created by skmi on 9. 11. 2017.
 */
public abstract class NameConfidenceValue {

    public String name;

    public Float confidence;

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
        return "(name='" + name + '\'' +
                ";conf=" + confidence +
                ')';
    }
}
