package com.skidata.wimc.domain;

import java.util.Arrays;

/**
 * Created by skmi on 31. 10. 2017.
 */
public class CarPosition {

    private String uuid;

    private String cameraSource;

    private String licencePlate;

    private PlateCoordinate[] plateCoordinates;

    private String carMake;

    private Bounding carBounding;

    private Bounding licencePlateBounding;

    private Float carMakeConfidence;

    private Float licencePlateConfidence;

    private Float confidence;

    private String carColor;

    private Float carColorConfidence;

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

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public Bounding getCarBounding() {
        return carBounding;
    }

    public void setCarBounding(Bounding carBounding) {
        this.carBounding = carBounding;
    }

    public Bounding getLicencePlateBounding() {
        return licencePlateBounding;
    }

    public void setLicencePlateBounding(Bounding licencePlateBounding) {
        this.licencePlateBounding = licencePlateBounding;
    }

    public Float getCarMakeConfidence() {
        return carMakeConfidence;
    }

    public void setCarMakeConfidence(Float carMakeConfidence) {
        this.carMakeConfidence = carMakeConfidence;
    }

    public Float getLicencePlateConfidence() {
        return licencePlateConfidence;
    }

    public void setLicencePlateConfidence(Float licencePlateConfidence) {
        this.licencePlateConfidence = licencePlateConfidence;
    }

    public String getCameraSource() {
        return cameraSource;
    }

    public void setCameraSource(String cameraSource) {
        this.cameraSource = cameraSource;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public Float getCarColorConfidence() {
        return carColorConfidence;
    }

    public void setCarColorConfidence(Float carColorConfidence) {
        this.carColorConfidence = carColorConfidence;
    }

    @Override
    public String toString() {
        return "CarPosition{" +
                "uuid='" + uuid + '\'' +
                ", cameraSource='" + cameraSource + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", plateCoordinates=" + Arrays.toString(plateCoordinates) +
                ", carMake='" + carMake + '\'' +
                ", carBounding=" + carBounding +
                ", licencePlateBounding=" + licencePlateBounding +
                ", carMakeConfidence=" + carMakeConfidence +
                ", licencePlateConfidence=" + licencePlateConfidence +
                ", confidence=" + confidence +
                ", carColor='" + carColor + '\'' +
                ", carColorConfidence=" + carColorConfidence +
                ", draw=" + draw +
                '}';
    }
}
