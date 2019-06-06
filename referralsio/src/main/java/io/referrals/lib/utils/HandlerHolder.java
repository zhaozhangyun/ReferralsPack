package io.referrals.lib.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.referrals.lib.L;

public class HandlerHolder {

    private static final String TAG = "HandlerHolder";
    private static List<Handler> handlers = new CopyOnWriteArrayList<>();

    public static void registerHandler(@NonNull Handler target) {
        if (!handlers.contains(target)) {
            handlers.add(target);
        }
    }

    public static void unregisterHandler(@NonNull Handler target) {
        if (handlers.contains(target)) {
            handlers.remove(target);
        }
    }

    // Notifiy any handlers.
    public static void notifyHandlers(@NonNull Message msg) {
        synchronized (handlers) {
            Iterator<Handler> it = handlers.iterator();
            while (it.hasNext()) {
                L.v(TAG, "sendToTarget(): " + msg.what);
                Handler target = it.next();
                Message message = Message.obtain(target, msg.what, msg.arg1, msg.arg2, msg.obj);
                message.sendToTarget();
            }
        }
    }
}
