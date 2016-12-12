package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import m2b.magic2brain.com.magic2brain.R;

public class QueryActivity extends AppCompatActivity {
    private ImageView imgv;
    private Toolbar hiding;
    private ArrayList<Card> set;
    private ArrayList<Card> wrongGuessed;
    private int indexCard;

    protected void onCreate(Bundle savedInstanceState) {
        // Build UI
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildMenu();
        // Prepare Query
        set = new ArrayList<>(); // TODO: Load set from a Intent (or get it elsewhere)
        set.add(new Card(410017,"Brain in a Jar"));
        set.add(new Card(418367,"Aerial Responder"));
        set.add(new Card(418607,"Aether Hub"));
        wrongGuessed = set;
        Collections.shuffle(wrongGuessed, new Random(System.nanoTime()));
        indexCard = 0;
        showNextPic();
    }

    public void showPic(int MultiID){
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.ic_timer)
                .error(R.drawable.ic_dehaze)
                .into(imgv);
    }

    public void showNextPic(){
        showPic(wrongGuessed.get(indexCard).getMultiverseid());
        hiding.bringToFront();
    }

    public void checkAnswer(String txt){
        if(txt.replaceAll("\\s+","").equalsIgnoreCase(wrongGuessed.get(indexCard).getName().replaceAll("\\s+",""))) {
            showHider();
           // wrongGuessed.remove(indexCard);
            if(indexCard == wrongGuessed.size()){
                setDone();
            } else {
                showNextPic();
            }
        } else {
           wrongAnswer();
        }
    }

    public void skip(){
        wrongAnswer();
    }

    public void wrongAnswer(){
       hideHider();
    }

    public void setDone(){

    }

    public void hideHider(){
        hiding.animate().alpha(0).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {hiding.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();}}).start();
    }

    public void showHider(){
        hiding.animate().alpha(1).setDuration(100).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            public void run() {hiding.animate().alpha(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();}}).start();
    }

    public void buildMenu(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute); // Get the View of the XML
        RelativeLayout.LayoutParams params;

        hiding = (Toolbar)findViewById(R.id.toolbar_query);
        hiding.getLayoutParams().width = (int)(0.5*scrWidth);
        hiding.setY((int)(0.07*scrHeight));
        hiding.setX((scrWidth/2 - hiding.getLayoutParams().width/2));
        hiding.setMinimumHeight((int)(0.025*scrHeight));

        imgv  = new ImageView(this); // Create new Imageview
        params = new RelativeLayout.LayoutParams(scrWidth /*Width*/, (int)(0.45*scrHeight))/*Height*/;
        params.leftMargin = 0; // X-Position
        params.topMargin = (int)(0.05*scrHeight); // Y-Position
        lyt.addView(imgv, params); // add it to the View

        // Add EditText like Imageview
        final EditText inputtxt = new EditText(this);
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
        params = new RelativeLayout.LayoutParams((int)(0.25*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (scrWidth/2)-(int)(0.25*scrWidth);
        params.topMargin = (int)(0.50*scrHeight)+(int)(0.15*scrHeight);
        lyt.addView(answer, params);
        answer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {checkAnswer(inputtxt.getText().toString());}
        });

        Button skip = new Button(this);
        skip.setText("Skip");
        params = new RelativeLayout.LayoutParams((int)(0.25*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (scrWidth/2);
        params.topMargin = (int)(0.50*scrHeight)+(int)(0.15*scrHeight);
        lyt.addView(skip, params);
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {skip();}
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
