package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import m2b.magic2brain.com.magic2brain.R;

/*
This class should show all recently learned sets.
 */
public class LastSeenActivity extends AppCompatActivity {
    private ArrayList<String> recentlyLearned; // loadRecent() will load all deck-codes in this arraylist
    private ArrayList<String> recentlyLearnedNames; // loadRecent() will load all deck-names in this arraylist
    private String[] names; // this will contain all the names of the decks
    private String[] reclearn; // this will contain all the deck-codes of the decks

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_last_seen); // This adds an View to our Activity. We defined at "/res/layout/activity_last_seen.xml" how our activity should look like.
        setTitle(getString(R.string.LastSeenActivity_title)); // We set a title to our View. We defined the title in "/res/values/strings.xml". We just load it from there.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();
        final Context currentContext = this;  // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.

        if (!loadRecent()) { // if it fails to load all the decks, we simply replace recentlyLearned with an empty arraylist
            recentlyLearned = new ArrayList<>();
        }
        names = new String[recentlyLearned.size()]; // we initiate names with the size of recentlyLearned
        reclearn = new String[recentlyLearned.size()]; // we initiate reclearn with the size of recentlyLearned
        for (int i = 0; i < recentlyLearned.size(); i++) {
            reclearn[i] = recentlyLearned.get(i); // we fill reclearn with all the deck-codes
            names[i] = recentlyLearnedNames.get(i); // we fill names with all the names
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names); // This is just a helper-class. It transforms our String-Array into an clickable List.
        ListView lv = (ListView) findViewById(R.id.mListView); // We want to add our list to the ListView. So we get the ListView from the View
        lv.setAdapter(adapter); // We add our list into it.

        lv.setOnItemClickListener(new OnItemClickListener() { // This performs an action when we click on an item from the list.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The following code starts DeckDisplayActivity and passes the Deck-Code and the Deck-Name of the item we clicked.
                String code = reclearn[position];
                String name = names[position];
                Intent intent = new Intent(currentContext, DeckDisplayActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }

    private boolean loadRecent() { // this function loads the recently learned deck from the memory. If it fails it returns false.
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("query_recent4", null);
        String json2 = sharedPrefs.getString("query_recent_names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> aL = gson.fromJson(json, type);
        ArrayList<String> aL2 = gson.fromJson(json2, type);
        if (aL == null) {
            return false;
        }
        recentlyLearned = aL;
        recentlyLearnedNames = aL2;
        return true;
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
