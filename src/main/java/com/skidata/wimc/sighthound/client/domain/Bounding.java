package com.skidata.wimc.sighthound.client.domain;

import java.util.Arrays;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Bounding {

    private Vertice[] vertices;

    public Vertice[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertice[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return "Bounding{" +
                "vertices=" + Arrays.toString(vertices) +
                '}';
    }
}
