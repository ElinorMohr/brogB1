package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Brygning extends AppCompatActivity {
    Button brygNu;
    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brygning);
        brygNu = (Button) findViewById(R.id.BrewNow);
        BrewingLoading brewingLoading = new BrewingLoading(Brygning.this);

        brygNu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                brewingLoading.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        brewingLoading.dismissDialog();
                    }
                }, 5000);
            }
        });
    }
}