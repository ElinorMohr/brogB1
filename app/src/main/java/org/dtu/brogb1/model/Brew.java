package org.dtu.brogb1.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brew {

    private String brewName, brewPics ;
    private double groundCoffee, grindSize, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;

    public Brew(double groundCoffee, double grindSize, double coffeeWaterRatio, double brewingTemperature,
                double bloomWater, double bloomTime, double totalBrewingTime, String brewName, String brewPics) {
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.totalBrewingTime = totalBrewingTime;
        this.brewName = brewName;
        this.brewPics = brewPics;
    }

    public String toJson() throws BrewException {

        JSONObject json = new JSONObject();

        try {
            json.put("groundCoffee", this.groundCoffee);
            json.put("grindSize", this.grindSize);
            json.put("coffeeWaterRatio", this.coffeeWaterRatio);
            json.put("brewingTemperature", this.brewingTemperature);
            json.put("bloomWater", this.bloomWater);
            json.put("bloomTime", this.bloomTime);
            json.put("totalBrewingTime", this.totalBrewingTime);
            json.put("brewName", this.brewName);
            json.put("brewPics", this.brewPics);

        } catch (JSONException e) {
            e.printStackTrace();
            throw new BrewException("fejl under json");
        }

        return json.toString();
    }
}
