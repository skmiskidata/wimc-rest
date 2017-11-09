package com.skidata.wimc.tracking.impl;

import com.skidata.wimc.tracking.Pixel;
import com.skidata.wimc.tracking.Position;
import com.skidata.wimc.tracking.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LPAreaPositionMapper implements PositionMapper {
    private static final Logger logger = LoggerFactory.getLogger(LPAreaPositionMapper.class);

    Map<String, LinkedList<Double>> lastAreasPerPlate = new HashMap<>();

    @Override
    public Position mapPixelToRealWorld(MappingContext ctx, Pixel p) {
        double area = ctx.lpWidth * 0.5 * (ctx.lpHeight1 + ctx.lpHeight2);
        insert(ctx.lp, area);
        double avgArea = avg(ctx.lp);
        Tuple<CalibrationLPArea2Dist, CalibrationLPArea2Dist> nearest = nearestFor(ctx.cam.getLpArea2Dist(), avgArea);
        logger.info("tuple is {}", nearest);
        double distance = interpolate(avgArea, nearest);
        logger.info("lp={} lpAvgArea={} distance={}", ctx.lp, avgArea, distance);
        return new Position(0, 0);
    }

    private double interpolate(double avgArea, Tuple<CalibrationLPArea2Dist, CalibrationLPArea2Dist> nearest) {
//        logger.info("using for lpArea interpolation {}, {}, {}", nearest.getV1(), nearest.getV2(), avgArea );
        double a1 = nearest.getV1().lpArea;
        double a2 = nearest.getV2().lpArea;
        double d1 = nearest.getV1().dist;
        double d2 = nearest.getV2().dist;

        double dist = d1 + Math.abs((d2-d1)/(a2-a1))*(a1-avgArea);
        logger.info("params a1={}, a2={}, d1={}, d2={}, avgArea={}, dist={}", a1, a2, d1, d2, avgArea, dist );
        return dist;
    }

    private Tuple<CalibrationLPArea2Dist,CalibrationLPArea2Dist> nearestFor(List<CalibrationLPArea2Dist> lpArea2Dist, double avgArea) {
        for (int i=0; i<lpArea2Dist.size()-2; i++) {
            if (lpArea2Dist.get(i).dist >= avgArea && avgArea > lpArea2Dist.get(i+1).dist) {
               return new Tuple<>(lpArea2Dist.get(i), lpArea2Dist.get(i+1));
            }
        }

        if (avgArea > lpArea2Dist.get(0).dist) {
            return new Tuple<>(lpArea2Dist.get(0), lpArea2Dist.get(1));
        }

        return new Tuple<>(lpArea2Dist.get(lpArea2Dist.size()-1), lpArea2Dist.get(lpArea2Dist.size()-2));
    }

    private double avg(String lp) {
        LinkedList<Double> as = lastAreasPerPlate.get(lp);
        double avg=0;
        for (Double a : as) {
            avg = avg + a;
        }
        return avg/as.size();
    }

    private void insert(String lp, double area) {
        LinkedList<Double> as = lastAreasPerPlate.get(lp);
        if (null == as) {
            as = new LinkedList<>();
            lastAreasPerPlate.put(lp, as);
        }

        as.add(area);
        if (as.size()>20) {
            as.removeFirst();
        }
    }


}
