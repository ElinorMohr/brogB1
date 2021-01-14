package org.dtu.brogb1.activity.clean;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;

/**
 * @author Elinor Mikkelsen s191242
 * @author Theis Villumsen s195461
 */

public class CleanActivityStep2Working extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_step2_working);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(CleanActivityStep2Working.this, CleanActivityStep3.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(intent);
                        CleanActivityStep2Working.this.finish();
                    }
                },
                1500
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}