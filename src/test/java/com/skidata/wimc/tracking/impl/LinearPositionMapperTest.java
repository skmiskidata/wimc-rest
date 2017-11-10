package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearPositionMapperTest {

    LinearPositionMapper mapper = new LinearPositionMapper();
    static Camera cam;
    static MappingContext ctx;
    static Camera cam2;
    static MappingContext ctx2;

    @Before
    public void init() {
        Set<CalibrationPixel2Pos> cal = new HashSet<>();
        cal.add(new CalibrationPixel2Pos(new Pixel(10,10), new Position(10, 10)));
        cal.add(new CalibrationPixel2Pos(new Pixel(20,10), new Position(20, 10)));
        cal.add(new CalibrationPixel2Pos(new Pixel(10,20), new Position(10, 20)));
        cal.add(new CalibrationPixel2Pos(new Pixel(20,20), new Position(20, 20)));
        cam = new Camera("CAM1", new Position(0,0), cal, new ArrayList<>(), 0.0);
        ctx = new MappingContext(cam, 0, 0, 0, "");

        Set<CalibrationPixel2Pos> pixel2pos = new HashSet<>();
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(1055, 573), new Position(450, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(750, 573), new Position(450, 260)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(476, 573), new Position(450, 450)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(988, 421), new Position(810, 80)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(824, 421), new Position(810, 260)));
        pixel2pos.add(new CalibrationPixel2Pos(new Pixel(648, 421), new Position(810, 450)));

        List<CalibrationLPArea2Dist> area2Dists = new ArrayList<>();
        cam2 = new Camera("899804908", new Position(0, 0), pixel2pos, area2Dists, Math.PI/180.0*(+75.0));

        ctx2 = new MappingContext(cam2, 0, 0, 0, "");
    }

    @Test
    public void testMap1() {
        Position out = mapper.mapPixelToRealWorld(ctx, new Pixel(10, 10));
        Assert.assertEquals(new Position(10, 10), out);
    }

    @Test
    public void testMap2() {
        Position out = mapper.mapPixelToRealWorld(ctx, new Pixel(12, 15));
        Assert.assertEquals(new Position(12, 15), out);
    }

    @Test
    public void testMap3() {
        Position out = mapper.mapPixelToRealWorld(ctx, new Pixel(24, 28));
        Assert.assertEquals(new Position(24, 28), out);
    }

    @Test
    public void testMap4() {
        Position out = mapper.mapPixelToRealWorld(ctx, new Pixel(-10, -50));
        Assert.assertEquals(new Position(-10, -50), out);
    }

    @Test
    public void testMap5() {
        Position out = mapper.mapPixelToRealWorld(ctx2, new Pixel(1055, 573));
        Assert.assertEquals(new Position(450, 80), out);
    }

    @Test
    public void testMap6() {
        Position out = mapper.mapPixelToRealWorld(ctx2, new Pixel(927, 573));
        Assert.assertEquals(new Position(450, 170), out);
    }

    @Test
    public void testMap7() {
        Position out = mapper.mapPixelToRealWorld(ctx2, new Pixel(824, 421));
        Assert.assertEquals(new Position(810, 260), out);
    }

}