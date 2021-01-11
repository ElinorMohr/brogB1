package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

public class EditBrew extends AppCompatActivity {
    Dialog dialogue;
    private String brewName, brewPics, grindSize;
    private int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    EditText Edit_ETBrewName, Edit_ETGroundCoffee, Edit_ETRatio, Edit_ETTemp, Edit_ETBloomWater, Edit_ETBloomTime, Edit_ETTotalMin, Edit_ETTotalSec;
    Spinner Edit_SpinnerInputGrindSize;
    Brew brew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);
        ImageButton info = findViewById(R.id.IGroundCoffee);
        Button EditNow = (Button) findViewById(R.id.editBT);
        StorageServiceSharedPref storageServiceSharedPref = StorageServiceSharedPref.getInstance();

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

        Edit_ETBrewName = findViewById(R.id.edit_Opskrifts_navn);
        Edit_ETGroundCoffee = findViewById(R.id.edit_inputGroundCoffee);
        Edit_SpinnerInputGrindSize = findViewById(R.id.edit_inputgrindsize);
        Edit_ETRatio = findViewById(R.id.edit_inputRatio);
        Edit_ETTemp = findViewById(R.id.edit_inputTemperature);
        Edit_ETBloomWater = findViewById(R.id.edit_inputBloomWater);
        Edit_ETBloomTime = findViewById(R.id.edit_inputBloomTime);
        Edit_ETTotalMin = findViewById(R.id.edit_inputTotalTimeMin);
        Edit_ETTotalSec =findViewById(R.id.edit_inputTotalTimeSec);
        // intervallerne for hver af inputs

        Edit_ETGroundCoffee.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        Edit_ETRatio.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        Edit_ETTemp.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        Edit_ETBloomWater.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        Edit_ETBloomTime.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        Edit_ETTotalMin.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});
        Edit_ETTotalSec.setFilters(new InputFilter[]{new MinMaxFilter("0", "59")});


        // Her lægges alle værdierne ind i edit teksene. Så de kommer frem.
        if (brew != null){
            if(!(brew.getBrewName().isEmpty())) {
                Edit_ETBrewName.setText(brew.getBrewName());
            }
            Edit_ETGroundCoffee.setText(Integer.toString(brew.getGroundCoffee()));
            System.out.println(Edit_ETGroundCoffee);

            // vi tjekker for hvilken en i spinneren er valgt
            grindSize = brew.getGrindSize();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.grind_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Edit_SpinnerInputGrindSize.setAdapter(adapter);
            if (grindSize != null) {
                int spinnerPosition = adapter.getPosition(grindSize);
                Edit_SpinnerInputGrindSize.setSelection(spinnerPosition);
            }

            Edit_ETRatio.setText(Integer.toString(brew.getCoffeeWaterRatio()));
            Edit_ETTemp.setText(Integer.toString(brew.getBrewingTemperature()));
            Edit_ETBloomWater.setText(Integer.toString(brew.getBloomWater()));
            Edit_ETBloomTime.setText(Integer.toString(brew.getBloomTime()));
            Edit_ETTotalMin.setText(Integer.toString(brew.getBrewTimeMin()));
            Edit_ETTotalSec.setText(Integer.toString(brew.getBrewTimeSec()));
        }


        //når der brygges
        EditNow.setOnClickListener(v -> {
                    // gemmer inputtet fra ui'en til værdierne
                    try {
                        groundCoffee = Integer.parseInt(Edit_ETGroundCoffee.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(this, "Need input at ground coffee", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    grindSize = Edit_SpinnerInputGrindSize.getSelectedItem().toString();

                    try {
                        coffeeWaterRatio = Integer.parseInt(Edit_ETRatio.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(this, "Need input at Coffee/water ratio", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    try {
                        brewingTemperature = Integer.parseInt(Edit_ETTemp.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(this, "Need input at brewing temperature", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                    try {
                        bloomWater = Integer.parseInt(Edit_ETBloomWater.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(this, "Need input at bloom water", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                    try {
                        bloomTime = Integer.parseInt(Edit_ETBloomTime.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(this, "Need input at bloom time", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

            // tiden skal blive lagt sammen
            if(Edit_ETTotalMin.getText().toString().isEmpty()){
                brewTimeMin = 0;
            } else {
                brewTimeMin = Integer.parseInt(Edit_ETTotalMin.getText().toString());
            }

            if(Edit_ETTotalSec.getText().toString().isEmpty()){
                brewTimeSec = 0;
            } else {
                brewTimeSec = Integer.parseInt(Edit_ETTotalSec.getText().toString());
            }
            try {
                if(Edit_ETTotalMin.getText().toString().isEmpty() && Edit_ETTotalSec.getText().toString().isEmpty() ){
                    Toast.makeText(this, "time can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Need input at total brewing time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }


                     //TODO den skal gemme brew hvis den er gemt ellers skal den bare gå videre med værdierne.
            /*
                    // vi skal gemme ændringerne
                    IStorageService storage = StorageServiceSharedPref.getInstance();
                    if (brewName.isEmpty()) {
                        Toast.makeText(this, "your brew needs a name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                       storage.saveBrew(brew);
                    } catch (BrewException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

             */


                    // vores ændret brew bliver sendt til brewing
            Intent intent = new Intent(this, Brewing.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                intent.putExtra("Brew", brew.toJson());
            } catch (BrewException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
        });



        // info knappen
        info.setOnClickListener(v -> {
            BrewSheetMenu brygMenu = new BrewSheetMenu();
            brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
        });
    }
}