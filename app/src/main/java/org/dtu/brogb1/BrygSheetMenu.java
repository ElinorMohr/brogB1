package org.dtu.brogb1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


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
                Toast.makeText(getActivity(), "recipes click", Toast.LENGTH_SHORT).show();
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
