package org.dtu.brogb1.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.io.IOException;

/**
 * @author Kristoffer Baumgarten s180500
 * @author Betina Hansen s195389
 * @author Elinor Mikkelsen s191242
 */

public class EditBrew extends AppCompatActivity {
    private static final String TAG = EditBrew.class.getSimpleName();
    private String brewName, grindSize;
    private int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    EditText editETBrewName, editETGroundCoffee, editETRatio, editETTemp, editETBloomWater, editETBloomTime, editETTotalMin, editETTotalSec;
    Spinner editSpinnerInputGrindSize;
    Brew brew;
    ImageView coffeeImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);
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
        Button EditNow = (Button) findViewById(R.id.editBT);
        // intervallerne for hver af inputs

        editETGroundCoffee.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETRatio.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETTemp.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETBloomWater.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETBloomTime.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETTotalMin.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});
        editETTotalSec.setFilters(new InputFilter[]{new MinMaxFilter("0", "59")});


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
                Uri image_uri = Uri.parse(brew.getBrewPics());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    coffeeImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //når der brygges
            EditNow.setOnClickListener(v -> {
                // gemmer inputtet fra ui'en til værdierne
                try {
                    groundCoffee = Integer.parseInt(editETGroundCoffee.getText().toString());
                    if (groundCoffee == 0) {
                        Toast.makeText(this, "input in ground coffee is 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Need input at ground coffee", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                grindSize = editSpinnerInputGrindSize.getSelectedItem().toString();

                try {
                    coffeeWaterRatio = Integer.parseInt(editETRatio.getText().toString());
                    if (coffeeWaterRatio == 0) {
                        Toast.makeText(this, "input in coffee Water Ratio is 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Need input at Coffee/water ratio", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                try {
                    brewingTemperature = Integer.parseInt(editETTemp.getText().toString());
                    if (brewingTemperature == 0) {
                        Toast.makeText(this, "input in brewing Temperature is 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Need input at brewing temperature", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

                try {
                    bloomWater = Integer.parseInt(editETBloomWater.getText().toString());
                    if (bloomWater == 0) {
                        Toast.makeText(this, "input in Bloom Water is 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Need input at bloom water", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                try {
                    bloomTime = Integer.parseInt(editETBloomTime.getText().toString());
                    if (bloomTime == 0) {
                        Toast.makeText(this, "input in Bloom Time is 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Need input at bloom time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

                // tiden skal blive lagt sammen
                if (editETTotalMin.getText().toString().isEmpty()) {
                    brewTimeMin = 0;
                } else {
                    brewTimeMin = Integer.parseInt(editETTotalMin.getText().toString());
                }

                if (editETTotalSec.getText().toString().isEmpty()) {
                    brewTimeSec = 0;
                } else {
                    brewTimeSec = Integer.parseInt(editETTotalSec.getText().toString());
                }
                if ((editETTotalMin.getText().toString().isEmpty() && editETTotalSec.getText().toString().isEmpty()) || (brewTimeMin == 0 && brewTimeSec == 0)) {
                    Toast.makeText(this, "time can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                setBrewValues();

                if (brew.getStorageKey() >= 0 || brew.getFavoriteKey() >= 0) {
                    // vi skal gemme ændringerne
                    IStorageService storage = StorageServiceSharedPref.getInstance();
                    if (brewName.isEmpty()) {
                        Toast.makeText(this, "your brew needs a name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        if (brew.getFavoriteKey() >= 0)
                            storage.overwriteFavoriteBrew(brew.getFavoriteKey(), brew);
                        else
                            storage.overwriteBrew(brew.getStorageKey(), brew);
                    } catch (BrewException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                // vores ændret brew bliver sendt til brewing
                Intent intent = new Intent(this, Brewing.class);
                try {
                    intent.putExtra("Brew", brew.toJson());
                    Log.d(TAG, brew.toJson());
                } catch (BrewException e) {
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });

            // info knappen
            info.setOnClickListener(v -> {
                BrewSheetMenu brygMenu = new BrewSheetMenu();
                brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
            });
            coffeeImage.setOnClickListener(v -> {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, 1); //request code til det der sendes videre.
            });
        }
    }

        private void setBrewValues () {
            brew.setGroundCoffee(groundCoffee);
            brew.setGrindSize(grindSize);
            brew.setCoffeeWaterRatio(coffeeWaterRatio);
            brew.setBrewingTemperature(brewingTemperature);
            brew.setBloomWater(bloomWater);
            brew.setBloomTime(bloomTime);
            brew.setBrewTimeMin(brewTimeMin);
            brew.setBrewTimeSec(brewTimeSec);
            brewName = editETBrewName.getText().toString();
            brew.setBrewName(brewName);
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Uri image_uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                        coffeeImage.setImageBitmap(bitmap);
                        brew.setBrewPics(image_uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
}
