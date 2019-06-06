package io.referrals.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class InstallUtil {
    /**
     * 安装下载好的APK
     *
     * @param context 必须是Activity的上下文
     * @param uri     文件对应的URI
     */
    public static void installApk(Context context, Uri uri) {
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        */
        Intent intent = getApkInstalledIntent(uri);
        context.startActivity(intent);
    }

    public static Intent getApkInstalledIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    public static Uri getFileUri(Context context, File file) {
        try {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            return uri;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取当前apk包的hash值
     *
     * @return
     */
    public static String getPackageHash(String apkPath) {
        MessageDigest msgDigest;

        FileInputStream fis = null;
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            fis = new FileInputStream(new File(apkPath));
            while ((byteCount = fis.read(bytes)) > 0) {
                msgDigest.update(bytes, 0, byteCount);
            }
            BigInteger bi = new BigInteger(1, msgDigest.digest());
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
