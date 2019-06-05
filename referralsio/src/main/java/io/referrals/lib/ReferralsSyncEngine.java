package io.referrals.lib;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.referrals.lib.utils.DownloadCallback;
import io.referrals.lib.utils.ReferralsUtil;

/**
 * @author rwondratschek
 */
public class ReferralsSyncEngine {

    private static final String TAG = "ReferralsSyncEngine";
    private final Context context;
    private Bundle bundle;
    private CountDownLatch latch;

    public ReferralsSyncEngine(Context context) {
        this.context = context;
    }

    @WorkerThread
    public Bundle sync() {
        // do something fancy
        L.d(TAG, "call sync()");

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }

        latch = new CountDownLatch(1);
        bundle = new Bundle();

//        SystemClock.sleep(1_000L);
//        result = Math.random() > 0.1; // successful 90% of the time
//        L.d(TAG, "sync result: " + result);

        final String filePath = "http://dldir1.qq.com/foxmail/qqmail_android_5.6.4.10138276.2438_0.apk";
        bundle.putString("file_path", filePath);
        if (ReferralsUtil.hasInstalled(context, "com.tencent.androidqqmail")) {
            L.d(TAG, "the apk has installed");
            bundle.putBoolean("result", true);
        } else {
            ReferralsUtil.getApkInBackground(context, filePath, new DownloadCallback() {
                @Override
                public void downloadSuccess() {
                    bundle.putBoolean("result", true);
                    latch.countDown();
                }

                @Override
                public void downloadFailed() {
                    Log.d(TAG, "download failed");
                    bundle.putBoolean("result", false);
                    latch.countDown();
                }
            });

            try {
                latch.await(15, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
            }
        }

        return bundle;
    }
}
