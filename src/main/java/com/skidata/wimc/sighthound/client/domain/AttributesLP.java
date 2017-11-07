package com.skidata.wimc.sighthound.client.domain;

/**
 * Created by skmi on 7. 11. 2017.
 */
public class AttributesLP {

    private SystemLP systemLP;

    public SystemLP getSystemLP() {
        return systemLP;
    }

    public void setSystemLP(SystemLP systemLP) {
        this.systemLP = systemLP;
    }

    @Override
    public String toString() {
        return "AttributesLP{" +
                "systemLP=" + systemLP +
                '}';
    }
}
