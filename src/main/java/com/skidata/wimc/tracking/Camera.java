package com.skidata.wimc.tracking;

import com.skidata.wimc.tracking.impl.CalibrationValues;

import java.util.Set;

public class Camera {
    String id;
    Position pos;
    Set<CalibrationValues> calibration;

    public Camera(String id, Position pos, Set<CalibrationValues> calibration) {
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

    public Set<CalibrationValues> getCalibration() {
        return calibration;
    }
}
