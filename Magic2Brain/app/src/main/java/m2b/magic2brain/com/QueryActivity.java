package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import m2b.magic2brain.com.magic2brain.R;

public class QueryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        final EditText inputtxt = (EditText) findViewById(R.id.cardname_input);
        inputtxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                inputtxt.setText("");
            }
        });

        Button answer = (Button) findViewById(R.id.answer_button);
        answer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showPic(410018);
            }
        });

        showPic(410017);
    }

    public void showPic(int MultiID){
        ImageView b = (ImageView) findViewById(R.id.imageViewQuiz);
        Picasso.with(this).load("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + MultiID + "&type=card").into(b);
    }

}
