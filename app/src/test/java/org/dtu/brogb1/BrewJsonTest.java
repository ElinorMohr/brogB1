package org.dtu.brogb1;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrewJsonTest {
    @Test
    public void SaveTest()
    {
        Brew brew = new Brew (1,1,1,1,1,
                1,1,"1","1");
        try {

            String Json = brew.toJson();
            Brew brew2 = BrewFactory.fromJson(Json);
            assertTrue(brew.equals(brew2));
        } catch (BrewException e) {
            e.printStackTrace();
            fail();
        }
    }
}
