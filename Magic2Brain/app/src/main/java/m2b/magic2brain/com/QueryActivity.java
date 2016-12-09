package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import m2b.magic2brain.com.magic2brain.R;

public class QueryActivity extends AppCompatActivity {
    ImageView imgv;

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildMenu();
        showPic(410017);
    }

    public void showPic(int MultiID){
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.ic_timer)
                .error(R.drawable.ic_dehaze)
                .into(imgv);
    }

    public void showPic(String MultiID){
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.ic_timer)
                .error(R.drawable.ic_dehaze)
                .into(imgv);
    }

    public void checkAnswer(String txt){
        showPic(txt);
    }

    public void buildMenu(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout lyt = (RelativeLayout) findViewById(R.id.query_absolute);
        RelativeLayout.LayoutParams params;
        int id = 1;

        imgv  = new ImageView(this);
        params = new RelativeLayout.LayoutParams(scrWidth, (int)(0.45*scrHeight));
        params.leftMargin = 0;
        params.topMargin = (int)(0.05*scrHeight);
        lyt.addView(imgv, params);

        final EditText inputtxt = new EditText(this);
        inputtxt.setHint("What is the name of this card?");
        params = new RelativeLayout.LayoutParams((int)(0.75*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (int)(0.125*scrWidth);
        params.topMargin = (int)(0.55*scrHeight);
        lyt.addView(inputtxt, params);
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
            public void onClick(View view) {
                checkAnswer(inputtxt.getText().toString());
            }
        });

        Button skip = new Button(this);
        skip.setText("Skip");
        params = new RelativeLayout.LayoutParams((int)(0.25*scrWidth), (int)(0.1*scrHeight));
        params.leftMargin = (scrWidth/2);
        params.topMargin = (int)(0.50*scrHeight)+(int)(0.15*scrHeight);
        lyt.addView(skip, params);
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
