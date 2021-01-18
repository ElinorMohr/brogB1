package org.dtu.brogb1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.filters.MinMaxFilter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.Util;

import java.io.IOException;

public abstract class EditBrewValues extends AppCompatActivity {
    protected String brewName, grindSize;
    protected int brewTimeMin, brewTimeSec, groundCoffee, coffeeWaterRatio, brewingTemperature, bloomWater, bloomTime;
    protected EditText editETGroundCoffee, editETBrewName;
    protected EditText editETRatio;
    protected EditText editETTemp;
    protected EditText editETBloomWater;
    protected EditText editETBloomTime;
    protected EditText editETTotalMin;
    protected EditText editETTotalSec;
    protected Spinner editSpinnerInputGrindSize;
    protected Brew brew;
    protected ImageView coffeeImage;
    protected boolean favoriteOn;
    protected ImageButton info;
    protected ImageButton favoriteBt;
    protected static String TAG;
    protected IStorageService storage;
    protected int GET_IMAGE_CODE = 4041;

    // intervallerne for hver af inputs
    protected void setFilters() {
        editETGroundCoffee.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETRatio.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETTemp.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETBloomWater.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETBloomTime.setFilters(new InputFilter[]{new MinMaxFilter("1", "99")});
        editETTotalMin.setFilters(new InputFilter[]{new MinMaxFilter("0", "99")});
        editETTotalSec.setFilters(new InputFilter[]{new MinMaxFilter("0", "59")});
    }

    protected void setBrewValues() {
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

    protected void getBrewValuesFromUI() throws Exception {
        groundCoffee = getIntInput(editETGroundCoffee, "ground coffee");
        grindSize = editSpinnerInputGrindSize.getSelectedItem().toString();
        coffeeWaterRatio = getIntInput(editETRatio, "coffee Water Ratio");
        brewingTemperature = getIntInput(editETTemp, "brewing Temperature");
        bloomWater = getIntInput(editETBloomWater, "Bloom Water");
        bloomTime = getIntInput(editETBloomWater, "Bloom Time");
        // tiden skal blive lagt sammen
        getTimeInput();
        brewName = editETBrewName.getText().toString();
    }

    protected int getIntInput(EditText v, String text) throws Exception {
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

    protected void getTimeInput() throws Exception {
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

    protected class AsyncTaskSaveImage extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private Context context;
        private String TAG;
        private Brew brew;

        public AsyncTaskSaveImage(Brew brew, Bitmap bitmap, Context context, String TAG) {
            super();
            this.brew = brew;
            this.TAG = TAG;
            this.bitmap = bitmap;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Util.saveImageFrom(brew, bitmap, context, TAG);
            return null;
        }
    }
    protected class AsyncTaskGetImage extends AsyncTask<Void, Void, Void> {
        private ImageView imageView;
        private ContentResolver contentResolver;
        private String TAG;
        private Brew brew;

        public AsyncTaskGetImage(Brew brew, ImageView imageView, ContentResolver contentResolver, String TAG) {
            super();
            this.brew = brew;
            this.TAG = TAG;
            this.imageView = imageView;
            this.contentResolver = contentResolver;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Util.setImageFromBrew(brew, coffeeImage, contentResolver, TAG);
            return null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_IMAGE_CODE) {
                try {
                    Uri image_uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    coffeeImage.setImageBitmap(bitmap);
                    new AsyncTaskSaveImage(brew, bitmap, this.getApplicationContext(), TAG).execute();
                } catch (IOException e) {
                    Util.log(TAG, e);
                }

            }
        }
    }


}
