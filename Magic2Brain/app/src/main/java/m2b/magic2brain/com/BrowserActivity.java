package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import m2b.magic2brain.com.magic2brain.R;

/*
This Activity should show a List with all sets and open DeckDisplayActivity with the selected set on a click.
*/

public class BrowserActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_browser); // This adds an View to our Activity. We defined at "/res/layout/activity_browser.xml" how our activity should look like.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // With this we get the Toolbar of the View.
        setSupportActionBar(toolbar); // We add the Toolbar as a SupportActionBar. This is needed for the next line.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();
        setTitle(getString(R.string.BrowserActivity_title)); // We set a title to our View. We defined the title in "/res/values/strings.xml". We just load it from there.
        final Context currentContext = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.

        Deck d[] = DeckAssetLoader.getDeckList(this); // We load all Sets with our own function into an Deck-Array.
        String[] it = new String[d.length]; // We just need the names of the sets. So we create an String-Array for the names.
        for (int i = 0; i < d.length; i++) {
            it[i] = d[i].getName();  // This for-loop adds the names from every Deck from the Deck-Array to the String-Array.
        }

        final String[] listItems = it; // We need to finalize the String-Array for the ArrayAdapter.
        final Deck[] finalD = d; // We need to finalize the Deck-Array, because we want to access it from an inner class.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems); // This is just a helper-class. It transforms our String-Array into an clickable List.
        ListView lv = (ListView) findViewById(R.id.deckbrowser_list); // We want to add our list to the ListView. So we get the ListView from the View
        lv.setAdapter(adapter); // We add our list into it.


        lv.setOnItemClickListener(new OnItemClickListener() { // This performs an action when we click on an item from the list.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The following code starts DeckDisplayActivity and passes the Deck-Code and the Deck-Name of the item we clicked.
                Intent intent = new Intent(currentContext, DeckDisplayActivity.class);
                intent.putExtra("code", finalD[position].getCode());
                intent.putExtra("name", finalD[position].getName());
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
