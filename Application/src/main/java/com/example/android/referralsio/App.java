package com.example.android.referralsio;

import android.app.Application;
import android.graphics.Color;

import io.referrals.lib.BuildConfig;
import io.referrals.lib.ReferralsConfiguration;
import io.referrals.lib.ReferralsHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ReferralsConfiguration configuration = new ReferralsConfiguration.Builder(this)
                .debug(BuildConfig.DEBUG)
                .periodic(false)
                .notificationChannelName(getString(R.string.notification_channel_name))
                .notificationContentTitle(getString(R.string.notification_content_title))
                .notificationContentText(getString(R.string.notification_content_text))
                .notificationRGBColor(Color.RED)
                .build();
        ReferralsHolder.fire(this, configuration);
    }
}
