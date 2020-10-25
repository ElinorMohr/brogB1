package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartSide extends AppCompatActivity implements View.OnClickListener{
    Button visit;
    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_side);

        Button brew = findViewById(R.id.Brew);
        Button clean = findViewById(R.id.Clean);
        Button community = findViewById(R.id.Community);
        Button guide = findViewById(R.id.Guide);
        ImageButton setting = findViewById(R.id.Settings);

        brew.setOnClickListener(this);
        clean.setOnClickListener(this);
        community.setOnClickListener(this);
        guide.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Brew:
                Toast.makeText(this, "Brew clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Clean:
                Toast.makeText(this, "Clean clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Community:
                Toast.makeText(this, "Community clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Guide:
                Toast.makeText(this, "Guide clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Settings:
                Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}