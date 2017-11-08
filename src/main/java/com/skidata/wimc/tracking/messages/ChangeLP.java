package com.skidata.wimc.tracking.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "ChangeLP")
public class ChangeLP extends Message {
    String uuid;
    String lp;

    public ChangeLP(String uuid, String lp) {
        this.uuid = uuid;
        this.lp = lp;
    }

    @JsonProperty
    public String getUuid() {
        return uuid;
    }

    @JsonProperty
    public String getLp() {
        return lp;
    }

    @Override
    public String toString() {
        return "ChangeLP{" +
                "uuid='" + uuid + '\'' +
                ", lp='" + lp + '\'' +
                '}';
    }
}
