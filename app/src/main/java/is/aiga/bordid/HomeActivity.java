package is.aiga.bordid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Check if user is logged in, if so display the user name on screen
        if(SaveSharedPreference.getUserName(this).length() > 0) {
            Toast.makeText(HomeActivity.this, "Logged in as: " + SaveSharedPreference.getUserName(this), Toast.LENGTH_LONG).show();
        }

        initialize();
    }

    private void initialize() {

        // Setup up click listeners
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton account = (FloatingActionButton) findViewById(R.id.fab);
        account.setOnClickListener(this);

        TextView nearMe = (TextView)findViewById(R.id.near_me);
        nearMe.setOnClickListener(this);

        TextView surprise = (TextView)findViewById(R.id.surprise);
        surprise.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                // Check if user is logged in
                if (SaveSharedPreference.getUserName(HomeActivity.this).length() > 0) {
                    Intent pi = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(pi);
                } else {
                    // If user is not logged in, redirect to login activity
                    Intent li = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(li);
                }
                break;

                // Start activity to find restaurants near me
            case R.id.near_me:
                Intent i = new Intent(HomeActivity.this, NearMeActivity.class);
                startActivity(i);
                break;

                // Start activity to find a random restaurant
            case R.id.surprise:
                Intent i2 = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(i2);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
