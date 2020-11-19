package org.dtu.brogb1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.dtu.brogb1.activity.Sections;
import org.dtu.brogb1.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipes_layout, container, false);
        ListView listMain = root.findViewById(R.id.list_view_main);
        ListView listSec = root.findViewById(R.id.list_view_sec);
        ArrayList<String> viewMain = new ArrayList<String>();
        ArrayList<String> viewSes = new ArrayList<String>();
        viewMain.add("Fars morgen kaffe");
        viewMain.add("Ekstra mælk i denne");
        viewSes.add("Latten");
        int fragmentNum = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (fragmentNum) {
            case 2:
                ArrayAdapter<String> adapterSec = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, viewSes);
                listSec.setAdapter(adapterSec);
            case 1:
                ArrayAdapter<String> adapterMain = new ArrayAdapter<String>(getActivity(), R.layout.list_view_layout, viewMain);
                listMain.setAdapter(adapterMain);
        }

        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent intent = new Intent(new Intent(getContext(), Sections.class));
                startActivity(intent);
            }
        });
        listSec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent intent = new Intent(new Intent(getContext(), Sections.class));
                startActivity(intent);
            }
        });
        return root;
    }
}