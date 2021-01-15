package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.espressif.provisioning.ESPProvisionManager;

import org.dtu.brogb1.R;

/**
 * Basically en carbon-copy af esp-idf-provisioning-android/AddDeviceActivity
 * https://github.com/espressif/esp-idf-provisioning-android/blob/master/app/src/main/java/com/espressif/ui/activities/AddDeviceActivity.java
 *
 * @author Theis Villumsen s195461
 */
public class ConnectStep2QrActivity extends AppCompatActivity {
    private static final String TAG = ConnectStep2QrActivity.class.getSimpleName();

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private ESPProvisionManager provisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step2_qr);
        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}