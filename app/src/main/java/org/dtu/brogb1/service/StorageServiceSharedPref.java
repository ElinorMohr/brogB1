package org.dtu.brogb1.service;

import android.content.SharedPreferences;

import org.dtu.brogb1.activity.LandingPage;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;

import java.util.ArrayList;
import java.util.List;

public class StorageServiceSharedPref implements IStorageService {
    private static StorageServiceSharedPref instance = null;
    public static StorageServiceSharedPref getInstance() {
        if (instance == null) {
            instance = new StorageServiceSharedPref();
        }
        return instance;
    }

    private final String brewKey = "brew_";
    private final String quickKey = brewKey + "quick";
    private final String favoritesKey = brewKey + "favorites";
    private final String historyKey = brewKey + "history";
    private final String countKey = brewKey + "count";
    private int brewCount = 0;
    private SharedPreferences preferences = null;
    private StorageServiceSharedPref() {
        this.preferences = LandingPage.mySharedPreferences;
        this.brewCount = this.preferences.getInt(this.countKey, 0);
    }

    @Override
    public void saveString(String key, String value) {
        this.preferences.edit().putString(key, value).apply();
    }

    @Override
    public String getString(String key) {
        return this.preferences.getString(key, "");
    }

    @Override
    public void saveBool(String key, boolean value) {
        this.preferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public boolean getBool(String key) {
        return this.preferences.getBoolean(key, false);
    }

    @Override
    public void saveInt(String key, int value) {
        this.preferences.edit().putInt(key, value).apply();
    }

    @Override
    public int getInt(String key) {
        return this.preferences.getInt(key, -1);
    }

    @Override
    public void saveFloat(String key, int value) {
        this.preferences.edit().putFloat(key, value).apply();
    }

    @Override
    public float getFloat(String key) {
        return this.preferences.getFloat(key, -1);
    }

    @Override
    public void unset(String key) {
        this.preferences.edit().remove(key).apply();
    }

    @Override
    public int saveBrew(Brew value) throws BrewException {
        this.preferences.edit().putString(this.brewKey + this.brewCount, value.toJson()).apply();
        this.brewCount++;
        this.preferences.edit().putInt(this.countKey, this.brewCount).apply();
        return this.brewCount - 1;
    }

    @Override
    public Brew getBrew(int key) throws StorageServiceException, BrewException {
        if (key >= this.brewCount)
            throw new StorageServiceException("Den brew findes ikke!");
        return BrewFactory.fromJson(this.preferences.getString(this.brewKey + key, ""));
    }

    @Override
    public List<Brew> getAllBrews() throws StorageServiceException, BrewException {
        ArrayList list = new ArrayList<Brew>();
        for (int i = 0; i < this.brewCount; i++) {
            list.add(BrewFactory.fromJson(this.preferences.getString(this.brewKey + i, "")));
        }
        return list;
    }

    @Override
    public int getBrewCount() {
        return this.brewCount;
    }

    @Override
    public void deleteBrew(int key) throws StorageServiceException, BrewException {
        if (key >= this.brewCount)
            throw new StorageServiceException("Den brew findes ikke!");

        // Slet den Brew der ønsked slettet
        this.unset(this.brewKey + key);
        // Hvis det ikke var den sidste Brew, så skal alle efterfølgende Brew's rykkes
        if (key < this.brewCount - 1) {
            // Startende med den der lige er slettet, overskriv med den næste
            for (int i = key; i < this.brewCount - 1; i++) {
                this.overwriteBrew(i, this.getBrew(i+1));
            }
            // Slet den sidste Brew
            this.unset(this.brewKey + (this.brewCount - 1));
        }
        // Opdater antallet af gemte Brews
        this.saveInt(this.countKey, this.brewCount - 1);
    }

    @Override
    public void overwriteBrew(int key, Brew value) throws BrewException {
        this.preferences.edit().putString(this.brewKey + key, value.toJson()).apply();
    }

    @Override
    public void setQuickBrew(int value) {
        this.saveInt(this.quickKey, value);
    }

    @Override
    public Brew getQuickBrew() throws StorageServiceException, BrewException {
        return this.getBrew(this.preferences.getInt(this.quickKey, -1));
    }
}
