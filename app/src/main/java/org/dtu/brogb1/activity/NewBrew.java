package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import org.dtu.brogb1.EditBrewValues;
import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

/**
 * @author Elinor Mikkelsen s191242
 * @author Kristoffer Baumgarten s180500
 * @author Theis Villumsen s195461
 * @author Asim Raja s040727
 * @author Betina Hansen s195389
 */

public class NewBrew extends EditBrewValues {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brew);
        TAG = NewBrew.class.getSimpleName();
        storage = StorageServiceSharedPref.getInstance();
        brew = new Brew();

        Button brewNow = (Button) findViewById(R.id.brew_now_recipe);
        info = findViewById(R.id.i_ground_coffee);
        favoriteBt = findViewById(R.id.edit_brewing_favorite_bt);

        coffeeImage = findViewById(R.id.new_brew_image);

        // instansere alle vores givende værdier til et bryg
        editETBrewName = findViewById(R.id.brew_name);
        editETGroundCoffee = findViewById(R.id.input_ground_coffee);
        editSpinnerInputGrindSize = findViewById(R.id.input_grind_size);
        editETRatio = findViewById(R.id.input_ratio);
        editETTemp = findViewById(R.id.input_temperature);
        editETBloomWater = findViewById(R.id.input_bloom_water);
        editETBloomTime = findViewById(R.id.input_bloom_time);
        editETTotalMin = findViewById(R.id.input_total_time_min);
        editETTotalSec = findViewById(R.id.input_total_time_sec);
        favoriteBt = findViewById(R.id.new_brew_favorite_bt);
        CheckBox saveBrew = (CheckBox) findViewById(R.id.savebox);
        setFilters();

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

            // her tjekker vi, hvis den er markeret som save eller favorit, bliver denne bryg gemt i storage
            if (saveBrew.isChecked()) {
                try {
                    if (!brew.getBrewName().isEmpty()){
                        Util.setStorage(brew, favoriteOn, storage, TAG);
                    }
                    else
                        Toast.makeText(this, "Giv bryg et navn", Toast.LENGTH_SHORT).show();
                } catch (StorageServiceException e) {
                    e.printStackTrace();
                    Util.log(TAG, e);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (!saveBrew.isChecked() && favoriteOn) {
                saveFavorite();
            }

            try {
                Intent intent = new Intent(this, Brewing.class);
                intent.putExtra("Brew", brew.toJson());
                startActivity(intent);
                finish();
            } catch (BrewException e) {
                e.printStackTrace();
                Util.log(TAG, e);
                Toast.makeText(this, "Kunne ikke gå til bryg", Toast.LENGTH_SHORT).show();
            }
        });

        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
        });

        favoriteBt.setOnClickListener(v -> {
            saveFavorite();
        });
        coffeeImage.setOnClickListener(v -> {
            getCoffeeImageFromUserInput();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}