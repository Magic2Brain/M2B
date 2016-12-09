package m2b.magic2brain.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import m2b.magic2brain.com.magic2brain.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
