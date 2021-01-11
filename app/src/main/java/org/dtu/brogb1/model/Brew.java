package org.dtu.brogb1.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brew {

    private String brewName, brewPics, grindSize;
    private double groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    private int brewTimeMin, brewTimeSec;
    private LocalDateTime lastBrew;

    public Brew(){
        this.groundCoffee = 0;
        this.grindSize = " ";
        this.coffeeWaterRatio = 0;
        this.brewingTemperature = 0;
        this.bloomWater = 0;
        this.bloomTime = 0;
        this.brewName = " ";
        this.brewPics = " ";
        this.brewTimeMin = 0;
        this.brewTimeSec = 0;
    }

    public Brew(double groundCoffee, String grindSize, double coffeeWaterRatio, double brewingTemperature,
                double bloomWater, double bloomTime, String brewName, String brewPics, int brewTimeMin, int brewTimeSec ) {
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.brewName = brewName;
        this.brewPics = brewPics;
        this.brewTimeMin = brewTimeMin;
        this.brewTimeSec = brewTimeSec;
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
            json.put("brewTimeMin", this.brewTimeMin);
            json.put("brewTimeSec", this.brewTimeMin);
            json.put("brewName", this.brewName);
            json.put("brewPics", this.brewPics);
            json.put("lastBrew", this.lastBrew);

        } catch (JSONException e) {
            e.printStackTrace();
            throw new BrewException("fejl under json");
        }

        return json.toString();
    }

    public int getBrewTimeMin() {
        return brewTimeMin;
    }

    public int getBrewTimeSec() {
        return brewTimeSec;
    }

    public void setBrewTimeMin(int brewTimeMin) {
        this.brewTimeMin = brewTimeMin;
    }

    public void setBrewTimeSec(int brewTimeSec) {
        this.brewTimeSec = brewTimeSec;
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


    public boolean equals(Brew brew) {
        return (
                this.groundCoffee == brew.getGroundCoffee() &&
                this.grindSize.equals(brew.getGrindSize()) &&
                this.coffeeWaterRatio == brew.getCoffeeWaterRatio() &&
                this.brewingTemperature == brew.getBrewingTemperature() &&
                this.bloomWater == brew.getBloomWater() &&
                this.bloomTime == brew.getBloomTime() &&
                this.brewTimeMin == brew.getBrewTimeMin() &&
                this.brewTimeSec == brew.getBrewTimeSec() &&
                this.brewName.equals(brew.getBrewName()) &&
                this.brewPics.equals(brew.getBrewPics())
        );
    }

    public void setLastBrewTime(){
        lastBrew = LocalDateTime.now();
    }
}
