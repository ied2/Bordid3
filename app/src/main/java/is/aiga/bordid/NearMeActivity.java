package is.aiga.bordid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NearMeActivity extends AppCompatActivity {

    public static final String GET_RESTAURANT_URL="http://bordid2.freeoda.com/Server/GetRestaurants.php";
    public static RecyclerView recyclerView;
    private VAdapter adapter;
    public static JSONArray restaurants = null; // Array of restaurants
    public static List<Restaurant> mRestaurants;
    private static List<Restaurant> mFilteredRestaurants;
    private String mSearchQuery;
    private EditText mSearchEt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.listview_restaurant);
        setContentView(R.layout.app_bar_nearme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.nearme_toolbar);

        setSupportActionBar(toolbar);
        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Download all restaurants
        downloadRestaurants();
    }

    private void downloadRestaurants() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NearMeActivity.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d("IED", result);
                loading.dismiss();
                try {
                    init_restaurants(result);
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
        gu.execute(GET_RESTAURANT_URL);
    }

    // After receiving all data we extract information and put it in arrays
    public void init_restaurants(String jsonString) throws Exception {

        restaurants = (JSONArray)(new JSONTokener(jsonString).nextValue());

        Log.d("IED", jsonString);

        String[] rId = new String[restaurants.length()];
        String[] rName = new String[restaurants.length()];
        String[] rImage = new String[restaurants.length()];
        String[] rphoneNumber = new String[restaurants.length()];
        String[] raddress = new String[restaurants.length()];
        String[] rwebsite = new String[restaurants.length()];
        String[] rDescription = new String[restaurants.length()];
        boolean[] open = new boolean[restaurants.length()];

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        boolean s = false;

        for(int i=0; i<restaurants.length(); i++) {
            JSONObject item = (JSONObject) restaurants.get(i);
            String r = item.getString("RestaurantId");
            String n = item.getString("RestName");
            String im = item.getString("RestImage");
            String p = item.getString("RestPhoneNumber");
            String a = item.getString("RestAddress");
            String w = item.getString("RestWebsite");
            String d = item.getString("RestDescription");

            String oh = item.getString("OpeningHours");
            JSONObject openHours = new JSONObject(oh);
            if(day == 1) {
                s = isOpen(openHours.getString("SunLunchOpen"), openHours.getString("SunLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("SunDinnerOpen"), openHours.getString("SunDinnerClose"));
            }
            if(day == 2) {
                s = isOpen(openHours.getString("MonLunchOpen"), openHours.getString("MonLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("MonDinnerOpen"), openHours.getString("MonDinnerClose"));
            }
            if(day == 3) {
                s = isOpen(openHours.getString("TueLunchOpen"), openHours.getString("TueLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("TueDinnerOpen"), openHours.getString("TueDinnerClose"));
            }
            if(day == 4) {
                s = isOpen(openHours.getString("WedLunchOpen"), openHours.getString("WedLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("WedDinnerOpen"), openHours.getString("WedDinnerClose"));
            }
            if(day == 5) {
                s = isOpen(openHours.getString("TueLunchOpen"), openHours.getString("TueLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("TueDinnerOpen"), openHours.getString("TueDinnerClose"));
            }
            if(day == 6) {
                s = isOpen(openHours.getString("FriLunchOpen"), openHours.getString("FriLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("FriDinnerOpen"), openHours.getString("FriDinnerClose"));
            }
            if(day == 7) {
                s = isOpen(openHours.getString("SatLunchOpen"), openHours.getString("SatLunchClose"));
                if(!s)
                    s = isOpen(openHours.getString("SatDinnerOpen"), openHours.getString("SatDinnerClose"));
            }
            rId[i] = r;
            rName[i] = n;
            rImage[i] = im;
            rphoneNumber[i] = p;
            raddress[i] = a;
            rwebsite[i] = w;
            rDescription[i] = d;
            open[i] = s;
        }
        populate(rId, rName, rImage, rphoneNumber, raddress, rwebsite, rDescription, open);
    }

    private boolean isOpen(String open, String close) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour * 100;

        int a = Integer.parseInt(open);
        int b = Integer.parseInt(close);

        return a <= hour && b >= hour;

    }

    // Populate recycleView list with our restaurants names and images
    private void populate(final String[] rId, final String[] rName, final String[] rImage, final String[] rphoneNumber, final String[] raddress, final String[] rwebsite, final String[] rDescription, final boolean[] open) {
        recyclerView = (RecyclerView) this.findViewById(R.id.recycle_list);
        adapter = new VAdapter(this, getData(rId, rName, rImage, rphoneNumber, raddress, rwebsite, rDescription, open));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchQuery = "";
        initSearchBar(mSearchQuery);
    }

    public static List<Restaurant> getData(String[] rId, String[] rName, String[] rImage, String[] rphoneNumber, String[] raddress, String[] rwebsite, String[] rDescription, boolean[] open) {
        mRestaurants = new ArrayList<>();
        String[] price = {"2000", "2500", "3000", "3500", "4000"};

        for(int i = 0; i < rName.length; i++) {
            Restaurant current = new Restaurant();
            current.setId(rId[i]);
            current.setName(rName[i]);
            current.setLogo(rImage[i]);
            current.setPhoneNumber(rphoneNumber[i]);
            current.setAddress(raddress[i]);
            current.setWebsite(rwebsite[i]);
            current.setDescription(rDescription[i]);
            current.setOpen(open[i]);
            Random random = new Random();
            int r = random.nextInt(4 - 0 + 1) + 0;
            current.setPrice(price[r]);
            mRestaurants.add(current);
        }

        mFilteredRestaurants = mRestaurants;

        return mRestaurants;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initSearchBar(String queryText) {
        // Search edit text field setup.
        mSearchEt = (EditText) findViewById(R.id.etSearch);
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setText(queryText);

        // Change search icon accordingly.
//        mSearchAction.setIcon(mIconCloseSearch);          // set the close icon
    }

    /**
     * Responsible for handling changes in search edit text.
     */
    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
            mSearchQuery = mSearchEt.getText().toString();
            mFilteredRestaurants = performSearch(mRestaurants, mSearchQuery);

            // Updates the list
            adapter.update(mFilteredRestaurants);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    /**
     * Goes through the given list and filters it according to the given query.
     */
    private List<Restaurant> performSearch(List<Restaurant> restaurants, String query) {
        // First we split the query so that we're able
        // to search word by word (in lower case).
        String[] queryByWords = query.toLowerCase().split("\\s+");

        // Empty list to fill with matches.
        List<Restaurant> restaurantsFiltered = new ArrayList<Restaurant>();

        // Go through initial releases and perform search.
        for (Restaurant movie : mRestaurants) {

            // Content to search through (in lower case).
            String content = (
                    movie.getName() + " " +
                            movie.getAddress() + " " +
                            String.valueOf(movie.getDescription())
            ).toLowerCase();

            for (String word : queryByWords) {

                // There is a match only if all of the words are contained.
                int numberOfMatches = queryByWords.length;

                // All query words have to be contained,
                // otherwise the release is filtered out.
                if (content.contains(word)) {
                    numberOfMatches--;
                } else {
                    break;
                }

                // They all match.
                if (numberOfMatches == 0) {
                    restaurantsFiltered.add(movie);
                }
            }
        }

        return restaurantsFiltered;
    }
}