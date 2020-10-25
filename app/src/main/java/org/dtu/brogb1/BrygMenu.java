package org.dtu.brogb1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BrygMenu extends BottomSheetDialogFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bryg_fragment_menu, container, false);

        Button quickBrew = v.findViewById(R.id.Guide);
        Button recipes = v.findViewById(R.id.Recipes);
        Button newBrew = v.findViewById(R.id.NewBrew);

        /*
        quickBrew.setOnClickListener(this);
        recipes.setOnClickListener(this);
        newBrew.setOnClickListener(this);

         */

        return v;
    }

    @Override
    public void onClick(View v) {

    }

}