package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LinearPositionMapperTest {


    LinearPositionMapper mapper = new LinearPositionMapper();
    static Camera cam;

    @BeforeClass
    public static void init() {
        Map<Pixel, Position> cal = new HashMap<>();
        cal.put(new Pixel(10,10), new Position(10, 10));
        cal.put(new Pixel(20,10), new Position(20, 10));
        cal.put(new Pixel(10,20), new Position(10, 20));
        cal.put(new Pixel(20,20), new Position(20, 20));
        cam = new Camera("CAM1", new Position(0,0), cal);
    }

    @Test
    public void testMap1() {
        Position out = mapper.mapPixelToRealWorld(cam, new Pixel(10, 10));
        Assert.assertEquals(new Position(10, 10), out);
    }

    @Test
    public void testMap2() {
        Position out = mapper.mapPixelToRealWorld(cam, new Pixel(12, 15));
        Assert.assertEquals(new Position(12, 15), out);
    }

    @Test
    public void testMap3() {
        Position out = mapper.mapPixelToRealWorld(cam, new Pixel(24, 28));
        Assert.assertEquals(new Position(24, 28), out);
    }

    @Test
    public void testMap4() {
        Position out = mapper.mapPixelToRealWorld(cam, new Pixel(-10, -50));
        Assert.assertEquals(new Position(-10, -50), out);
    }

}