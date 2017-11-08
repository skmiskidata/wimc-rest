package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "RemoveVehicle")
public class RemoveVehicle extends Message {
    String uuid;

    public RemoveVehicle(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty
    public String getUuid() {
        return uuid;
    }
}
