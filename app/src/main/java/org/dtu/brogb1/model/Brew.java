package org.dtu.brogb1.model;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brew {

    private String brewName, brewPics, grindSize;
    private int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    boolean saveBrew, favoriteBrew;
    private LocalDateTime lastBrew;
    private int storageKey, favoriteKey;

    public Brew(){
        this.groundCoffee = 0;
        this.grindSize = " ";
        this.coffeeWaterRatio = 0;
        this.brewingTemperature = 0;
        this.bloomWater = 0;
        this.bloomTime = 0;
        this.brewTimeMin = 0;
        this.brewTimeSec = 0;
        this.brewName = " ";
        this.brewPics = " ";
        this.saveBrew = false;
        this.favoriteBrew = false;
        this.storageKey = -1;
        this.favoriteKey = -1;
    }

    public Brew(int groundCoffee, String grindSize, int coffeeWaterRatio, int brewingTemperature,
                int bloomWater, int bloomTime, int brewTimeMin, int brewTimeSec,String brewName, String brewPics, boolean saveBrew, boolean favoriteBrew ) {
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.brewTimeMin = brewTimeMin;
        this.brewTimeSec = brewTimeSec;
        this.brewName = brewName;
        this.brewPics = brewPics;
        this.saveBrew = saveBrew;
        this.favoriteBrew = favoriteBrew;
        this.storageKey = -1;
        this.favoriteKey = -1;
    }

    public Brew(int groundCoffee, String grindSize, int coffeeWaterRatio, int brewingTemperature,
                int bloomWater, int bloomTime, int brewTimeMin, int brewTimeSec,String brewName, String brewPics, boolean saveBrew, boolean favoriteBrew, int storageKey, int favoriteKey) {
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.brewTimeMin = brewTimeMin;
        this.brewTimeSec = brewTimeSec;
        this.brewName = brewName;
        this.brewPics = brewPics;
        this.saveBrew = saveBrew;
        this.favoriteBrew = favoriteBrew;
        this.storageKey = storageKey;
        this.favoriteKey = favoriteKey;
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
            json.put("brewTimeSec", this.brewTimeSec);
            json.put("brewName", this.brewName);
            json.put("brewPics", this.brewPics);
            json.put("lastBrew", this.lastBrew);
            json.put("saveBrew", this.saveBrew);
            json.put("favoriteBrew", this.favoriteBrew);
            json.put("storageKey", this.storageKey);
            json.put("favoriteKey", this.favoriteKey);
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

    public String getGrindSize() {
        return grindSize;
    }

    public void setGrindSize(String grindSize) {
        this.grindSize = grindSize;
    }

    public int getBrewTimeMin() {
        return brewTimeMin;
    }

    public void setBrewTimeMin(int brewTimeMin) {
        this.brewTimeMin = brewTimeMin;
    }

    public int getBrewTimeSec() {
        return brewTimeSec;
    }

    public void setBrewTimeSec(int brewTimeSec) {
        this.brewTimeSec = brewTimeSec;
    }

    public int getGroundCoffee() {
        return groundCoffee;
    }

    public void setGroundCoffee(int groundCoffee) {
        this.groundCoffee = groundCoffee;
    }

    public int getCoffeeWaterRatio() {
        return coffeeWaterRatio;
    }

    public void setCoffeeWaterRatio(int coffeeWaterRatio) {
        this.coffeeWaterRatio = coffeeWaterRatio;
    }

    public int getBrewingTemperature() {
        return brewingTemperature;
    }

    public void setBrewingTemperature(int brewingTemperature) {
        this.brewingTemperature = brewingTemperature;
    }

    public boolean isSaveBrew() {
        return saveBrew;
    }

    public void setSaveBrew(boolean saveBrew) {
        this.saveBrew = saveBrew;
    }

    public boolean isFavoriteBrew() {
        return favoriteBrew;
    }

    public void setFavoriteBrew(boolean favoriteBrew) {
        this.favoriteBrew = favoriteBrew;
    }

    public int getBloomWater() {
        return bloomWater;
    }

    public void setBloomWater(int bloomWater) {
        this.bloomWater = bloomWater;
    }

    public int getBloomTime() {
        return bloomTime;
    }

    public void setBloomTime(int bloomTime) {
        this.bloomTime = bloomTime;
    }

    public int getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(int storageKey) {
        this.storageKey = storageKey;
    }

    public int getFavoriteKey() {
        return favoriteKey;
    }

    public void setFavoriteKey(int favoriteKey) {
        this.favoriteKey = favoriteKey;
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
                this.brewPics.equals(brew.getBrewPics()) &&
                this.saveBrew == brew.isSaveBrew() &&
                this.favoriteBrew == brew.isFavoriteBrew()
        );
    }

    public void setLastBrewTime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastBrew = LocalDateTime.now();
        }
    }
}
