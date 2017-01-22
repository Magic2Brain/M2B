package m2b.magic2brain.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;
import m2b.magic2brain.com.magic2brain.R;

/*
This activity should show one card with all informations
*/
public class CardBrowserActivity extends AppCompatActivity {

    ImageView cImage; // We will store our ImageView in a global variable, because we want to acces it from various methods.

    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        final Context context = this; // This doesn't actually do anything, but it's needed if we want to refer to "this" from an inner class.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.

        Intent mIntent = getIntent(); // We want to access any data that is passed.
        final Card card = (Card) mIntent.getSerializableExtra("currentCard"); // We load the card-object from the Intent

        setContentView(R.layout.activity_card_browser);  // This adds an View to our Activity. We defined at "/res/layout/activity_card_browser.xml" how our activity should look like.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // With this we get the Toolbar of the View.
        toolbar.setTitle(card.getName()); // We set a title to our View. The title should be the name of the card.
        setSupportActionBar(toolbar); // We add the Toolbar as a SupportActionBar.

        cImage = (ImageView) findViewById(R.id.cbaImage); // We load our ImageView from the View
        cImage.setPadding(0, 10, 0, 10); // We add a padding to it.

        // The next couple lines builds a GUI with the informations from the card. We access all Views from our View.
        TextView tv = (TextView) findViewById(R.id.cbaInfo);
        String info = "";
        info += " Name: " + card.getName();
        info += "\n MVID: " + card.getMultiverseid();
        tv.setText(info);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(0, 10, 0, 10);

        TextView text = (TextView) findViewById(R.id.cbaText);
        text.setText(card.getText());
        text.setTextColor(Color.BLACK);
        text.setPadding(20, 10, 10, 20);

        TextView flavor = (TextView) findViewById(R.id.cbaFlavor);
        flavor.setText(card.getFlavor());
        flavor.setTextColor(Color.BLACK);
        flavor.setPadding(20, 10, 10, 20);

        TextView type = (TextView) findViewById(R.id.cbaType);
        type.setText(card.getType());
        type.setTextColor(Color.BLACK);
        type.setPadding(0, 10, 0, 10);

        LinearLayout ll = (LinearLayout) findViewById(R.id.cba_mcost_layout);
        setManaCost(card.getManaCost(), ll);
        ll.setGravity(Gravity.CENTER);
        ll.setPadding(0, 10, 0, 10);

        final int mvid = card.getMultiverseid(); // This gets the MultiverseID from the card
        showPic(mvid); // and loads the picture of the card.

        cImage.setOnClickListener(new View.OnClickListener() {  // This performs an action if we click the image
            public void onClick(View view) {
                // The following code starts the CardImageDisplayActivity and passes the MultiverseID of the Card.
                Intent intent = new Intent(context, CardImageDisplayActivity.class);
                intent.putExtra("pic", mvid);
                startActivity(intent);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); // This gets our "add to favorites"-button from the view
        // We check if its already added to favorites. If so we mark it as already added.
        if (!checkCard(card.getName())) {
            fab.setImageResource(R.drawable.ic_favorite_border);
        } else {
            fab.setImageResource(R.drawable.ic_favorite);
        }

        fab.setOnClickListener(new View.OnClickListener() { // This performs an action if we click the "add to favorites"-Button
            // The following code toggles the Button, so it shows that the card was added or not, gives a notification and add the card to the favorites-list (which will be saved).
            public void onClick(View view) {
                if (checkCard(card.getName())) {
                    removeCard(card.getName());
                    fab.setImageResource(R.drawable.ic_favorite_border);
                    Snackbar.make(view, R.string.remove_fav, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Favorites.favorites_mvid.add(card);
                    fab.setImageResource(R.drawable.ic_favorite);
                    Snackbar.make(view, R.string.add_fav, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private boolean checkCard(String name) { // This function checks if the card was added to the favorites
        for (Card c : Favorites.favorites_mvid) {
            if (c.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    private void removeCard(String name) { // This function removes the card from the favorites-list
        ArrayList<Card> cards = new ArrayList<>();
        for (Card c : Favorites.favorites_mvid) {
            if (c.getName().contains(name)) {
                cards.add(c);
            }
        }
        for (Card c : cards) {
            Favorites.favorites_mvid.remove(c);
        }
    }

    private void setManaCost(String manatext, LinearLayout layout) { // This function adds the mana-cost to the View by loading the images.
        manatext = manatext.replaceAll("\\{", "");
        manatext = manatext.replaceAll("\\}", "");
        String[] items = manatext.split("");
        items = Arrays.copyOfRange(items, 1, items.length);

        for (int i = 0; i < items.length; i++) {
            if (RUtils.isInteger(items[i])) {
                TextView tv = new TextView(this);
                tv.setText(items[i]);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80);
                tv.setTextColor(Color.BLACK);
                layout.addView(tv);
            } else {
                ImageView imgv = new ImageView(this);
                int resid = 0;
                if (items[i].equals("B")) {
                    resid = R.drawable.b;
                } else if (items[i].equals("C")) {
                    resid = R.drawable.c;
                } else if (items[i].equals("G")) {
                    resid = R.drawable.g;
                } else if (items[i].equals("R")) {
                    resid = R.drawable.r;
                } else if (items[i].equals("U")) {
                    resid = R.drawable.u;
                } else if (items[i].equals("W")) {
                    resid = R.drawable.w;
                } else {
                    resid = R.drawable.ic_action_cancel;
                }
                imgv.setBackgroundResource(resid);
                layout.addView(imgv);
                android.view.ViewGroup.LayoutParams layoutParams = imgv.getLayoutParams();
                layoutParams.width = 60;
                layoutParams.height = 60;
                imgv.setLayoutParams(layoutParams);
            }
        }
    }

    private void showPic(int MultiID) { // This method loads an cardimage into cImage with a given MultiverseID.
        Picasso.with(this)
                .load(getString(R.string.image_link_1) + MultiID + getString(R.string.image_link_2)) // This tries to load an image from a link.
                .placeholder(R.drawable.loading_image) // We want to show a image while its loading. We load our image from the "/res/drawable" folder
                .error(R.drawable.image_not_found) // If it fails to load image we show an error-image.
                .into(cImage); // This places the image into our ImageView.
    }

    public void onBackPressed() {
        finish(); // This closes our Activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right); // We want to close it with an fancy animation.
    }
}
