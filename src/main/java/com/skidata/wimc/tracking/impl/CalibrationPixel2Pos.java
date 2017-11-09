package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;

public class CalibrationPixel2Pos {
    public Pixel pixel;
    public Position pos;

    public CalibrationPixel2Pos(Pixel pixel, Position pos) {
        this.pixel = pixel;
        this.pos = pos;
    }
}
