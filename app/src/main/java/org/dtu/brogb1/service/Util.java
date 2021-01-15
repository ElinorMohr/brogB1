package org.dtu.brogb1.service;

import android.os.Build;
import android.util.Log;

import io.sentry.Sentry;

public class Util {
    /**
     * Sender en fejl til sentry.io, hvis den akltive enhed ikke er en emulator
     *
     * @param TAG
     * @param e
     */
    public static void log(String TAG, Exception e) {
        boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
        if (!EMULATOR) {
            Sentry.setTag("TAG", TAG);
            Sentry.captureException(e);
            Sentry.removeTag("TAG");
        } else {
            Log.d(TAG, e.getMessage());
        }
    }
}
