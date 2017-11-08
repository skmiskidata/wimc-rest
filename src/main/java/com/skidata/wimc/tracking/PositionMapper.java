package com.skidata.wimc.tracking;

public interface PositionMapper {
    Position mapPixelToRealWorld(Camera c, Pixel p);
}
