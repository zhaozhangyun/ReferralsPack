package com.example.android.referralsio;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;

import io.referrals.lib.BuildConfig;
import io.referrals.lib.ReferralsConfiguration;
import io.referrals.lib.ReferralsHolder;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // 打开Logcat输出，上线时，一定要关闭
            StatConfig.setDebugEnable(BuildConfig.DEBUG);
            // 注册activity生命周期，统计时长
            StatService.registerActivityLifecycleCallbacks(this);
            StatService.startStatService(this, "ASAGG526EW7E", StatConstants.VERSION);
        } catch (MtaSDkException e) {
        }

        ReferralsConfiguration configuration = new ReferralsConfiguration.Builder(this)
                .debug(BuildConfig.DEBUG)
                .periodic(false)
                .build();
        ReferralsHolder.fire(this, configuration);
    }
}
