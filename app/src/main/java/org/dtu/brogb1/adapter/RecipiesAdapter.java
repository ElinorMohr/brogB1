package org.dtu.brogb1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.Brewing;
import org.dtu.brogb1.activity.Sections;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.model.BrewException;
import org.dtu.brogb1.service.IStorageService;
import org.dtu.brogb1.service.StorageServiceException;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

import java.util.ArrayList;

public class RecipiesAdapter extends ArrayAdapter<Brew> {
    private static final String TAG = RecipiesAdapter.class.getSimpleName();
    private String mode = "normal";
    private final IStorageService storage;
    private final Context context;

    // Adapteren kan states på 3 måder, i "favorit" og "normal"
    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects) {
        super(context, 0, objects);
        this.storage = StorageServiceSharedPref.getInstance();
        this.context = context;
    }
    public RecipiesAdapter(@NonNull Context context, ArrayList<Brew> objects, String mode) {
        super(context, 0, objects);
        if (!mode.equals("normal") && !mode.equals("favorite") && !mode.equals("history"))
            mode = "normal";
        this.mode = mode;
        this.storage = StorageServiceSharedPref.getInstance();
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Find information om den Brew der skal vises i dette element
        Brew brew = this.getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_brew, parent, false);
        }
        // Find de steder i template hvor der skal sættes data ind
        TextView favCount = (TextView) convertView.findViewById(R.id.favNumber);
        Button brewName = (Button) convertView.findViewById(R.id.brewName);
        ImageView btnQuickBrew = (ImageView) convertView.findViewById(R.id.quickBrew);

        // Put data ind i templaten der er valgt tidligere
        // Hvis det er en "favorit"-liste, så skal der gøres noget ekstra, ellers så skal tælleren og stjernen bare skjules
        if (this.mode.equals("favorite")) {
            // Sæt favorit-nummeret
            favCount.setText((position + 1) + ".");
            // Forsøg at sætte stjernen, hvis denne Brew er den samme som quick-brew
            try {
                Brew quick = this.storage.getQuickBrew();
                Log.d(TAG, "Sammenligner " + brew.getFavoriteKey() + " og " + quick.getFavoriteKey());
                if (brew.getFavoriteKey() == quick.getFavoriteKey()) {
                    btnQuickBrew.setImageDrawable(this.getContext().getDrawable(R.drawable.ic_mug_hot));
                } else {
                    btnQuickBrew.setImageDrawable(this.getContext().getDrawable(R.drawable.ic_mug_hot_light));
                }

                // Gem noget data, så man kan trykke på stjernen
                {
                    btnQuickBrew.setTag(brew);
                }
                btnQuickBrew.setOnClickListener(this.onQuickBrewClickListener);
            } catch (StorageServiceException | BrewException e) {
                Util.log(TAG, e);
                e.printStackTrace();
            }
            brewName.setText(brew.getBrewName());
        } else if (this.mode.equals("history")) {
            favCount.setVisibility(View.INVISIBLE);
            btnQuickBrew.setVisibility(View.INVISIBLE);
            brewName.setText(Html.fromHtml("<b>" + brew.getBrewName() + "</b> <br\n> <small>(" + brew.getLastBrew() + ")</small>"));
            brewName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        } else {
            favCount.setVisibility(View.INVISIBLE);
            btnQuickBrew.setVisibility(View.INVISIBLE);
            brewName.setText(brew.getBrewName());
        }
        // Sæt navnet på Brew ind og tilføj data så man kan trykke på det
        brewName.setTag(brew);
        brewName.setOnClickListener(this.onButtonClickListener);

        // Returner det viewet, som er klar til at blive renderet
        return convertView;
    }

    // Følgende stumper er til at få klik på under-elementer til at virke
    // De første to metoder gør at der ikke sker noget når man trykker på selve liste-punktet
    @Override
    public boolean  areAllItemsEnabled() { return false; }
    @Override
    public boolean isEnabled(int position) { return false; }

    // Håndterer at der bliver trykket på "knappen" i templaten og sender videre til "Brewing"
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
                Util.log(TAG, e);
                e.printStackTrace();
            }
        }
    };

    // Håndterer at der bliver trykket på stjernen i templaten og gemmer at det er gen der er quick-brew
    private View.OnClickListener onQuickBrewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Brew brew = (Brew) v.getTag();
            System.out.println("Star clicked, row " + brew.getBrewName());
            storage.setQuickBrew(brew.getFavoriteKey());
            Intent intent = new Intent(new Intent(getContext(), Sections.class));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            ((Activity) context).finish();

        }
    };

}
