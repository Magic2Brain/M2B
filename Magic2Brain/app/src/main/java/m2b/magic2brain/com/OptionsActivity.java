package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import m2b.magic2brain.com.magic2brain.R;

public class OptionsActivity extends AppCompatActivity { // Same shit like in MainActivity.class

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish(); // To go back to the last activity (or kill the activity) we simply FINISH HIM!
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // Let's add a fancy animation shall we?
            }
        });
    }
}
