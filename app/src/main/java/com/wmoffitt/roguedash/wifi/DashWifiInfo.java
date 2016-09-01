package com.wmoffitt.roguedash.wifi;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class DashWifiInfo {

    @NonNull
    public List<WifiAccessPoint> getWifiAccessPoints(@NonNull final BluetoothDevice device) {
        final List<WifiAccessPoint> accessPoints = new ArrayList<>();
        final int accessPointCount = getWifiAccessPointCount(device);



        return accessPoints;
    }

    private int getWifiAccessPointCount(@NonNull final BluetoothDevice device) {
        return -1;
    }

}
