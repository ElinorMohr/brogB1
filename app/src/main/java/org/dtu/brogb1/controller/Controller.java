package org.dtu.brogb1.controller;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewFactory;

/**
 * @author Elinor Mikkelsen s191242
 */

public class Controller {
    private static Controller single_instance;
    private Brew brew;
    private BrewFactory brewFactory;
    private Controller(){
        brewFactory = new BrewFactory();
        Brew brew = brewFactory.getBrew("");
    }

    public static Controller getInstance(){
        if (single_instance == null)
            single_instance = new Controller();
        return single_instance;
    }
}
