package com.wmoffitt.roguedash.wifi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class DashWifiInfo {
    private ArrayList wifiList = null;

    public DashWifiInfo() {
        setWifiList(new ArrayList<HashMap>());
    }

    public ArrayList getWifiList() {
        return wifiList;
    }

    public void setWifiList(ArrayList wifiList) {
        this.wifiList = wifiList;
    }

    // Create new Wifi Entry from parsed bluetooth data
    public void parseWifiInfo() {
        ArrayList<WifiEntry> wifiEntries = new ArrayList<>();
        // parse the data
        JSONObject BlueSON = new JSONObject();
        // going to bed :-)
    }
}
