package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.clean.CleanActivityStep1Working;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceSharedPref;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 * @author Asim Raja s040727
 */

public class Option extends AppCompatActivity {

    Button language, connections, btn_reset_quick_brew;
    IStorageService storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        storage = StorageServiceSharedPref.getInstance();

        language = findViewById(R.id.language);
        connections = findViewById(R.id.connections);
        btn_reset_quick_brew = findViewById(R.id.btn_reset_quick_brew);
        btn_reset_quick_brew.setOnClickListener(v -> {
            storage.setQuickBrew(-1);
            Toast.makeText(this, "Quick-brew has been reset", Toast.LENGTH_LONG).show();
        });
    }
}
