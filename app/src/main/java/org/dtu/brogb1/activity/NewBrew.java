package org.dtu.brogb1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;
import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import io.sentry.Sentry;

import static android.content.Intent.ACTION_OPEN_DOCUMENT;

/**
 * @author Elinor Mikkelsen s191242
 * @author Kristoffer Baumgarten s180500
 * @author Theis Villumsen s195461
 * @author Asim Raja s040727
 * @author Betina Hansen s195389
 */

public class NewBrew extends AppCompatActivity {
    private static final String TAG = NewBrew.class.getSimpleName();
    private String brewName, grindSize;
    private int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    EditText editBrewName, editGroundCoffee, editRatio, editTemp, editBloomWater, editBloomTime, editTotalMin, editTotalSec;
    Spinner spinnerInputGrindSize;
    ImageButton favoriteBT;
    ImageView coffeeImageView;
    boolean favoriteOn;
    IStorageService storage;
    Brew newBrew = new Brew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brew);
        storage = StorageServiceSharedPref.getInstance();

        Button brewNow = (Button) findViewById(R.id.brew_now_recipe);
        ImageButton info = findViewById(R.id.i_ground_coffee);

        coffeeImageView = findViewById(R.id.new_brew_image);

        // instansere alle vores givende værdier til et bryg
        editBrewName = findViewById(R.id.brew_name);
        editGroundCoffee = findViewById(R.id.input_ground_coffee);
        spinnerInputGrindSize = findViewById(R.id.input_grind_size);
        editRatio = findViewById(R.id.input_ratio);
        editTemp = findViewById(R.id.input_temperature);
        editBloomWater = findViewById(R.id.input_bloom_water);
        editBloomTime = findViewById(R.id.input_bloom_time);
        editTotalMin = findViewById(R.id.input_total_time_min);
        editTotalSec = findViewById(R.id.input_total_time_sec);
        favoriteBT = findViewById(R.id.new_brew_favorite_bt);
        CheckBox saveBrew = (CheckBox) findViewById(R.id.savebox);


        editGroundCoffee.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editRatio.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editTemp.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editBloomWater.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editBloomTime.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editTotalMin.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});
        editTotalSec.setFilters(new InputFilter[]{new MinMaxFilter("0", "59")});

        //når der brygges
        brewNow.setOnClickListener(v -> {
            // gemmer inputtet fra ui'en til værdierne
            try {
                getBrewValuesFromUI();
                setBrewValues();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // her tjekker vi, hvis den er markeret som save. bliver denne bryg gemt i storage
            if (saveBrew.isChecked()) {
                try {
                    Util.setStorage(newBrew, favoriteOn, storage, TAG);
                } catch (StorageServiceException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            Intent intent = new Intent(this, Brewing.class);
            try {
                intent.putExtra("Brew", newBrew.toJson());
            } catch (BrewException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });

        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
        });

        favoriteBT.setOnClickListener(v -> {
            if (!favoriteOn) {
                try {
                    getBrewValuesFromUI();
                    setBrewValues();
                    Util.setStorage(newBrew, !favoriteOn, storage, TAG);
                } catch (Exception e) {
                    Toast.makeText(this, "Error in save", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                Toast.makeText(this,"saved as favorite.", Toast.LENGTH_SHORT).show();
            } else {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_empty));
            }
            favoriteOn = !favoriteOn;
        });
        coffeeImageView.setOnClickListener(v -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, 1); //request code til det der sendes videre.
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Uri image_uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    Util.saveImageFrom(newBrew,bitmap,this.getApplicationContext(), TAG);
                    coffeeImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Util.log(TAG, e);
                }

            }
        }
    }

    private void setBrewValues() {
        newBrew.setGroundCoffee(groundCoffee);
        newBrew.setGrindSize(grindSize);
        newBrew.setCoffeeWaterRatio(coffeeWaterRatio);
        newBrew.setBrewingTemperature(brewingTemperature);
        newBrew.setBloomWater(bloomWater);
        newBrew.setBloomTime(bloomTime);
        newBrew.setBrewTimeMin(brewTimeMin);
        newBrew.setBrewTimeSec(brewTimeSec);
        newBrew.setFavoriteKey(-1);
        newBrew.setStorageKey(-1);
        brewName = editBrewName.getText().toString();
        newBrew.setBrewName(brewName);
    }
    private void getBrewValuesFromUI() throws Exception {
        groundCoffee = getIntInput(editGroundCoffee, "ground Coffee");
        grindSize = spinnerInputGrindSize.getSelectedItem().toString();
        coffeeWaterRatio = getIntInput(editRatio, "ratio");
        brewingTemperature = getIntInput(editTemp, "Temperature");
        bloomWater = getIntInput(editTemp, "bloom Water");
        bloomTime = getIntInput(editTemp, "bloom Time");
        getTimeInput();
        brewName = editBrewName.getText().toString();
    }

    private int getIntInput(EditText v, String text) throws Exception {
        int output;
        try {
            output = Integer.parseInt(v.getText().toString());
            if (output == 0) {
                Toast.makeText(this, "input in " + text + " is 0", Toast.LENGTH_SHORT).show();
                throw new Exception();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Need input at " + text, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            throw new Exception();
        }
        return output;
    }

    private void getTimeInput() throws Exception {
        // tiden skal blive lagt sammen
        if (editTotalMin.getText().toString().isEmpty()) {
            brewTimeMin = 0;
        } else {
            brewTimeMin = Integer.parseInt(editTotalMin.getText().toString());
        }
        if (editTotalSec.getText().toString().isEmpty()) {
            brewTimeSec = 0;
        } else {
            brewTimeSec = Integer.parseInt(editTotalSec.getText().toString());
        }
        if ((editTotalMin.getText().toString().isEmpty() && editTotalSec.getText().toString().isEmpty()) || (brewTimeMin == 0 && brewTimeSec == 0)) {
            Toast.makeText(this, "time can't be empty", Toast.LENGTH_SHORT).show();
            throw new Exception();
        }
    }
}