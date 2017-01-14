package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import m2b.magic2brain.com.magic2brain.R;

public class SearchHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_handler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = this;
        Intent inte = getIntent();
        Boolean cardsearch = inte.getBooleanExtra("cardsearch",true);
        DeckAssetLoader dc = new DeckAssetLoader();

        final EditText search_field = (EditText) findViewById(R.id.search_text);
        if(cardsearch){search_field.setHint("Cardname");}
        else {search_field.setHint("Setname");}
        final ListView searchresultsview = (ListView) findViewById(R.id.search_lv);

        String[] names = new String[1];
        names[0] = "error";

        Deck[] deckarray = new Deck[1];
        Deck[] cardarray = new Deck[1];

        if(cardsearch){
            //searchCards();
            Card[] darray = new Card[1];
            darray[0] = new Card();
            darray[0].setName("error");
            try {
                darray = dc.getAllCards(this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            names = new String[darray.length];

            for(int i = 0; i < darray.length; i++){
                names[i] = darray[i].getName();
            }
        }
        else{
            Deck[] darray = new Deck[1];
            try {
                darray = dc.getDeckList(this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            names = new String[darray.length];

            for(int i = 0; i < darray.length; i++){
                names[i] = darray[i].getName();
            }
        }



        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, names);
        search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString().toLowerCase());
            }
        });
        searchresultsview.setAdapter(adapter);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
