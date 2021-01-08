package org.dtu.brogb1.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class LandingPage extends AppCompatActivity implements View.OnClickListener {
    public static SharedPreferences mySharedPreferences = null;
    Button bluetooth, offline;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mySharedPreferences = getApplicationContext().getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        IStorageService storage = StorageServiceSharedPref.getInstance();

        bluetooth = findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(this);

        offline = findViewById(R.id.offline);
        offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bluetooth) {
            Intent intent = new Intent(this, Loading.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (v == offline) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}