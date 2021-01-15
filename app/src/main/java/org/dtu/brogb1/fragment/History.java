package org.dtu.brogb1.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import org.dtu.brogb1.R;
import org.dtu.brogb1.activity.Brewing;
import org.dtu.brogb1.activity.EditBrew;
import org.dtu.brogb1.adapter.RecipiesAdapter;
import org.dtu.brogb1.model.Brew;
import org.dtu.brogb1.service.StorageServiceSharedPref;
import org.dtu.brogb1.service.Util;

import java.util.ArrayList;

/**
 * @author Elinor Mikkelsen s191242
 * @author Betina Hansen s195389
 */

public class History extends Fragment {
    private static final String TAG = History.class.getSimpleName();
    StorageServiceSharedPref sharedPref = StorageServiceSharedPref.getInstance();
    ArrayList<Brew> viewMain;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.history_layout, container, false);
        ListView listMain = root.findViewById(R.id.list_view_main_history);
        try {
            viewMain = sharedPref.getBrewHistory();
        } catch (Exception e) {
            viewMain = new ArrayList<Brew>();
            Util.log(TAG, e);
            e.printStackTrace();
        }

        RecipiesAdapter adapterMain = new RecipiesAdapter(getActivity(), viewMain, "history");
        listMain.setAdapter(adapterMain);

        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                Intent intent = new Intent(getContext(), Brewing.class);
                try {
                    intent.putExtra("Brew", viewMain.get(position).toJson());
                } catch (Exception e) {
                    Util.log(TAG, e);
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        return root;
    }
}
