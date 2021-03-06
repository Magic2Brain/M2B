package m2b.magic2brain.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import m2b.magic2brain.com.magic2brain.R;

public class QueryActivity extends AppCompatActivity {
    private ImageView imgv; // The image of the card gets stored here
    private ImageView imgCorr; // Is over the image. It's to indicate if the answer was correct or not
    private Toolbar hiding; // This is a bar that hides a certain area
    private ArrayList<Card> set; //This Arraylist holds all Cards that need to query. It won't be edited (after loading it)
    private ArrayList<Card> wrongGuessed;
    private int indexCard; // Actually not needed (because we remove cards from WrongGuessed) but may be useful in later edits
    private boolean firstGuess; //This is to check if he guessed it at first try. If so we remove the card from the Arraylist. Else it stays there.
    private String deckName; //Name of the deck. Only for saving/loading purpose
    private TextView score; //this will show the user the current progress
    private boolean queryLand = true; // should we really query lands?
    private boolean skipped = false; // this is to store if the user has skipped or not
    private ArrayList<String> recentlyLearned; // this is to save the deckname if a new set is learned
    private ArrayList<String> recentlyLearnedNames; // the name of the sets
    private int Mode; // the query mode
    private ArrayList<Button> choices; // We store the buttons here

    protected void onCreate(Bundle savedInstanceState) {
        // Standard stuff
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left); // This adds an fancy slide animation, when this activity starts.
        super.onCreate(savedInstanceState); // This does some intern stuff. We don't need to worry about that. It's just needed.
        setContentView(R.layout.activity_query); // This adds an View to our Activity. We defined at "/res/layout/activity_query.xml" how our activity should look like.
        // Hide the status bar.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Get some informations
        Intent i = getIntent();
        Deck qur = (Deck) i.getSerializableExtra("Set");
        deckName = qur.getName(); // Get the name of the Deck
        String code = qur.getCode(); // Get the Deck-Code
        if (deckName == null) {
            deckName = "DEFAULT"; // If deckname isn't defined, we set it to "DEFAULT"
        }
        setTitle(deckName); // we set the title to the deckname
        //Add set to recent learned
        if (!loadRecent()) {
            recentlyLearned = new ArrayList<>();
            recentlyLearnedNames = new ArrayList<>();
        }
        if (!recentlyLearned.contains(code)) {
            recentlyLearned.add(code);
            recentlyLearnedNames.add(deckName);
        }
        if (recentlyLearned.size() == 10) {
            recentlyLearned.remove(0);
            recentlyLearnedNames.remove(0);
        }
        saveRecent();
        //load set
        set = qur.getSet();
        if (!loadProgress()) { //First we try to load the progress. If this fails, we simply start over
            wrongGuessed = (ArrayList) set.clone(); //Lets assume he guessed everything wrong and remove the card of this Array when he guesses it right
            shuffleWrongs(); //Shuffle it a bit (better learn-effect)
            indexCard = 0; // We start at card No. 1
        }
        //Set Mode
        Mode = 1;
        if (set.size() < 4) {
            Mode = 0;
        }
        // Build UI
        hiding = (Toolbar) findViewById(R.id.toolbar_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // With this line we add an "back"-Button to the Toolbar. If we press it it calls onOptionsItemSelected();
        buildMenu();
        //Start the query
        showFirstPic();
        if (deckName.contains("DEFAULT") || deckName.contains("Favorites")) {
            restartAll(); // Because this decks are dynamic
        }
    }

    protected void onPause() { // We save the progress when the user leaves
        super.onPause();
        saveProgress();
    }

    public boolean loadRecent() { // This loads all recently learned sets
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

    public void saveRecent() { // This saves all recently learned sets
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentlyLearned);
        String json2 = gson.toJson(recentlyLearnedNames);
        editor.putString("query_recent4", json);
        editor.putString("query_recent_names", json2);
        editor.commit();
    }

    public boolean loadProgress() { // This loads the progress of the current deck
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("query_list_" + deckName, null);
        Type type = new TypeToken<ArrayList<Card>>() {
        }.getType();
        ArrayList<Card> aL = gson.fromJson(json, type);
        int loadedIndex = sharedPrefs.getInt("query_index_" + deckName, -1);
        if (aL == null) {
            return false;
        }
        wrongGuessed = aL;
        indexCard = loadedIndex;
        return true;
    }

