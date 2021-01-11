package org.dtu.brogb1.activity.connect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.espressif.provisioning.DeviceConnectionEvent;
import com.espressif.provisioning.ESPConstants;
import com.espressif.provisioning.ESPProvisionManager;
import com.espressif.provisioning.listeners.BleScanListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import org.dtu.brogb1.R;
import org.dtu.brogb1.adapter.BleDevicesAdapter;
import org.dtu.brogb1.model.BleDevice;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Basically en carbon-copy af esp-idf-provisioning-android/BLEProvisionLanding
 * https://github.com/espressif/esp-idf-provisioning-android/blob/master/app/src/main/java/com/espressif/ui/activities/BLEProvisionLanding.java
 *
 * @author Theis Villumsen s195461
 */
public class ConnectStep2ManualListActivity extends AppCompatActivity {
    private static final String TAG = ConnectStep2ManualListActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 2;
    private static final long DEVICE_CONNECT_TIMEOUT = 20000;

    private Button btnScan;
    private ListView listView;
    private ProgressBar progressBar;

    private BleDevicesAdapter adapter;
    private BluetoothAdapter bleAdapter;
    private ArrayList<BleDevice> deviceList;
    private HashMap<BluetoothDevice, String> bluetoothDevices;
    private Handler handler;

    private int position = -1;
    private final String deviceNamePrefix = "PROV_";
    private boolean isDeviceConnected = false, isConnecting = false;
    private ESPProvisionManager provisionManager;
    private int securityType;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_step2_manual_list);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.error_ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (bleAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        isConnecting = false;
        isDeviceConnected = false;
        handler = new Handler();
        bluetoothDevices = new HashMap<>();
        deviceList = new ArrayList<>();

        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());
        initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!bleAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {

            if (!isDeviceConnected && !isConnecting) {
                startScan();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isScanning) {
            stopScan();
        }
        if (provisionManager.getEspDevice() != null) {
            provisionManager.getEspDevice().disconnectDevice();
        }

        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, requestCode : " + requestCode + ", resultCode : " + resultCode);

        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            startScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScan();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }
            break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceConnectionEvent event) {
        Log.d(TAG, "ON Device Prov Event RECEIVED : " + event.getEventType());
        handler.removeCallbacks(disconnectDeviceTask);

        switch (event.getEventType()) {
            case ESPConstants.EVENT_DEVICE_CONNECTED:
                Log.e(TAG, "Device Connected Event Received");
                ArrayList<String> deviceCaps = provisionManager.getEspDevice().getDeviceCapabilities();
                progressBar.setVisibility(View.GONE);
                isConnecting = false;
                isDeviceConnected = true;

                if (deviceCaps != null && !deviceCaps.contains("no_pop") && securityType == 1) {
                    goToPopActivity();
                } else if (deviceCaps.contains("wifi_scan")) {
                    goToWifiScanListActivity();
                } else {
                    goToWiFiConfigActivity();
                }
                break;
            case ESPConstants.EVENT_DEVICE_DISCONNECTED:
                progressBar.setVisibility(View.GONE);
                isConnecting = false;
                isDeviceConnected = false;
                Toast.makeText(ConnectStep2ManualListActivity.this, "Device disconnected", Toast.LENGTH_LONG).show();
                break;
            case ESPConstants.EVENT_DEVICE_CONNECTION_FAILED:
                progressBar.setVisibility(View.GONE);
                isConnecting = false;
                isDeviceConnected = false;
                alertForDeviceNotSupported("Failed to connect with device");
                break;
        }
    }
    private void initViews() {
        btnScan = findViewById(R.id.btn_scan);
        listView = findViewById(R.id.ble_devices_list);
        progressBar = findViewById(R.id.ble_landing_progress_indicator);

        adapter = new BleDevicesAdapter(this, deviceList);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onDeviceCLickListener);
        btnScan.setOnClickListener(btnScanClickListener);
    }

    private boolean hasPermissions() {

        if (bleAdapter == null || !bleAdapter.isEnabled()) {

            requestBluetoothEnable();
            return false;

        } else if (!hasLocationPermissions()) {

            requestLocationPermission();
            return false;
        }
        return true;
    }

    private void requestBluetoothEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        Log.d(TAG, "Requested user enables Bluetooth.");
    }

    private boolean hasLocationPermissions() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    private void startScan() {
        if (!hasPermissions() || isScanning) {
            return;
        }

        isScanning = true;
        deviceList.clear();
        bluetoothDevices.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            provisionManager.searchBleEspDevices(deviceNamePrefix, bleScanListener);
            updateProgressAndScanBtn();
        } else {
            Log.e(TAG, "Not able to start scan as Location permission is not granted.");
            Toast.makeText(ConnectStep2ManualListActivity.this, "Please give location permission to start BLE scan", Toast.LENGTH_LONG).show();
        }
    }

    private void stopScan() {
        isScanning = false;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            provisionManager.stopBleScan();
            updateProgressAndScanBtn();
        } else {
            Log.e(TAG, "Not able to stop scan as Location permission is not granted.");
            Toast.makeText(ConnectStep2ManualListActivity.this, "Please give location permission to stop BLE scan", Toast.LENGTH_LONG).show();
        }

        if (deviceList.size() <= 0) {
            Toast.makeText(ConnectStep2ManualListActivity.this, R.string.error_no_ble_device, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will update UI (Scan button enable / disable and progressbar visibility)
     */
    private void updateProgressAndScanBtn() {
        if (isScanning) {
            btnScan.setEnabled(false);
            btnScan.setAlpha(0.5f);
            btnScan.setTextColor(Color.WHITE);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            btnScan.setEnabled(true);
            btnScan.setAlpha(1f);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private void alertForDeviceNotSupported(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setCancelable(false);

        builder.setTitle(R.string.error_title);
        builder.setMessage(msg);

        // Set up the buttons
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.show();
    }

    private View.OnClickListener btnScanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bluetoothDevices.clear();
            adapter.clear();
            startScan();
        }
    };

    private BleScanListener bleScanListener = new BleScanListener() {
        @Override
        public void scanStartFailed() {
            Toast.makeText(ConnectStep2ManualListActivity.this, "Please turn on Bluetooth to connect BLE device", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPeripheralFound(BluetoothDevice device, ScanResult scanResult) {
            Log.d(TAG, "====== onPeripheralFound ===== " + device.getName());
            boolean deviceExists = false;
            String serviceUuid = "";

            if (scanResult.getScanRecord().getServiceUuids() != null && scanResult.getScanRecord().getServiceUuids().size() > 0) {
                serviceUuid = scanResult.getScanRecord().getServiceUuids().get(0).toString();
            }
            Log.d(TAG, "Add service UUID : " + serviceUuid);

            if (bluetoothDevices.containsKey(device)) {
                deviceExists = true;
            }

            if (!deviceExists) {
                BleDevice bleDevice = new BleDevice();
                bleDevice.setName(scanResult.getScanRecord().getDeviceName());
                bleDevice.setBluetoothDevice(device);

                listView.setVisibility(View.VISIBLE);
                bluetoothDevices.put(device, serviceUuid);
                deviceList.add(bleDevice);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void scanCompleted() {
            isScanning = false;
            updateProgressAndScanBtn();
        }

        @Override
        public void onFailure(Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    };

    private AdapterView.OnItemClickListener onDeviceCLickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            stopScan();
            isConnecting = true;
            isDeviceConnected = false;
            btnScan.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ConnectStep2ManualListActivity.this.position = position;
            BleDevice bleDevice = adapter.getItem(position);
            String uuid = bluetoothDevices.get(bleDevice.getBluetoothDevice());
            Log.d(TAG, "=================== Connect to device : " + bleDevice.getName() + " UUID : " + uuid);

            if (ActivityCompat.checkSelfPermission(ConnectStep2ManualListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                provisionManager.getEspDevice().connectBLEDevice(bleDevice.getBluetoothDevice(), uuid);
                handler.postDelayed(disconnectDeviceTask, DEVICE_CONNECT_TIMEOUT);
            } else {
                Log.e(TAG, "Not able to connect device as Location permission is not granted.");
                Toast.makeText(ConnectStep2ManualListActivity.this, "Please give location permission to connect device", Toast.LENGTH_LONG).show();
            }
        }
    };

    private Runnable disconnectDeviceTask = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "Disconnect device");

            // TODO Disconnect device
            progressBar.setVisibility(View.GONE);
            alertForDeviceNotSupported(getString(R.string.error_device_not_supported));
        }
    };

    private void goToPopActivity() {
        finish();
        Intent popIntent = new Intent(getApplicationContext(), ConnectStep2ManualPopActivity.class);
        popIntent.putExtra("ble_device", deviceList.get(position).getName());
        startActivity(popIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void goToWifiScanListActivity() {
        finish();
        Intent wifiListIntent = new Intent(getApplicationContext(), ConnectStep3WifiScanActivity.class);
        wifiListIntent.putExtra("ble_device", deviceList.get(position).getName());
        startActivity(wifiListIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void goToWiFiConfigActivity() {
        finish();
        Intent wifiConfigIntent = new Intent(getApplicationContext(), ConnectStep4WifiConfigActivity.class);
        startActivity(wifiConfigIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}