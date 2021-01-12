package org.dtu.brogb1.service;

import android.content.SharedPreferences;

import org.dtu.brogb1.activity.LandingPage;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;

import java.util.ArrayList;
import java.util.List;

public class    StorageServiceSharedPref implements IStorageService {
    private static StorageServiceSharedPref instance = null;
    public static StorageServiceSharedPref getInstance() {
        if (instance == null) {
            instance = new StorageServiceSharedPref();
        }
        return instance;
    }

    private final String brewKey = "brew_";
    private final String quickKey = brewKey + "quick";
    private final String favoritesKey = brewKey + "favorite_";
    private final String historyKey = brewKey + "history_";
    private final String brewCountKey = brewKey + "count";
    private final String historyCountKey = historyKey + "count";
    private final String favoriteCountKey = favoritesKey + "count";
    private int brewCount = 0;
    private int historyCount = 0;
    private int favoriteCount = 0;
    private SharedPreferences preferences = null;
    private StorageServiceSharedPref() {
        this.preferences = LandingPage.mySharedPreferences;
        this.brewCount = this.getInt(this.brewCountKey);
        if(this.brewCount < 0)
            this.brewCount = 0;
        this.historyCount = this.getInt(this.historyCountKey);
        if(this.historyCount < 0)
            this.historyCount = 0;
        this.favoriteCount = this.getInt(this.favoriteCountKey);
        if(this.favoriteCount < 0)
            this.favoriteCount = 0;
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
        this.saveString(this.brewKey + this.brewCount, value.toJson());
        this.brewCount++;
        this.saveInt(this.brewCountKey, this.brewCount);
        return this.brewCount - 1;
    }

    @Override
    public Brew getBrew(int key) throws StorageServiceException, BrewException {
        if (key >= this.brewCount)
            throw new StorageServiceException("Den brew findes ikke!");
        Brew brew = BrewFactory.fromJson(this.getString(this.brewKey + key));
        brew.setStorageKey(key);
        return brew;
    }

    @Override
    public ArrayList<Brew> getAllBrews() throws StorageServiceException, BrewException {
        ArrayList list = new ArrayList<Brew>();
        for (int i = 0; i < this.brewCount; i++) {
            Brew brew = BrewFactory.fromJson(this.getString(this.brewKey + i));
            brew.setStorageKey(i);
            list.add(brew);
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
        this.saveInt(this.brewCountKey, this.brewCount - 1);
    }

    @Override
    public void overwriteBrew(int key, Brew value) throws BrewException {
        this.saveString(this.brewKey + key, value.toJson());
    }

    @Override
    public void setQuickBrew(int value) {
        this.saveInt(this.quickKey, value);
    }

    @Override
    public Brew getQuickBrew() throws StorageServiceException, BrewException {
        int id = this.preferences.getInt(this.quickKey, -1);
        if (id == -1){
            return BrewFactory.getBrew("Default");
        }
        return this.getBrew(id);
    }

    @Override
    public void saveBrewToHistory(Brew value) throws BrewException, StorageServiceException {
        for (int i = this.historyCount; i > 0; i--) {
            this.saveString(this.historyKey + i, this.getBrewFromHistory(i-1).toJson());
        }
        this.saveString(this.historyKey + "0", value.toJson());
        this.historyCount++;
        this.saveInt(this.historyCountKey, this.historyCount);
    }

    @Override
    public Brew getBrewFromHistory(int key) throws StorageServiceException, BrewException {
        if (key >= this.historyCount)
            throw new StorageServiceException("Den brew findes ikke!");
        return BrewFactory.fromJson(this.getString(this.historyKey + key));
    }

    @Override
    public ArrayList<Brew> getBrewHistory() throws StorageServiceException, BrewException {
        ArrayList list = new ArrayList<Brew>();
        for (int i = 0; i < this.historyCount; i++) {
            list.add(BrewFactory.fromJson(this.getString(this.historyKey + i)));
        }
        return list;
    }

    @Override
    public int getBrewHistoryCount() {
        return this.historyCount;
    }

    @Override
    public int saveBrewToFavorites(Brew value) throws BrewException {
        this.saveString(this.favoritesKey + this.favoriteCount, value.toJson());
        this.favoriteCount++;
        this.saveInt(this.favoritesKey, this.favoriteCount);
        return this.favoriteCount - 1;
    }

    @Override
    public Brew getBrewFromFavorites(int key) throws StorageServiceException, BrewException {
        if (key >= this.favoriteCount)
            throw new StorageServiceException("Den brew findes ikke!");
        Brew brew = BrewFactory.fromJson(this.getString(this.favoritesKey + key));
        brew.setFavoriteKey(key);
        return brew;
    }

    @Override
    public ArrayList<Brew> getFavoriteBrews() throws StorageServiceException, BrewException {
        ArrayList list = new ArrayList<Brew>();
        for (int i = 0; i < this.favoriteCount; i++) {
            Brew brew = BrewFactory.fromJson(this.getString(this.favoritesKey + i));
            brew.setFavoriteKey(i);
            list.add(brew);
        }
        return list;
    }

    @Override
    public int getBrewFavoriteCount() {
        return this.favoriteCount;
    }

    @Override
    public void deleteFavoriteBrew(int key) throws StorageServiceException, BrewException {
        if (key >= this.favoriteCount)
            throw new StorageServiceException("Den brew findes ikke!");

        // Slet den Brew der ønsked slettet
        this.unset(this.favoritesKey + key);
        // Hvis det ikke var den sidste Brew, så skal alle efterfølgende Brew's rykkes
        if (key < this.favoriteCount - 1) {
            // Startende med den der lige er slettet, overskriv med den næste
            for (int i = key; i < this.favoriteCount - 1; i++) {
                this.overwriteFavoriteBrew(i, this.getBrewFromFavorites(i+1));
            }
            // Slet den sidste Brew
            this.unset(this.favoritesKey + (this.favoriteCount - 1));
        }
        // Opdater antallet af gemte Brews
        this.saveInt(this.favoriteCountKey, this.favoriteCount - 1);
    }

    @Override
    public void overwriteFavoriteBrew(int key, Brew value) throws BrewException {
        this.saveString(this.favoritesKey + key, value.toJson());
    }
}
