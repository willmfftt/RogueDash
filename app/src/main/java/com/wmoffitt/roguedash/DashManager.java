package com.wmoffitt.roguedash;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import com.wmoffitt.roguedash.util.BluetoothUtil;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class DashManager {

    public interface EnableBluetoothListener {
        void onBluetoothEnabled();
        void onBluetoothDenied();
    }

    public enum BluetoothState {
        ENABLED,
        DISABLED,
        ENABLING
    }

    public static final int REQUEST_ENABLE_BT = 1000;

    @NonNull private final Activity mActivity;
    @NonNull private BluetoothState mState = BluetoothState.DISABLED;

    public DashManager(@NonNull final Activity activity) {
        mActivity = activity;
    }

    public BluetoothState initialBluetoothSetup() {
        if (mState == BluetoothState.DISABLED) {
            final BluetoothAdapter adapter = BluetoothUtil.getBluetoothAdapter();
            if (adapter == null) {
                mState = BluetoothState.DISABLED;
                return mState;
            }

            if (BluetoothUtil.enableBluetoothIfNeeded(mActivity)) {
                // Waiting for user to setup bluetooth, setup will be called again
                mState = BluetoothState.ENABLING;
            } else {
                mState = BluetoothState.ENABLED;
            }
        }
        return mState;
    }

    public void setup() {
        if (mState != BluetoothState.ENABLED) {
            return;
        }

        // Check if the dash is currently paired
        if (BluetoothUtil.isDeviceNamePaired(Constants.DASH_DEVICE_NAME)) {
            // Get a handle to the device

        }
    }

    public void handleEnableBTActivityResult(final int requestCode,
                                                    final int resultCode,
                                                    @NonNull final EnableBluetoothListener listener) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            mState = BluetoothState.ENABLED;
            listener.onBluetoothEnabled();
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            mState = BluetoothState.DISABLED;
            listener.onBluetoothDenied();
        }
    }

}
