package org.dtu.brogb1.model;

/**
 * @author Elinor Mikkelsen s191242
 */

public class BrewFactory {
    public Brew getBrew(String option){
        if(option.equals("")){
            return new Brew(1,1,1,1,1,1,1);
        }
        return new Brew(1,1,1,1,1,1,1);
    }
}
