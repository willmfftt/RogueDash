package com.wmoffitt.roguedash.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
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

    public interface BluetoothDiscoveryListener {
        void onBluetoothDiscovered(@NonNull final BluetoothDevice device);
        void onBluetoothDiscoveryTimeout();
    }

    public static final int REQUEST_ENABLE_BT = 1000;

    @Nullable private static BluetoothAdapter mBluetoothAdapter;
    private static boolean mBluetoothNonexistent;
    @Nullable private static BroadcastReceiver mBluetoothReceiver;

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

    public static void startDiscoveryOfDeviceName(@NonNull final Context context,
                                                  @NonNull final String deviceName,
                                                  @NonNull final BluetoothDiscoveryListener listener) {
        if (mBluetoothReceiver == null && mBluetoothAdapter != null) {
            // Create a BroadcastReceiver for ACTION_FOUND
            mBluetoothReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                        if (device.getName().equals(deviceName)) {
                            listener.onBluetoothDiscovered(device);

                            // Unregister this receiver
                            context.unregisterReceiver(mBluetoothReceiver);
                            mBluetoothReceiver = null;

                            // Stop discovery
                            mBluetoothAdapter.cancelDiscovery();
                        }
                    }
                }
            };

            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBluetoothReceiver, filter);

            // Start discovery
            mBluetoothAdapter.startDiscovery();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onBluetoothDiscoveryTimeout();

                    // Unregister the receiver
                    context.unregisterReceiver(mBluetoothReceiver);

                    // Stop discovery
                    mBluetoothAdapter.cancelDiscovery();
                }
            }, 1000L * 30L);
        }
    }

}
