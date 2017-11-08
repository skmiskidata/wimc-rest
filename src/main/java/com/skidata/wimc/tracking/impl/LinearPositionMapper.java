package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import com.skidata.wimc.tracking.PositionMapper;

import java.util.Map;

public class LinearPositionMapper implements PositionMapper {

    @Override
    public Position mapPixelToRealWorld(Camera c, Pixel p) {
        Tuple<Tuple<Tuple<Integer, Position>, Tuple<Integer, Position>>,
                Tuple<Tuple<Integer, Position>, Tuple<Integer, Position>>> ps = nearestFor(c, p);
        int x = interpolate(ps.getV1().getV1().getV1(), ps.getV1().getV1().getV2().getX(),
                ps.getV1().getV2().getV1(), ps.getV1().getV2().getV2().getX(), p.getX());
        int y = interpolate(ps.getV2().getV1().getV1(), ps.getV2().getV1().getV2().getY(),
                ps.getV2().getV2().getV1(), ps.getV2().getV2().getV2().getY(), p.getY());
        return new Position(x, y);
    }

    private int interpolate(int px1, int pos1, int px2, int pos2, int px) {
        if (px1 == px2)
            return pos1;

        double pxd = px2 - px1;
        double posd = pos2 - pos1;

        return (int) Math.round(pos1 + posd*((px - px1) / pxd));
    }

    private Tuple<Tuple<Tuple<Integer,Position>, Tuple<Integer,Position>>,
            Tuple<Tuple<Integer,Position>, Tuple<Integer,Position>>> nearestFor(Camera c, Pixel p) {
        int bestX1 = Integer.MIN_VALUE;
        int bestX2 = Integer.MAX_VALUE;
        int bestY1 = Integer.MIN_VALUE;
        int bestY2 = Integer.MAX_VALUE;
        int smallestX = Integer.MAX_VALUE;
        int smallestY = Integer.MAX_VALUE;
        int biggestX = Integer.MIN_VALUE;
        int biggestY = Integer.MIN_VALUE;
        Position smallestXPos = null;
        Position smallestYPos = null;
        Position biggestXPos = null;
        Position biggestYPos = null;

        Tuple xres = new Tuple<>(
                new Tuple<>(Integer.MIN_VALUE, new Position(0, 0)),
                new Tuple<>(Integer.MAX_VALUE, new Position(0, 0)));
        Tuple yres = new Tuple<>(
                new Tuple<>(Integer.MIN_VALUE, new Position(0, 0)),
                new Tuple<>(Integer.MAX_VALUE, new Position(0, 0)));
        for (Map.Entry<Pixel, Position> e : c.getCalibration().entrySet()) {
            int ex = e.getKey().getX();
            if (ex < smallestX) {
                smallestX = ex;
                smallestXPos = e.getValue();
            }
            if (biggestX < ex) {
                biggestX = ex;
                biggestXPos = e.getValue();
            }
            if (ex <= p.getX() && bestX1 < ex ) {
                bestX1 = ex;
                ((Tuple<Integer, Position>)xres.getV1()).setV1(bestX1);
                ((Tuple<Integer, Position>)xres.getV1()).setV2(e.getValue());
            }
            if (p.getX() <= ex &&  ex < bestX2) {
                bestX2 = ex;
                ((Tuple<Integer, Position>)xres.getV2()).setV1(bestX2);
                ((Tuple<Integer, Position>)xres.getV2()).setV2(e.getValue());
            }
            int ey = e.getKey().getX();
            if (ey < smallestY) {
                smallestY = ey;
                smallestYPos = e.getValue();
            }
            if (biggestY < ey) {
                biggestY = ey;
                biggestYPos = e.getValue();
            }
            if (ey <= p.getY() && bestY1 < ey ) {
                bestY1 = ey;
                ((Tuple<Integer, Position>)yres.getV1()).setV1(bestY1);
                ((Tuple<Integer, Position>)yres.getV1()).setV2(e.getValue());
            }
            if (p.getY() <= ey &&  ey < bestY2) {
                bestY2 = ey;
                ((Tuple<Integer, Position>)yres.getV2()).setV1(bestY2);
                ((Tuple<Integer, Position>)yres.getV2()).setV2(e.getValue());
            }
        }

        if (((Tuple<Integer, Position>)xres.getV2()).getV1() == Integer.MAX_VALUE) {
            xres.setV2(xres.getV1());
            xres.setV1(new Tuple<Integer, Position>(smallestX, smallestXPos));
        }
        if (((Tuple<Integer, Position>)xres.getV1()).getV1() == Integer.MIN_VALUE) {
            xres.setV1(xres.getV2());
            xres.setV2(new Tuple<Integer, Position>(biggestX, biggestXPos));
        }
        if (((Tuple<Integer, Position>)yres.getV2()).getV1() == Integer.MAX_VALUE) {
            yres.setV2(yres.getV1());
            yres.setV1(new Tuple<Integer, Position>(smallestY, smallestYPos));
        }
        if (((Tuple<Integer, Position>)yres.getV1()).getV1() == Integer.MIN_VALUE) {
            yres.setV1(yres.getV2());
            yres.setV2(new Tuple<Integer, Position>(biggestY, biggestYPos));
        }

        return new Tuple<>(xres, yres);
    }
}
