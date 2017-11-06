package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Object {

    private String objectId;

    private String objectType;

    private VehicleAnnotation vehicleAnnotation;

    public VehicleAnnotation getVehicleAnnotation() {
        return vehicleAnnotation;
    }

    public void setVehicleAnnotation(VehicleAnnotation vehicleAnnotation) {
        this.vehicleAnnotation = vehicleAnnotation;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "Object{" +
                "objectId='" + objectId + '\'' +
                ", objectType='" + objectType + '\'' +
                ", vehicleAnnotation=" + vehicleAnnotation +
                '}';
    }
}
