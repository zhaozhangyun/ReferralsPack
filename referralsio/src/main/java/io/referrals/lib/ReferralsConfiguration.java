package io.referrals.lib;

import android.content.Context;

public final class ReferralsConfiguration {

    private Context context;
    private boolean debug;
    private boolean forceChannelJob;
    private boolean periodic;

    private ReferralsConfiguration(Builder builder) {
        context = builder.context;
        forceChannelJob = builder.forceChannelJob;
        debug = builder.debug;
        periodic = builder.periodic;
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

    /**
     * Builder for {@link ReferralsConfiguration}
     */
    public static class Builder {

        private Context context;
        private boolean debug;
        private boolean forceChannelJob;
        private boolean periodic;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.debug = false;
            this.forceChannelJob = false;
            this.periodic = false;
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

        public ReferralsConfiguration build() {
            check();
            return new ReferralsConfiguration(this);
        }

        private void check() {
        }
    }

    public static class ReferralsRuntimeException extends RuntimeException {
        private static final long serialVersionUID = -996812356902545307L;

        public ReferralsRuntimeException(String info) {
            super(info);
        }
    }
}
