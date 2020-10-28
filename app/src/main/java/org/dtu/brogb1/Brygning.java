package org.dtu.brogb1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Brygning extends AppCompatActivity {
Button brygNu;
    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brygning);
        brygNu = findViewById(R.id.BrewNow);

        brygNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogue = new Dialog(v.getContext(), android.R.style.Theme_Black_NoTitleBar);
                //fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                dialogue.setContentView(R.layout.brewing_progress);
                dialogue.setCancelable(true);
                dialogue.show();
            }
        });
    }
}