package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.dtu.brogb1.service.Loading;

public class FirstTime extends AppCompatActivity implements View.OnClickListener {

    Button bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime);

        bluetooth = findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == bluetooth){
            Intent intent = new Intent(this, Loading.class);
            startActivity(intent);
        }

    }
}