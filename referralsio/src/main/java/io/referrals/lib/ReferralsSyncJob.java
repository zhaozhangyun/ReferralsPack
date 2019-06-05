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
    private ReferralsConfiguration config;
    private int PENDING_ID = 1;

    public ReferralsSyncJob(ReferralsConfiguration config) {
        this.config = config;
    }

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        Bundle b = new ReferralsSyncEngine(getContext()).sync();
        L.d(TAG, "call onRunJob(): " + b);

        if (!config.isNotify()) {
            doSecondaryTask();
            return (b != null && b.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
        }

        Intent intent = new Intent(getContext(), ReferralsReceiver.class);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), PENDING_ID,
                intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(REFERRALS_TAG,
                    config.getNotificationChannelName(), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Job referrals job");
            getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        L.v(TAG, "Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic()
                + ", transient " + params.isTransient());

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
                "ref_io_notification_icon", "drawable",
                config.getContext().getPackageName());
        if (resSmallIconId > 0) {
            builder.setSmallIcon(resSmallIconId);
        } else {
            builder.setSmallIcon(R.drawable.ref_io_notification_icon_default);
        }

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), builder.build());

        return (b != null && b.getBoolean("result")) ? Result.SUCCESS : Result.FAILURE;
    }

    private void doSecondaryTask() {
        // TODO do something here ...
    }
}
