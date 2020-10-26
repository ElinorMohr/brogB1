package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class StartSide extends AppCompatActivity implements View.OnClickListener  {
    Button brew,clean,community,guide,bsmquickBrew, bsmrecipes, bsmnewBrew;
    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_side);
        brew = findViewById(R.id.Brew);
        clean = findViewById(R.id.Clean);
        community = findViewById(R.id.Community);
        guide = findViewById(R.id.Guide);
        settings = findViewById(R.id.Settings);


        brew.setOnClickListener(this);
        clean.setOnClickListener(this);
        community.setOnClickListener(this);
        guide.setOnClickListener(this);
        settings.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Brew:
                BrygSheetMenu brygMenu = new BrygSheetMenu();
                brygMenu.show(getSupportFragmentManager(),"FragmentBrygMenu");
                break;
            case R.id.Clean:
                Toast.makeText(this, "Clean clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Community:
                Toast.makeText(this, "Community clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Guide:
                Toast.makeText(this, "Guide clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Settings:
                Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}