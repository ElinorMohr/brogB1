package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CleanActivityStep3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_step3);

        Button next = (Button) findViewById(R.id.clean_step3_button);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(CleanActivityStep3.this, MainActivity.class);
            startActivity(intent);
        });
    }
}