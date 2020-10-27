package org.dtu.brogb1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.dtu.brogb1.R;

public class RecipeActivity extends AppCompatActivity {
    Button edit, brew;
    EditText input;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edit = findViewById(R.id.edit_recipe);
        brew = findViewById(R.id.brew_recipe);
        image = findViewById(R.id.image_recipe);
        input = findViewById(R.id.input_recipe);
        setContentView(R.layout.activity_recipe);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}