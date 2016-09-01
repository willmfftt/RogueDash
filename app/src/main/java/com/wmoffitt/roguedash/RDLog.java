package com.wmoffitt.roguedash;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author William Moffitt
 * @version 1.0 8/31/16
 */
public class RDLog {

    private static final String TAG = "RogueDash";

    public static void d(@NonNull final String msg) {
        Log.d(TAG, msg);
    }

    public static void e(@NonNull final String msg) {
        Log.e(TAG, msg);
    }

    public static void i(@NonNull final String msg) {
        Log.i(TAG, msg);
    }

}
