package com.skidata.wimc.domain;

import java.util.UUID;

/**
 * Created by skmi on 31. 10. 2017.
 */
public class CarPosition {

    private String uuid;

    private String licencePlate;

    private PlateCoordinate[] plateCoordinates;

    private Float confidence;

    private boolean draw;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public PlateCoordinate[] getPlateCoordinates() {
        return plateCoordinates;
    }

    public void setPlateCoordinates(PlateCoordinate[] plateCoordinates) {
        this.plateCoordinates = plateCoordinates;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

}
