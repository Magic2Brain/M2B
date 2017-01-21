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
import org.json.JSONException;
import java.io.IOException;
import m2b.magic2brain.com.magic2brain.R;

public class BrowserActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(getString(R.string.BrowserActivity_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Context currentContext = this;
        DeckAssetLoader dc = new DeckAssetLoader();

        Deck d[] = new Deck[1];
        try {
            d = dc.getDeckList(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] it = new String[d.length];
        for (int i = 0; i < d.length; i++) {
            it[i] = d[i].getName();
        }

        final String[] listItems = it;
        final Deck[] finalD = d;
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        ListView lv = (ListView) findViewById(R.id.deckbrowser_list);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
