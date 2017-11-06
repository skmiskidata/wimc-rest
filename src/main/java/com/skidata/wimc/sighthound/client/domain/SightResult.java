package com.skidata.wimc.sighthound.client.domain;

import java.util.Arrays;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class SightResult {

    private Image image;

    private String requestId;

    private Object[] objects;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "SightResult{" +
                "image=" + image +
                ", requestId='" + requestId + '\'' +
                ", objects=" + Arrays.toString(objects) +
                '}';
    }
}
