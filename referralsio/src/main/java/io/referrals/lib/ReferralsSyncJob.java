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

public class ReferralsSyncJob extends Job {

    private static final String TAG = "ReferralsSyncJob";
    public static final String REFERRALS_TAG = "job_referrals_tag";
    private AppConfiguration config;
    private int PENDING_ID = 1;

    public ReferralsSyncJob(AppConfiguration config) {
        this.config = config;
    }

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        L.d(TAG, "Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic()
                + ", transient " + params.isTransient());
        Bundle data = new ReferralsSyncEngine(getContext(), config).sync();
        L.d(TAG, "call onRunJob(): " + data);

        if (data == null) {
            L.w(TAG, "Engine data is null.");
            return Result.FAILURE;
        } else if (!data.getBoolean("result")) {
            L.w(TAG, "Engine data result is false.");
            return Result.FAILURE;
        }

        if (!config.isNotify()) {
            Bundle b = doSecondaryTask();
            L.d(TAG, "call doSecondaryTask(): " + b);
            return (b != null && b.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
        }

        Intent intent = new Intent(getContext(), ReferralsReceiver.class);
        intent.putExtras(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), PENDING_ID,
                intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(REFERRALS_TAG,
                    config.getNotificationChannelName(), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Job referrals job");
            getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), REFERRALS_TAG)
                .setContentTitle(config.getNotificationContentTitle())
                .setContentText(config.getNotificationContentText())
                .setAutoCancel(true)
                .setChannelId(REFERRALS_TAG)
                .setSound(null)
                .setContentIntent(pendingIntent)
                .setLargeIcon(config.getNotificationLargeIcon())
                .setShowWhen(true)
                .setColor(config.getNotificationColor())
                .setLocalOnly(true);
        int resSmallIconId = config.getContext().getResources().getIdentifier(
                Config.DEFAULT_REFERRALS_IO_NOTIFICATION_SMALL_ICON_ID, "drawable",
                config.getContext().getPackageName());
        if (resSmallIconId > 0) {
            builder.setSmallIcon(resSmallIconId);
        } else {
            builder.setSmallIcon(R.drawable.ref_io_notification_icon_default);
        }

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), builder.build());

        return (data != null && data.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
    }

    private Bundle doSecondaryTask() {
        // TODO do something here ...
        Bundle b = new Bundle();
        b.putBoolean("result", false);
        return b;
    }
}
