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

import java.util.ArrayList;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class Recipes extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.recipes_layout, container, false);
        ListView listMain = root.findViewById(R.id.list_view_favorites);
        ListView listSec = root.findViewById(R.id.list_view_sec_recipes);
        ArrayList<String> viewMain = new ArrayList<String>();
        ArrayList<String> viewSes = new ArrayList<String>();
        viewMain.add("Fars morgen kaffe");
        viewMain.add("Ekstra mælk i denne");
        viewSes.add("Latten");
        ArrayAdapter<String> adapterSec = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, viewSes);
        listSec.setAdapter(adapterSec);
        ArrayAdapter<String> adapterMain = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, viewMain);
        listMain.setAdapter(adapterMain);

        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent intent = new Intent(new Intent(getContext(), Brewing.class));
                startActivity(intent);
            }
        });
        listSec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent intent = new Intent(new Intent(getContext(), Brewing.class));
                startActivity(intent);
            }
        });
        return root;
    }
}
