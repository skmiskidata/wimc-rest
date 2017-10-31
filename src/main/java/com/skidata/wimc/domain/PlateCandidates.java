package com.skidata.wimc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by skmi on 30. 10. 2017.
 */
public class PlateCandidates {

    private String plate;

    private Float confidence;

    @JsonProperty("matches_template")
    private Long matchingTemplate;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Long getMatchingTemplate() {
        return matchingTemplate;
    }

    public void setMatchingTemplate(Long matchingTemplate) {
        this.matchingTemplate = matchingTemplate;
    }

    @Override
    public String toString() {
        return "PlateCandidates{" +
                "plate='" + plate + '\'' +
                ", confidence=" + confidence +
                ", matchingTemplate=" + matchingTemplate +
                '}';
    }
}
