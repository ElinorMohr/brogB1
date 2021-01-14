package org.dtu.brogb1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;
import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.model.BrewFactory;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.io.File;
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
    boolean favoriteOn;
    IStorageService storage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brew);
        storage = StorageServiceSharedPref.getInstance();

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
        ImageButton favoriteBt = findViewById(R.id.edit_brewing_favorite_bt);
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
                Uri image_uri = Uri.fromFile(new File(brew.getBrewPics()));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    coffeeImage.setImageBitmap(bitmap);
                    coffeeImage.setPadding(0,0,0,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (brew.getFavoriteKey() >= 0) {
                favoriteBt.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                favoriteOn = true;
            }
            //når der brygges
            EditNow.setOnClickListener(v -> {
                try {
                    groundCoffee = getIntInput(editETGroundCoffee, "ground coffee");
                    grindSize = editSpinnerInputGrindSize.getSelectedItem().toString();
                    coffeeWaterRatio = getIntInput(editETRatio, "coffee Water Ratio");
                    brewingTemperature = getIntInput(editETTemp, "brewing Temperature");
                    bloomWater = getIntInput(editETBloomWater, "Bloom Water");
                    bloomTime = getIntInput(editETBloomWater, "Bloom Time");
                    // tiden skal blive lagt sammen
                    getTimeInput();
                    setBrewValues();
                } catch (Exception e) {
                    return;
                }

                if (brew.getStorageKey() >= 0 ||  favoriteOn) {
                    // vi skal gemme ændringerne
                    if (brewName.isEmpty()) {
                        Toast.makeText(this, "your brew needs a name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        if (favoriteOn)
                            if (brew.getFavoriteKey() >= 0)
                                storage.overwriteFavoriteBrew(brew.getFavoriteKey(), brew);
                            else
                                brew.setFavoriteKey(storage.saveBrewToFavorites(brew));
                        else
                            storage.overwriteBrew(brew.getStorageKey(), brew);
                    } catch (BrewException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                if (brew.getFavoriteKey() >= 0 && !favoriteOn) {
                    try {
                        storage.deleteFavoriteBrew(brew);
                        if (brew.getStorageKey() >= 0)
                            storage.overwriteBrew(brew.getStorageKey(), brew);
                        else
                            brew.setStorageKey(storage.saveBrew(brew));
                    } catch (StorageServiceException e) {
                        e.printStackTrace();
                    } catch (BrewException e) {
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });

            // info knappen
            info.setOnClickListener(v -> {
                BrewSheetMenu brygMenu = new BrewSheetMenu();
                brygMenu.show(getSupportFragmentManager(), "FragmentBrygMenu");
            });

            favoriteBt.setOnClickListener(v -> {
                if (!favoriteOn) {
                    favoriteBt.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                } else {
                    favoriteBt.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_empty));
                }
                favoriteOn = !favoriteOn;
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

    private void setBrewValues() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri image_uri = data.getData();
                File file = new File(image_uri.getPath());//create path from uri
                final String[] split = file.getPath().split(":");//split the path.
                brew.setBrewPics(split[1]);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    coffeeImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
