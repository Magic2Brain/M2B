package m2b.magic2brain.com;

//  Standard Imports
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import m2b.magic2brain.com.magic2brain.R;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button; // Imports Buttons
import android.widget.ListView;

public class MainActivity extends Activity {
    private String[] mSideBarItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSideBarItems = getResources().getStringArray(R.array.sidebar_items);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mSideBarItems));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.common_google_signin_btn_icon_dark_normal,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                String mTitle = "ActionTitle";
                getActionBar().setTitle(mTitle);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                String mDrawerTitle = "MainDrawer";
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        @Override
        protected void onPostCreate (Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }

        /*

        @Override
        public void onConfigurationChanged (Configuration newConfig){
            super.onConfigurationChanged(newConfig);
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Pass the event to ActionBarDrawerToggle, if it returns
            // true, then it has handled the app icon touch event
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            // Handle your other action bar items...

            return super.onOptionsItemSelected(item);
        }*/
    }
}