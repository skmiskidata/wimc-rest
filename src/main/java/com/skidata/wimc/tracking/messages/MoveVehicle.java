package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "MoveVehicle")
public class MoveVehicle extends Message {
    String uuid;
    int x;
    int y;
    String lp;

    public MoveVehicle(String uuid, int x, int y, String lp) {
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

    @Override
    public String toString() {
        return "MoveVehicle{" +
                "uuid='" + uuid + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", lp='" + lp + '\'' +
                '}';
    }
}