    public void saveProgress() { // This saves the progress of the current deck
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(wrongGuessed);
        editor.putString("query_list_" + deckName, json);
        editor.putInt("query_index_" + deckName, indexCard);
        editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.query, menu);
        return true;
    }

    public void shuffleWrongs() { // This shuffles the wrongGuessed-Arraylist
        Collections.shuffle(wrongGuessed, new Random(System.nanoTime()));
    }

    public void showPic(int MultiID) { // This method loads an cardimage into imgv with a given MultiverseID.
        Picasso.with(this)
                .load(getString(R.string.image_link_1) + MultiID +  getString(R.string.image_link_2)) // This tries to load an image from a link.
                .placeholder(R.drawable.loading_image)  // We want to show a image while its loading. We load our image from the "/res/drawable" folder
                .error(R.drawable.image_not_found) // If it fails to load image we show an error-image.
                .into(imgv); // This places the image into our ImageView.
    }

    public void showFirstPic() { // We need to call this, if we start with the first item
        updateScore(); // This will update the shown score
        showHiderInstant(); // This will instantly hide a part of the card
        firstGuess = true; // The user didn't guess yet, so it's his first guess
        showPic(wrongGuessed.get(indexCard).getMultiverseid()); // We show the current picture
        hiding.bringToFront(); // We want the hider in the front
        imgCorr.bringToFront(); // And imgCorr aswell, so the users sees these
        if (Mode == 1) {
            updateChoices(); // If the users uses mode one, we need to update the text of the buttons
        }
    }

    public void showNextPic() {
        updateScore(); // This will update the shown score
        showHider(); // We fade the hider in
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() { // We add a little delay of 800 milliseconds, because of the animation
                firstGuess = true; // The user didn't guess yet, so it's his first guess
                showPic(wrongGuessed.get(indexCard).getMultiverseid()); // We show the current picture
                hiding.bringToFront(); // We want the hider in the front
                imgCorr.bringToFront(); // And imgCorr aswell, so the users sees these
                skipped = false; // The user didn't skip the current card
            }
        }, 800);
        if (Mode == 1) {
            updateChoices(); // If the users uses mode one, we need to update the text of the buttons
        }
    }

    public void checkAnswer(String txt) { // This method checks the answer
        if (txt.replaceAll("\\s+", "").equalsIgnoreCase(wrongGuessed.get(indexCard).getName().replaceAll("\\s+", ""))) {
            if (firstGuess) {
                wrongGuessed.remove(indexCard);//if he guessed it, we remove it.
            }
            else {
                indexCard++; // else we continue with the next card
            }
            if (indexCard == wrongGuessed.size()) { //If this true he's through the set
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // Add a delay of 1 second
                    public void run() {
                        setDone();
                    }
                }, 1000);
            } else {
                if (!skipped) {
                    imgCorr.setImageResource(R.drawable.correct_answer); // If the user didn't skip we show the "correct"-symbol
                    showImgCorr();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // Add a delay of 800 milliseconds
                    public void run() {
                        if (!skipped) {
                            hideImgCorr(); // We hide the symbol again
                        }
                        showNextPic(); // And show the next card
                    }
                }, 800);
            }
        } else {
            wrongAnswer(); // If the user didn't guess correctly, we call wrongAnswer()
        }
    }

    public void skip() { // Skips the card with an animation
        if (!skipped) {
            skipped = true; // The user skipped we set skipped to true
            wrongAnswer(); // The user didn't guess correctly
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    checkAnswer(wrongGuessed.get(indexCard).getName()); // after a delay of 600 milliseconds we simulate a correct answer.
                }
            }, 600);
        }
    }

    public void wrongAnswer() {
        firstGuess = false; // The user didn't guess it on the first time
        imgCorr.setImageResource(R.drawable.wrong_answer); // We want to show the "Wrong"-image
        showImgCorr(); // We show the image
        hideHider(); // We show the correct answer
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hideImgCorr(); // We hide the image after an delay of 1 second
            }
        }, 1000);
    }

    public void setDone() { // If the set is done, we show the endscreen and reshuffle the wrongGuessed-arraylist
        buildEndScreen();
        shuffleWrongs();
    }

    public void hideHider() { // Shows the name of the card fadingly
        hiding.animate().alpha(0).setDuration(600).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {
                hiding.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();
            }
        }).start();
    }

    public void showHider() { // Hides the name of the card fadingly
        hiding.animate().alpha(1).setDuration(600).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {
                hiding.animate().alpha(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
            }
        }).start();
    }

    public void showHiderInstant() { // Hides the name of the card instantly
        hiding.animate().alpha(1).setDuration(10).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {
                hiding.animate().alpha(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
            }
        }).start();
    }

    public void hideImgCorr() {
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        imgCorr.startAnimation(myFadeInAnimation); //Set animation to your ImageView
    }

    public void showImgCorr() {
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imgCorr.startAnimation(myFadeInAnimation); //Set animation to your ImageView
    }

    public void buildMenu() { // This method simply build the UI
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth(); // Get the width of the screen
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight(); // Get the height of the screen
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params; // The parameters we want to add our views
        lyt.removeAllViews(); //Clear the Board

        imgv = new ImageView(this); // Create new Imageview
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.5 * scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.02 * scrHeight); // Y-Position
        lyt.addView(imgv, params); // add it to the View

        imgCorr = new ImageView(this); // Create new Imageview
        hideImgCorr(); // Hide this ImageView
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.5 * scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.02 * scrHeight); // Y-Position
        lyt.addView(imgCorr, params); // add it to the View

        switch (Mode) {
            case 0:
                buildMode0(); // If we have Mode 0, we continue building with Mode 0
                break;
            case 1:
                buildMode1(); // else we build Mode 1
        }
    }

    public void buildMode0() { // This method builds the UI for Mode 0
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth(); // Get the width of the screen
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();  // Get the height of the screen
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params; // The parameters we want to add our views

        params = new RelativeLayout.LayoutParams((int) (0.55 * scrWidth) /*Width*/, (int) (0.03 * scrHeight)/*Height*/);
        params.leftMargin = (scrWidth / 2 - (int) (0.55 * scrWidth) / 2); // X-Position
        params.topMargin = (int) (0.045 * scrHeight); // Y-Position
        lyt.addView(hiding, params); // We add the hider

        // Add EditText like Imageview
        final EditText inputtxt = new EditText(this);
        inputtxt.setGravity(Gravity.CENTER);
        inputtxt.setHint(R.string.query_mode0_hint);
        params = new RelativeLayout.LayoutParams((int) (0.75 * scrWidth) /*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.125 * scrWidth);
        params.topMargin = (int) (0.55 * scrHeight);
        lyt.addView(inputtxt, params);
        // Add a Listener to it (so the User can simply press ENTER on the keyboard)
        inputtxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
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

        // add answer button
        Button answer = new Button(this);
        answer.setText("Answer");
        answer.setTextColor(Color.WHITE);
        answer.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.25 * scrWidth) /*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = (scrWidth / 2) - (int) (0.25 * scrWidth);
        params.topMargin = (int) (0.50 * scrHeight) + (int) (0.15 * scrHeight);
        lyt.addView(answer, params);
        answer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkAnswer(inputtxt.getText().toString());
                inputtxt.setText("");
            }
        });

        // Add skip button
        Button skip = new Button(this);
        skip.setText("Skip");
        skip.setTextColor(Color.WHITE);
        skip.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.25 * scrWidth) /*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = (scrWidth / 2);
        params.topMargin = (int) (0.50 * scrHeight) + (int) (0.15 * scrHeight);
        lyt.addView(skip, params);
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                skip();
            }
        });

        // show the score
        score = new TextView(this); // Create new Textview
        score.setGravity(Gravity.CENTER);
        score.setTextSize(36);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.78 * scrHeight); // Y-Position
        lyt.addView(score, params); // add it to the View
    }

    public void buildMode1() { // This method builds the UI for Mode 1
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth(); // Get the width of the screen
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight(); // Get the height of the screen
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params; // The parameters we want to add our views

        params = new RelativeLayout.LayoutParams((int) (0.55 * scrWidth)/*Width*/, (int) (0.16 * scrHeight)/*Height*/);
        params.leftMargin = (scrWidth / 2 - (int) (0.55 * scrWidth) / 2); // X-Position
        params.topMargin = (int) (0.32 * scrHeight); // Y-Position
        lyt.addView(hiding, params); // Add the hider

        // Add all four buttons
        Button choice0 = new Button(this);
        choice0.setText("choice0");
        choice0.setTextSize(8);
        choice0.setTextColor(Color.WHITE);
        choice0.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.48 * scrWidth)/*Width*/, (int) (0.19 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.02 * scrWidth);
        params.topMargin = (int) (0.53 * scrHeight);
        lyt.addView(choice0, params);
        choice0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickChoice(0);
            }
        });

        Button choice1 = new Button(this);
        choice1.setText("choice1");
        choice1.setTextSize(8);
        choice1.setTextColor(Color.WHITE);
        choice1.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.48 * scrWidth)/*Width*/, (int) (0.19 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.50 * scrWidth);
        params.topMargin = (int) (0.53 * scrHeight);
        lyt.addView(choice1, params);
        choice1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickChoice(1);
            }
        });

        Button choice2 = new Button(this);
        choice2.setText("choice2");
        choice2.setTextSize(8);
        choice2.setTextColor(Color.WHITE);
        choice2.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.48 * scrWidth)/*Width*/, (int) (0.19 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.02 * scrWidth);
        params.topMargin = (int) (0.71 * scrHeight);
        lyt.addView(choice2, params);
        choice2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickChoice(2);
            }
        });

        Button choice3 = new Button(this);
        choice3.setText("choice3");
        choice3.setTextSize(8);
        choice3.setTextColor(Color.WHITE);
        choice3.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.48 * scrWidth)/*Width*/, (int) (0.19 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.50 * scrWidth);
        params.topMargin = (int) (0.71 * scrHeight);
        lyt.addView(choice3, params);
        choice3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickChoice(3);
            }
        });

        // Add the score
        score = new TextView(this); // Create new Textview
        score.setGravity(Gravity.CENTER);
        score.setTextSize(0);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int) (0.78 * scrHeight); // Y-Position
        lyt.addView(score, params); // add it to the View

        choices = new ArrayList<>(); // Fill the arraylist with the buttons
        choices.add(choice0);
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);
    }

    public void onClickChoice(int nr) {
        if (choices.get(nr).getText() == wrongGuessed.get(indexCard).getText()) {
            if (firstGuess) {
                wrongGuessed.remove(indexCard);//if he guessed it, we remove it.
            }
            else {
                indexCard++; // else we continue with the next card
            }
            if (indexCard == wrongGuessed.size()) { //If this true he's through the set
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // 1 second delay
                    public void run() {
                        setDone(); // finish query
                    }
                }, 1000);
            } else {
                if (!skipped) {
                    imgCorr.setImageResource(R.drawable.correct_answer); // set "correct"-image if the user guessed it right
                    showImgCorr(); // show image
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { // 800 milliseconds delay
                    public void run() {
                        if (!skipped) {
                            hideImgCorr(); // hide image
                        }
                        showNextPic(); // continue query
                    }
                }, 800);
            }
        } else {
            wrongAnswer(); // If the user guessed it wrong, we call wrongAnswer()
        }
    }

    public void updateChoices() { // This sets the text of one button to the correct answer and the other ones to texts of random cards
        int rightOne = (int) (Math.random() * 4);
        for (int i = 0; i < choices.size(); i++) {
            if (i == rightOne) {
                choices.get(i).setText(wrongGuessed.get(indexCard).getText());
            } else {
                choices.get(i).setText(set.get((int) (Math.random() * set.size())).getText());
            }
        }

        for (int i = 0; i < choices.size() - 1; i++) {
            if (choices.get(i).getText() == choices.get(i + 1).getText()) {
                choices.get(i + 1).setText(set.get((int) (Math.random() * set.size())).getText());
            }
        }
    }

    public void updateScore() { // This updates the score text
        score.setText((set.size() - wrongGuessed.size()) + " / " + indexCard + " / " + (wrongGuessed.size() - indexCard));
    }

    public void buildEndScreen() { // This builds the end UI with the score and two buttons to restart.
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth(); // Get screen-width
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight(); // Get screen-height
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        lyt.removeAllViews(); //Clear the board
        RelativeLayout.LayoutParams params; // the parameters we need for our Views

        // Show the amount of right guessed cards
        TextView rights = new TextView(this);
        rights.setText("Right: ");
        rights.setTextSize(36);
        rights.setTextColor(getResources().getColor(R.color.colorPrimary));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.1 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight); // Y-Position
        lyt.addView(rights, params); // add it to the View

        TextView rights2 = new TextView(this);
        rights2.setText("" + (set.size() - wrongGuessed.size()));
        rights2.setTextSize(36);
        rights2.setTextColor(getResources().getColor(R.color.colorPrimary));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.4 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight); // Y-Position
        lyt.addView(rights2, params); // add it to the View

        // Show the amount of wrong guessed cards
        TextView wrongs = new TextView(this);
        wrongs.setText("Wrong: ");
        wrongs.setTextSize(36);
        wrongs.setTextColor(getResources().getColor(R.color.colorAccent));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.1 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight) + (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(wrongs, params); // add it to the View

        TextView wrongs2 = new TextView(this);
        wrongs2.setText("" + (wrongGuessed.size()));
        wrongs2.setTextSize(36);
        wrongs2.setTextColor(getResources().getColor(R.color.colorAccent));
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.1 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.4 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight) + (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(wrongs2, params); // add it to the View

        // Show the amount of total cards
        TextView total = new TextView(this);
        total.setText("Total: ");
        total.setTextSize(36);
        total.setTextColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.09 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.1 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(total, params); // add it to the View

        TextView total2 = new TextView(this);
        total2.setText("" + (set.size()));
        total2.setTextSize(36);
        total2.setTextColor(Color.BLACK);
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int) (0.09 * scrHeight))/*Height*/;
        params.leftMargin = (int) (0.4 * scrHeight); // X-Position
        params.topMargin = (int) (0.05 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight); // Y-Position
        lyt.addView(total2, params); // add it to the View

        // Show "repeat wrong guessed"-button if there are cards left
        if (wrongGuessed.size() > 0) {
            Button repWrong = new Button(this);
            repWrong.setText("Repeat wrong guessed");
            repWrong.setTextColor(Color.WHITE);
            repWrong.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            params = new RelativeLayout.LayoutParams((int) (0.90 * scrWidth)/*Width*/, (int) (0.1 * scrHeight)/*Height*/);
            params.leftMargin = (int) (0.05 * scrWidth);
            params.topMargin = (int) (0.4 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight);
            lyt.addView(repWrong, params);
            repWrong.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    buildMenu();
                    indexCard = 0;
                    showFirstPic();
                }
            });
        }

        // Show "Repeat all"-button
        Button repAll = new Button(this);
        repAll.setText("Repeat all");
        repAll.setTextColor(Color.WHITE);
        repAll.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        params = new RelativeLayout.LayoutParams((int) (0.90 * scrWidth)/*Width*/, (int) (0.1 * scrHeight)/*Height*/);
        params.leftMargin = (int) (0.05 * scrWidth);
        params.topMargin = (int) (0.5 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight) + (int) (0.1 * scrHeight);
        lyt.addView(repAll, params);
        repAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                restartAll();
            }
        });

    }

    public void restartAll() { // This restarts all. A fresh start
        wrongGuessed = (ArrayList) set.clone();
        if (!queryLand) {
            removeLands();
        }
        shuffleWrongs();
        buildMenu();
        indexCard = 0;
        showFirstPic();
    }

    public void removeLands() { // This removes all Lands of wrongGuessed
        ArrayList<Card> remove = new ArrayList<>();
        for (Card c : wrongGuessed) {
            if (c.getType().contains("Land")) {
                remove.add(c);
            }
        }
        for (Card c : remove) {
            wrongGuessed.remove(c);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed(); // If the user presses the "back"-button we close the activity
                break;
            case R.id.restart_all:
                restartAll(); // restart the query if the user presses "restart"
                break;
            case R.id.query_lands: // removes/adds lands
                queryLand = !queryLand;
                item.setChecked(queryLand);
                if (queryLand) {
                    restartAll();
                } else {
                    removeLands();
                    updateScore();
                }
                break;
            case R.id.query_revers: // Change modes
                if (set.size() < 4) {
                    break;
                }
                if (Mode == 1) {
                    Mode = 0;
                } else {
                    Mode = 1;
                }
                restartAll();
                break;
        }
        return true;
    }

    public void onBackPressed() {
        finish(); // This closes our Activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right); // We want to close it with an fancy animation.
    }
}
