package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import m2b.magic2brain.com.magic2brain.R;

/*
This class shows all Favorites in a list
*/

public class FavoritesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setTitle(getString(R.string.FavoritesActivity_title)); // We set a title to our View. We defined the title in "/res/values/strings.xml". We just load it from there.
        setContentView(R.layout.activity_favorites); // This adds an View to our Activity. We defined at "/res/layout/activity_favorites.xml" how our activity should look like.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();
        buildMenu(); // This simply builds the menu
    }

    protected void onResume() { // If we get back to this activity, we build the menu again, because its possible that the user removed a card.
        super.onResume();
        buildMenu();
    }

    private void buildMenu() {
        final Context currentContext = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.

        final ArrayList<Card> alist_favs = Favorites.favorites_mvid; // This loads all the favorites
        final Card[] cards = new Card[alist_favs.size()]; // We want to turn it into a card-array, so its finite
        final String[] favs = new String[alist_favs.size()]; // For the list we need just the names
        for (int i = 0; i < alist_favs.size(); i++) {
            favs[i] = alist_favs.get(i).getName(); // We add all names into the string-array
            cards[i] = alist_favs.get(i); // We add all cards into the card-array
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favs); // This is just a helper-class. It transforms our String-Array into an clickable List.
        final ListView lv = (ListView) findViewById(R.id.favList); // We want to add our list to the ListView. So we get the ListView from the View
        lv.setAdapter(adapter); // We add our list into it.

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // This performs an action when we click on an item from the list.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The following code starts CardBrowserActivity and passes the card of the item we clicked.
                Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                intent.putExtra("currentCard", cards[position]);
                startActivity(intent);
            }
        });


        FloatingActionButton fam = (FloatingActionButton) findViewById(R.id.fab_addlearn); // This gets the "start"-button from the view
        fam.setOnClickListener(new View.OnClickListener() { // when we press the button an action should be performed
            public void onClick(View view) {
                // The following code checks the amount of the favorites. If it's zero, the user gets a notification which says, that there are no cards to learn.
                // If there are cards in favorites, it constructs a deck-object and fills it with all the cards. After that it starts the query with that deck.
                if (alist_favs.size() > 0) {
                    Intent intent = new Intent(currentContext, QueryActivity.class);
                    Deck d = new Deck();
                    d.setCode("FAVS");
                    d.setName("Favorites");
                    d.setSet(alist_favs);
                    intent.putExtra("Set", d);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, R.string.No_Favs_to_learn, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
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
        finish();  // This closes our Activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right); // We want to close it with an fancy animation.
    }
}
