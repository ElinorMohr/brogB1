package org.dtu.brogb1.model;

public class BrewFactory {
    public Brew getBrew(String option){
        if(option.equals("")){
            return new Brew(1,1,1,1,1,1,1);
        }
        return new Brew(1,1,1,1,1,1,1);
    }
}
