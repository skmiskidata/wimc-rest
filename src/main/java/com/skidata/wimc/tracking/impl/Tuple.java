package com.skidata.wimc.tracking.impl;

public class Tuple<T, V> {
    T v1;
    V v2;

    public Tuple(T v1, V v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T getV1() {
        return v1;
    }

    public V getV2() {
        return v2;
    }

    public void setV1(T v1) {
        this.v1 = v1;
    }

    public void setV2(V v2) {
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }
}
