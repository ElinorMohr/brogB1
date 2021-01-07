package org.dtu.brogb1.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brew {

    private String brewName, brewPics, grindSize;
    private double groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;

    public Brew(){
        this.groundCoffee = 0;
        this.grindSize = null;
        this.coffeeWaterRatio = 0;
        this.brewingTemperature = 0;
        this.bloomWater = 0;
        this.bloomTime = 0;
        this.totalBrewingTime = 0;
        this.brewName = null;
        this.brewPics = null;
    }

    public Brew(double groundCoffee, String grindSize, double coffeeWaterRatio, double brewingTemperature,
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

    public String getBrewName() {
        return brewName;
    }

    public void setBrewName(String brewName) {
        this.brewName = brewName;
    }

    public String getBrewPics() {
        return brewPics;
    }

    public void setBrewPics(String brewPics) {
        this.brewPics = brewPics;
    }

    public double getGroundCoffee() {
        return groundCoffee;
    }

    public void setGroundCoffee(double groundCoffee) {
        this.groundCoffee = groundCoffee;
    }

    public String getGrindSize() {
        return grindSize;
    }

    public void setGrindSize(String grindSize) {
        this.grindSize = grindSize;
    }

    public double getCoffeeWaterRatio() {
        return coffeeWaterRatio;
    }

    public void setCoffeeWaterRatio(double coffeeWaterRatio) {
        this.coffeeWaterRatio = coffeeWaterRatio;
    }

    public double getBrewingTemperature() {
        return brewingTemperature;
    }

    public void setBrewingTemperature(double brewingTemperature) {
        this.brewingTemperature = brewingTemperature;
    }

    public double getBloomWater() {
        return bloomWater;
    }

    public void setBloomWater(double bloomWater) {
        this.bloomWater = bloomWater;
    }

    public double getBloomTime() {
        return bloomTime;
    }

    public void setBloomTime(double bloomTime) {
        this.bloomTime = bloomTime;
    }

    public double getTotalBrewingTime() {
        return totalBrewingTime;
    }

    public void setTotalBrewingTime(double totalBrewingTime) {
        this.totalBrewingTime = totalBrewingTime;
    }

    public boolean equals(Brew brew) {
        return (
                this.groundCoffee == brew.getGroundCoffee() &&
                this.grindSize.equals(brew.getGrindSize()) &&
                this.coffeeWaterRatio == brew.getCoffeeWaterRatio() &&
                this.brewingTemperature == brew.getBrewingTemperature() &&
                this.bloomWater == brew.getBloomWater() &&
                this.bloomTime == brew.getBloomTime() &&
                this.totalBrewingTime == brew.getTotalBrewingTime() &&
                this.brewName.equals(brew.getBrewName()) &&
                this.brewPics.equals(brew.getBrewPics())
        );
    }
}
