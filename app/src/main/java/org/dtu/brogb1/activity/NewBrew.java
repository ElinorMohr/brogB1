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
    private double groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;
    EditText editBrewName, editGroundCoffee, editRatio, editTemp, editBloomWater, editBloomTime, editTotalMin, editTotalSec;
    Spinner SpinnerInputGrindSize;
    StorageServiceSharedPref sharedPref = StorageServiceSharedPref.getInstance();

    ImageButton favoriteBT;
    boolean buttonOn;

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
                groundCoffee = Double.parseDouble(editGroundCoffee.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Need input at ground coffee", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            grindSize = SpinnerInputGrindSize.getSelectedItem().toString();

            try {
                coffeeWaterRatio = Double.parseDouble(editRatio.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Need input at Coffee/water ratio", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                brewingTemperature = Double.parseDouble(editTemp.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Need input at brewing temperature", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            try {
                bloomWater = Double.parseDouble(editBloomWater.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Need input at bloom water", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                bloomTime = Double.parseDouble(editBloomTime.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Need input at bloom time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            totalBrewingTime = 0.0;
            // tiden skal blive lagt sammen
            if(editTotalMin.getText().toString().isEmpty()){
                totalBrewingTime = 0;
            } else {
                totalBrewingTime += Double.parseDouble(editTotalMin.getText().toString());
            }
            try {
                if(editTotalSec.getText().toString().isEmpty()){
                    totalBrewingTime = 0;
                } else {
                    totalBrewingTime += 0.01 * (Double.parseDouble(editTotalSec.getText().toString()));
                }

                if(totalBrewingTime == 0.0){
                    Toast.makeText(this, "time can't be 0.0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at total brewing time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            // gemmer det i en newBrew
            setBrewValues();

            CheckBox saveBrew = (CheckBox) findViewById(R.id.savebox);

            // her tjekker vi, hvis den er markeret som save. bliver denne bryg gemt i storage
            if (saveBrew.isChecked()) {
                IStorageService storage = StorageServiceSharedPref.getInstance();
                if (brewName.isEmpty()) {
                    Toast.makeText(this, "your brew needs a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    storage.saveBrew(newBrew);
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

            if (!buttonOn) {
                buttonOn = true;
                favoriteBT.setBackground(getResources().getDrawable(R.drawable.ic_heart));
                setBrewValues();
                try {
                    sharedPref.saveBrewToFavorites(newBrew);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                buttonOn = false;
                favoriteBT.setBackground(getResources().getDrawable(R.drawable.ic_heart_empty));
                setBrewValues();
                try {
                    //TODO
                    //sharedPref.deleteFavoriteBrew();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private void setBrewValues(){
        newBrew.setGroundCoffee(groundCoffee);
        newBrew.setGrindSize(grindSize);
        newBrew.setCoffeeWaterRatio(coffeeWaterRatio);
        newBrew.setBrewingTemperature(brewingTemperature);
        newBrew.setBloomWater(bloomWater);
        newBrew.setBloomTime(bloomTime);
        newBrew.setTotalBrewingTime(totalBrewingTime);
        brewName = editBrewName.getText().toString();
        newBrew.setBrewName(brewName);
    }
}