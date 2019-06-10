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
        String desPath = DownloadUtil.getDownloadFilePath(context, url);
        try {
            File f = new File(desPath);
            if (f.exists()) {
                try {
                    if (MD5Util.checkMD5(appConfig.getMD5(), f)) {
                        callback.downloadSuccess();
                        return;
                    } else {
                        f.delete();
                    }
                } catch (Exception e) {
                    f.delete();
                }
            }
        } catch (Exception e) {
        }
        DownloadUtil.downLoad(url, desPath, callback);
    }
}
