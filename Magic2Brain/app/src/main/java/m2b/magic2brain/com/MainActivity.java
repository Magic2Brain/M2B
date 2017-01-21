package m2b.magic2brain.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import m2b.magic2brain.com.magic2brain.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Card[] cardarray;
    private ImageView imgv;

    protected void onCreate(Bundle savedInstanceState) {
        if (!checkCardArray()) {
            saveCardArray();
        }
        hideStatusBar();
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setListener(setRandomCard());
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
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("favobj", null);
        Type type = new TypeToken<ArrayList<Card>>() {
        }.getType();
        ArrayList<Card> favs = gson.fromJson(json, type);
        Favorites.init();
        if (favs != null) {
            Favorites.favorites_mvid = favs;
        }
        buildNewsFeed();
    }

    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void onPause() {
        super.onPause();
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Favorites.favorites_mvid);
        prefsEditor.putString("favobj", json);
        prefsEditor.commit();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
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
                c = dc.getDeck("KLD.json", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            d.setSet(new ArrayList<Card>(Arrays.asList(c)));
            d.setName("Kaladesh");
            d.setCode("KLD");
            Intent i = new Intent(this, QueryActivity.class);
            i.putExtra("Set", d); // Just for testing
            startActivity(i);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, LastSeenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=m2b.magic2brain.com");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void buildNewsFeed() {
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.main_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params;

        TextView score = new TextView(this); // Create new Textview
        score.setTextColor(Color.BLACK);
        score.setGravity(Gravity.CENTER);
        score.setText("Random Card");
        score.setTextSize(26);
        params = new RelativeLayout.LayoutParams(scrWidth, (int) (0.1 * scrHeight));
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(score, params); // add it to the View

        imgv = new ImageView(this); // Create new Imageview
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.6 * scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.2 * scrHeight); // Y-Position
        lyt.addView(imgv, params); // add it to the View
        setListener(setRandomCard());
    }

    public void setListener(int MultiID2) {
        final int MultiID = MultiID2;
        imgv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardImageDisplayActivity.class);
                intent.putExtra("pic", MultiID);
                startActivity(intent);
            }
        });
    }

    public int setRandomCard() {
        int MultiID = 1;
        Card c = cardarray[(int) (Math.random() * cardarray.length)];
        if (c != null) {
            MultiID = c.getMultiverseid();
        }
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.image_not_found)
                .into(imgv);
        return MultiID;
    }

    public boolean checkCardArray() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("cardArray", null);
        Type type = new TypeToken<Card[]>() {
        }.getType();
        Card[] aL = gson.fromJson(json, type);
        if (aL == null) {
            return false;
        }
        cardarray = aL;
        return true;
    }

    public void saveCardArray() {
        Card[] c = buildCardArray();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(c);
        editor.putString("cardArray", json);
        editor.commit();
        cardarray = c;
    }

    private Card[] buildCardArray() {
        ArrayList<Card> list = new ArrayList<>();
        DeckAssetLoader dc = new DeckAssetLoader();
        Deck[] deckarray = new Deck[1];

        try {
            deckarray = dc.getDeckList(this);
        } catch (Exception e) {
        }

        for (int i = 0; i < deckarray.length; i++) {
            //load current deck and append it to list
            Card[] c = new Card[1];
            c[0] = new Card();
            c[0].setName("error");
            try {
                c = dc.getDeck(deckarray[i].getCode() + ".json", this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (c[0].getName().equals("error")) {
                System.err.println("error ocurred at " + deckarray[i].getCode() + ", please update your database");
            } else {
                list.addAll(Arrays.asList(c));
            }
        }
        return list.toArray(new Card[list.size()]);
    }

}


