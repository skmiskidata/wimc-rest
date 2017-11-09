package com.skidata.wimc.tracking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by skmi on 9. 11. 2017.
 */
public class UniqueCar {

    private String uuid;

    private String plate;

    private Map<String, Position> positionPerCamera = new ConcurrentHashMap<>();

    private Map<String, Long> lastSeenAtPerCamera = new ConcurrentHashMap<>();

    private Position bestPosition = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, Position> getPositionPerCamera() {
        return positionPerCamera;
    }

    public void setPositionPerCamera(Map<String, Position> positionPerCamera) {
        this.positionPerCamera = positionPerCamera;
    }

    public Position getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(Position bestPosition) {
        this.bestPosition = bestPosition;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Map<String, Long> getLastSeenAtPerCamera() {
        return lastSeenAtPerCamera;
    }

    public void setLastSeenAtPerCamera(Map<String, Long> lastSeenAtPerCamera) {
        this.lastSeenAtPerCamera = lastSeenAtPerCamera;
    }
}
