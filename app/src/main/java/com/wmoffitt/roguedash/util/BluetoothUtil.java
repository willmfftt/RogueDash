package com.wmoffitt.roguedash.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wmoffitt.roguedash.DashManager;

import java.util.Set;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class BluetoothUtil {

    public interface BluetoothDiscoveryListener {
        void onBluetoothDiscovered(@NonNull final BluetoothDevice device, int rssi, byte[] scanRecord);
        void onBluetoothDiscoveryTimeout();
    }

    private static final long SCAN_PERIOD = 10L * 1000L; // 10 secs

    @Nullable private static BluetoothAdapter mBluetoothAdapter;
    private static boolean mBluetoothNonexistent;
    private static boolean mScanning;

    public static BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null && !mBluetoothNonexistent) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                mBluetoothNonexistent = true;
            }
        }
        return mBluetoothAdapter;
    }

    /**
     * Ask user to enable bluetooth, unless not required
     *
     * @param activity Activity to pass result back to
     * @return True if enable needed, false if not
     */
    public static boolean enableBluetoothIfNeeded(@NonNull final Activity activity) {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, DashManager.REQUEST_ENABLE_BT);
            return true;
        }
        return false;
    }

    public static boolean isDeviceNamePaired(@NonNull final String deviceName) {
        if (mBluetoothAdapter == null) {
            return false;
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(deviceName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static void startDiscoveryOfDeviceName(@NonNull final String deviceName,
                                                  @NonNull final BluetoothDiscoveryListener listener) {
        if (mBluetoothAdapter != null) {
            // Callback
            final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (device.getName().equals(deviceName)) {
                        // Stop scanning
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(this);

                        listener.onBluetoothDiscovered(device, rssi, scanRecord);
                    }
                }
            };

            // Stops scanning after a pre-defined scan period.
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        // Stop scanning
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(callback);

                        listener.onBluetoothDiscoveryTimeout();
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(callback);
        }
    }

    public static BluetoothGatt makeGattConnection(@NonNull final Context context,
                                                   @NonNull final BluetoothDevice device,
                                                   @NonNull final BluetoothGattCallback callback) {
        return device.connectGatt(context, false, callback);
    }

}
