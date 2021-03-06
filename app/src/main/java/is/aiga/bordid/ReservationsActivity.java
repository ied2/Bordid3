package is.aiga.bordid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReservationsActivity extends AppCompatActivity {

    public static final String GET_RESERVATION_URL="http://bordid2.freeoda.com/Server/GetUserReservations.php";
    public static final String UPLOAD_KEY = "username";
    public static RecyclerView recyclerView;
    private RAdapter adapter;
    private static JSONArray reservations = null; // Array of restaurants
    private static List<Reservation> mReservations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_reservations);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Download all restaurants
        downloadReservations();
    }

    private void downloadReservations() {
        class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ReservationsActivity.this,"Loading...","Please Wait...",true,true);
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
                try {
                    Service service = new Service();

                    HashMap<String,String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, SaveSharedPreference.getUserId(ReservationsActivity.this)); // UPLOAD_KEY = "username", keyword for server POST request

                    String result = service.sendPostRequest(GET_RESERVATION_URL, data); // Posts a String to server, String created by HashMap

                    return result;

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute();
    }

    // After receiving all data we extract information and put it in arrays
    public void init_restaurants(String jsonString) throws Exception {

        reservations = (JSONArray)(new JSONTokener(jsonString).nextValue());

        String[] reservationId = new String[reservations.length()];
        String[] custId = new String[reservations.length()];
        String[] restaurantId = new String[reservations.length()];
        String[] numSeats = new String[reservations.length()];
        String[] reservationDate = new String[reservations.length()];
        String[] restName = new String[reservations.length()];
        String[] address = new String[reservations.length()];
        String[] city = new String[reservations.length()];
        String[] zip = new String[reservations.length()];
        String[] phone = new String[reservations.length()];
        String[] website = new String[reservations.length()];
        String[] email = new String[reservations.length()];

        for(int i=0; i<reservations.length(); i++) {
            JSONObject item = (JSONObject) reservations.get(i);
            String r = item.getString("ReservationId");
            String n = item.getString("CustId");
            String im = item.getString("RestaurantId");
            String p = item.getString("ReservedSeats");
            String a = item.getString("ReservationDate");

            String rest = item.getString("Restaurant");
            JSONObject item2 = new JSONObject(rest);
            String w = item2.getString("RestName");
            String c = item2.getString("RestAddress");
            String o = item2.getString("RestZip");
            String m = item2.getString("RestCity");
            String u = item2.getString("RestPhoneNumber");
            String q = item2.getString("RestWebsite");
            String j = item2.getString("RestEmail");

            reservationId[i] = r;
            custId[i] = n;
            restaurantId[i] = im;
            numSeats[i] = p;
            reservationDate[i] = a;
            restName[i] = w;
            address[i] = c;
            zip[i] = o;
            city[i] = m;
            phone[i] = u;
            website[i] = q;
            email[i] = j;
        }
        populate(reservationId, custId, restaurantId, numSeats, reservationDate, restName, address, zip, city, phone, website,email);
    }

    // Populate recycleView list with our restaurants names and images
    private void populate(final String[] reservationId, final String[] custId, final String[] restaurantId, final String[] numSeats, final String[] reservationDate, final String[] restName, final String[] address, final String[] zip, final String[] city, final String[] phone, final String[] website, final String[] email) {
        recyclerView = (RecyclerView) this.findViewById(R.id.recycle_list);
        adapter = new RAdapter(this, getData(reservationId, custId, restaurantId, numSeats, reservationDate, restName, address, zip, city, phone, website,email));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static List<Reservation> getData(String[] reservationId, String[] custId, String[] restaurantId, String[] numSeats, String[] reservationDate, String[] restName, String[] address, String[] zip, String[] city, String[] phone, String[] website, String[] email) {
        mReservations = new ArrayList<>();

        for(int i = 0; i < reservationId.length; i++) {
            Reservation current = new Reservation();
            current.setReservationId(reservationId[i]);
            current.setCustId(custId[i]);
            current.setRestaurantId(restaurantId[i]);
            current.setNumSeats(Integer.parseInt(numSeats[i]));
            current.setReservationDate(reservationDate[i]);

            Restaurant restaurant = new Restaurant();
            restaurant.setId(restaurantId[i]);
            restaurant.setName(restName[i]);
            restaurant.setPhoneNumber(phone[i]);
            restaurant.setAddress(address[i]);
            restaurant.setWebsite(website[i]);
            restaurant.setZip(Integer.parseInt(zip[i]));
            restaurant.setCity(city[i]);
            restaurant.setEmail(email[i]);

            current.setRestaurant(restaurant);

            mReservations.add(current);
        }

        return mReservations;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
//                this.onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
