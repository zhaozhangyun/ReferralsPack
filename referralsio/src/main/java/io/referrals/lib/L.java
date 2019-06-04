package io.referrals.lib;

import android.util.Log;

public final class L {

    private final static String LOG_TAG = "referralsio";
    private final static String TAG_PREFIX = "[ReferralsIO - ";
    private static boolean sEnable = BuildConfig.DEBUG;

    private L() {
    }

    public static void v(String tag, String message) {
        if (!sEnable && !Log.isLoggable(LOG_TAG, Log.VERBOSE)) {
            return;
        }
        Log.v(TAG_PREFIX + tag + "]", message, null);
    }

    public static void d(String tag, String message) {
        if (!sEnable && !Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            return;
        }
        Log.d(TAG_PREFIX + tag + "]", message, null);
    }

    public static void i(String tag, String message) {
        if (!sEnable && !Log.isLoggable(LOG_TAG, Log.INFO)) {
            return;
        }
        Log.i(TAG_PREFIX + tag + "]", message);
    }

    public static void w(String tag, String message) {
        if (!sEnable && !Log.isLoggable(LOG_TAG, Log.WARN)) {
            return;
        }
        Log.w(TAG_PREFIX + tag + "]", message);
    }

    public static void e(String tag, String message) {
        e(tag, message, null);
    }

    public static void e(String tag, String message, Throwable tr) {
        if (!sEnable && !Log.isLoggable(LOG_TAG, Log.ERROR)) {
            return;
        }
        Log.e(TAG_PREFIX + tag + "]", message, tr);
    }
}
