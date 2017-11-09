package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "InitVehicle")
public class InitVehicle extends Message {
    String uuid;
    int x;
    int y;
    String lp;
    String color;

    public InitVehicle(String uuid, int x, int y, String lp, String color) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.lp = lp;
        this.color = color;
    }

    @JsonProperty
    public String getUuid() {
        return uuid;
    }

    @JsonProperty
    public int getX() {
        return x;
    }

    @JsonProperty
    public int getY() {
        return y;
    }

    @JsonProperty
    public String getLp() {
        return lp;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    @JsonProperty
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "InitVehicle{" +
                "uuid='" + uuid + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", lp='" + lp + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
