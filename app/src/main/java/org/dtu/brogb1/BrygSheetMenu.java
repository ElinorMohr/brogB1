package org.dtu.brogb1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.dtu.brogb1.activity.RecipeActivity;
import org.dtu.brogb1.ui.main.PlaceholderFragment;


public class BrygSheetMenu extends BottomSheetDialogFragment {

    Button quickBrew, recipes, newBrew;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bryg_sheet_menu, container, false);


        quickBrew = v.findViewById(R.id.QuickBrew);
        recipes = v.findViewById(R.id.Recipes);
        newBrew = v.findViewById(R.id.NewBrew);

        quickBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Brygning.class);
                startActivity(intent);
            }
        });

        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);
            }
        });
        newBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NyOpskrift.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
