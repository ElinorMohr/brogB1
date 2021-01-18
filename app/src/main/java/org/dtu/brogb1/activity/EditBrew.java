package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

/**
 * @author Kristoffer Baumgarten s180500
 * @author Betina Hansen s195389
 * @author Elinor Mikkelsen s191242
 */

public class EditBrew extends EditBrewValuesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);
        storage = StorageServiceSharedPref.getInstance();
        TAG = EditBrew.class.getSimpleName();

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
            Util.log(TAG, e);
            finish();
        }

        coffeeImage = findViewById(R.id.edit_image);
        editETBrewName = findViewById(R.id.edit_Opskrifts_navn);
        editETGroundCoffee = findViewById(R.id.edit_inputGroundCoffee);
        editSpinnerInputGrindSize = findViewById(R.id.edit_inputgrindsize);
        editETRatio = findViewById(R.id.edit_inputRatio);
        editETTemp = findViewById(R.id.edit_inputTemperature);
        editETBloomWater = findViewById(R.id.edit_inputBloomWater);
        editETBloomTime = findViewById(R.id.edit_inputBloomTime);
        editETTotalMin = findViewById(R.id.edit_inputTotalTimeMin);
        editETTotalSec = findViewById(R.id.edit_inputTotalTimeSec);
        ImageButton info = findViewById(R.id.i_ground_coffee);
        ImageButton favoriteBt = findViewById(R.id.edit_brewing_favorite_bt);
        Button EditNow = (Button) findViewById(R.id.editBT);

        setFilters();

        // Her lægges alle værdierne ind i edit teksene. Så de kommer frem.
        if (brew != null) {
            if (!(brew.getBrewName().isEmpty())) {
                editETBrewName.setText(brew.getBrewName());
            }
            editETGroundCoffee.setText(Integer.toString(brew.getGroundCoffee()));
            System.out.println(editETGroundCoffee);

            // vi tjekker for hvilken en i spinneren er valgt
            grindSize = brew.getGrindSize();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.grind_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editSpinnerInputGrindSize.setAdapter(adapter);
            if (grindSize != null) {
                int spinnerPosition = adapter.getPosition(grindSize);
                editSpinnerInputGrindSize.setSelection(spinnerPosition);
            }

            editETRatio.setText(Integer.toString(brew.getCoffeeWaterRatio()));
            editETTemp.setText(Integer.toString(brew.getBrewingTemperature()));
            editETBloomWater.setText(Integer.toString(brew.getBloomWater()));
            editETBloomTime.setText(Integer.toString(brew.getBloomTime()));
            editETTotalMin.setText(Integer.toString(brew.getBrewTimeMin()));
            editETTotalSec.setText(Integer.toString(brew.getBrewTimeSec()));
            if (!brew.getBrewPics().isEmpty()) {
                new AsyncTaskGetImage(brew, coffeeImage, this.getContentResolver(), TAG).execute();
            }
            if (brew.getFavoriteKey() >= 0) {
                favoriteBt.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                favoriteOn = true;
            }
            //når der brygges
            EditNow.setOnClickListener(v -> {
                try {
                    getBrewValuesFromUI();
                    setBrewValues();
                } catch (Exception e) {
                    return;
                }
                try {
                    Util.setStorage(brew, favoriteOn, storage, TAG);
                } catch (StorageServiceException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                // vores ændret brew bliver sendt til brewing
                Intent intent = new Intent(this, Brewing.class);
                try {
                    intent.putExtra("Brew", brew.toJson());
                    Log.d(TAG, brew.toJson());
                } catch (BrewException e) {
                    Util.log(TAG, e);
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });

            // info knappen
            info.setOnClickListener(v -> {
                BrewSheetMenu brygMenu = new BrewSheetMenu();
                brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
            });

            favoriteBt.setOnClickListener(v -> {
                saveFavorite();
            });

            coffeeImage.setOnClickListener(v -> {
                getCoffeeImageFromUserInput();
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
