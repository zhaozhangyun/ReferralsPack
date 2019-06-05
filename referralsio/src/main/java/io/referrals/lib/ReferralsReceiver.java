package io.referrals.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

import io.referrals.lib.utils.DownloadUtil;
import io.referrals.lib.utils.InstallUtil;

public class ReferralsReceiver extends BroadcastReceiver {
    private static final String TAG = "ReferralsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d(TAG, "call onReceive(): " + intent);
        // TODO 启动应用安装
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        Uri uri = InstallUtil.getFileUri(context, new File(DownloadUtil.getDownloadFilePath(
                context, bundle.getString("file_path"))));
        L.v(TAG, "uri " + uri.toString());
        InstallUtil.installApk(context, uri);
    }
}
