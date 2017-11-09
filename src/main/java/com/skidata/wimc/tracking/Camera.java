package com.skidata.wimc.tracking;

import com.skidata.wimc.tracking.impl.CalibrationLPArea2Dist;
import com.skidata.wimc.tracking.impl.CalibrationPixel2Pos;

import java.util.List;
import java.util.Set;

public class Camera {
    String id;
    Position pos;
    Set<CalibrationPixel2Pos> pixel2Pos;
    List<CalibrationLPArea2Dist> lpArea2Dist;
    double angle=90;

    public Camera(String id, Position pos, Set<CalibrationPixel2Pos> pixel2Pos, List<CalibrationLPArea2Dist> lpArea2Dist, double angle) {
        this.id = id;
        this.pos = pos;
        this.pixel2Pos = pixel2Pos;
        this.lpArea2Dist = lpArea2Dist;
        this.angle = angle;
    }

    public String getId() {
        return id;
    }

    public Position getPos() {
        return pos;
    }

    public Set<CalibrationPixel2Pos> getPixel2Pos() {
        return pixel2Pos;
    }

    public List<CalibrationLPArea2Dist> getLpArea2Dist() {
        return lpArea2Dist;
    }

    public double getAngle() {
        return angle;
    }
}
