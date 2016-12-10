package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import m2b.magic2brain.com.magic2brain.R;

public class LastSeenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_seen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DeckAssetLoader dc = new DeckAssetLoader();

        Card c[] = new Card[2064];
        try {
            c = dc.getDeck("LEA");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(c[1].getName());

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
}
