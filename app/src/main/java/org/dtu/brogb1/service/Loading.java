package org.dtu.brogb1.service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.CleanActivityStep1Working;
import org.dtu.brogb1.CleanActivityStep2;
import org.dtu.brogb1.R;
import org.dtu.brogb1.StartSide;

public class Loading extends AppCompatActivity {

    TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loading = findViewById(R.id.loading);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(Loading.this, StartSide.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(intent);
                        Loading.this.finish();
                    }
                },
                1500
        );

    }
}