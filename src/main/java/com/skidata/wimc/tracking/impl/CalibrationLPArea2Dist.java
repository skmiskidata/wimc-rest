package com.skidata.wimc.tracking.impl;

public class CalibrationLPArea2Dist {
    public double lpArea;
    public double dist;

    public CalibrationLPArea2Dist(double lpArea, double dist) {
        this.lpArea = lpArea;
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "CalibrationLPArea2Dist{" +
                "lpArea=" + lpArea +
                ", dist=" + dist +
                '}';
    }
}
