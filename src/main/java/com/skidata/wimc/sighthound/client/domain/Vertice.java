package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Vertice {

    private long x;

    private long y;


    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "Vertice{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
