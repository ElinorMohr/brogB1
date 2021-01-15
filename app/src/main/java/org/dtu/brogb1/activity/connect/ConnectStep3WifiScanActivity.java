package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.dtu.brogb1.R;

/**
 * Basically en carbon-copy af esp-idf-provisioning-android/WiFiScanActivity
 * https://github.com/espressif/esp-idf-provisioning-android/blob/master/app/src/main/java/com/espressif/ui/activities/WiFiScanActivity.java
 *
 * @author Theis Villumsen s195461
 */
public class ConnectStep3WifiScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step3_wifi_scan);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}