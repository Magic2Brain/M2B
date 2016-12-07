package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import m2b.magic2brain.com.magic2brain.R;

public class QueryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        showPic(410017);
        buildMenu();
    }

    public void showPic(int MultiID){
        ImageView b = (ImageView) findViewById(R.id.imageViewQuiz);
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.white_loader)
                .error(R.drawable.white_notfound)
                .into(b);
    }

    public void showPic(String MultiID){
        ImageView b = (ImageView) findViewById(R.id.imageViewQuiz);
        Picasso.with(this)
                .load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card")
                .placeholder(R.drawable.white_loader)
                .error(R.drawable.white_notfound)
                .into(b);
    }

    public void checkAnswer(String txt){
        showPic(txt);
    }

    public void buildMenu(){
        int scrWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();

        ImageView b = (ImageView) findViewById(R.id.imageViewQuiz);
        setParams(b,0,0,scrWidth,(int)(0.50*scrHeight));

        final EditText inputtxt = (EditText) findViewById(R.id.cardname_input);
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
        setParams(inputtxt,0,(int)(0.50*scrHeight),scrWidth,(int)(0.15*scrHeight));

        Button answer = (Button) findViewById(R.id.answer_button);
        answer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkAnswer(inputtxt.getText().toString());
            }
        });
        setParams(answer,(scrWidth/2)-(int)(0.25*scrWidth),(int)(0.50*scrHeight)+(int)(0.15*scrHeight),(int)(0.25*scrWidth),(int)(0.1*scrHeight));

        Button skip = (Button) findViewById(R.id.skip_button);
        setParams(skip,(scrWidth/2),(int)(0.50*scrHeight)+(int)(0.15*scrHeight),(int)(0.25*scrWidth),(int)(0.1*scrHeight));
    }

    public void setParams(View v, int x, int y, int w, int h){
        v.getLayoutParams().width = w;
        v.getLayoutParams().height = h;
        //v.setX(x);
        //v.setY(y);
    }
}
