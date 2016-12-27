package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import m2b.magic2brain.com.magic2brain.R;

public class LastSeenActivity extends AppCompatActivity {
    //TODO: Remove Padding of the list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_seen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Context currentContext = this;

        DeckAssetLoader dc = new DeckAssetLoader();

        Card c[] = new Card[2064];
        try {
            c = dc.getDeck("LEA.json", this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] listItems = getListified(c);

        ListView lv = (ListView) findViewById(R.id.mListView);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);

        final Card[] finalC = c;
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String yourData = listItems[position];
                Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                intent.putExtra("currentCard", finalC[position]);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String[] getListified(Card[] cards){
        String[] list = new String[cards.length];
        for(int i = 0; i < cards.length; i++){
            list[i] = cards[i].getName();
        }

        return list;
    }
}
