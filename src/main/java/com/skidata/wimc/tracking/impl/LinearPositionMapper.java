package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import com.skidata.wimc.tracking.PositionMapper;

public class LinearPositionMapper implements PositionMapper {

    @Override
    public Position mapPixelToRealWorld(Camera c, Pixel p) {
        return new Position(800,450);
    }
}
