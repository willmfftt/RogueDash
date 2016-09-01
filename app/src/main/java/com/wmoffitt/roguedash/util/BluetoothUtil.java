package com.wmoffitt.roguedash.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class BluetoothUtil {

    public interface EnableBluetoothListener {
        void onBluetoothEnabled();
        void onBluetoothDenied();
    }

    public static final int REQUEST_ENABLE_BT = 1000;

    @Nullable
    private static BluetoothAdapter mBluetoothAdapter;
    private static boolean mBluetoothNonexistent;

    public static BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null && !mBluetoothNonexistent) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                mBluetoothNonexistent = true;
            }
        }
        return mBluetoothAdapter;
    }

    public static void enableBluetoothIfNeeded(@NonNull final Activity activity) {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public static void handleEnableBTActivityResult(final int requestCode,
                                                    final int resultCode,
                                                    @NonNull final Intent data,
                                                    @NonNull final EnableBluetoothListener listener) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            listener.onBluetoothEnabled();
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            listener.onBluetoothDenied();
        }
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

}
