package com.skidata.wimc.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.skidata.wimc.domain.LicencePlate;

import java.util.List;

@JsonRootName(value = "Car")
public class Car {
    List<LicencePlate> candidateLPs;
    Position ll;
    Position ur;

    @JsonProperty
    public List<LicencePlate> getCandidateLPs() {
        return candidateLPs;
    }

    @JsonProperty
    public Position getLl() {
        return ll;
    }

    @JsonProperty
    public Position getUr() {
        return ur;
    }
}
