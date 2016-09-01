package com.wmoffitt.roguedash.wifi;

/**
 * Created by slimjim2234 on 8/31/16.
 */
public enum WifiKeyMgmt {
    OPEN("OPEN"),
    WPA2_PSK("WPA2_PSK");

    private final String type;

    WifiKeyMgmt(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
