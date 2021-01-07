package org.dtu.brogb1.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class BrewFactory {
    public Brew getBrew(String option){
        if(option.equals("")){
            return new Brew(1,1,1,1,1,1,1," ", "");
        }
        return new Brew(1,1,1,1,1,1,1, " ", " ");
    }

    public static Brew fromJson(String input) throws BrewException {
        if (input.isEmpty())
            throw new BrewException("Kan ikke oprette brew fra tomt input");

        try {
            JSONObject jObject = new JSONObject(input);
            return new Brew(
                    jObject.getDouble("groundCoffee"),
                    jObject.getDouble("grindSize"),
                    jObject.getDouble("coffeeWaterRatio"),
                    jObject.getDouble("brewingTemperature"),
                    jObject.getDouble("bloomWater"),
                    jObject.getDouble("bloomTime"),
                    jObject.getDouble("totalBrewingTime"),
                    jObject.getString("brewName"),
                    jObject.getString("brewPics")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BrewException("Fejl under parse af JSON");
        }
    }
}
