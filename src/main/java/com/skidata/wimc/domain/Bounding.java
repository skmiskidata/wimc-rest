package com.skidata.wimc.domain;

/**
 * Created by skmi on 8. 11. 2017.
 */
public class Bounding {

    private Long x1;

    private Long y1;

    private Long x2;

    private Long y2;

    private Long x3;

    private Long y3;

    private Long x4;

    private Long y4;

    public Long getX1() {
        return x1;
    }

    public void setX1(Long x1) {
        this.x1 = x1;
    }

    public Long getY1() {
        return y1;
    }

    public void setY1(Long y1) {
        this.y1 = y1;
    }

    public Long getX2() {
        return x2;
    }

    public void setX2(Long x2) {
        this.x2 = x2;
    }

    public Long getY2() {
        return y2;
    }

    public void setY2(Long y2) {
        this.y2 = y2;
    }

    public Long getX3() {
        return x3;
    }

    public void setX3(Long x3) {
        this.x3 = x3;
    }

    public Long getY3() {
        return y3;
    }

    public void setY3(Long y3) {
        this.y3 = y3;
    }

    public Long getX4() {
        return x4;
    }

    public void setX4(Long x4) {
        this.x4 = x4;
    }

    public Long getY4() {
        return y4;
    }

    public void setY4(Long y4) {
        this.y4 = y4;
    }

    @Override
    public String toString() {
        return "Bounding{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", x3=" + x3 +
                ", y3=" + y3 +
                ", x4=" + x4 +
                ", y4=" + y4 +
                '}';
    }
}
