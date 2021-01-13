package org.dtu.brogb1.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import org.dtu.brogb1.R;
import org.dtu.brogb1.adapter.SectionsPagerAdapter;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class Sections extends AppCompatActivity {

    @Override
    //højre og venstre side i vores fragment
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sections);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}


