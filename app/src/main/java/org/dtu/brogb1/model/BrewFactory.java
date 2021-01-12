package org.dtu.brogb1.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class BrewFactory {
    public static Brew getBrew(String option){
        if(option.equals("Default")){
            return new Brew(18,"Medium",60,93,45,30, 3 , 0, "Golden Cup", " ", false,false);
        }
        return new Brew(1," ",1,1,1,1, 1 , 1 ," "," ", false,false);
    }

    public static Brew fromJson(String input) throws BrewException {
        if (input.isEmpty())
            throw new BrewException("Kan ikke oprette brew fra tomt input");

        try {
            JSONObject jObject = new JSONObject(input);
            return new Brew(
                    jObject.getInt("groundCoffee"),
                    jObject.getString("grindSize"),
                    jObject.getInt("coffeeWaterRatio"),
                    jObject.getInt("brewingTemperature"),
                    jObject.getInt("bloomWater"),
                    jObject.getInt("bloomTime"),
                    jObject.getInt("brewTimeMin"),
                    jObject.getInt("brewTimeSec"),
                    jObject.getString("brewName"),
                    jObject.getString("brewPics"),
                    jObject.getBoolean("saveBrew"),
                    jObject.getBoolean("favoriteBrew")
            );
        } catch (JSONException e) {
            System.out.println(input);
            e.printStackTrace();
            throw new BrewException("Fejl under parse af JSON");
        }
    }
}
