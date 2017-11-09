package com.skidata.wimc.tracking;

import com.skidata.wimc.tracking.impl.MappingContext;

public interface PositionMapper {
    Position mapPixelToRealWorld(MappingContext ctx, Pixel p);
}
