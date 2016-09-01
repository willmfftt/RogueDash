package com.wmoffitt.roguedash.wifi;

/**
 * Created by slimjim2234 on 8/31/16.
 */
public class WifiEntry {
    private String status = null;
    private String ssid = null;
    private String bssid = null;
    private String keymgmt = null;
    private String frequency = null;
    private String rssi = null;
    private String listIndex = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getKeymgmt() {
        return keymgmt;
    }

    public void setKeymgmt(String keymgmt) {
        this.keymgmt = keymgmt;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getListIndex() {
        return listIndex;
    }

    public void setListIndex(String listIndex) {
        this.listIndex = listIndex;
    }

    public WifiEntry() {
        setStatus("");
        setSsid("");
        setBssid("");
        setKeymgmt("");
        setFrequency("");
        setRssi("");
        setListIndex("");
    }

}
