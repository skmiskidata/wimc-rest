package com.skidata.wimc.domain;

/**
 * Created by skmi on 30. 10. 2017.
 */
public class PlateCoordinate {

    private Long x;

    private Long y;

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

    @Override
    public String toString() {
        return "PlateCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
