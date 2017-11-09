package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.messages.InitVehicle;
import com.skidata.wimc.tracking.messages.Message;
import com.skidata.wimc.tracking.messages.MoveVehicle;
import com.skidata.wimc.tracking.messages.RemoveVehicle;

import java.util.UUID;

public class DummyCar {
    String uuid = UUID.randomUUID().toString();
    String lp;
    int x;
    int y;
    int initX;
    int initY;
    int yInc = 10;
    String color = null;

    public DummyCar(String lp, int x, int y, String color) {
        this.lp = lp;
        this.initX = x;
        this.initY = y;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Message nextMsg() {
        if (x == initX && y == initY) {
            x = x + 10;
            y = y + 10;
            return new InitVehicle(uuid, x, y, lp, "red");
        }

        if (x < 1500) {
            x = x + 10;
            y = y + yInc;
            if (y >= 600 || y <= 300) {
                yInc = -1 * yInc;
            }
            return new MoveVehicle(uuid, x, y, lp, "white");
        } else {
            x = initX;
            y = initY;
            return new RemoveVehicle(uuid);
        }
    }
}
