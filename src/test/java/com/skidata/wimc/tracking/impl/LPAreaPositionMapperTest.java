package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Position;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;

public class LPAreaPositionMapperTest {
    LPAreaPositionMapper mapper = new LPAreaPositionMapper();
    static Camera cam;
    static MappingContext ctx;

    @BeforeClass
    public static void init() {
        List<CalibrationLPArea2Dist> cal = new ArrayList<>();
//        cal.add(new CalibrationLPArea2Dist());
//        cal.add(new CalibrationLPArea2Dist());
//        cal.add(new CalibrationLPArea2Dist());
//        cal.add(new CalibrationLPArea2Dist());
        cam = new Camera("CAM1", new Position(0,0), null, cal, 45.0);
        ctx = new MappingContext(cam, 0, 0, 0, "");
    }

}