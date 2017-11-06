package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class VehicleAnnotation {

    private Bounding bounding;

    private Float recognitionConfidence;

    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Float getRecognitionConfidence() {
        return recognitionConfidence;
    }

    public void setRecognitionConfidence(Float recognitionConfidence) {
        this.recognitionConfidence = recognitionConfidence;
    }

    public Bounding getBounding() {
        return bounding;
    }

    public void setBounding(Bounding bounding) {
        this.bounding = bounding;
    }

    @Override
    public String toString() {
        return "VehicleAnnotation{" +
                "bounding=" + bounding +
                ", recognitionConfidence=" + recognitionConfidence +
                ", attributes=" + attributes +
                '}';
    }
}
