package org.dtu.brogb1.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 * @author Kristoffer Baumgarten s180500
 * @author Asim Raja s040727
 * @author Betina Hansen s195389
 */

public class Brewing extends AppCompatActivity {
    private static final String TAG = Brewing.class.getSimpleName();
    Button brewNow;
    Dialog dialogue;
    TextView tvBrewName, tvGrindSize, tvGroundCoffe, tvRatio, tvTemp, tvBloomWater, tvBloomTime, tvTimeMin, tvTimeSec, tvEdit;
    ImageButton favoriteBT, trashBT;
    Brew brew, defaultBrew;
    StorageServiceSharedPref storageServiceSharedPref = StorageServiceSharedPref.getInstance();

    ImageView coffeeImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewing);
        brewNow = findViewById(R.id.BrewNow);
        try {
            defaultBrew = BrewFactory.getBrew("Default");
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

        coffeeImage = findViewById(R.id.kaffebillede);
        tvBrewName = findViewById(R.id.Opskriftens_navn);
        tvGroundCoffe = findViewById(R.id.valueGroundCoffee);
        tvGrindSize = findViewById(R.id.valuegrindsize);
        tvRatio = findViewById(R.id.valueRatio);
        tvTemp = findViewById(R.id.valueTemperature);
        tvBloomWater = findViewById(R.id.valueBloomWater);
        tvBloomTime = findViewById(R.id.valueBloomTime);
        tvTimeMin = findViewById(R.id.valueTimeMin);
        tvTimeSec = findViewById(R.id.valueTimeSec);
        trashBT = (ImageButton) findViewById(R.id.trashcan);

        if((brew.getStorageKey() == -1) && (brew.getFavoriteKey() == -1)){
            trashBT.setVisibility(View.GONE);
        }
        favoriteBT = (ImageButton) findViewById(R.id.brewing_favorite_bt);
        tvEdit = findViewById(R.id.EditBrewTxt);

        // Edit teksten
        if (brew != null){
            tvBrewName.setText(Html.fromHtml("<u>" + brew.getBrewName() + "</u>"));
            tvGroundCoffe.setText(Integer.toString(brew.getGroundCoffee()));
            tvGrindSize.setText(brew.getGrindSize());
            tvRatio.setText(Integer.toString(brew.getCoffeeWaterRatio()));
            tvTemp.setText(Integer.toString(brew.getBrewingTemperature()));
            tvBloomWater.setText(Integer.toString(brew.getBloomWater()));
            tvBloomTime.setText(Integer.toString(brew.getBloomTime()));
            tvTimeMin.setText(Integer.toString(brew.getBrewTimeMin()));
            tvTimeSec.setText(Integer.toString(brew.getBrewTimeSec()));
            if (brew.getFavoriteKey() >= 0) {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
            }

            if(!brew.getBrewPics().isEmpty()){
                Uri image_uri = Uri.fromFile(new File(brew.getBrewPics()));;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    coffeeImage.setImageBitmap(bitmap);
                    coffeeImage.setPadding(0,0,0,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (brew.equals(defaultBrew)) {
                // Henter ikon fra drawable og viser det, hvis det er goldencup og der ikke er sat et billede
                coffeeImage.setImageDrawable(getDrawable(R.drawable.ic_mug_marshmallows));
                // Konverterer drawable til bitmat, konverterer det til base64 og gemmer det på brew, så billedet bliver gemt, hvis man trykker "favorit"
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_mug_marshmallows);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    brew.setBrewPics(encoded);
                } catch(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        trashBT.setOnClickListener(new View.OnClickListener() {

            // først skal vi have adgang til vores storage
            @Override
            public void onClick (View v) {

                // her tjekker vi om denne brew lægger gemt i storage (recipes)
                if (brew.getStorageKey() != -1) {
                    try {
                        storageServiceSharedPref.deleteBrew(brew);
                        brew.setStorageKey(-1);
                        finish();
                    } catch (StorageServiceException e) {
                        e.printStackTrace();
                    } catch (BrewException e) {
                        e.printStackTrace();
                    }
                } else {
                    // eller i favoritter.
                    if (brew.getFavoriteKey() != -1) {
                        try {
                            storageServiceSharedPref.deleteFavoriteBrew(brew);
                            brew.setFavoriteKey(-1);
                            finish();
                        } catch (StorageServiceException e) {
                            e.printStackTrace();
                        } catch (BrewException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        brewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brew != null) {
                    brew.setLastBrewTime();
                    try {
                        storageServiceSharedPref.saveBrewToHistory(brew);
                    } catch (Exception e) {
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

        tvEdit.setOnClickListener(new View.OnClickListener() {
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onClick(View v) {
            if (brew.getFavoriteKey() < 0) {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                try {
                    int k = storageServiceSharedPref.saveBrewToFavorites(brew);
                    storageServiceSharedPref.deleteBrew(brew.getStorageKey());
                    if (brew.equals(defaultBrew) && brew.getFavoriteKey() == -1 && brew.getStorageKey() == -1) {
                        storageServiceSharedPref.setQuickBrew(k);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_empty));
                try {
                    storageServiceSharedPref.deleteFavoriteBrew(brew.getFavoriteKey());
                    storageServiceSharedPref.saveBrew(brew);
                } catch (Exception e) {
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
