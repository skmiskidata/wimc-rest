package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "MoveVehicle")
public class MoveVehicle extends Message {
    String uuid;
    int x;
    int y;

    public MoveVehicle(String uuid, int x, int y) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
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
}
