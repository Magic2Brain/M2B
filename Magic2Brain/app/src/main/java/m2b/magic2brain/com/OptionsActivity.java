package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.widget.Toast;

import m2b.magic2brain.com.magic2brain.R;

public class OptionsActivity extends AppCompatActivity { // Same shit like in MainActivity.class

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish(); // To go back to the last activity (or kill the activity) we simply FINISH HIM!
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // Let's add a fancy animation shall we?
            }
        });
        */

        NestedScrollView mainView = (NestedScrollView) findViewById(R.id.optionView);
        mainView.setOnTouchListener(new OnSwipeTouchListener(OptionsActivity.this){
            public void onSwipeTop() {
                Toast.makeText(OptionsActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(OptionsActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(OptionsActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(OptionsActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
