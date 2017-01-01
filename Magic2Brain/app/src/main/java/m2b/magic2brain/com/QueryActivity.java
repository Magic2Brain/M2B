package m2b.magic2brain.com;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import m2b.magic2brain.com.magic2brain.R;
//TODO: Add alternative query-mode (All vs. Sets of eg. 7)
public class QueryActivity extends AppCompatActivity {
    private ImageView imgv; // The image gets stored here
    private Toolbar hiding; // This is a bar that hides a certain area
    private ArrayList<Card> set; //This Arraylist holds all Cards that need to query. It won't be edited (after loading it)
    private ArrayList<Card> wrongGuessed;
    private int indexCard; // Actually not needed (because we remove cards from WrongGuessed) but may be useful in later edits
    private boolean firstGuess; //This is to check if he guessed it at first try. If so we remove the card from the Arraylist. Else it stays there.

    protected void onCreate(Bundle savedInstanceState) {
        // Build UI
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        hiding = (Toolbar) findViewById(R.id.toolbar_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildMenu();
        // Hide the status bar.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Prepare Query
        Intent i = getIntent();
        Deck qur = (Deck) i.getSerializableExtra("Set");
        set = qur.getSet();
        wrongGuessed = (ArrayList)set.clone(); //Lets assume he guessed everything wrong and remove the card of this Array when he guesses it right
        shuffleWrongs(); //Shuffle it a bit (better learn-effect)
        indexCard = 0;
        //buildEndScreen(); //Just for testing
        showFirstPic(); //Start the query
    }

    public void shuffleWrongs(){
        Collections.shuffle(wrongGuessed, new Random(System.nanoTime()));
    }

    public void showPic(int MultiID){
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.image_not_found)
                .into(imgv);
    }

    public void showFirstPic(){
        showHiderInstant();
        firstGuess = true;
        showPic(wrongGuessed.get(indexCard).getMultiverseid());
        hiding.bringToFront();
    }

