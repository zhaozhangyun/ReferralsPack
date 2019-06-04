package io.referrals.lib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReferralsHolder {

    private static final String TAG = "ReferralsHolder";
    private static ReferralsHolder instance = new ReferralsHolder();
    private static JobManager jobManager;
    private static ReferralsConfiguration sConfig;
    private int lastJobId;

    private ReferralsHolder() {
    }

    public static void fire(Context context, final ReferralsConfiguration config) {
        L.i(TAG, "call fire(): context=" + context);
        sConfig = config;
        jobManager = JobManager.create(context);
        if (config.isForceCannelJob()) {
            jobManager.cancelAll();
        }
//        jobManager.addJobCreator(new ReferralsJobCreator(config));
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (config.isPeriodic()) {
                    instance.schedulePeriodicJob();
                } else {
                    instance.scheduleJob();
                }
            }
        }, 2_000L);
    }

    static ReferralsConfiguration getConfig() {
        return sConfig;
    }

    private int scheduleJob() {
        return scheduleJob(null);
    }

    private int scheduleJob(Map<String, String> data) {
        JobRequest.Builder builder = new JobRequest.Builder(ReferralsSyncJob.REFERRALS_TAG);
        builder.setExecutionWindow(3_000L, 4_000L)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.LINEAR)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setRequirementsEnforced(true);
        if (data != null) {
            PersistableBundleCompat extras = new PersistableBundleCompat();
            try {
                Set<String> set = data.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = data.get(key);
                    extras.putString(key, value);
                }
                builder.setExtras(extras);
            } catch (Exception e) {
            }
        }
        lastJobId = builder.build().schedule();
        L.i(TAG, "Job[" + lastJobId + "] scheduled.");
        return lastJobId;
    }

    private int schedulePeriodicJob() {
        lastJobId = new JobRequest.Builder(ReferralsSyncJob.REFERRALS_TAG)
                .setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .build()
                .schedule();
        L.i(TAG, "Job[" + lastJobId + "] scheduled.");
        return lastJobId;
    }
}
