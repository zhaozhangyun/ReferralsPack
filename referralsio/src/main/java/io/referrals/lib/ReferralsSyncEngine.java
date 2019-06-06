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
class ReferralsSyncEngine {

    private static final String TAG = "ReferralsSyncEngine";
    private Context context;
    private AppConfiguration appConfig;
    private Bundle bundle;
    private CountDownLatch latch;

    ReferralsSyncEngine(Context context, AppConfiguration appConfig) {
        this.context = context;
        this.appConfig = appConfig;
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

        bundle.putString("file_path", appConfig.getUrl());
        if (ReferralsUtil.hasInstalled(context, appConfig.getPackageName())) {
            bundle.putBoolean("installed", true);
            bundle.putBoolean("result", true);
        } else {
            bundle.putBoolean("installed", false);
            ReferralsUtil.getApkInBackground(context, appConfig, new DownloadCallback() {
                @Override
                public void downloadSuccess() {
                    bundle.putBoolean("downloaded", true);
                    bundle.putBoolean("result", true);
                    latch.countDown();
                }

                @Override
                public void downloadFailed() {
                    bundle.putBoolean("downloaded", false);
                    bundle.putBoolean("result", false);
                    latch.countDown();
                }
            });

            try {
                latch.await(15, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
            }
        }

        L.i(TAG, "Sync result: " + bundle);
        return bundle;
    }
}
