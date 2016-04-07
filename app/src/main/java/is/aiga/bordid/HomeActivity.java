package is.aiga.bordid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final String GET_IMAGE_URL="http://bordid2.freeoda.com/Server/GetRestaurants.php";
    public List<Restaurant> restaurants;
    private Random randomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restaurants = new ArrayList<>();
        randomGenerator = new Random();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        isLoggedIn();

        initialize();

        downloadRestaurants();
    }

    private void initialize() {

        // Setup up click listeners
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton account = (FloatingActionButton) findViewById(R.id.fab);
        account.setOnClickListener(this);

        TextView nearMe = (TextView)findViewById(R.id.near_me);
        nearMe.setOnClickListener(this);

        TextView surprise = (TextView)findViewById(R.id.surprise);
        surprise.setOnClickListener(this);
    }

    private void downloadRestaurants() {
        class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(HomeActivity.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                try {
                    populate_restaurant(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }

    // After receiving all data we extract information and put it in arrays
    public void populate_restaurant(String jsonString) throws Exception {

        JSONArray jsonRestaurants = (JSONArray)(new JSONTokener(jsonString).nextValue());

        Log.d("IED", jsonString);

        for(int i=0; i<jsonRestaurants.length(); i++) {
            Restaurant rest = new Restaurant();
            JSONObject item = (JSONObject) jsonRestaurants.get(i);
            String n = item.getString("RestName");
            rest.setName(n);
            String im = item.getString("RestImage");
            rest.setLogo(im);
            String p = item.getString("RestPhoneNumber");
            rest.setPhoneNumber(p);
            String a = item.getString("RestAddress");
            rest.setAddress(a);
            String w = item.getString("RestWebsite");
            rest.setWebsite(w);
            String d = item.getString("RestDescription");
            rest.setDescription(d);
            restaurants.add(rest);
        }
    }

    // Check if user is logged in, if so display the user name on screen
    private void isLoggedIn() {
        if(SaveSharedPreference.getUserName(this).length() > 0) {
            Toast.makeText(HomeActivity.this, "Logged in as: " + SaveSharedPreference.getUserName(this), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Click account button
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

            // Start activity to find a random restaurants
            case R.id.surprise:
                surpriseMe();
                break;
        }
    }

    private void surpriseMe() {
        int index = randomGenerator.nextInt(restaurants.size());
        Restaurant rest = restaurants.get(index);
        Intent i2 = new Intent(HomeActivity.this, InfoActivity.class);
        i2.putExtra("name", rest.getName());
        i2.putExtra("logo", rest.getLogo());
        i2.putExtra("phoneNumber", rest.getPhoneNumber());
        i2.putExtra("address", rest.getAddress());
        i2.putExtra("website", rest.getWebsite());
        i2.putExtra("description", rest.getDescription());
        startActivity(i2);
    }

    public Restaurant randomItem()
    {
        int index = randomGenerator.nextInt(restaurants.size());
        return restaurants.get(index);
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
