package com.skidata.wimc.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value = "CarPark")
public class CarPark {
    List<Slot> slots;

    public CarPark() {
        slots.add(new Slot("A1", new Position(300, 560), new Position(550,900)));
        slots.add(new Slot("A2", new Position(550, 560), new Position(800,900)));
        slots.add(new Slot("A3", new Position(800, 560), new Position(1050,900)));
        slots.add(new Slot("A4", new Position(1050, 560), new Position(3000,900)));
        slots.add(new Slot("A5", new Position(460, 0), new Position(810,270)));
        slots.add(new Slot("A6", new Position(810, 0), new Position(1160,270)));
    }

    @JsonProperty
    public List<Slot> getSlots() {
        return slots;
    }
}
