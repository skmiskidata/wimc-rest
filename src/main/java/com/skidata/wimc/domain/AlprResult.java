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

    @JsonProperty("epoch_start")
    private Long epochStart;

    @JsonProperty("epoch_end")
    private Long epochEnd;

    @JsonProperty("agent_uid")
    private String agentUid;

    @JsonProperty("agent_version")
    private String agentVersion;

    @JsonProperty("agent_type")
    private String agentType;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("country")
    private String country;

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

    @JsonProperty("best_plate")
    private LicencePlate bestPlate;

    @JsonProperty("vehicle")
    private Vehicle vehicle;


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

    public Long getEpochStart() {
        return epochStart;
    }

    public void setEpochStart(Long epochStart) {
        this.epochStart = epochStart;
    }

    public Long getEpochEnd() {
        return epochEnd;
    }

    public void setEpochEnd(Long epochEnd) {
        this.epochEnd = epochEnd;
    }

    public String getAgentUid() {
        return agentUid;
    }

    public void setAgentUid(String agentUid) {
        this.agentUid = agentUid;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LicencePlate getBestPlate() {
        return bestPlate;
    }

    public void setBestPlate(LicencePlate bestPlate) {
        this.bestPlate = bestPlate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "AlprResult{" +
                "version='" + version + '\'' +
                ", dataType='" + dataType + '\'' +
                ", epochTime='" + epochTime + '\'' +
                ", epochStart=" + epochStart +
                ", epochEnd=" + epochEnd +
                ", agentUid='" + agentUid + '\'' +
                ", agentVersion='" + agentVersion + '\'' +
                ", agentType='" + agentType + '\'' +
                ", companyId='" + companyId + '\'' +
                ", country='" + country + '\'' +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", processingTimeMs=" + processingTimeMs +
                ", results=" + Arrays.toString(results) +
                ", uuid='" + uuid + '\'' +
                ", cameraId='" + cameraId + '\'' +
                ", siteId='" + siteId + '\'' +
                ", bestPlate=" + bestPlate +
                ", vehicle=" + vehicle +
                '}';
    }
}
