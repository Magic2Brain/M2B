package m2b.magic2brain.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import m2b.magic2brain.com.magic2brain.R;
import android.widget.AdapterView.OnItemClickListener;

public class DeckDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context currentContext = this;
        DeckAssetLoader dc = new DeckAssetLoader();
        Intent intent = getIntent();
        String deckcode = intent.getStringExtra("code");
        String name = intent.getStringExtra("name");
        //----------------------------------------------

        setTitle(name);
        Card c[] = new Card[1];
        c[0] = new Card("notaname", "notaflavor", "notatext", "notatype", "0");

        try {
            c = dc.getDeck(deckcode, this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Card[] cCopy = c;

        FloatingActionButton fam = (FloatingActionButton) findViewById(R.id.fab_setlearn);
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentContext, QueryActivity.class);
                Deck d = new Deck();
                List<Card> clist = Arrays.asList(cCopy);
                d.setSet(new ArrayList<Card>(clist));
                intent.putExtra("Set", d);
                startActivity(intent);
            }
        });

        ListView lv = (ListView) findViewById(R.id.deckdisplay);

        if(c[0].getName().equals("notaname")){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Sadly, this Deck was not Found");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("I unserstand, bill me your server costs",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            finish();
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            lv.setVisibility(View.GONE);
            fam.setVisibility(View.GONE);
        }

        final String[] listItems = RUtils.getListified(c);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);

        final Card[] finalC = c;

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(currentContext, CardBrowserActivity.class);
                intent.putExtra("currentCard", finalC[position]);
                startActivity(intent);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
        return true;
    }

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
