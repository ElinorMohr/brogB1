package org.dtu.brogb1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.dtu.brogb1.R;
import org.dtu.brogb1.adapter.RecipiesAdapter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

import java.util.ArrayList;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class Recipes extends Fragment {
    private static final String TAG = Recipes.class.getSimpleName();
    IStorageService storage;
    ArrayList<Brew> favoriteList = new ArrayList<Brew>(), recipeList = new ArrayList<Brew>();
    ListView listMain, listSec;
    ArrayAdapter<Brew> adapterSec, adapterMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        storage = StorageServiceSharedPref.getInstance();
        View root = inflater.inflate(R.layout.recipes_layout, container, false);
        return populateLists(root);
    }

    private View populateLists(View root) {
        try {
            // Henter gemte informationer om Brew fra storage
            favoriteList = storage.getFavoriteBrews();

            // Find elementerne, som der skal udfyldes med lister
            listMain = root.findViewById(R.id.list_view_favorites);

            // Sammenkobling af elementerne og data
            adapterMain = new RecipiesAdapter(getContext(), favoriteList, "favorite");
            listMain.setAdapter(adapterMain);

        } catch (StorageServiceException | BrewException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
        try {
            // Henter gemte informationer om Brew fra storage
            recipeList = storage.getAllBrews();
            // Find elementerne, som der skal udfyldes med lister
            listSec = root.findViewById(R.id.list_view_sec_recipes);

            // Sammenkobling af elementerne og data

            adapterSec = new RecipiesAdapter(getContext(), recipeList);
            listSec.setAdapter(adapterSec);
        } catch (StorageServiceException | BrewException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            favoriteList.clear();
            favoriteList.addAll(storage.getFavoriteBrews());
            recipeList.clear();
            recipeList.addAll(storage.getAllBrews());
        } catch (StorageServiceException | BrewException e) {
            Util.log(TAG, e);
            e.printStackTrace();
        }
        adapterMain.notifyDataSetChanged();
        adapterSec.notifyDataSetChanged();
    }

}
