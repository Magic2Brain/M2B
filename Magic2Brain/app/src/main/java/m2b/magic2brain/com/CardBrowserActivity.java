package m2b.magic2brain.com;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import m2b.magic2brain.com.magic2brain.R;

public class CardBrowserActivity extends AppCompatActivity {

    ImageView cImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO implement new view & optimize for card display
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        Card card = (Card) mIntent.getSerializableExtra("currentCard");

        setContentView(R.layout.activity_card_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(card.getName());
        setSupportActionBar(toolbar);

        cImage = (ImageView) findViewById(R.id.cbaImage);



        TextView tv = (TextView) findViewById(R.id.cbaInfo);
        String info = null;
        info += " Name: "+card.getName();
        info += "\n MVID: "+card.getMultiverseid();
        tv.setText(info);

        TextView text = (TextView) findViewById(R.id.cbaText);
        text.setText(card.getText());
        text.setTextColor(Color.CYAN);

        TextView flavor = (TextView) findViewById(R.id.cbaFlavor);
        flavor.setText(card.getFlavor());

        showPic(card.getMultiverseid());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showPic(int MultiID){
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.ic_hourglass_empty)
                .error(R.drawable.ic_error)
                .into(cImage);
    }
}
