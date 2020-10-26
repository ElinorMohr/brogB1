package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CleanActivityStep2Working extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_step2_working);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(CleanActivityStep2Working.this, CleanActivityStep3.class);
                        startActivity(intent);
                    }
                },
                1500
        );
    }
}