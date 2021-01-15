package org.dtu.brogb1.service;

import android.os.Build;
import android.util.Log;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;

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

    public static void setStorage(Brew brew, boolean favoriteOn, IStorageService storage, String TAG) throws StorageServiceException {
        if (brew.getBrewName().isEmpty()) {
            throw new StorageServiceException("Brew need a name");
        }
        try {
            if (favoriteOn) {
                if (brew.getStorageKey() >= 0) {
                    storage.deleteBrew(brew);
                }
                if (brew.getFavoriteKey() >= 0)
                    storage.overwriteFavoriteBrew(brew.getFavoriteKey(), brew);
                else
                    brew.setFavoriteKey(storage.saveBrewToFavorites(brew));
            } else if (!favoriteOn && brew.getFavoriteKey() >= 0) {
                storage.deleteFavoriteBrew(brew);
                if (brew.getStorageKey() >= 0)
                    storage.overwriteBrew(brew.getStorageKey(), brew);
                else
                    brew.setStorageKey(storage.saveBrew(brew));
            } else {
                if (brew.getStorageKey() >= 0) {
                    storage.overwriteBrew(brew.getStorageKey(), brew);
                } else
                    storage.saveBrew(brew);
            }
        } catch (StorageServiceException | BrewException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
    }
}
