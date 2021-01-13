package org.dtu.brogb1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.clean.CleanActivityStep1;
import org.dtu.brogb1.activity.community.About;
import org.dtu.brogb1.activity.community.CommunityActivity;
import org.dtu.brogb1.activity.community.Guide;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 * @author Betina Hansen s195389
 */

public class HomePage extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    Button brew, list, quick;
    ImageButton settings;
    IStorageService storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = StorageServiceSharedPref.getInstance();
        setContentView(R.layout.activity_start_side);
        brew = findViewById(R.id.brew_now);
        list = findViewById(R.id.see_list);
        quick = findViewById(R.id.quick_brew);
        settings = findViewById(R.id.Settings);

        brew.setOnClickListener(this);
        list.setOnClickListener(this);
        quick.setOnClickListener(this);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePage.this, v);
                popup.setOnMenuItemClickListener(HomePage.this);
                popup.inflate(R.menu.menu);
                popup.show();
            }
        });
        registerForContextMenu(settings);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_1:
                startActivity(new Intent(this, CleanActivityStep1.class));
                return true;
            case R.id.option_2:
                startActivity(new Intent(this, CommunityActivity.class));
                return true;
            case R.id.option_3:
                startActivity(new Intent(this, Guide.class));
                return true;
            case R.id.option_4:
                startActivity(new Intent(this, Option.class));
                return true;
            case R.id.option_5:
                startActivity(new Intent(this, About.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return this.onContextItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.brew_now:
                intent = new Intent(this, NewBrew.class);
                startActivity(intent);
                break;
            case R.id.see_list:
                intent = new Intent(this, Sections.class);
                startActivity(intent);
                break;
            case R.id.quick_brew:
                try {
                    Brew quickbrew = storage.getQuickBrew();
                    intent = new Intent(this, Brewing.class);
                    intent.putExtra("Brew", quickbrew.toJson());
                    startActivity(intent);
                } catch (StorageServiceException | BrewException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Du har ikke valgt en quick-brew", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.Settings:
                openContextMenu(settings);
                break;
        }

    }
}