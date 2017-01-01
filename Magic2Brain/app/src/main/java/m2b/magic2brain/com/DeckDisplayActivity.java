package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;

import m2b.magic2brain.com.magic2brain.R;
import android.widget.AdapterView.OnItemClickListener;

public class DeckDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_display);

        final Context currentContext = this;
        DeckAssetLoader dc = new DeckAssetLoader();
        Intent intent = getIntent();
        String deckcode = intent.getStringExtra("code");
        String name = intent.getStringExtra("name");
        //----------------------------------------------


        Card c[] = new Card[2064];
        try {
            c = dc.getDeck(deckcode, this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] listItems = RUtils.getListified(c);

        ListView lv = (ListView) findViewById(R.id.deckdisplay);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);

        final Card[] finalC = c;

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                intent.putExtra("currentCard", finalC[position]);
                startActivity(intent);
            }
        });

    }
}
