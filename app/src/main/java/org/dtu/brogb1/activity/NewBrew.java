package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.lang.reflect.Array;

/**
 * @author Elinor Mikkelsen s191242
 * @author Kristoffer Baumgarten s180500
 */

public class NewBrew extends AppCompatActivity {
    private String brewName, brewPics, grindSize ;
    private double groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime, totalBrewingTime;
    EditText editGroundCoffee, editRatio, editTemp, editBloomWater, editBloomTime, editTotal ;
    Spinner Spinnerinputgrindsize;

 Brew newBrew = new Brew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brew);

        Button brewNow = (Button) findViewById(R.id.BrewNowRecipe);
        ImageButton info =  findViewById(R.id.IGroundCoffee);

        // instansere alle vores givende værdier til et bryg
        editGroundCoffee = findViewById(R.id.inputGroundCoffee);
        Spinnerinputgrindsize = findViewById(R.id.inputgrindsize);
        editRatio = findViewById(R.id.inputRatio);
        editTemp = findViewById(R.id.inputTemperature);
        editBloomWater = findViewById(R.id.inputBloomWater);
        editBloomTime = findViewById(R.id.inputBloomTime);
        editTotal = findViewById(R.id.inputTotalTime);


        //når der brygges
        brewNow.setOnClickListener(v -> {


            // gemmer inputtet fra ui'en til værdierne
            try {
                groundCoffee = Double.parseDouble(editGroundCoffee.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at ground coffee", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            grindSize = Spinnerinputgrindsize.getSelectedItem().toString();

            try {
                coffeeWaterRatio = Double.parseDouble(editRatio.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at Coffee/water ratio", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                brewingTemperature = Double.parseDouble(editTemp.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at brewing temperature", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            try {
                bloomWater = Double.parseDouble(editBloomWater.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at bloom water", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                bloomTime = Double.parseDouble(editBloomTime.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at bloom time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            try {
                totalBrewingTime = Double.parseDouble(editTotal.getText().toString());
            }catch (Exception e) {
                Toast.makeText(this, "Need input at total brewing time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            // gemmer det i en newBrew
            newBrew.setGroundCoffee(groundCoffee);
            newBrew.setGrindSize(grindSize);
            newBrew.setCoffeeWaterRatio(coffeeWaterRatio);
            newBrew.setBrewingTemperature(brewingTemperature);
            newBrew.setBloomWater(bloomWater);
            newBrew.setBloomTime(bloomTime);
            newBrew.setTotalBrewingTime(totalBrewingTime);


            CheckBox saveBrew =(CheckBox) findViewById(R.id.savebox);

            // her tjekker vi, hvis den er markeret som save. bliver denne bryg gemt i storage
            if(saveBrew.isChecked()){
                IStorageService storage = StorageServiceSharedPref.getInstance();
                try {
                    storage.saveBrew(newBrew);
                } catch (BrewException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(this, Brewing.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(),"FragmentBrygMenu");
        });
    }
}