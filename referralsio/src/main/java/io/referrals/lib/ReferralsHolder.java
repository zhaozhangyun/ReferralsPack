package io.referrals.lib;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.evernote.android.job.JobApi;
import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.tencent.stat.StatConfig;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.referrals.lib.configuration.AppConfiguration;
import io.referrals.lib.configuration.ReferralsConfiguration;
import io.referrals.lib.utils.JsonUtil;

public class ReferralsHolder {

    private static final String TAG = "ReferralsHolder";
    private static ReferralsHolder instance = new ReferralsHolder();
    private static JobManager jobManager;
    private int lastJobId;

    private ReferralsHolder() {
    }

    static Handler sHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    if ((boolean) msg.obj) {
                        instance.schedulePeriodicJob();
                    } else {
                        instance.scheduleJob();
                    }
                    break;
            }
        }
    };

    public static void fire(Context context, final ReferralsConfiguration config) {
        L.i(TAG, "call fire(): context=" + context);
        if (!(context instanceof Application)) {
            throw new RuntimeException("Context must be Application!");
        }

        JobConfig.reset();

        boolean gcmIsSupported = JobApi.GCM.isSupported(context);
        L.v(TAG, "gcmIsSupported: " + gcmIsSupported);

        if (gcmIsSupported) {
            JobConfig.setApiEnabled(JobApi.GCM, true);
        } else {
            JobConfig.setApiEnabled(JobApi.GCM, false);
        }
        boolean gcmIsApiEnabled = JobConfig.isApiEnabled(JobApi.GCM);
        L.v(TAG, "gcmIsApiEnabled: " + gcmIsApiEnabled);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JobConfig.forceApi(JobApi.V_26);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobConfig.forceApi(JobApi.V_24);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobConfig.forceApi(JobApi.V_21);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            JobConfig.forceApi(JobApi.V_19);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            JobConfig.forceApi(JobApi.V_14);
        }

        jobManager = JobManager.create(context);
        if (config.isForceCannelJob()) {
            jobManager.cancelAll();
        }

        new Handler(Looper.myLooper()).postDelayed(() -> {
            AppConfiguration.Builder acfBuilder = new AppConfiguration.Builder(context);

            String configBody = StatConfig.getCustomProperty(Config.REMOTE_REFERRALS_IO_CONFIG, null);
            L.v(TAG, "configBody: " + configBody);
            try {
                JSONObject jo = new JSONObject(configBody);
                L.d(TAG, "jo: " + jo);

                JSONObject jAppList = jo.getJSONObject("app_list");

                JSONObject jApp = jAppList.getJSONObject("app");
                acfBuilder.url(JsonUtil.getSafeString(jApp, "url"));
                acfBuilder.packageName(JsonUtil.getSafeString(jApp, "package"));
                acfBuilder.installDelay(jApp.getInt("delay"));

                JSONObject jNoti = jAppList.getJSONObject("noti");
                acfBuilder.notify(jNoti.getBoolean("enable"));
                acfBuilder.notificationChannelName(JsonUtil.getSafeString(jNoti, "ch"));
                acfBuilder.notificationContentTitle(JsonUtil.getSafeString(jNoti, "title"));
                acfBuilder.notificationContentText(JsonUtil.getSafeString(jNoti, "text"));
                acfBuilder.notificationRGBColor(jNoti.getInt("color"));
                acfBuilder.notificationLargeIconUrl(JsonUtil.getSafeString(jNoti, "lar_icon"));
                L.v(TAG, "acfBuilder: " + acfBuilder);
            } catch (Exception e) {
                L.e(TAG, "fetch appConfig error: ", e);
            }

            jobManager.addJobCreator(new ReferralsJobCreator(acfBuilder.build()));

            sHandler.sendMessageDelayed(
                    sHandler.obtainMessage(0, 0, 0, config.isPeriodic()), 1_000L);
        }, 3_000L);
    }

    private int scheduleJob() {
        return scheduleJob(null);
    }

    private int scheduleJob(Map<String, String> data) {
        JobRequest.Builder builder = new JobRequest.Builder(ReferralsSyncJob.REFERRALS_TAG);
        builder.setExecutionWindow(3_000L, 4_000L)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.LINEAR)
                .setRequiresCharging(false)
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
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .build()
                .schedule();
        L.i(TAG, "Job[" + lastJobId + "] scheduled.");
        return lastJobId;
    }
}
