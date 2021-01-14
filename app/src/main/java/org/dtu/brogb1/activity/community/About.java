package org.dtu.brogb1.activity.community;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.dtu.brogb1.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Betina Hansen s195389
 */

public class About extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tv = findViewById(R.id.text_about);
        try {
            ReadTextFile(getCurrentFocus());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ReadTextFile(View v) throws IOException {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is = this.getResources().openRawResource(R.raw.about);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
            tv.setText(stringBuilder);
        }
        is.close();
        //Toast.makeText(getBaseContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }
}
