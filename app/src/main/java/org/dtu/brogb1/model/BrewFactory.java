package org.dtu.brogb1.model;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import org.dtu.brogb1.service.Util;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class BrewFactory {
    private static final String TAG = BrewFactory.class.getSimpleName();

    public static Brew getBrew(String option) {
        if (option.equals("Default")) {
            return new Brew(18,"Medium",60,93,45,30, 3 , 0, "Golden Cup", "", false,false);
        }
        return new Brew(1," ",1,1,1,1, 1 , 1 ," ","", false,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Brew fromJson(String input) throws BrewException {
        Log.d(TAG, "fromJson: " + input);
        if (input.isEmpty())
            throw new BrewException("Kan ikke oprette brew fra tomt input");

        try {
            JSONObject jObject = new JSONObject(input);
            Brew brew = new Brew(
                    jObject.getInt("groundCoffee"),
                    jObject.has("grindSize") ? jObject.getString("grindSize") : "Fine",
                    jObject.getInt("coffeeWaterRatio"),
                    jObject.getInt("brewingTemperature"),
                    jObject.getInt("bloomWater"),
                    jObject.getInt("bloomTime"),
                    jObject.getInt("brewTimeMin"),
                    jObject.getInt("brewTimeSec"),
                    jObject.getString("brewName"),
                    jObject.getString("brewPics"),
                    jObject.getBoolean("saveBrew"),
                    jObject.getBoolean("favoriteBrew"),
                    jObject.has("storageKey") ? jObject.getInt("storageKey") : -1,
                    jObject.has("favoriteKey") ? jObject.getInt("favoriteKey") : -1
            );
            if (jObject.has("lastBrew"))
                brew.setLastBrewTime(jObject.getString("lastBrew"));
            return brew;
        } catch (JSONException e) {
            Util.log(TAG, e);
            e.printStackTrace();
            throw new BrewException("Fejl under parse af JSON");
        }
    }
}
