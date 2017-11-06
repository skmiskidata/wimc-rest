package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Image {

    private long width;

    private long height;

    private long orientation;

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getOrientation() {
        return orientation;
    }

    public void setOrientation(long orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "Image{" +
                "width=" + width +
                ", height=" + height +
                ", orientation=" + orientation +
                '}';
    }
}
