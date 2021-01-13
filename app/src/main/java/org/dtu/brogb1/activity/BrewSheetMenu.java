package org.dtu.brogb1.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.dtu.brogb1.R;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */


public class BrewSheetMenu extends BottomSheetDialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.brew_sheet_menu, container, false);
        return v;
    }
}
