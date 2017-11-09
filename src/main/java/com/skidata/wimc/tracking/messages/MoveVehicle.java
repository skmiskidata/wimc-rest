package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "MoveVehicle")
public class MoveVehicle extends Message {
    String uuid;
    int x;
    int y;
    String lp;
    String color;

    public MoveVehicle(String uuid, int x, int y, String lp, String Color) {
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

    @JsonProperty
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "MoveVehicle{" +
                "uuid='" + uuid + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", lp='" + lp + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
