package com.skidata.wimc.tracking;

import java.util.Map;

public class Camera {
    String id;
    Position pos;
    Map<Pixel, Position> calibration;

    public Camera(String id, Position pos, Map<Pixel, Position> calibration) {
        this.id = id;
        this.pos = pos;
        this.calibration = calibration;
    }

    public String getId() {
        return id;
    }

    public Position getPos() {
        return pos;
    }

    public Map<Pixel, Position> getCalibration() {
        return calibration;
    }
}
