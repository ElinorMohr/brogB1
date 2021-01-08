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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        storage = StorageServiceSharedPref.getInstance();
        View root = inflater.inflate(R.layout.recipes_layout, container, false);
        try {
            // Henter gemte informationer om Brew fra storage
            ArrayList<Brew> favoriteList = storage.getFavoriteBrews();
            ArrayList<Brew> recipeList = storage.getAllBrews();
            ArrayList<String> viewMain = new ArrayList<String>();
            viewMain.add("Fars morgen kaffe");
            viewMain.add("Ekstra mælk i denne");

            // Find elementerne, som der skal udfyldes med lister
            ListView listMain = root.findViewById(R.id.list_view_favorites);
            ListView listSec = root.findViewById(R.id.list_view_sec_recipes);

            // Sammenkobling af elementerne og data
            //ArrayAdapter<Brew> adapterMain = new RecipiesAdapter(getContext(), recipeList, "favorite");
            ArrayAdapter<String> adapterMain = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, viewMain);
            listMain.setAdapter(adapterMain);
            listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Intent intent = new Intent(new Intent(getContext(), Brewing.class));
                    startActivity(intent);
                }
            });
            ArrayAdapter<Brew> adapterSec = new RecipiesAdapter(getContext(), recipeList);
            listSec.setAdapter(adapterSec);
        } catch (StorageServiceException e) {
            e.printStackTrace();
        } catch (BrewException e) {
            e.printStackTrace();
        }

        return root;
    }
}
