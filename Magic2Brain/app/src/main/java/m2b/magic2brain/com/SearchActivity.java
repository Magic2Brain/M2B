package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import m2b.magic2brain.com.magic2brain.R;

/*
This Activity asks the user if he wants to search for cards or set and opens a SearchHandlerActivity if the answer.
*/

public class SearchActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_search); // This adds an View to our Activity. We defined at "/res/layout/activity_search.xml" how our activity should look like.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();
        final Context context = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.
        setTitle(getString(R.string.SearchActivity_title)); // We set a title to our View. We defined the title in "/res/values/strings.xml". We just load it from there.
        Button cards = (Button) findViewById(R.id.c_search_cards); // We get the cards-button
        Button decks = (Button) findViewById(R.id.c_search_decks); // We get the sets-button

        cards.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // If the user presses the cards-button, we start SearchHandlerActivity and pass to it, that the user pressed "cards"
                Intent intent = new Intent(context, SearchHandlerActivity.class);
                intent.putExtra("cardsearch", true);
                startActivity(intent);
            }
        });

        decks.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // If the user presses the sets-button, we start SearchHandlerActivity and pass to it, that the user pressed "sets"
                Intent intent = new Intent(context, SearchHandlerActivity.class);
                intent.putExtra("cardsearch", false);
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    public void onBackPressed() {
        finish(); // This closes our Activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right); // We want to close it with an fancy animation.
    }
}
