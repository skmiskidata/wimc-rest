package com.skidata.wimc.sighthound.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by skmi on 7. 11. 2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicencePlate {

    private Bounding bounding;

    private AttributesLP attributes;

    public Bounding getBounding() {
        return bounding;
    }

    public void setBounding(Bounding bounding) {
        this.bounding = bounding;
    }

    public AttributesLP getAttributes() {
        return attributes;
    }

    public void setAttributesLP(AttributesLP attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "LicencePlate{" +
                "bounding=" + bounding +
                ", attributesLP=" + attributes +
                '}';
    }
}
