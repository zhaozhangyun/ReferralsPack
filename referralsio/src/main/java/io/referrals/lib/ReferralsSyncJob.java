package io.referrals.lib;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.referrals.lib.configuration.AppConfiguration;
import io.referrals.lib.configuration.ReferralsConfiguration;

public class ReferralsSyncJob extends Job {

    private static final String TAG = "ReferralsSyncJob";
    public static final String REFERRALS_TAG = "job_referrals_tag";
    private AppConfiguration appConfig;
    private JobListener listener;
    private int PENDING_ID = 1;

    public ReferralsSyncJob(ReferralsConfiguration refConfig, AppConfiguration appConfig) {
        this.appConfig = appConfig;
        listener = refConfig.getJobListener();
    }

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        L.d(TAG, "Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic()
                + ", transient " + params.isTransient());
        Bundle data = new ReferralsSyncEngine(getContext(), appConfig).sync();
        L.d(TAG, "call onRunJob(): " + data);

        if (data == null) {
            L.w(TAG, "Engine data is null.");
            if (listener != null) {
                listener.onJobFinished(data != null && data.getBoolean("result"));
            }
            return (data != null && data.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
        } else if (!data.getBoolean("result")) {
            if (!data.getBoolean("downloaded")) {
                L.w(TAG, "Failed to download [" + appConfig.getPackageName() + "]");
                if (listener != null) {
                    listener.onJobFinished(data != null && data.getBoolean("result"));
                }
                return (data != null && data.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
            }
        } else if (data.getBoolean("result")) {
            if (data.getBoolean("installed")) {
                L.w(TAG, "[" + appConfig.getPackageName() + "] has been " + data.getBoolean("installed"));
                if (listener != null) {
                    listener.onJobFinished(data != null && data.getBoolean("result"));
                }
                return (data != null && data.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
            }
        }

        if (!appConfig.isNotify()) {
            Bundle b = doSecondaryTask();
            L.d(TAG, "call doSecondaryTask(): " + b);
            if (listener != null) {
                listener.onJobFinished(b != null && b.getBoolean("result"));
            }
            return (b != null && b.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
        }

        L.d(TAG, "start await for " + appConfig.getInstallDelay() + " seconds");
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(appConfig.getInstallDelay(), TimeUnit.SECONDS);
        } catch (Exception e) {
            L.e(TAG, "latch await error: ", e);
        }
        L.d(TAG, "await finished");

        Intent intent = new Intent(getContext(), ReferralsReceiver.class);
        intent.putExtras(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), PENDING_ID,
                intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(REFERRALS_TAG,
                    appConfig.getNotificationChannelName(), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Job referrals job");
            getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), REFERRALS_TAG)
                .setContentTitle(appConfig.getNotificationContentTitle())
                .setContentText(appConfig.getNotificationContentText())
                .setAutoCancel(true)
                .setChannelId(REFERRALS_TAG)
                .setSound(null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(appConfig.getNotificationSmallIcon())
                .setLargeIcon(appConfig.getNotificationLargeIcon())
                .setShowWhen(true)
                .setColor(appConfig.getNotificationColor())
                .setLocalOnly(true);
        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), builder.build());

        if (listener != null) {
            listener.onJobFinished(data != null && data.getBoolean("result"));
        }
        return (data != null && data.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
    }

    private Bundle doSecondaryTask() {
        // TODO do something here ...
        Bundle b = new Bundle();
        b.putBoolean("result", false);
        return b;
    }

    public interface JobListener {
        void onJobFinished(boolean result);
    }
}
