package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

/**
 * @author Elinor Mikkelsen s191242
 * @author Kristoffer Baumgarten s180500
 * @author Theis Villumsen s195461
 * @author Asim Raja s040727
 */

public class NewBrew extends AppCompatActivity {
    private String brewName, brewPics, grindSize;
    private int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    EditText editBrewName, editGroundCoffee, editRatio, editTemp, editBloomWater, editBloomTime, editTotalMin, editTotalSec;
    Spinner SpinnerInputGrindSize;
    StorageServiceSharedPref sharedPref = StorageServiceSharedPref.getInstance();

    ImageButton favoriteBT;
    boolean favoriteOn;

    Brew newBrew = new Brew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brew);

        Button brewNow = (Button) findViewById(R.id.BrewNowRecipe);
        ImageButton info = findViewById(R.id.IGroundCoffee);

        // instansere alle vores givende værdier til et bryg

        editBrewName = findViewById(R.id.Opskrifts_navn);
        editGroundCoffee = findViewById(R.id.inputGroundCoffee);
        SpinnerInputGrindSize = findViewById(R.id.inputgrindsize);
        editRatio = findViewById(R.id.inputRatio);
        editTemp = findViewById(R.id.inputTemperature);
        editBloomWater = findViewById(R.id.inputBloomWater);
        editBloomTime = findViewById(R.id.inputBloomTime);
        editTotalMin = findViewById(R.id.inputTotalTimeMin);
        editTotalSec = findViewById(R.id.inputTotalTimeSec);

        // dette er vores favorite knap, onclick er i bunden!
        favoriteBT = (ImageButton) findViewById(R.id.NewBrewFavoriteBT);
        favoriteBT.setOnClickListener(imgButtonHandler);

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
                groundCoffee = Integer.parseInt(editGroundCoffee.getText().toString());
                if(groundCoffee == 0){
                    Toast.makeText(this, "input in ground coffee is 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at ground coffee", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            grindSize = SpinnerInputGrindSize.getSelectedItem().toString();

            try {
                coffeeWaterRatio = Integer.parseInt(editRatio.getText().toString());
                if(coffeeWaterRatio == 0) {
                    Toast.makeText(this, "input in coffee Water Ratio is 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at Coffee/water ratio", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                brewingTemperature = Integer.parseInt(editTemp.getText().toString());
                if(brewingTemperature == 0) {
                    Toast.makeText(this, "input in brewing Temperature is 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at brewing temperature", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            try {
                bloomWater = Integer.parseInt(editBloomWater.getText().toString());
                if(bloomWater == 0) {
                    Toast.makeText(this, "input in Bloom Water is 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at bloom water", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                bloomTime = Integer.parseInt(editBloomTime.getText().toString());
                if(bloomTime == 0) {
                    Toast.makeText(this, "input in Bloom Time is 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at bloom time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            // tiden skal blive lagt sammen
            if(editTotalMin.getText().toString().isEmpty()){
                brewTimeMin = 0;
            } else {
                brewTimeMin = Integer.parseInt(editTotalMin.getText().toString());
            }

            if(editTotalSec.getText().toString().isEmpty()){
                brewTimeSec = 0;
            } else {
                brewTimeSec = Integer.parseInt(editTotalSec.getText().toString());
            }
            if((editTotalMin.getText().toString().isEmpty() && editTotalSec.getText().toString().isEmpty()) || (brewTimeMin == 0 && brewTimeSec == 0) ){
                Toast.makeText(this, "Time can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // gemmer det i en newBrew
            setBrewValues();

            CheckBox saveBrew = (CheckBox) findViewById(R.id.savebox);

            // her tjekker vi, hvis den er markeret som save. bliver denne bryg gemt i storage
            if (saveBrew.isChecked()) {
                IStorageService storage = StorageServiceSharedPref.getInstance();
                if (brewName.isEmpty()) {
                    Toast.makeText(this, "Your brew needs a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (favoriteOn)
                        newBrew.setFavoriteKey(storage.saveBrewToFavorites(newBrew));
                    else
                        newBrew.setStorageKey(storage.saveBrew(newBrew));
                } catch (BrewException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(this, Brewing.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    }

    View.OnClickListener imgButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if (!favoriteOn) {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
            } else {
                favoriteBT.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_empty));
            }
            favoriteOn = !favoriteOn;
        }
    };

    private void setBrewValues(){
        newBrew.setGroundCoffee(groundCoffee);
        newBrew.setGrindSize(grindSize);
        newBrew.setCoffeeWaterRatio(coffeeWaterRatio);
        newBrew.setBrewingTemperature(brewingTemperature);
        newBrew.setBloomWater(bloomWater);
        newBrew.setBloomTime(bloomTime);
        newBrew.setBrewTimeMin(brewTimeMin);
        newBrew.setBrewTimeSec(brewTimeSec);
        brewName = editBrewName.getText().toString();
        newBrew.setBrewName(brewName);
    }
}