package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.HomePage;
import org.dtu.brogb1.activity.Loading;

public class ConnectStep1Activity extends AppCompatActivity implements View.OnClickListener {
    Button qr, manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step1);

        qr = findViewById(R.id.connect_qr);
        qr.setOnClickListener(this);

        manual = findViewById(R.id.connect_manual);
        manual.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == qr) {
            Intent intent = new Intent(this, ConnectStep2QrActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (v == manual) {
            Intent intent = new Intent(this, ConnectStep2ManualListActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}