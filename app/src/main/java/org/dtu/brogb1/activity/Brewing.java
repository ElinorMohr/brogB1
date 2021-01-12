package org.dtu.brogb1.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.util.Base64;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 * @author Kristoffer Baumgarten s180500
 * @author Asim Raja s040727
 * @author Betina Hansen s195389
 */

public class Brewing extends AppCompatActivity {
    Button brewNow;
    Dialog dialogue;
    TextView TVBrewName, TVGrindSize, TVGroundCoffee, TVRatio, TVTemp, TVBloomWater, TVBloomTime, TVTimeMin, TVTimeSec, TVEdit;
    ImageButton favoriteBT;
    boolean buttonOn;
    Brew brew;
    //TODO
    //StorageServiceSharedPref storageServiceSharedPref = StorageServiceSharedPref.getInstance();

    ImageView kaffebillede;

    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewing);
        brewNow = findViewById(R.id.BrewNow);
        try {
            if (getIntent().hasExtra("Brew")) {
                brew = BrewFactory.fromJson(getIntent().getExtras().getString("Brew"));
            } else {
                Toast.makeText(this, "Der blev ikke givet information om en bryg", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (BrewException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        kaffebillede = findViewById(R.id.new_brew_image);


        TVBrewName = findViewById(R.id.Opskriftens_navn);
        TVGroundCoffee = findViewById(R.id.valueGroundCoffee);
        TVGrindSize = findViewById(R.id.valuegrindsize);
        TVRatio = findViewById(R.id.valueRatio);
        TVTemp = findViewById(R.id.valueTemperature);
        TVBloomWater = findViewById(R.id.valueBloomWater);
        TVBloomTime = findViewById(R.id.valueBloomTime);
        TVTimeMin = findViewById(R.id.valueTimeMin);
        TVTimeSec = findViewById(R.id.valueTimeSec);

        favoriteBT = (ImageButton) findViewById(R.id.BrewingFavoriteBT);
        favoriteBT.setOnClickListener(imgButtonHandler);
        TVEdit = findViewById(R.id.EditBrewTxt);

        // Edit teksten
        if (brew != null){
            TVBrewName.setText(Html.fromHtml("<u>" + brew.getBrewName() + "</u>"));
            TVGroundCoffee.setText(Integer.toString(brew.getGroundCoffee()));
            TVGrindSize.setText(brew.getGrindSize());
            TVRatio.setText(Integer.toString(brew.getCoffeeWaterRatio()));
            TVTemp.setText(Integer.toString(brew.getBrewingTemperature()));
            TVBloomWater.setText(Integer.toString(brew.getBloomWater()));
            TVBloomTime.setText(Integer.toString(brew.getBloomTime()));
            TVTimeMin.setText(Integer.toString(brew.getBrewTimeMin()));
            TVTimeSec.setText(Integer.toString(brew.getBrewTimeSec()));
            if (brew.getFavoriteKey() >= 0) {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
            }
            if (brew.getFavoriteKey() == -1 && brew.getStorageKey() == -1) {
                favoriteBT.setVisibility(View.INVISIBLE);
            }
            if(false && brew.getBrewPics().isEmpty()){
                byte[] decodedString = new byte[0];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    decodedString = Base64.getDecoder().decode(brew.getBrewPics());
                }
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                kaffebillede.setImageBitmap(decodedByte);
            }
        }

        brewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brew != null){
                    brew.setLastBrewTime();
                    try {
                        //TODO
                        //storageServiceSharedPref.saveBrewToHistory(brew);
                    } catch (Exception e){
                        Toast.makeText(v.getContext(), "Fejl under gem", Toast.LENGTH_SHORT).show();
                    }
                }
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
                        finish();
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

        TVEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditBrew.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    intent.putExtra("Brew", brew.toJson());
                } catch (BrewException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }


    View.OnClickListener imgButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if (brew.getFavoriteKey() < 0) {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                try {
                    storageServiceSharedPref.saveBrewToFavorites(brew);
                    storageServiceSharedPref.deleteBrew(brew.getStorageKey());
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_empty));
                try {
                    storageServiceSharedPref.deleteFavoriteBrew(brew.getFavoriteKey());
                    storageServiceSharedPref.saveBrew(brew);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
