package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;

public class CalibrationValues {
    public Pixel pixel;
    public Position pos;
    public long lpArea;
    public long lpAreaY;

    public CalibrationValues(Pixel pixel, Position pos, long lpArea, long lpAreaY) {
        this.pixel = pixel;
        this.pos = pos;
        this.lpArea = lpArea;
        this.lpAreaY = lpAreaY;
    }
}
