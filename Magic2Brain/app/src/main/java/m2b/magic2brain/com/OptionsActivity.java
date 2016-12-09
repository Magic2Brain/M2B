package m2b.magic2brain.com;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.Toast;

import m2b.magic2brain.com.magic2brain.R;

public class OptionsActivity extends AppCompatActivity { // Same shit like in MainActivity.class

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab t = ab.newTab();
        t.setText("hw");
        t.setTabListener(this);
        ab.addTab(t);



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
