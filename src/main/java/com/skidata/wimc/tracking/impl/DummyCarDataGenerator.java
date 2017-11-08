package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.messages.InitVehicle;
import com.skidata.wimc.tracking.messages.Message;

import java.util.UUID;

public class DummyCarDataGenerator {

    public Message next() {
        return new InitVehicle(UUID.randomUUID().toString(), 800, 200, "ABC123");
    }
}
