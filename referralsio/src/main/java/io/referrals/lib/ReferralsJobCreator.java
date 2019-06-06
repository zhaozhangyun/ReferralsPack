package io.referrals.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

import io.referrals.lib.configuration.AppConfiguration;
import io.referrals.lib.configuration.ReferralsConfiguration;

public class ReferralsJobCreator implements JobCreator {
    private static final String TAG = "ReferralsJobCreator";
    private ReferralsConfiguration refConfig;
    private AppConfiguration appConfig;

    public ReferralsJobCreator(ReferralsConfiguration refConfig, AppConfiguration appConfig) {
        this.refConfig = refConfig;
        this.appConfig = appConfig;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        L.d(TAG, "call create(): " + tag);
        switch (tag) {
            case ReferralsSyncJob.REFERRALS_TAG:
                return new ReferralsSyncJob(refConfig, appConfig);
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            L.d(TAG, "call addJobCreator: " + manager);
//            manager.addJobCreator(new ReferralsJobCreator(ReferralsHolder.getConfig()));
        }
    }
}
