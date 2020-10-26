package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CleanActivityStep1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_step1);

        Button next = (Button) findViewById(R.id.clean_step1_button);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(CleanActivityStep1.this, CleanActivityStep1Working.class);
            startActivity(intent);
        });
    }
}