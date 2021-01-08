package org.dtu.brogb1.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class Brewing extends AppCompatActivity {
    Button brewNow;
    Dialog dialogue;
    TextView TVBrewName, TVGrindSize, TVGroundCoffee, TVRatio, TVTemp, TVBloomWater, TVBloomTime, TVTotal;

    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewing);
        brewNow = findViewById(R.id.BrewNow);
        Brew brew;
        try {
            brew = BrewFactory.fromJson(getIntent().getExtras().getString("Brew"));
        } catch (BrewException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
            brew = new Brew();
        }

        TVBrewName = findViewById(R.id.Opskriftens_navn);
        TVGroundCoffee = findViewById(R.id.valueGroundCoffee);
        TVGrindSize = findViewById(R.id.valuegrindsize);
        TVRatio = findViewById(R.id.valueRatio);
        TVTemp = findViewById(R.id.valueTemperature);
        TVBloomWater = findViewById(R.id.valueBloomWater);
        TVBloomTime = findViewById(R.id.valueBloomTime);
        TVTotal = findViewById(R.id.valueTotalTime);


        TVBrewName.setText(brew.getBrewName());
        TVGroundCoffee.setText(Double.toString(brew.getGroundCoffee()));
        TVGrindSize.setText(brew.getGrindSize());
        TVRatio.setText(Double.toString(brew.getCoffeeWaterRatio()));
        TVTemp.setText(Double.toString(brew.getBrewingTemperature()));
        TVBloomWater.setText(Double.toString(brew.getBloomWater()));
        TVBloomTime.setText(Double.toString(brew.getBloomTime()));
        TVTotal.setText(Double.toString(brew.getTotalBrewingTime()));


        brewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue = new Dialog(v.getContext(), android.R.style.Theme_Black_NoTitleBar);
                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                dialogue.setContentView(R.layout.brewing_progress);
                Button button = dialogue.findViewById(R.id.done_brew);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogue.cancel();
                        Intent intent = new Intent(v.getContext(), HomePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                dialogue.setCancelable(true);
                dialogue.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }, 10000);
            }
        });
    }
}