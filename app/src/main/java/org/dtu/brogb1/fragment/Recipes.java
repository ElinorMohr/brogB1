package org.dtu.brogb1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.Brewing;
import org.dtu.brogb1.adapter.RecipiesAdapter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.util.ArrayList;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class Recipes extends Fragment {
    IStorageService storage;
    ArrayList<Brew> favoriteList;
    ArrayList<Brew> recipeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO
        //storage = StorageServiceSharedPref.getInstance();
        View root = inflater.inflate(R.layout.recipes_layout, container, false);
        try {
            // Henter gemte informationer om Brew fra storage
            favoriteList = storage.getFavoriteBrews();
            recipeList = storage.getAllBrews();


            // Find elementerne, som der skal udfyldes med lister
            ListView listMain = root.findViewById(R.id.list_view_favorites);
            ListView listSec = root.findViewById(R.id.list_view_sec_recipes);

            // Sammenkobling af elementerne og data
            ArrayAdapter<Brew> adapterMain = new RecipiesAdapter(getContext(), favoriteList, "favorite");
            listMain.setAdapter(adapterMain);
            listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(getContext(), Brewing.class);
                    try {
                        intent.putExtra("Brew", favoriteList.get(position).toJson());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
            ArrayAdapter<Brew> adapterSec = new RecipiesAdapter(getContext(), recipeList);
            listSec.setAdapter(adapterSec);
            listSec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(getContext(), Brewing.class);
                    try {
                        intent.putExtra("Brew", recipeList.get(position).toJson());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        } catch (StorageServiceException e) {
            e.printStackTrace();
        } catch (BrewException e) {
            e.printStackTrace();
        }

        return root;
    }
}
