package com.skidata.wimc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by skmi on 30. 10. 2017.
 */
public class LicencePlate {

    private String plate;

    private Float confidence;

    @JsonProperty("matches_template")
    private Long matchesTemplate;

    @JsonProperty("plate_index")
    private Long plateIndex;

    @JsonProperty("region")
    private String region;

    @JsonProperty("region_confidence")
    private Float regionConfidence;

    @JsonProperty("processing_time_ms")
    private Float processingTimeMs;

    @JsonProperty("requested_topn")
    private String requested_topn;

    @JsonProperty("coordinates")
    private PlateCoordinate[] plateCoordinates;

    @JsonProperty("vehicle_region")
    private VehicleRegion vehicleRegion;

    @JsonProperty("best_confidence")
    private Float bestConfidence;

    @JsonProperty("best_uuid")
    private String bestUuid;

    @JsonProperty("best_plate_number")
    private String bestPlateNumber;

    @JsonProperty("best_region")
    private String bestRegion;

    @JsonProperty("best_region_confidence")
    private Float bestRegionConfidence;


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

    public Long getMatchesTemplate() {
        return matchesTemplate;
    }

    public void setMatchesTemplate(Long matchesTemplate) {
        this.matchesTemplate = matchesTemplate;
    }

    public Long getPlateIndex() {
        return plateIndex;
    }

    public void setPlateIndex(Long plateIndex) {
        this.plateIndex = plateIndex;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Float getRegionConfidence() {
        return regionConfidence;
    }

    public void setRegionConfidence(Float regionConfidence) {
        this.regionConfidence = regionConfidence;
    }

    public Float getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Float processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getRequested_topn() {
        return requested_topn;
    }

    public void setRequested_topn(String requested_topn) {
        this.requested_topn = requested_topn;
    }

    public PlateCoordinate[] getPlateCoordinates() {
        return plateCoordinates;
    }

    public void setPlateCoordinates(PlateCoordinate[] plateCoordinates) {
        this.plateCoordinates = plateCoordinates;
    }

    @Override
    public String toString() {
        return "LicencePlate{" +
                "plate='" + plate + '\'' +
                ", confidence=" + confidence +
                ", matchesTemplate=" + matchesTemplate +
                ", plateIndex=" + plateIndex +
                ", region='" + region + '\'' +
                ", regionConfidence=" + regionConfidence +
                ", processingTimeMs=" + processingTimeMs +
                ", requested_topn='" + requested_topn + '\'' +
                ", plateCoordinates=" + Arrays.toString(plateCoordinates) +
                '}';
    }
}
