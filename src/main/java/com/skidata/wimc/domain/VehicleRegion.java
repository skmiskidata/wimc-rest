package com.skidata.wimc.domain;

/**
 * Created by skmi on 9. 11. 2017.
 */
public class VehicleRegion {

    private Long x;

    private Long y;

    private Long width;

    private Long height;

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "VehicleRegion{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
