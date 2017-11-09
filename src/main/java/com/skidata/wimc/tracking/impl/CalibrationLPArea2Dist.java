package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Pixel;

public class CalibrationLPArea2Dist {
    public Pixel pixel;
    public double angle;
    public double lpArea;
    public double dist;

    public CalibrationLPArea2Dist(Pixel pixel, double angle, double lpArea, double dist) {
        this.pixel = pixel;
        this.angle = angle;
        this.lpArea = lpArea;
        this.dist = dist;
    }

    public Pixel getPixel() {
        return pixel;
    }

    public double getAngle() {
        return angle;
    }

    public double getLpArea() {
        return lpArea;
    }

    public double getDist() {
        return dist;
    }
}
