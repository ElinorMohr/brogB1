package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 * @author Asim Raja s040727
 */

public class Option extends AppCompatActivity implements View.OnClickListener {

    Button language, connections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        language = findViewById(R.id.language);
        connections = findViewById(R.id.connections);

        language.setOnClickListener(this);
        connections.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == language){

        }

        if (v == connections){

        }


    }
}
