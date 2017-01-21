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

public class FavoritesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.FavoritesActivity_title));
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildMenu();
    }

    protected void onResume() {
        super.onResume();
        buildMenu();
    }

    private void buildMenu() {
        final Context currentContext = this;

        final ArrayList<Card> alist_favs = Favorites.favorites_mvid;
        final Card[] cards = new Card[alist_favs.size()];
        final String[] favs = new String[alist_favs.size()];
        for (int i = 0; i < alist_favs.size(); i++) {
            favs[i] = alist_favs.get(i).getName();
            cards[i] = alist_favs.get(i);
        }

        final ListView lv = (ListView) findViewById(R.id.favList);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favs);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                intent.putExtra("currentCard", cards[position]);
                startActivity(intent);
            }
        });


        FloatingActionButton fam = (FloatingActionButton) findViewById(R.id.fab_addlearn);
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
