package io.referrals.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.net.URL;

public final class ReferralsConfiguration {

    private Context context;
    private boolean debug;
    private boolean forceChannelJob;
    private boolean periodic;
    private boolean notify;
    private String notificationChannelName;
    private String notificationContentTitle;
    private String notificationContentText;
    private int notificationColor;
    private Bitmap notificationLargeIcon;
    private String notificationLargeIconUrl;

    private ReferralsConfiguration(Builder builder) {
        context = builder.context;
        forceChannelJob = builder.forceChannelJob;
        debug = builder.debug;
        periodic = builder.periodic;
        notify = builder.notify;
        notificationChannelName = builder.notificationChannelName;
        notificationContentTitle = builder.notificationContentTitle;
        notificationContentText = builder.notificationContentText;
        notificationColor = builder.notificationColor;
        notificationLargeIconUrl = builder.notificationLargeIconUrl;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    notificationLargeIcon = fetchNotificationLargeIcon(notificationLargeIconUrl);
                } catch (ReferralsRuntimeException e) {
                }
            }
        }).start();
    }

    private Bitmap fetchNotificationLargeIcon(String url) throws ReferralsRuntimeException {
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (Exception | Error e) {
            throw new ReferralsRuntimeException("Failed to get " + notificationLargeIconUrl);
        }
    }

    public Context getContext() {
        return context;
    }

    public boolean isForceCannelJob() {
        return forceChannelJob;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isPeriodic() {
        return periodic;
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

    @NonNull
    public Bitmap getNotificationLargeIcon() {
        if (notificationLargeIcon == null) {
            int resId = context.getResources().getIdentifier(
                    "referrals_io_notification_large", "drawable",
                    context.getPackageName());
            notificationLargeIcon = BitmapFactory.decodeResource(context.getResources(), resId);
        }
        return notificationLargeIcon;
    }

    /**
     * Builder for {@link ReferralsConfiguration}
     */
    public static class Builder {

        private Context context;
        private boolean debug;
        private boolean forceChannelJob;
        private boolean periodic;
        private boolean notify;
        private String notificationChannelName;
        private String notificationContentTitle;
        private String notificationContentText;
        private int notificationColor;
        private String notificationLargeIconUrl;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.debug = false;
            this.forceChannelJob = false;
            this.periodic = false;
            this.notify = true;
            this.notificationChannelName = null;
            this.notificationContentTitle = null;
            this.notificationContentText = null;
            this.notificationColor = Color.TRANSPARENT;
            this.notificationLargeIconUrl = null;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder forceChannelJob(boolean forceChannelJob) {
            this.forceChannelJob = forceChannelJob;
            return this;
        }

        public Builder periodic(boolean periodic) {
            this.periodic = periodic;
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

        public ReferralsConfiguration build() {
            check();
            return new ReferralsConfiguration(this);
        }

        private void check() {
            if (notify) {
                if (TextUtils.isEmpty(notificationChannelName)) {
                    notificationChannelName = "Referrals Job";
                }
                if (TextUtils.isEmpty(notificationContentTitle)) {
                    notificationChannelName = "Referrals Title";
                }
                if (TextUtils.isEmpty(notificationChannelName)) {
                    notificationChannelName = "Referrals Description";
                }
                if (notificationColor == Color.TRANSPARENT) {
                    notificationColor = Color.GREEN;
                }
            }
        }
    }

    public static class ReferralsRuntimeException extends RuntimeException {
        private static final long serialVersionUID = -996812356902545307L;

        public ReferralsRuntimeException(String info) {
            super(info);
        }
    }
}
