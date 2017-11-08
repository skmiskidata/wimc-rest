package com.skidata.wimc.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Slot")
public class Slot {
    String id;
    boolean free;

    Position ll; // lower left
    Position ur; // upper right

    public Slot(String id, Position ll, Position ur) {
        this.id = id;
        this.ll = ll;
        this.ur = ur;
    }

    @JsonProperty
    public boolean isFree() {
        return free;
    }

    @JsonProperty
    public void setFree(boolean free) {
        this.free = free;
    }

    @JsonProperty
    public Position getLl() {
        return ll;
    }

    @JsonProperty
    public Position getUr() {
        return ur;
    }

    @JsonProperty
    public String getId() {
        return id;
    }
}
