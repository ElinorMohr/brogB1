package org.dtu.brogb1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dtu.brogb1.R;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;

import java.util.ArrayList;

public class RecipiesAdapter extends ArrayAdapter<Brew> {
    private boolean favorites = false;
    private IStorageService storage;
    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects) {
        super(context, 0, objects);
        this.storage = StorageServiceSharedPref.getInstance();
    }
    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects, boolean favorites) {
        super(context, 0, objects);
        this.favorites = favorites;
        this.storage = StorageServiceSharedPref.getInstance();
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
        TextView brewName = (TextView) convertView.findViewById(R.id.brewName);
        ImageView star = convertView.findViewById(R.id.quickBrew);

        // Populate the data into the template view using the data object
        if (favorites) {
            favCount.setText((position+1) + ".");
            try {
                if (brew.equals(storage.getQuickBrew()))
                    star.setImageDrawable(getContext().getDrawable(R.drawable.ic_star));
            } catch (StorageServiceException | BrewException e) {
                e.printStackTrace();
            }
        } else {
            favCount.setAlpha(0);
            star.setImageAlpha(0);
        }
        brewName.setText(brew.getBrewName());

        // Return the completed view to render on screen
        return convertView;
    }
}
