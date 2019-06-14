package com.example.android.referralsio;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;

import java.util.List;

import io.referrals.lib.ReferralsHolder;
import io.referrals.lib.configuration.ReferralsConfiguration;

public class App extends Application {

    private static final String TAG = "App";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 在主进程设置信鸽相关的内容
        if (isMainProcess()) {
            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
            // 收到通知时，会调用本回调函数。
            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
            XGPushManager.setNotifactionCallback(xGNotifaction -> {
                Log.i(TAG, "处理信鸽通知：" + xGNotifaction);
                // 获取标签、内容、自定义内容
                String title = xGNotifaction.getTitle();
                String content = xGNotifaction.getContent();
                String customContent = xGNotifaction.getCustomContent();
                // 其它的处理
                // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                xGNotifaction.doNotify();
            });
        }

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
                .jobListener(result -> {
                    Log.d(TAG, "jobListener result: " + result);
                })
                .build();
        ReferralsHolder.fire(this, configuration);
    }

    private boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
