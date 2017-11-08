package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "IniVehicle")
public class InitVehicle extends Message {
    String uuid;
    int x;
    int y;
    String lp;

    public InitVehicle(String uuid, int x, int y, String lp) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.lp = lp;
    }

    @JsonProperty
    public String getUuid() {
        return uuid;
    }

    @JsonProperty
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty
    public int getX() {
        return x;
    }

    @JsonProperty
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }
}
