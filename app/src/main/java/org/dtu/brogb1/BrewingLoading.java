package org.dtu.brogb1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

public class BrewingLoading {
    private Activity activity;
   private AlertDialog dialog;
   private ProgressBar progressBarAnimation;
   private ObjectAnimator progressAnimator;

    BrewingLoading(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.brewing_progress, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
