package io.referrals.lib.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {
    public static final String APK_FOLDER = "downloads";

    public static String getDownloadFilePath(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String rootDir = context.getExternalFilesDir(null).getAbsolutePath();
        File downloadFolder = new File(rootDir, APK_FOLDER);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }
        String apkName = getApkFileName(url);
        return new File(downloadFolder, apkName).getAbsolutePath();
    }

    private static String getApkFileName(String url) {
        int index = url.lastIndexOf("/");
        String apkName = url.substring(index + 1);
        if (apkName.contains("apk")) {
            return apkName;
        } else {
            return "base.apk";
        }
    }

    static void downLoad(final String path, final String filePath, final DownloadCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(path);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {
                    InputStream is = con.getInputStream();//获取输入流
                    FileOutputStream fileOutputStream = null;//文件输出流
                    if (is != null) {
                        //FileUtils fileUtils = new FileUtils();
                        fileOutputStream = new FileOutputStream(new File(filePath));//指定文件保存路径，代码看下一步
                        byte[] buf = new byte[1024];
                        int ch;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    callback.downloadSuccess();
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.downloadFailed();
            }
        }).start();
    }

}
