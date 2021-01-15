package org.dtu.brogb1;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Mohammad Asim Raja s040727
 * @author Theis Villumsen s195461
 */

public class BrewJsonTest {
    /**
     * Tester om BrewFactory kan generere Golden Cup med de rigtige parametre
     */
    @Test
    public void goldenCupTest() {
        Brew brew = BrewFactory.getBrew("Default");
        assertEquals(18, brew.getGroundCoffee());
        assertEquals("Medium", brew.getGrindSize());
        assertEquals(60, brew.getCoffeeWaterRatio());
        assertEquals(93, brew.getBrewingTemperature());
        assertEquals(45, brew.getBloomWater());
        assertEquals(30, brew.getBloomTime());
        assertEquals(3, brew.getBrewTimeMin());
        assertEquals(0, brew.getBrewTimeSec());
        assertEquals("Golden Cup", brew.getBrewName());
    }

    /**
     * Tester at BrewFactory kan generere en tom bryg og om denne kan konveretes til json
     */
    @Test
    public void toJsonTest() {
        try {
            Brew brew = BrewFactory.getBrew("");
            String json = brew.toJson();
            if (json.isEmpty()) {
                fail();
            } else {
                JSONObject jObject = new JSONObject(json);
                if (!jObject.has("groundCoffee")) {
                    fail("Missing groundCoffee in jObject");
                } else if (!jObject.has("grindSize")) {
                    fail("Missing grindSize in jObject");
                } else if (!jObject.has("coffeeWaterRatio")) {
                    fail("Missing coffeeWaterRatio in jObject");
                } else if (!jObject.has("brewingTemperature")) {
                    fail("Missing brewingTemperature in jObject");
                } else if (!jObject.has("bloomWater")) {
                    fail("Missing bloomWater in jObject");
                } else if (!jObject.has("bloomTime")) {
                    fail("Missing bloomTime in jObject");
                } else if (!jObject.has("brewTimeMin")) {
                    fail("Missing brewTimeMin in jObject");
                } else if (!jObject.has("brewTimeSec")) {
                    fail("Missing brewTimeSec in jObject");
                } else if (!jObject.has("lastBrew")) {
                    fail("Missing lastBrew in jObject");
                } else if (!jObject.has("saveBrew")) {
                    fail("Missing saveBrew in jObject");
                } else if (!jObject.has("favoriteBrew")) {
                    fail("Missing favoriteBrew in jObject");
                } else if (!jObject.has("storageKey")) {
                    fail("Missing storageKey in jObject");
                } else if (!jObject.has("favoriteKey")) {
                    fail("Missing favoriteKey in jObject");
                } else {
                    assertEquals(jObject.getInt("groundCoffee"), brew.getGroundCoffee());
                    assertEquals(jObject.getString("grindSize"), brew.getGrindSize());
                    assertEquals(jObject.getInt("coffeeWaterRatio"), brew.getCoffeeWaterRatio());
                    assertEquals(jObject.getInt("brewingTemperature"), brew.getBrewingTemperature());
                    assertEquals(jObject.getInt("bloomWater"), brew.getBloomWater());
                    assertEquals(jObject.getInt("bloomTime"), brew.getBloomTime());
                    assertEquals(jObject.getInt("brewTimeMin"), brew.getBrewTimeMin());
                    assertEquals(jObject.getInt("brewTimeSec"), brew.getBrewTimeSec());
                    assertEquals(jObject.getString("brewName"), brew.getBrewName());
                    assertEquals(jObject.getString("brewPics"), brew.getBrewPics());
                    assertEquals(jObject.getString("lastBrew"), brew.getLastBrew());
                    assertEquals(jObject.getBoolean("saveBrew"), brew.isSaveBrew());
                    assertEquals(jObject.getBoolean("favoriteBrew"), brew.isFavoriteBrew());
                    assertEquals(jObject.getInt("storageKey"), brew.getStorageKey());
                    assertEquals(jObject.getInt("favoriteKey"), brew.getFavoriteKey());
                }
            }
        } catch (BrewException | JSONException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Tester om BrewFactory kan tage json og generere en Brew
     */
    @Test
    public void fromJsonTest() {
        JSONObject json = new JSONObject();
        try {
            json.put("groundCoffee", 1);
            json.put("grindSize", "Fine");
            json.put("coffeeWaterRatio", 2);
            json.put("brewingTemperature", 3);
            json.put("bloomWater", 4);
            json.put("bloomTime", 5);
            json.put("brewTimeMin", 6);
            json.put("brewTimeSec", 7);
            json.put("brewName", "name");
            json.put("brewPics", "pic");
            json.put("lastBrew", "2020-01-01 01:01:01");
            json.put("saveBrew", true);
            json.put("favoriteBrew", false);
            json.put("storageKey", 8);
            json.put("favoriteKey", 0);
            Brew brew = BrewFactory.fromJson(json.toString());
            assertEquals(json.getInt("groundCoffee"), brew.getGroundCoffee());
            assertEquals(json.getString("grindSize"), brew.getGrindSize());
            assertEquals(json.getInt("coffeeWaterRatio"), brew.getCoffeeWaterRatio());
            assertEquals(json.getInt("brewingTemperature"), brew.getBrewingTemperature());
            assertEquals(json.getInt("bloomWater"), brew.getBloomWater());
            assertEquals(json.getInt("bloomTime"), brew.getBloomTime());
            assertEquals(json.getInt("brewTimeMin"), brew.getBrewTimeMin());
            assertEquals(json.getInt("brewTimeSec"), brew.getBrewTimeSec());
            assertEquals(json.getString("brewName"), brew.getBrewName());
            assertEquals(json.getString("brewPics"), brew.getBrewPics());
            assertEquals(json.getString("lastBrew"), brew.getLastBrew());
            assertEquals(json.getBoolean("saveBrew"), brew.isSaveBrew());
            assertEquals(json.getBoolean("favoriteBrew"), brew.isFavoriteBrew());
            assertEquals(json.getInt("storageKey"), brew.getStorageKey());
            assertEquals(json.getInt("favoriteKey"), brew.getFavoriteKey());
        } catch (JSONException | BrewException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Tester generering af Brew i Brewfactory, Brew til json og Brew fra json
     */
    @Test
    public void backAndForthTest() {
        Brew brew = BrewFactory.getBrew("");
        try {
            String json = brew.toJson();
            Brew brew2 = BrewFactory.fromJson(json);
            assertTrue(brew.equals(brew2));
        } catch (BrewException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
