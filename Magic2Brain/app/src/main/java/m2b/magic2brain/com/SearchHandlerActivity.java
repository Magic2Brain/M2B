package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import m2b.magic2brain.com.magic2brain.R;

/*
This Activity is the one that actually searches for cards/sets.
*/

public class SearchHandlerActivity extends AppCompatActivity {
    Card[] cardarray; // This is the array, where all cards are stored

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState);  // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_search_handler); // This adds an View to our Activity. We defined at "/res/layout/activity_search_handler.xml" how our activity should look like.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();

        final Context context = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.
        Intent inte = getIntent();  // We want to access any data that is passed.
        final Boolean cardsearch = inte.getBooleanExtra("cardsearch", true); // We look if the user wants to search for a card or set

        final EditText search_field = (EditText) findViewById(R.id.search_text); // We get the search-field
        if (cardsearch) {
            search_field.setHint(R.string.SearchHandler_hint_1); // if he wants to search a card, we set the hint to "Cardname"
        } else {
            search_field.setHint(R.string.SearchHandler_hint_2); // if he wants to search a set, we set the hint to "Setname"
        }
        final ListView searchresultsview = (ListView) findViewById(R.id.search_lv); // We get the ListView so we can show the results

        String[] names = new String[1]; // We only need the names so we create a names-array
        names[0] = getString(R.string.error); // This is to ensure that the array isn't empty
        Deck[] deckarray = DeckAssetLoader.getDeckList(this); // We get all decks


        if (cardsearch) {
            cardarray = MainActivity.cardarray; // We load our card-array
            names = new String[cardarray.length];

            for (int i = 0; i < cardarray.length; i++) {
                names[i] = cardarray[i].getName(); // And add all names to the string-array
            }
        } else {
            names = new String[deckarray.length]; // The same like cards but with sets

            for (int i = 0; i < deckarray.length; i++) {
                names[i] = deckarray[i].getName();  // The same like cards but with sets
            }
        }

        //finalizing variables for further use
        final Deck[] cdeckarray = deckarray;
        final Card[] ccardarray = cardarray;


        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, names); // We turn the string-array into a list
        search_field.addTextChangedListener(new TextWatcher() { // and add a listener, so it updates when we write something
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString().toLowerCase());
            }
        });

        searchresultsview.setAdapter(adapter); // We show the list
        searchresultsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cardsearch) {
                    // If it's a card-search, we open CardBrowserActivity with the clicked card.
                    String cardname = adapter.getItem(position).toString();
                    Intent intent = new Intent(context, CardBrowserActivity.class);
                    intent.putExtra("currentCard", searchforcard(cardname, ccardarray));
                    startActivity(intent);
                } else {
                    // If it's a set-search, we open DeckDisplayActivity with the clicked Set
                    String deckname = adapter.getItem(position).toString();
                    Intent intent = new Intent(context, DeckDisplayActivity.class);
                    intent.putExtra("code", searchfordeck(deckname, cdeckarray));
                    intent.putExtra("name", deckname);
                    startActivity(intent);
                }
            }
        });

    }

    private Card searchforcard(String name, Card[] cardarray) { // This function searchs for a card and returns it
        Card rc = new Card();

        for (int i = 0; i < cardarray.length; i++) {
            if (cardarray[i].getName().equals(name)) {
                rc = cardarray[i];
                break;
            }
        }
        return rc;
    }

    private String searchfordeck(String name, Deck[] darray) { // This function searchs for a set and returns it
        String code = getString(R.string.error);

        for (int i = 0; i < darray.length; i++) {
            String dname = darray[i].getName();

            if (dname.equals(name)) {
                code = darray[i].getCode();
                break;
            }
        }

        return code;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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
