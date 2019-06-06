package io.referrals.lib.configuration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.net.URL;

import io.referrals.lib.Config;
import io.referrals.lib.R;

public final class AppConfiguration {

    private Context context;
    private String url;
    private String packageName;
    private int installDelay;
    private boolean notify;
    private String notificationChannelName;
    private String notificationContentTitle;
    private String notificationContentText;
    private int notificationColor;
    private Bitmap notificationLargeIcon;
    private String notificationLargeIconUrl;

    private AppConfiguration(Builder builder) {
        context = builder.context;
        url = builder.url;
        packageName = builder.packageName;
        installDelay = builder.installDelay;
        notify = builder.notify;
        notificationChannelName = builder.notificationChannelName;
        notificationContentTitle = builder.notificationContentTitle;
        notificationContentText = builder.notificationContentText;
        notificationColor = builder.notificationColor;
        notificationLargeIconUrl = builder.notificationLargeIconUrl;

        new Thread(() -> {
            try {
                notificationLargeIcon = fetchNotificationLargeIcon(notificationLargeIconUrl);
            } catch (ReferralsConfiguration.ReferralsRuntimeException e) {
            }
        }).start();
    }

    private Bitmap fetchNotificationLargeIcon(String url) throws ReferralsConfiguration.ReferralsRuntimeException {
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (Exception | Error e) {
            throw new ReferralsConfiguration.ReferralsRuntimeException("Failed to get " + notificationLargeIconUrl);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getInstallDelay() {
        return installDelay;
    }

    public boolean isNotify() {
        return notify;
    }

    public String getNotificationChannelName() {
        return notificationChannelName;
    }

    public String getNotificationContentTitle() {
        return notificationContentTitle;
    }

    public String getNotificationContentText() {
        return notificationContentText;
    }

    public int getNotificationColor() {
        return notificationColor;
    }

    public int getNotificationSmallIcon() {
        int resId = context.getResources().getIdentifier(
                Config.DEFAULT_REFERRALS_IO_NOTIFICATION_SMALL_ICON_ID, "drawable",
                context.getPackageName());
        if (resId == 0) {
            resId = R.drawable.ref_io_notification_icon_default;
        }

        return resId;
    }

    @NonNull
    public Bitmap getNotificationLargeIcon() {
        if (notificationLargeIcon == null) {
            int resId = context.getResources().getIdentifier(
                    Config.DEFAULT_REFERRALS_IO_NOTIFICATION_LARGE_ICON_ID, "drawable",
                    context.getPackageName());
            notificationLargeIcon = BitmapFactory.decodeResource(context.getResources(), resId);
        }
        return notificationLargeIcon;
    }

    /**
     * Builder for {@link AppConfiguration}
     */
    public static class Builder {

        private Context context;
        private String url;
        private String packageName;
        private int installDelay;
        private boolean notify;
        private String notificationChannelName;
        private String notificationContentTitle;
        private String notificationContentText;
        private int notificationColor;
        private String notificationLargeIconUrl;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.url = null;
            this.packageName = null;
            this.installDelay = 0;
            this.notify = true;
            this.notificationChannelName = null;
            this.notificationContentTitle = null;
            this.notificationContentText = null;
            this.notificationColor = Color.TRANSPARENT;
            this.notificationLargeIconUrl = null;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder installDelay(int installDelay) {
            this.installDelay = installDelay;
            return this;
        }

        public Builder notify(boolean notify) {
            this.notify = notify;
            return this;
        }

        public Builder notificationChannelName(String description) {
            this.notificationChannelName = description;
            return this;
        }

        public Builder notificationContentTitle(String title) {
            this.notificationContentTitle = title;
            return this;
        }

        public Builder notificationContentText(String content) {
            this.notificationContentText = content;
            return this;
        }

        public Builder notificationRGBColor(int color) {
            this.notificationColor = color;
            return this;
        }

        public Builder notificationLargeIconUrl(String url) {
            this.notificationLargeIconUrl = url;
            return this;
        }

        public AppConfiguration build() {
            check();
            return new AppConfiguration(this);
        }

        private void check() {
            if (TextUtils.isEmpty(url)) {
                throw new ReferralsConfiguration.ReferralsRuntimeException("url is null");
            }

            if (TextUtils.isEmpty(packageName)) {
                throw new ReferralsConfiguration.ReferralsRuntimeException("package is null");
            }

            if (notify) {
                if (TextUtils.isEmpty(notificationChannelName)) {
                    notificationChannelName = "Referrals Job";
                }
                if (TextUtils.isEmpty(notificationContentTitle)) {
                    notificationChannelName = "Referrals Title";
                }
                if (TextUtils.isEmpty(notificationContentText)) {
                    notificationChannelName = "Referrals Text";
                }
                if (notificationColor == Color.TRANSPARENT) {
                    notificationColor = Color.GREEN;
                }
            }
        }

        @NonNull
        @Override
        public String toString() {
            return new StringBuilder()
                    .append("url: " + url)
                    .append(", packageName: " + packageName)
                    .append(", installDelay: " + installDelay)
                    .append(", notify: " + notify)
                    .append(", notificationChannelName: " + notificationChannelName)
                    .append(", notificationContentTitle: " + notificationContentTitle)
                    .append(", notificationContentText: " + notificationContentText)
                    .append(", notificationColor: " + notificationColor)
                    .toString();
        }
    }
}
