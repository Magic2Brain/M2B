package m2b.magic2brain.com;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_search_handler);
        final Context context = this;

        DeckAssetLoader dc = new DeckAssetLoader();

        Button submit = (Button) findViewById(R.id.search_button);
        final EditText search_field = (EditText) findViewById(R.id.search_text);
        final ListView searchresultsview = (ListView) findViewById(R.id.search_lv);

        Deck[] darray = new Deck[1];
        try {
            darray = dc.getDeckList(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] names = new String[darray.length];

        for(int i = 0; i < darray.length; i++){
            names[i] = darray[i].getName();
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

        /*
        final SortedMap<String, Deck> nameNum = new TreeMap<String, Deck>();

        //fill map
        Deck[] decks = new Deck[1];
        try {
            decks = dc.getDeckList(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Deck deck : decks){
            nameNum.put(deck.getName(), deck);
        }

        //copy deck to final var
        final SortedMap<String, Deck> cNameNum = nameNum;

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String prefix = search_field.getText().toString();
                String[] sresults = new String[2048];
                int iterator = 0;
                for(Map.Entry<String,Deck> entry : RUtils.filterPrefix(cNameNum, prefix).entrySet()) {
                    System.out.println(entry);
                    sresults[iterator] = entry.toString();
                    iterator++;
                }

                final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, sresults);
                searchresultsview.setAdapter(adapter);
            }
        });*/
    }
}
