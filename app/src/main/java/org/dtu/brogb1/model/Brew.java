package org.dtu.brogb1.model;

public class Brew {

    private double groundCoffee, grindSize, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;

    public Brew(double groundCoffee, double grindSize, double coffeeWaterRatio, double brewingTemperature,
                double bloomWater, double bloomTime, double totalBrewingTime){
        this.groundCoffee = groundCoffee;
        this.grindSize = grindSize;
        this.coffeeWaterRatio = coffeeWaterRatio;
        this.brewingTemperature = brewingTemperature;
        this.bloomWater = bloomWater;
        this.bloomTime = bloomTime;
        this.totalBrewingTime = totalBrewingTime;
    }


}
