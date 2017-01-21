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

public class SearchHandlerActivity extends AppCompatActivity {
    Card[] cardarray;

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_handler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = this;
        Intent inte = getIntent();
        final Boolean cardsearch = inte.getBooleanExtra("cardsearch", true);

        final EditText search_field = (EditText) findViewById(R.id.search_text);
        if (cardsearch) {
            search_field.setHint(R.string.SearchHandler_hint_1);
        } else {
            search_field.setHint(R.string.SearchHandler_hint_2);
        }
        final ListView searchresultsview = (ListView) findViewById(R.id.search_lv);

        String[] names = new String[1];
        names[0] = getString(R.string.error);
        Deck[] deckarray = DeckAssetLoader.getDeckList(this);


        if (cardsearch) {
            cardarray = MainActivity.cardarray;
            names = new String[cardarray.length];

            for (int i = 0; i < cardarray.length; i++) {
                names[i] = cardarray[i].getName();
            }
        } else {
            names = new String[deckarray.length];

            for (int i = 0; i < deckarray.length; i++) {
                names[i] = deckarray[i].getName();
            }
        }

        //finalizing variables for further use
        final Deck[] cdeckarray = deckarray;
        final Card[] ccardarray = cardarray;


        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, names);
        search_field.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString().toLowerCase());
            }
        });

        searchresultsview.setAdapter(adapter);
        searchresultsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cardsearch) {
                    String cardname = adapter.getItem(position).toString();

                    Intent intent = new Intent(context, CardBrowserActivity.class);
                    intent.putExtra("currentCard", searchforcard(cardname, ccardarray));
                    startActivity(intent);
                } else {
                    String deckname = adapter.getItem(position).toString();

                    Intent intent = new Intent(context, DeckDisplayActivity.class);
                    intent.putExtra("code", searchfordeck(deckname, cdeckarray));
                    intent.putExtra("name", deckname);
                    startActivity(intent);
                }
            }
        });

    }

    private Card searchforcard(String name, Card[] cardarray) {
        Card rc = new Card();

        for (int i = 0; i < cardarray.length; i++) {
            if (cardarray[i].getName().equals(name)) {
                rc = cardarray[i];
                break;
            }
        }
        return rc;
    }

    private String searchfordeck(String name, Deck[] darray) {
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
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
