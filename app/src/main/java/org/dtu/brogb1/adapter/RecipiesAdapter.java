package org.dtu.brogb1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.Brewing;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.util.ArrayList;

public class RecipiesAdapter extends ArrayAdapter<Brew> {
    private boolean favorites = false;
    private final IStorageService storage;
    private final Context context;

    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects) {
        super(context, 0, objects);
        this.storage = StorageServiceSharedPref.getInstance();
        this.context = context;
    }
    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects, boolean favorites) {
        super(context, 0, objects);
        this.favorites = favorites;
        this.storage = StorageServiceSharedPref.getInstance();
        this.context = context;
    }

    @Override
    public boolean  areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Brew brew = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_brew, parent, false);
        }
        // Lookup view for data population
        TextView favCount = (TextView) convertView.findViewById(R.id.favNumber);
        Button brewName = (Button) convertView.findViewById(R.id.brewName);
        ImageView star = convertView.findViewById(R.id.quickBrew);

        // Populate the data into the template view using the data object
        if (favorites) {
            favCount.setText((position+1) + ".");
            try {
                if (brew.equals(storage.getQuickBrew()))
                    star.setImageDrawable(getContext().getDrawable(R.drawable.ic_star));
                star.setTag(brew);
                star.setOnClickListener(onStarClickListener);
            } catch (StorageServiceException | BrewException e) {
                e.printStackTrace();
            }
        } else {
            favCount.setAlpha(0);
            star.setImageAlpha(0);
        }
        brewName.setText(brew.getBrewName());
        brewName.setTag(brew);
        brewName.setOnClickListener(onButtonClickListener);

        // Return the completed view to render on screen
        return convertView;
    }

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Brew brew = (Brew) v.getTag();
            System.out.println("Button clicked, row " + brew.getBrewName());
            Intent intent = new Intent(new Intent(getContext(), Brewing.class));
            try {
                intent.putExtra("Brew", brew.toJson());
                context.startActivity(intent);
            } catch (BrewException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener onStarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Brew brew = (Brew) v.getTag();
            System.out.println("Star clicked, row " + brew.getBrewName());
        }
    };
}
