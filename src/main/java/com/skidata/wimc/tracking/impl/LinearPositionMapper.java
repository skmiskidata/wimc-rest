package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import com.skidata.wimc.tracking.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LinearPositionMapper implements PositionMapper {

    private static final Logger logger = LoggerFactory.getLogger(LinearPositionMapper.class);

    @Override
    public Position mapPixelToRealWorld(MappingContext ctx, Pixel p) {
        List<Tuple<Double, CalibrationPixel2Pos>> d2p = nearestFor(ctx.cam, p);

        int pxx1 = d2p.get(0).getV2().pixel.getX();
        int pox1 = d2p.get(0).getV2().pos.getX();
        int pxx2 = d2p.get(1).getV2().pixel.getX();
        int pox2 = d2p.get(1).getV2().pos.getX();
        int pxy1 = d2p.get(0).getV2().pixel.getY();
        int poy1 = d2p.get(0).getV2().pos.getY();
        int pxy2 = d2p.get(1).getV2().pixel.getY();
        int poy2 = d2p.get(1).getV2().pos.getY();

        Position p1 = moveAndRotate(ctx, new Position(pox1, poy1));
        Position p2 = moveAndRotate(ctx, new Position(pox2, poy2));
        int x = interpolate(0, pxx1, p1.getX(), pxx2, p2.getX(), p.getX());
        int y = interpolate(0, pxy1, p1.getY(), pxy2, p2.getY(), p.getY());
        return rotateAndMove(ctx, new Position(x, y));
    }

    private Position rotateAndMove(MappingContext ctx, Position in) {
        double a = ctx.cam.getAngle();
        int x = (int) Math.round(Math.cos(a)*in.getX() + Math.sin(a)*in.getY());
        int y = (int) Math.round(-1*Math.sin(a)*in.getX() + Math.cos(a)*in.getY());

        Position p = new Position(x+ctx.cam.getPos().getX(), y+ctx.cam.getPos().getY());
        return p;
    }

    private Position moveAndRotate(MappingContext ctx, Position in) {
        Position p = new Position(in.getX()-ctx.cam.getPos().getX(), in.getY()-ctx.cam.getPos().getY());
        double a = ctx.cam.getAngle();
        int x = (int) Math.round(Math.cos(a)*p.getX() - Math.sin(a)*p.getY());
        int y = (int) Math.round(Math.sin(a)*p.getX() + Math.cos(a)*p.getY());
        return new Position(x, y);
    }

    private int interpolate(int camOffset, int px1, int pos1, int px2, int pos2, int px) {
        logger.info("using for interpolation pixel1={} pos1={} pixel2={} pos2={} px={}", px1, pos1, px2, pos2, px);
        if (px1 == px2)
            return pos1;

        double pxd = px2 - px1;
        double posd = pos2- pos1 + camOffset;

        return (int) Math.round(pos1 + posd*((px - px1) / pxd))-camOffset;
    }

    private List<Tuple<Double, CalibrationPixel2Pos>> nearestFor(Camera c, Pixel p) {
        List<Tuple<Double, CalibrationPixel2Pos>> d2p = new ArrayList<>();

        for (CalibrationPixel2Pos e : c.getPixel2Pos()) {
            double d = dist(e.pixel, p);
            d2p.add(new Tuple<>(d, e));
        }

        Collections.sort(d2p, new Comparator<Tuple<Double, CalibrationPixel2Pos>>() {
            @Override
            public int compare(Tuple<Double, CalibrationPixel2Pos> o1, Tuple<Double, CalibrationPixel2Pos> o2) {
                return o1.getV1().compareTo(o2.getV1());
            }
        });

        return d2p;
    }

    private double dist(Pixel p1, Pixel p2) {
        double xd = p1.getX() - p2.getX();
        double yd = p1.getY() - p2.getY();
        return Math.sqrt(xd*xd+yd*yd);
    }
}
