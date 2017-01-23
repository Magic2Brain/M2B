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

/*
This is where our app starts. It has a menu, shows a random card and loads some needed data.
*/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Card[] cardarray; // We will store all cards in this array. This is important for the SearchActivity. It makes the whole process alot faster.
    private ImageView imgv; // We will show the random card with this ImageView

    protected void onCreate(Bundle savedInstanceState) { // This is the very first function the app will execute at the start of the app.
        if (!checkCardArray()) { // We try to load our card-list.
            saveCardArray(); // If we don't find the card-list, we have to generate it and save it.
        }
        hideStatusBar(); // Does what is says. It hides the Statusbar
        setTheme(R.style.AppTheme_NoActionBar);  // We want to hide the ActionBar too
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_main); // This adds an View to our Activity. We defined at "/res/layout/activity_main.xml" how our activity should look like.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // With this we get the Toolbar of the View.
        setSupportActionBar(toolbar); // We add the Toolbar as a SupportActionBar. If we click something on the Toolbar it will call onNavigationItemSelected();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); // We get the "new random card"-button
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setListener(setRandomCard()); // If we click the button it should generate a new random card.
            }
        });
        // The following lines generate the drawer (Sidemenu)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Favorites.init(); // Initate Favorites

        // The following code loads the Favorites from the memory
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
        buildNewsFeed(); // This builds the frontpage
    }

    private void hideStatusBar() { // This method simply removes the Statusbar
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove the title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // set it to fullscreen
    }

    protected void onPause() {
        super.onPause();
        // If the user leaves our app in any way, we save all his favorites.
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Favorites.favorites_mvid);
        prefsEditor.putString("favobj", json);
        prefsEditor.commit();
    }

    public void onBackPressed() { // Closes the Drawer if we press the Back-Button on the phone
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
            // Starts the SearchActivitiy if we click on Search
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_favorite) {
            // Starts the FavoritesActivity if we click ond Favorites
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_browser) {
            // Starts the BrowserActivity if we click on the Cardbrowser
            Intent intent = new Intent(this, BrowserActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_quick_learn) {
            // Starts the QueryActivity and passes the newest set (Kaladesh), if we click Quick learn
            Deck d = new Deck();
            Card[] c = DeckAssetLoader.getDeck("KLD.json",this);
            d.setSet(c);
            d.setName("Kaladesh");
            d.setCode("KLD");
            Intent i = new Intent(this, QueryActivity.class);
            i.putExtra("Set", d);
            startActivity(i);

        } else if (id == R.id.nav_history) {
            // Starts the LastSeenActivity if we click recently learned
            Intent intent = new Intent(this, LastSeenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            // Starts an Activity, which opens all possibilities to send the link to our app.
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.play_store_link));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); // We get the drawer
        drawer.closeDrawer(GravityCompat.START); // Th drawer should start closed
        return true;
    }

    public void buildNewsFeed() { // This Function generates a text and a Imageview on the screen
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth(); // Get the width of the screen
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight(); // Get the Height of the screen
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.main_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params; // The parameters we want to add our TextView and ImageView

        TextView score = new TextView(this); // Create new Textview
        score.setTextColor(Color.BLACK);
        score.setGravity(Gravity.CENTER);
        score.setText("Random Card");
        score.setTextSize(26);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(score, params); // add it to the View

        imgv = new ImageView(this); // Create new Imageview
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.6 * scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.2 * scrHeight); // Y-Position
        lyt.addView(imgv, params); // add it to the View
        setListener(setRandomCard()); // Opens a random card
    }

    public void setListener(int MultiID2) { // This function sets an listener to the image, so if we click it, it shows a big image of the card.
        final int MultiID = MultiID2;
        imgv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardImageDisplayActivity.class);
                intent.putExtra("pic", MultiID);
                startActivity(intent);
            }
        });
    }

    public int setRandomCard() { // This gets a random card from the card-array and loads the image into the ImageView
        int MultiID = 1;
        Card c = cardarray[(int) (Math.random() * cardarray.length)];
        if (c != null) {
            MultiID = c.getMultiverseid();
        }
        Picasso.with(this)
                .load(getString(R.string.image_link_1) + MultiID +  getString(R.string.image_link_2)) // This tries to load an image from a link.
                .placeholder(R.drawable.loading_image) // We want to show a image while its loading. We load our image from the "/res/drawable" folder
                .error(R.drawable.image_not_found)  // If it fails to load image we show an error-image.
                .into(imgv);  // This places the image into our ImageView.
        return MultiID;
    }

    public boolean checkCardArray() { // This method checks if there is a saved version of our card array. If that's the case it tries to load it into the card-array.
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("cardArray", null);
        Type type = new TypeToken<Card[]>() {
        }.getType();
        Card[] aL = gson.fromJson(json, type);
        if (aL == null) {return false;}
        cardarray = aL;
        return true;
    }

    public void saveCardArray() { // This function saves the card-array into the memory.
        Card[] c = buildCardArray();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(c);
        editor.putString("cardArray", json);
        editor.commit();
        cardarray = c;
    }

    private Card[] buildCardArray() { // This method generates the card-array by opening all sets and loading the cards from it into out array.
        ArrayList<Card> list = new ArrayList<>();
        Deck[] deckarray = DeckAssetLoader.getDeckList(this);
        for (int i = 0; i < deckarray.length; i++) {
            //load current deck and append it to list
            Card[] c = DeckAssetLoader.getDeck(deckarray[i].getCode() + ".json", this);
            if (c[0].getName().equals("error")) {
                System.err.println("error ocurred at " + deckarray[i].getCode() + ", please update your database");
            } else {
                list.addAll(Arrays.asList(c));
            }
        }
        return list.toArray(new Card[list.size()]);
    }
}


