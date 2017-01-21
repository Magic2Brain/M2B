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

public class CardBrowserActivity extends AppCompatActivity {

    ImageView cImage;

    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        final Context context = this;
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        final Card card = (Card) mIntent.getSerializableExtra("currentCard");

        setContentView(R.layout.activity_card_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(card.getName());
        setSupportActionBar(toolbar);

        cImage = (ImageView) findViewById(R.id.cbaImage);
        cImage.setPadding(0, 10, 0, 10);

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
        setManaCost(card.getManaCost(), this, ll);
        ll.setGravity(Gravity.CENTER);
        ll.setPadding(0, 10, 0, 10);

        final int mvid = card.getMultiverseid();
        showPic(mvid);

        cImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardImageDisplayActivity.class);
                intent.putExtra("pic", mvid);
                startActivity(intent);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!checkCard(card.getName())) {
            fab.setImageResource(R.drawable.ic_favorite_border);
        } else {
            fab.setImageResource(R.drawable.ic_favorite);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
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
                //onCreate(savedInstanceState);
            }
        });
    }

    private boolean checkCard(String name) {
        for (Card c : Favorites.favorites_mvid) {
            if (c.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    private void removeCard(String name) {
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

    private void setManaCost(String manatext, Context context, LinearLayout layout) {
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

    private void showPic(int MultiID) {
        Picasso.with(this)
                .load(getString(R.string.image_link_1) + MultiID + getString(R.string.image_link_2))
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.image_not_found)
                .into(cImage);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
