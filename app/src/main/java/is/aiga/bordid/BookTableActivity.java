package is.aiga.bordid;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class BookTableActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GET_RESTAURANT_URL="http://bordid2.freeoda.com/Server/GetRestaurants.php";
    public static JSONArray jOpenHours = null; // Array of restaurants
    private OpenHours openHours = new OpenHours();

//    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/Reservation.php";
//    public static final String UPLOAD_KEY = "booking";

//    private getOpenHours task;
    private DatePicker datePicker;
    public static TextView date;
    public TextView numberOfSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    public void init() {

        // Start task to fetch information about the user
//        task = new getOpenHours();
//        task.execute((Void) null);

        downloadRestaurants();
    }

    public void openingHoursButtons(String o, String c, final int day, final int month, final int year) {

        int open = Integer.parseInt(o);
        int close = Integer.parseInt(c);

        int length = ((close - open) * 2)/100 ;
        int swing = 0;

        for(int i = 0; i < length+1; i++) {

            LinearLayout ll = (LinearLayout)findViewById(R.id.timeButtonsView);

            String openTime = Integer.toString(open);
            String temp = openTime.substring(0, 2);
            String temp2 = openTime.substring(2,4);
            openTime = temp + ":" + temp2;

            Button myButton = new Button(BookTableActivity.this);
            myButton.setText(openTime);
            final String time = openTime;
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    BookTableFragment dialogFragment = new BookTableFragment(time, day, month, year);
                    Log.d("IED", time +" "+ day +" "+ month +" " + year);
                    dialogFragment.show(fm, "Sample Fragment");
                }
            });
            ll.addView(myButton);

            if(swing == 0) {
                open += 30;
                swing = 1;
            }
            else {
                open += 70;
                swing = 0;
            }
        }
    }

    private void fillOpenHours(String jsonString) throws JSONException {

        jOpenHours = (JSONArray)(new JSONTokener(jsonString).nextValue());
        for(int i=0; i< jOpenHours.length(); i++) {
            JSONObject item = (JSONObject) jOpenHours.get(i);
            String oh = item.getString("OpeningHours");
            if(item.getString("RestaurantId").equals(RestaurantInfoActivity.id)) {
                JSONObject item2 = new JSONObject(oh);
                openHours.setSunday_lunch_open(item2.getString("SunLunchOpen"));
                openHours.setSunday_lunch_close(item2.getString("SunLunchClose"));
                openHours.setSunday_lunch_open(item2.getString("SunDinnerOpen"));
                openHours.setSunday_dinner_close(item2.getString("SunDinnerClose"));

                openHours.setMonday_lunch_open(item2.getString("MonLunchOpen"));
                openHours.setMonday_lunch_close(item2.getString("MonLunchClose"));
                openHours.setMonday_dinner_open(item2.getString("MonDinnerOpen"));
                openHours.setMonday_dinner_close(item2.getString("MonDinnerClose"));

                openHours.setTuesday_lunch_open(item2.getString("TueLunchOpen"));
                openHours.setTuesday_lunch_close(item2.getString("TueLunchClose"));
                openHours.setTuesday_dinner_open(item2.getString("TueDinnerOpen"));
                openHours.setTuesday_dinner_close(item2.getString("TueDinnerClose"));

                openHours.setWednesday_lunch_open(item2.getString("WedLunchOpen"));
                openHours.setWednesday_lunch_close(item2.getString("WedLunchClose"));
                openHours.setWednesday_lunch_open(item2.getString("WedDinnerOpen"));
                openHours.setWednesday_lunch_close(item2.getString("WedDinnerClose"));

                openHours.setThursday_lunch_open(item2.getString("ThuLunchOpen"));
                openHours.setThursday_lunch_open(item2.getString("ThuLunchClose"));
                openHours.setThursday_dinner_open(item2.getString("ThuDinnerOpen"));
                openHours.setThursday_dinner_close(item2.getString("ThuDinnerClose"));

                openHours.setFriday_lunch_open(item2.getString("FriLunchOpen"));
                openHours.setFriday_lunch_close(item2.getString("FriLunchClose"));
                openHours.setFriday_lunch_open(item2.getString("FriDinnerOpen"));
                openHours.setFriday_dinner_close(item2.getString("FriDinnerClose"));

                openHours.setSaturday_lunch_open(item2.getString("SatLunchOpen"));
                openHours.setSaturday_lunch_close(item2.getString("SatLunchClose"));
                openHours.setSaturday_lunch_open(item2.getString("SatDinnerOpen"));
                openHours.setSaturday_dinner_close(item2.getString("SatDinnerClose"));
            }
        }
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(datePicker.getYear(), datePicker.getDayOfMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);


                LinearLayout ll = (LinearLayout)findViewById(R.id.timeButtonsView);
                ll.removeAllViews();

                if (day == 1) {
                    openingHoursButtons(openHours.getSunday_lunch_close(), openHours.getSunday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getSunday_dinner_open(), openHours.getSunday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 2) {
                    openingHoursButtons(openHours.getMonday_lunch_close(), openHours.getMonday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getMonday_dinner_open(), openHours.getMonday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 3) {
                    openingHoursButtons(openHours.getTuesday_lunch_close(), openHours.getTuesday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getTuesday_dinner_open(), openHours.getTuesday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 4) {
                    openingHoursButtons(openHours.getWednesday_lunch_close(), openHours.getWednesday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getWednesday_dinner_open(), openHours.getWednesday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 5) {
                    openingHoursButtons(openHours.getThursday_lunch_close(), openHours.getThursday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getThursday_dinner_open(), openHours.getThursday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 6) {
                    openingHoursButtons(openHours.getFriday_lunch_close(), openHours.getFriday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getFriday_dinner_open(), openHours.getFriday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
                if (day == 7) {
                    openingHoursButtons(openHours.getSaturday_lunch_close(), openHours.getSaturday_lunch_open(), dayOfMonth, monthOfYear, year);
                    openingHoursButtons(openHours.getSaturday_dinner_open(), openHours.getSaturday_dinner_close(), dayOfMonth, monthOfYear, year);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // decrement seat quantity
            case R.id.decrement:
                int dTemp = Integer.parseInt((String) numberOfSeats.getText());
                if(dTemp != 1) dTemp -= 1;
                numberOfSeats.setText(Integer.toString(dTemp));
                break;
            // increment seat quantity
            case R.id.increment:
                int iTemp = Integer.parseInt((String) numberOfSeats.getText());
                if(iTemp < 10) iTemp += 1;
                numberOfSeats.setText(Integer.toString(iTemp));
                break;
        }
    }

    private void downloadRestaurants() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BookTableActivity.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d("IED", result);
                loading.dismiss();
                try {
                    fillOpenHours(result);
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

    // Get opening hours for restaurant
//    public class getOpenHours extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            Service service = new Service(); // Service class is used to validate username and password
//
//            String updateString = RestaurantInfoActivity.id;
//
//            Log.d("IED", "updateString or id: " +  updateString);
//
//            HashMap<String,String> data = new HashMap<>();
//            data.put(UPLOAD_KEY, updateString); // UPLOAD_KEY = "username", keyword for server POST request
//
//            String result = service.sendPostRequest(UPLOAD_URL, data);
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(final String s) {
//            Log.d("IED", s);
//        }
//    }

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
}