    public void showNextPic(){
        showHider();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firstGuess = true;
                showPic(wrongGuessed.get(indexCard).getMultiverseid());
                hiding.bringToFront();
            }
        }, 800);
    }

    public void checkAnswer(String txt){ //TODO: Optional: Ask other things (like Mana-Cost etc.)
        if(txt.replaceAll("\\s+","").equalsIgnoreCase(wrongGuessed.get(indexCard).getName().replaceAll("\\s+",""))) {
            if(firstGuess){wrongGuessed.remove(indexCard);} //if he guessed it, we remove it.
            else {indexCard++;} // else we continue with the next card
            if(indexCard == wrongGuessed.size()){ //If this true he's through the set
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setDone();
                    }
                }, 1000);

            } else {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNextPic();
                    }
                }, 1000);
            }
        } else {
           wrongAnswer();
        }
    }

    public void skip(){
        wrongAnswer();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAnswer(wrongGuessed.get(indexCard).getName());
            }
        }, 1000);
    }

    public void wrongAnswer(){
        firstGuess = false;
        hideHider();
    }

    public void setDone(){
        buildEndScreen();
        indexCard = 0;
        shuffleWrongs();
    }

    public void hideHider(){
        hiding.animate().alpha(0).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {hiding.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();}}).start();
    }

    public void showHider(){
        hiding.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {hiding.animate().alpha(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();}}).start();
    }

    public void showHiderInstant(){
        hiding.animate().alpha(1).setDuration(10).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {hiding.animate().alpha(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();}}).start();
    }

    public void buildMenu(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        lyt.removeAllViews(); //Clear the Board
        RelativeLayout.LayoutParams params;

        params = new RelativeLayout.LayoutParams((int)(0.55*scrWidth),(int)(0.03*scrHeight));
        params.leftMargin = (scrWidth/2 - (int)(0.55*scrWidth)/2); // X-Position
        params.topMargin = (int)(0.07*scrHeight); // Y-Position
        lyt.addView(hiding,params);

        imgv  = new ImageView(this); // Create new Imageview
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.5*scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int)(0.05*scrHeight); // Y-Position
        lyt.addView(imgv, params); // add it to the View

        // Add EditText like Imageview
        final EditText inputtxt = new EditText(this);
        inputtxt.setGravity(Gravity.CENTER);
        inputtxt.setHint("What is the name of this card?");
        params = new RelativeLayout.LayoutParams((int)(0.75*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (int)(0.125*scrWidth);
        params.topMargin = (int)(0.55*scrHeight);
        lyt.addView(inputtxt, params);
        // Add a Listener to it (so the User can simply press ENTER on the keyboard)
        inputtxt.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            checkAnswer(inputtxt.getText().toString());
                            inputtxt.setText("");
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        Button answer = new Button(this);
        answer.setText("Answer");
        answer.setTextColor(Color.WHITE);
        answer.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int)(0.25*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (scrWidth/2)-(int)(0.25*scrWidth);
        params.topMargin = (int)(0.50*scrHeight)+(int)(0.15*scrHeight);
        lyt.addView(answer, params);
        answer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkAnswer(inputtxt.getText().toString());
                inputtxt.setText("");
            }
        });

        Button skip = new Button(this);
        skip.setText("Skip");
        skip.setTextColor(Color.WHITE);
        skip.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int)(0.25*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (scrWidth/2);
        params.topMargin = (int)(0.50*scrHeight)+(int)(0.15*scrHeight);
        lyt.addView(skip, params);
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {skip();}
        });
      }

    public void buildEndScreen(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        lyt.removeAllViews(); //Clear the board
        RelativeLayout.LayoutParams params;

        TextView rights = new TextView(this);
        rights.setText("Right: ");
        rights.setTextSize(36);
        rights.setTextColor(getResources().getColor(R.color.colorPrimary));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.1*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.1*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight); // Y-Position
        lyt.addView(rights, params); // add it to the View

        TextView rights2 = new TextView(this);
        rights2.setText(""+(set.size() - wrongGuessed.size()));
        rights2.setTextSize(36);
        rights2.setTextColor(getResources().getColor(R.color.colorPrimary));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.1*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.4*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight); // Y-Position
        lyt.addView(rights2, params); // add it to the View

        TextView wrongs = new TextView(this);
        wrongs.setText("Wrong: ");
        wrongs.setTextSize(36);
        wrongs.setTextColor(getResources().getColor(R.color.colorAccent));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.1*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.1*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight) + (int)(0.1*scrHeight); // Y-Position
        lyt.addView(wrongs, params); // add it to the View

        TextView wrongs2 = new TextView(this);
        wrongs2.setText("" + (wrongGuessed.size()));
        wrongs2.setTextSize(36);
        wrongs2.setTextColor(getResources().getColor(R.color.colorAccent));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.1*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.4*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight) + (int)(0.1*scrHeight); // Y-Position
        lyt.addView(wrongs2, params); // add it to the View

        TextView total = new TextView(this);
        total.setText("Total: ");
        total.setTextSize(36);
        total.setTextColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.09*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.1*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight) + (int)(0.1*scrHeight) + (int)(0.1*scrHeight); // Y-Position
        lyt.addView(total, params); // add it to the View

        TextView total2 = new TextView(this);
        total2.setText("" + (set.size()));
        total2.setTextSize(36);
        total2.setTextColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.09*scrHeight))/*Height*/;
        params.leftMargin = (int)(0.4*scrHeight); // X-Position
        params.topMargin = (int)(0.05*scrHeight) + (int)(0.1*scrHeight) + (int)(0.1*scrHeight); // Y-Position
        lyt.addView(total2, params); // add it to the View

        if(wrongGuessed.size() > 0) {
            Button repWrong = new Button(this);
            repWrong.setText("Repeat wrong guessed");
            repWrong.setTextColor(Color.WHITE);
            repWrong.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            params = new RelativeLayout.LayoutParams((int) (0.90 * scrWidth), (int) (0.1 * scrHeight));
            params.leftMargin = (int) (0.05 * scrWidth);
            params.topMargin = (int) (0.4 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight);
            lyt.addView(repWrong, params);
            repWrong.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    buildMenu();
                    showFirstPic();
                }
            });
        }
        Button repAll = new Button(this);
        repAll.setText("Repeat all");
        repAll.setTextColor(Color.WHITE);
        repAll.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int)(0.90*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (int)(0.05*scrWidth);
        params.topMargin = (int)(0.5*scrHeight) + (int)(0.1*scrHeight) + (int)(0.1*scrHeight) + (int)(0.1*scrHeight);
        lyt.addView(repAll, params);
        repAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                wrongGuessed = (ArrayList)set.clone();
                shuffleWrongs();
                buildMenu();
                showFirstPic();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
