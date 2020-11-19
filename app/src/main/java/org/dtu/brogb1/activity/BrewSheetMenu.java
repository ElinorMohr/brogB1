package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.dtu.brogb1.R;


public class BrewSheetMenu extends BottomSheetDialogFragment {

    Button quickBrew, recipes, newBrew;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.brew_sheet_menu, container, false);


        quickBrew = v.findViewById(R.id.QuickBrew);
        recipes = v.findViewById(R.id.Recipes);
        newBrew = v.findViewById(R.id.NewBrew);

        quickBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Brewing.class);
                startActivity(intent);
            }
        });

        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sections.class);
                startActivity(intent);
            }
        });
        newBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewBrew.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
