package m2b.magic2brain.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import m2b.magic2brain.com.magic2brain.R;

/*
This class shows all cards of a deck in a list
 */
public class DeckDisplayActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_deck_display); // This adds an View to our Activity. We defined at "/res/layout/activity_deck_display.xml" how our activity should look like.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();

        final Context currentContext = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.
        Intent intent = getIntent(); // We want to access any data that is passed.
        final String deckcode = intent.getStringExtra("code"); // we get the deck-code
        final String name = intent.getStringExtra("name"); // we get the deck-name

        setTitle(name); // We set the title of the activity to the name of the deck
        Card c[] = DeckAssetLoader.getDeck(deckcode + ".json", this); // This gets all the cards from the deck
        final Card[] cCopy = c; // and adds it to an Array

        FloatingActionButton fam = (FloatingActionButton) findViewById(R.id.fab_setlearn); // This gets the "Learn"-Button
        fam.setOnClickListener(new View.OnClickListener() { // and adds a listener to it
            public void onClick(View view) {
                // The following code starts the QueryActivity and passes the deck
                Intent intent = new Intent(currentContext, QueryActivity.class);
                Deck d = new Deck();
                List<Card> clist = Arrays.asList(cCopy);
                d.setCode(deckcode);
                d.setName(name);
                d.setSet(new ArrayList<Card>(clist));
                intent.putExtra("Set", d);
                startActivity(intent);
            }
        });

        ListView lv = (ListView) findViewById(R.id.deckdisplay); // We get the Listview

        if (c[0] == null) { // If there are no cards in the deck, we show an error message
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Sadly, this Deck was not Found");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("I understand, bill me your server costs",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            onBackPressed();
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            lv.setVisibility(View.GONE);
            fam.setVisibility(View.GONE);
        } else { // If there are some cards in the deck, we generate a card-list like we did in various other Activities
            final String[] listItems = RUtils.getListified(c);
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            lv.setAdapter(adapter);

            final Card[] finalC = c;

            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                    intent.putExtra("currentCard", finalC[position]);
                    startActivity(intent);
                }
            });
        }
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
