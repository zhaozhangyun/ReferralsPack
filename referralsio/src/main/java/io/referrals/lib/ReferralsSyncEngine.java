package io.referrals.lib;

import android.content.Context;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;

/**
 * @author rwondratschek
 */
public class ReferralsSyncEngine {

    private static final String TAG = "ReferralsSyncEngine";
    private final Context context;

    public ReferralsSyncEngine(Context context) {
        this.context = context;
    }

    @WorkerThread
    public boolean sync() {
        // do something fancy
        L.d(TAG, "call sync()");

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }

        SystemClock.sleep(1_000);
        boolean success = Math.random() > 0.1; // successful 90% of the time
        return success;
    }
}
