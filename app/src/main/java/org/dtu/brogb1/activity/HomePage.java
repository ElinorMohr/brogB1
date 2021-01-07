package org.dtu.brogb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.clean.CleanActivityStep1;
import org.dtu.brogb1.activity.community.CommunityActivity;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class HomePage extends AppCompatActivity implements View.OnClickListener  {
    Button brew,list,quick,bsmquickBrew, bsmrecipes, bsmnewBrew;
    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_side);
        brew = findViewById(R.id.brew_now);
        list = findViewById(R.id.see_list);
        quick = findViewById(R.id.quick_brew);
        settings = findViewById(R.id.Settings);


        brew.setOnClickListener(this);
        list.setOnClickListener(this);
        quick.setOnClickListener(this);
        settings.setOnClickListener(this);

        //settings contextMenuTextView = view.findViewById<TextView>(R.id.context_menu_tv)
                // Register context menu for TextView
                //registerForContextMenu(contextMenuTextView)

        registerForContextMenu(settings);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_1:
                Intent intent = new Intent(this, CleanActivityStep1.class);
                startActivity(intent);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.brew_now:
                intent = new Intent(this, NewBrew.class);
                startActivity(intent);
                break;
            case R.id.see_list:
                intent = new Intent(this, Sections.class);
                startActivity(intent);
                break;
            case R.id.quick_brew:
                intent = new Intent(this, Brewing.class);
                startActivity(intent);
                break;
            case R.id.Settings:
                intent = new Intent(this, Option.class);
                startActivity(intent);
                break;
        }

    }
}