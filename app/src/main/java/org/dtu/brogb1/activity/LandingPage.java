package org.dtu.brogb1.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.connect.ConnectStep1Activity;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class LandingPage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LandingPage.class.getSimpleName();
    public static SharedPreferences mySharedPreferences = null;
    Button connect, offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        try {
            setContentView(R.layout.activity_landing_page);
        } catch(Exception ignored) {}

        mySharedPreferences = getApplicationContext().getSharedPreferences("preferences", Activity.MODE_PRIVATE);

        connect = findViewById(R.id.connect);
        connect.setOnClickListener(this);

        offline = findViewById(R.id.offline);
        offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == connect) {
            Intent intent = new Intent(this, ConnectStep1Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (v == offline) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}