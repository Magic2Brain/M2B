package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import m2b.magic2brain.com.magic2brain.R;

public class LastSeenActivity extends AppCompatActivity {
    private ArrayList<String> recentlyLearned;
    private ArrayList<String> recentlyLearnedNames;
    private String[] names;
    private String[] reclearn;

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_seen);
        setTitle(getString(R.string.LastSeenActivity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Context currentContext = this;
        if (!loadRecent()) {
            recentlyLearned = new ArrayList<>();
        }
        names = new String[recentlyLearned.size()];
        reclearn = new String[recentlyLearned.size()];
        for (int i = 0; i < recentlyLearned.size(); i++) {
            reclearn[i] = recentlyLearned.get(i);
            names[i] = recentlyLearnedNames.get(i);
        }


        ListView lv = (ListView) findViewById(R.id.mListView);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String code = reclearn[position];
                String name = names[position];
                Intent intent = new Intent(currentContext, DeckDisplayActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }

    private boolean loadRecent() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("query_recent4", null);
        String json2 = sharedPrefs.getString("query_recent_names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> aL = gson.fromJson(json, type);
        ArrayList<String> aL2 = gson.fromJson(json2, type);
        if (aL == null) {
            return false;
        }
        recentlyLearned = aL;
        recentlyLearnedNames = aL2;
        return true;
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
