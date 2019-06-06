package io.referrals.lib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import io.referrals.lib.configuration.AppConfiguration;
import io.referrals.lib.configuration.ReferralsConfiguration;

public class ReferralsJobCreator implements JobCreator {
    private static final String TAG = "ReferralsJobCreator";
    private static ReferralsJobCreator sInstance;
    private static ReferralsConfiguration sRefConfig;
    private static AppConfiguration sAppConfig;

    public static ReferralsJobCreator attach(ReferralsConfiguration refConfig, AppConfiguration appConfig) {
        sRefConfig = refConfig;
        sAppConfig = appConfig;
        sInstance = new ReferralsJobCreator(refConfig, appConfig);
        return sInstance;
    }

    private ReferralsJobCreator(ReferralsConfiguration refConfig, AppConfiguration appConfig) {
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        L.d(TAG, "call create(): " + tag);
        switch (tag) {
            case ReferralsSyncJob.REFERRALS_TAG:
                return new ReferralsSyncJob(sRefConfig, sAppConfig);
            default:
                return null;
        }
    }
}
