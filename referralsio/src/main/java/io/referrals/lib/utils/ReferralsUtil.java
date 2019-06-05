package io.referrals.lib.utils;

import android.content.Context;

import java.io.File;

public class ReferralsUtil {
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

    public static void getApkInBackground(Context context, String url, DownloadCallback callback) {
        String destinationPath = DownloadUtil.getDownloadFilePath(context, url);
        if (new File(destinationPath).exists()) {
            callback.downloadSuccess();
            return;
        }
        DownloadUtil.downLoad(url, destinationPath, callback);
    }
}
