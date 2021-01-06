package org.dtu.brogb1.model;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brew {

    private double groundCoffee, grindSize, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;

    public Brew(double groundCoffee, double grindSize, double coffeeWaterRatio, double brewingTemperature,
                double bloomWater, double bloomTime, double totalBrewingTime) {
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.totalBrewingTime = totalBrewingTime;
    }

    public String toJson() {
        return "{" +
                    "\"groundCoffee\": " + this.groundCoffee + "," +
                    "\"grindSize\": " + this.grindSize + "," +
                    "\"coffeeWaterRatio\": " + this.coffeeWaterRatio + "," +
                    "\"brewingTemperature\": " + this.brewingTemperature + "," +
                    "\"bloomWater\": " + this.bloomWater + "," +
                    "\"bloomTime\": " + this.bloomTime + "," +
                    "\"totalBrewingTime\": " + this.totalBrewingTime +
                "}";
    }
}
