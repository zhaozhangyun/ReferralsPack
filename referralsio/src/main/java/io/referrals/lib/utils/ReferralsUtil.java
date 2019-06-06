package io.referrals.lib.utils;

import android.content.Context;

import java.io.File;

import io.referrals.lib.L;
import io.referrals.lib.configuration.AppConfiguration;

public class ReferralsUtil {

    private static final String TAG = "ReferralsUtil";

    /**
     * 根据apk的包名判断apk是否安装了；
     */
    public static boolean hasInstalled(Context context, String pkgName) {
        boolean installed;
        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
            installed = true;
        } catch (Exception | Error e) {
            installed = false;
        }
        return installed;
    }

    public static void getApkInBackground(Context context, AppConfiguration appConfig, DownloadCallback callback) {
        String url = appConfig.getUrl();
        String destinationPath = DownloadUtil.getDownloadFilePath(context, url);
        File f = new File(destinationPath);
        if (f.exists()) {
            String sha1 = InstallUtil.getPackageHash(destinationPath);
            L.v(TAG, "sha1: " + sha1);
            try {
                if (sha1.equalsIgnoreCase(appConfig.getSha1())) {
                    callback.downloadSuccess();
                    return;
                } else {
                    f.delete();
                }
            } catch (Exception e) {
                f.delete();
            }
        }
        DownloadUtil.downLoad(url, destinationPath, callback);
    }
}
