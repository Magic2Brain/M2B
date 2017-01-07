package m2b.magic2brain.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import m2b.magic2brain.com.magic2brain.R;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FAVS_SAVEFILE = "FavoritesSavefile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Add image to NavigationView
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Get toasted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Favorites.init();

        // Restore preferences
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("favobj", "");
        Type type = new TypeToken<ArrayList<Card>>(){}.getType();
        ArrayList<Card> favs= gson.fromJson(json, type);
        if(favs == null){
            Favorites.init();
        }
        else{
            Favorites.favorites_mvid = favs;
        }
        buildNewsFeed();
    }

    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        ArrayList<Card> mStudentObject= Favorites.favorites_mvid;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mStudentObject);
        prefsEditor.putString("favobj", json);
        prefsEditor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //TODO: Add some fancy transition animations
        if (id == R.id.nav_search) {
            // Handle the camera action
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_browser) {
            Intent intent = new Intent(this, BrowserActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_quick_learn) {
            Deck d = new Deck(); // Just for testing
            DeckAssetLoader dc = new DeckAssetLoader();
            Card[] c = new Card[1];
            try {
                c = dc.getDeck("C16.json", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            d.setSet(new ArrayList<Card>(Arrays.asList(c)));
            d.setName("Commander 2016");
            Intent i = new Intent(this, QueryActivity.class);
            i.putExtra("Set",d); // Just for testing
            startActivity(i);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, LastSeenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupWindowAnimations() {

    }

    public void buildNewsFeed(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.main_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params;

        TextView score = new TextView(this); // Create new Textview
        score.setText("Changelog 2.6 \n \n - Better learning algorithm \n - New fancy menu \n - Changed Splash-art \n \n - Fixed Save-bug \n - Fixed Browser-bug \n - Fixed Search-bug");
        score.setTextSize(26);
        params = new RelativeLayout.LayoutParams(scrWidth, (int)(0.8*scrHeight));
        params.leftMargin = (int)(0.03*scrWidth); // X-Position
        params.topMargin = (int)(0.1*scrHeight); // Y-Position
        lyt.addView(score, params); // add it to the View

    }

}
