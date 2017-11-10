package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Camera;
import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import com.skidata.wimc.tracking.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearPositionMapper implements PositionMapper {

    private static final Logger logger = LoggerFactory.getLogger(LinearPositionMapper.class);

    @Override
    public Position mapPixelToRealWorld(MappingContext ctx, Pixel p) {
                Tuple<Tuple<Tuple<Integer, Position>, Tuple<Integer, Position>>,
                Tuple<Tuple<Integer, Position>, Tuple<Integer, Position>>> ps = nearestFor(ctx.cam, p);
        int pxx1 =  ps.getV1().getV1().getV1();
        int pox1 =  ps.getV1().getV1().getV2().getX();
        int pxx2 =  ps.getV1().getV2().getV1();
        int pox2 =  ps.getV1().getV2().getV2().getX();
        int pxy1 =  ps.getV2().getV1().getV1();
        int poy1 =  ps.getV2().getV1().getV2().getY();
        int pxy2 =  ps.getV2().getV2().getV1();
        int poy2 =  ps.getV2().getV2().getV2().getY();


        Position p1 = moveAndRotate(ctx, new Position(pox1, poy1));
        Position p2 = moveAndRotate(ctx, new Position(pox2, poy2));
        int x = interpolate(0, pxx1, p1.getX(), pxx2, p2.getX(), p.getX());
        int y = interpolate(0, pxy1, p1.getY(), pxy2, p2.getY(), p.getY());
        Position r = rotateAndMove(ctx, new Position(x, y));
//        if (r.getY()<280) {
//            return new Position(r.getX()-40, r.getY()-40);
//        }
        return r;
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
        for (CalibrationPixel2Pos e : c.getPixel2Pos()) {
            int ex = e.pixel.getX();
            if (ex < smallestX) {
                smallestX = ex;
                smallestXPos = e.pos;
            }
            if (biggestX < ex) {
                biggestX = ex;
                biggestXPos = e.pos;
            }
            if (ex <= p.getX() && bestX1 <= ex ) {
                bestX1 = ex;
                ((Tuple<Integer, Position>)xres.getV1()).setV1(bestX1);
                ((Tuple<Integer, Position>)xres.getV1()).setV2(e.pos);
            }
            if (p.getX() < ex &&  ex < bestX2) {
                bestX2 = ex;
                ((Tuple<Integer, Position>)xres.getV2()).setV1(bestX2);
                ((Tuple<Integer, Position>)xres.getV2()).setV2(e.pos);
            }

            int ey = e.pixel.getY();
            if (ey < smallestY) {
                smallestY = ey;
                smallestYPos = e.pos;
            }
            if (biggestY < ey) {
                biggestY = ey;
                biggestYPos = e.pos;
            }
            if (ey <= p.getY() && bestY1 <= ey ) {
                bestY1 = ey;
                ((Tuple<Integer, Position>)yres.getV1()).setV1(bestY1);
                ((Tuple<Integer, Position>)yres.getV1()).setV2(e.pos);
            }
            if (p.getY() < ey &&  ey < bestY2) {
                bestY2 = ey;
                ((Tuple<Integer, Position>)yres.getV2()).setV1(bestY2);
                ((Tuple<Integer, Position>)yres.getV2()).setV2(e.pos);
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

    private double dist(Pixel p1, Pixel p2) {
        double xd = p1.getX() - p2.getX();
        double yd = p1.getY() - p2.getY();
        return Math.sqrt(xd*xd+yd*yd);
    }
}
