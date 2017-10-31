package com.skidata.wimc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by skmi on 30. 10. 2017.
 */
@JsonIgnoreProperties
public class AlprResult {

    private String version;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("epoch_time")
    private String epochTime;

    @JsonProperty("img_width")
    private Long imageWidth;

    @JsonProperty("img_height")
    private Long imageHeight;

    @JsonProperty("processing_time_ms")
    private Float processingTimeMs;

    @JsonProperty("results")
    private LicencePlate[] results;

    private String uuid;

    @JsonProperty("camera_id")
    private String cameraId;

    @JsonProperty("site_id")
    private String siteId;

    public LicencePlate[] getResults() {
        return results;
    }

    public void setResults(LicencePlate[] results) {
        this.results = results;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(String epochTime) {
        this.epochTime = epochTime;
    }

    public Long getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Long getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Float getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Float processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "AlprResult{" +
                "version='" + version + '\'' +
                ", dataType='" + dataType + '\'' +
                ", epochTime='" + epochTime + '\'' +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", processingTimeMs=" + processingTimeMs +
                ", results=" + Arrays.toString(results) +
                ", uuid='" + uuid + '\'' +
                ", cameraId='" + cameraId + '\'' +
                ", siteId='" + siteId + '\'' +
                '}';
    }
}
