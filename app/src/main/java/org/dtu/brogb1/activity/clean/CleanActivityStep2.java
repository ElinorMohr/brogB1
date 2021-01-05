package org.dtu.brogb1.activity.clean;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class CleanActivityStep2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_step2);

        Button next = (Button) findViewById(R.id.clean_step2_button);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(this, CleanActivityStep2Working.class);
            startActivity(intent);
        });
    }
}