package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.Loading;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class LandingPage extends AppCompatActivity implements View.OnClickListener {

    Button bluetooth;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime);

        bluetooth = findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == bluetooth) {
            Intent intent = new Intent(this, Loading.class);
            startActivity(intent);
        }

    }
}