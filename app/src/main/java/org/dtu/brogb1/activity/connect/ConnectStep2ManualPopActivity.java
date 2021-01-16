package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.espressif.provisioning.DeviceConnectionEvent;
import com.espressif.provisioning.ESPConstants;
import com.espressif.provisioning.ESPProvisionManager;

import org.dtu.brogb1.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Basically en carbon-copy af esp-idf-provisioning-android/ProofOfPossessionActivity
 * https://github.com/espressif/esp-idf-provisioning-android/blob/master/app/src/main/java/com/espressif/ui/activities/ProofOfPossessionActivity.java
 *
 * @author Theis Villumsen s195461
 */
public class ConnectStep2ManualPopActivity extends AppCompatActivity {
    private static final String TAG = ConnectStep2ManualPopActivity.class.getSimpleName();

    private CardView btnNext;

    private String deviceName;
    private EditText etPop;
    private ESPProvisionManager provisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step2_manual_pop);

        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());
        initViews();
        EventBus.getDefault().register(this);

        deviceName = provisionManager.getEspDevice().getDeviceName();

        String pop = getResources().getString(R.string.proof_of_possesion);

        if (!TextUtils.isEmpty(pop)) {
            etPop.setText(pop);
            etPop.setSelection(etPop.getText().length());
        }
        etPop.requestFocus();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        provisionManager.getEspDevice().disconnectDevice();
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceConnectionEvent event) {
        Log.d(TAG, "On Device Connection Event RECEIVED : " + event.getEventType());

        switch (event.getEventType()) {
            case ESPConstants.EVENT_DEVICE_DISCONNECTED:
                if (!isFinishing()) {
                    showAlertForDeviceDisconnected();
                }
                break;
        }
    }

    private View.OnClickListener nextBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String pop = etPop.getText().toString();
            Log.d(TAG, "POP : " + pop);
            provisionManager.getEspDevice().setProofOfPossession(pop);
            ArrayList<String> deviceCaps = provisionManager.getEspDevice().getDeviceCapabilities();

            if (deviceCaps.contains("wifi_scan")) {
                goToWiFiScanListActivity();
            } else {
                goToWiFiConfigActivity();
            }
        }
    };

    private void initViews() {
        btnNext = findViewById(R.id.connect_pop);
        etPop = findViewById(R.id.pop_input);

        btnNext.setOnClickListener(nextBtnClickListener);
    }

    private void goToWiFiScanListActivity() {
        Intent wifiListIntent = new Intent(getApplicationContext(), ConnectStep3WifiScanActivity.class);
        wifiListIntent.putExtras(getIntent());
        startActivity(wifiListIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void goToWiFiConfigActivity() {
        Intent wifiConfigIntent = new Intent(getApplicationContext(), ConnectStep4WifiConfigActivity.class);
        wifiConfigIntent.putExtras(getIntent());
        startActivity(wifiConfigIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void showAlertForDeviceDisconnected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        builder.setTitle(R.string.error_title);
        builder.setMessage(R.string.dialog_msg_ble_device_disconnection);

        // Set up the buttons
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }
}