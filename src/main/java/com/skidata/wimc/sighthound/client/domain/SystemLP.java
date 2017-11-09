package com.skidata.wimc.sighthound.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by skmi on 7. 11. 2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemLP {

    private StringLP string;

    public StringLP getString() {
        return string;
    }

    public void setString(StringLP string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return "SystemLP{" +
                "string=" + string +
                '}';
    }
}
