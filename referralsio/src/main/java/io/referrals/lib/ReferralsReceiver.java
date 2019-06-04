package io.referrals.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReferralsReceiver extends BroadcastReceiver {
    private static final String TAG = "ReferralsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d(TAG, "call onReceive(): " + intent);
        // TODO 启动应用安装
    }
}
