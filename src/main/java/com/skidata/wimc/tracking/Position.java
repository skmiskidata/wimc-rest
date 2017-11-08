package com.skidata.wimc.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
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
