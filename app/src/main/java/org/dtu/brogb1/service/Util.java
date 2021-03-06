package org.dtu.brogb1.service;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.sentry.Sentry;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

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
                if (brew.getFavoriteKey() >= 0)
                    storage.overwriteFavoriteBrew(brew.getFavoriteKey(), brew);
                else
                    brew.setFavoriteKey(storage.saveBrewToFavorites(brew));
                // Slet fra almindelig liste, hvis Brew er på den liste, efter den er tilføjet til favorit, da det kan være der ikke er plads og det fejler
                if (brew.getStorageKey() >= 0) {
                    storage.deleteBrew(brew);
                }
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
                    brew.setStorageKey(storage.saveBrew(brew));
            }
        } catch (StorageServiceException | BrewException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
    }

    public static void setImageViewSize(ImageView view, Bitmap bitmap, float scale) {
        Util.setImagePadding(view, scale);
        // Beregner hvor bredt billedet skal være og opdaterer view
        int h = view.getLayoutParams().height;
        int w = bitmap.getWidth();
        float r = ((float) w) / (float) bitmap.getHeight();
        view.getLayoutParams().width = (int) (Math.ceil(h*r)+(padding_in_dp*2));
        view.requestLayout();
    }

    private final static int padding_in_dp = 5;
    public static void setImagePadding(ImageView view, float scale) {
        // Sætter padding på billedet
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        view.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
    }

    public static Bitmap setImageFromBrew(Brew brew, ImageView v, ContentResolver resolver, String TAG){
        Uri image_uri = Uri.fromFile(new File(brew.getBrewPics()));
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, image_uri);
            v.setImageBitmap(bitmap);
            return bitmap;
        } catch (IOException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
        return null;
    }

    public static void saveImageFrom(Brew brew, Bitmap bitmap, Context conext, String TAG){
        File folder;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                folder = new File(conext.getExternalFilesDir(null), File.separator + "brog/");
            else
                folder = new File(Environment.getExternalStorageState(), File.separator + "brog/");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                File file = new File (folder, System.currentTimeMillis() + ".jpg");
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                brew.setBrewPics(file.getAbsolutePath());
            }

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
