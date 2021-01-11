package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.espressif.provisioning.ESPProvisionManager;

import org.dtu.brogb1.R;

public class ConnectStep2QrActivity extends AppCompatActivity {
    private ESPProvisionManager provisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step2_qr);
        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());

        //provisionManager.scanQRCode(Activity activityContext, CameraSourcePreview cameraSourcePreview, QRCodeScanListener qrCodeScanListener)
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}