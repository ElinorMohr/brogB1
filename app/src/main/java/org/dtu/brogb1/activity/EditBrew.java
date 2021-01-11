package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.dtu.brogb1.R;

public class EditBrew extends AppCompatActivity {
    Button editBT;
    Dialog dialogue;
    TextView Edit_TVBrewName, Edit_TVGrindSize, Edit_TVGroundCoffee, Edit_TVRatio, Edit_TVTemp, Edit_TVBloomWater, Edit_TVBloomTime, Edit_TVTotal, Edit_TVEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);
        ImageButton info = findViewById(R.id.IGroundCoffee);







        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
        });
    }
}