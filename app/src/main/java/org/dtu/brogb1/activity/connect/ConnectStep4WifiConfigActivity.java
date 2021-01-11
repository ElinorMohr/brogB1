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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.espressif.provisioning.DeviceConnectionEvent;
import com.espressif.provisioning.ESPConstants;
import com.espressif.provisioning.ESPProvisionManager;

import org.dtu.brogb1.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Basically en carbon-copy af esp-idf-provisioning-android/WiFiConfigActivity
 * https://github.com/espressif/esp-idf-provisioning-android/blob/master/app/src/main/java/com/espressif/ui/activities/WiFiConfigActivity.java
 *
 * @author Theis Villumsen s195461
 */
public class ConnectStep4WifiConfigActivity extends AppCompatActivity {
    private static final String TAG = ConnectStep4WifiConfigActivity.class.getSimpleName();

    private Button btnNext;
    private TextView txtNextBtn;

    private EditText etSsid, etPassword;
    private ESPProvisionManager provisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step4_wifi_config);

        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());
        initViews();
        EventBus.getDefault().register(this);
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
            String ssid = etSsid.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(ssid)) {
                etSsid.setError(getString(R.string.error_ssid_empty));
                return;
            }

            goToProvisionActivity(ssid, password);
        }
    };

    private void initViews() {
        etSsid = findViewById(R.id.wifi_name);
        etPassword = findViewById(R.id.wifi_pass);

        btnNext = findViewById(R.id.connect_wifi);
        btnNext.setOnClickListener(nextBtnClickListener);
    }

    private void goToProvisionActivity(String ssid, String password) {
        finish();
        Intent provisionIntent = new Intent(getApplicationContext(), ConnectStep5ProvisionActivity.class);
        provisionIntent.putExtras(getIntent());
        provisionIntent.putExtra("wifi_name", ssid);
        provisionIntent.putExtra("wifi_pass", password);
        startActivity(provisionIntent);
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