package org.dtu.brogb1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.espressif.provisioning.ESPDevice;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.BleDevice;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.util.ArrayList;

public class BleDevicesAdapter extends ArrayAdapter<BleDevice> {
    private final IStorageService storage;
    private final Context context;

    public BleDevicesAdapter(@NonNull Context context, ArrayList<BleDevice> objects) {
        super(context, 0, objects);
        this.storage = StorageServiceSharedPref.getInstance();
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BleDevice device = this.getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_espdevice, parent, false);
        }

        Button deviceName = (Button) convertView.findViewById(R.id.deviceName);
        deviceName.setText(device.getName());

        // Returner det viewet, som er klar til at blive renderet
        return convertView;
    }
}
