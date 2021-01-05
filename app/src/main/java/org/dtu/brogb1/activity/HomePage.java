package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.BrewSheetMenu;
import org.dtu.brogb1.activity.Option;
import org.dtu.brogb1.activity.clean.CleanActivityStep1;
import org.dtu.brogb1.activity.community.CommunityActivity;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class HomePage extends AppCompatActivity implements View.OnClickListener  {
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
        Intent intent;
        switch (v.getId()){
            case R.id.Brew:
                BrewSheetMenu brygMenu = new BrewSheetMenu();
                brygMenu.show(getSupportFragmentManager(),"FragmentBrygMenu");
                break;
            case R.id.Clean:
                intent = new Intent(this, CleanActivityStep1.class);
                startActivity(intent);
                break;
            case R.id.Community:
                intent = new Intent(this, CommunityActivity.class);
                startActivity(intent);
                break;
            case R.id.Guide:
                Toast.makeText(this, "Guide clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Settings:
                intent = new Intent(this, Option.class);
                startActivity(intent);
                break;
        }

    }
}