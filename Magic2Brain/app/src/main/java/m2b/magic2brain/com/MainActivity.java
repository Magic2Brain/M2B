package m2b.magic2brain.com;

//  Standard Imports
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import m2b.magic2brain.com.magic2brain.R;

import android.view.View;
import android.widget.Button; // Imports Buttons

public class MainActivity extends AppCompatActivity {

    @Override // Just a reminder... You can delete this line
    protected void onCreate(Bundle savedInstanceState) { // The Activity starts here
        super.onCreate(savedInstanceState); // Don't worry about this line. JUST DO IT
        setContentView(R.layout.activity_main); // We set the View to our designed xml
        Button b = (Button) findViewById(R.id.button); // This is how you get the Button with the ID
        b.setOnClickListener(new View.OnClickListener() { // Add a listener
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QueryActivity.class); // To start a new activity we create an Intent with the current object and the target class
                startActivity(intent); // This starts the new activity
            }
        });
    }
}
