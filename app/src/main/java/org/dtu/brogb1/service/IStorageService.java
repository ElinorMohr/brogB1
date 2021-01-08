package org.dtu.brogb1.service;

import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;

import java.util.ArrayList;
import java.util.List;

public interface IStorageService {
    void saveString(String key, String value);
    String getString(String key);
    void saveBool(String key, boolean value);
    boolean getBool(String key);
    void saveInt(String key, int value);
    int getInt(String key);
    void saveFloat(String key, int value);
    float getFloat(String key);
    void unset(String key);

    int saveBrew(Brew value) throws BrewException;
    Brew getBrew(int key) throws StorageServiceException, BrewException;
    ArrayList<Brew> getAllBrews() throws StorageServiceException, BrewException;
    int getBrewCount();
    void deleteBrew(int key) throws StorageServiceException, BrewException;
    void overwriteBrew(int key, Brew value) throws BrewException;

    void setQuickBrew(int value);
    Brew getQuickBrew() throws StorageServiceException, BrewException;

    void saveBrewToHistory(Brew value) throws BrewException, StorageServiceException;
    Brew getBrewFromHistory(int key) throws StorageServiceException, BrewException;
    ArrayList<Brew> getBrewHistory() throws StorageServiceException, BrewException;
    int getBrewHistoryCount();

    int saveBrewToFavorites(Brew value) throws BrewException;
    Brew getBrewFromFavorites(int key) throws StorageServiceException, BrewException;
    ArrayList<Brew> getFavoriteBrews() throws StorageServiceException, BrewException;
    int getBrewFavoriteCount();
    void deleteFavoriteBrew(int key) throws StorageServiceException, BrewException;
    void overwriteFavoriteBrew(int key, Brew value) throws BrewException;
}
