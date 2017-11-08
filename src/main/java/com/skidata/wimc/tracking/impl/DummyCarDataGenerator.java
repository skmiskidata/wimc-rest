package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.messages.Message;

public class DummyCarDataGenerator {

    DummyCar car = new DummyCar("ABC123", 300, 450);

    public Message next() {
        return car.nextMsg();
    }
}
