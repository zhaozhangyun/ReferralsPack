package io.referrals.lib;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.WorkerThread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.referrals.lib.configuration.AppConfiguration;
import io.referrals.lib.utils.DownloadCallback;
import io.referrals.lib.utils.ReferralsUtil;

/**
 * @author rwondratschek
 */
public class ReferralsSyncEngine {

    private static final String TAG = "ReferralsSyncEngine";
    private Context context;
    private AppConfiguration config;
    private Bundle bundle;
    private CountDownLatch latch;

    public ReferralsSyncEngine(Context context, AppConfiguration config) {
        this.context = context;
        this.config = config;
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

        bundle.putString("file_path", config.getUrl());
        if (ReferralsUtil.hasInstalled(context, config.getPackageName())) {
            L.d(TAG, "the apk has installed");
            bundle.putBoolean("result", true);
        } else {
            ReferralsUtil.getApkInBackground(context, config.getUrl(), new DownloadCallback() {
                @Override
                public void downloadSuccess() {
                    bundle.putBoolean("result", true);
                    latch.countDown();
                }

                @Override
                public void downloadFailed() {
                    bundle.putBoolean("result", false);
                    latch.countDown();
                }
            });

            try {
                latch.await(15, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
            }
        }

        L.i(TAG, "sync result: " + bundle);
        return bundle;
    }
}
