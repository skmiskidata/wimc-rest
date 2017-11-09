package com.skidata.wimc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by skmi on 9. 11. 2017.
 */
public class Vehicle {

    @JsonProperty("color")
    private Color[] colors;

    @JsonProperty("make")
    private Make[] makes;

    @JsonProperty("make_model")
    private MakeModel[] makeModels;

    @JsonProperty("body_type")
    private BodyType[] bodyTypes;

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Make[] getMakes() {
        return makes;
    }

    public void setMakes(Make[] makes) {
        this.makes = makes;
    }

    public MakeModel[] getMakeModels() {
        return makeModels;
    }

    public void setMakeModels(MakeModel[] makeModels) {
        this.makeModels = makeModels;
    }

    public BodyType[] getBodyTypes() {
        return bodyTypes;
    }

    public void setBodyTypes(BodyType[] bodyTypes) {
        this.bodyTypes = bodyTypes;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "colors=" + Arrays.toString(colors) +
                ", makes=" + Arrays.toString(makes) +
                ", makeModels=" + Arrays.toString(makeModels) +
                ", bodyTypes=" + Arrays.toString(bodyTypes) +
                '}';
    }
}
