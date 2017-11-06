package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by Mihael on 6. 11. 2017.
 */
public class Attributes {

    private System system;

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "system=" + system +
                '}';
    }
}
