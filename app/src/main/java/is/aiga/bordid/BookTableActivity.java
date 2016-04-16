package is.aiga.bordid;

import android.app.FragmentManager;
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
import java.util.HashMap;

public class BookTableActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/GetRestaurantOpenHours.php";
    public static final String UPLOAD_KEY = "restaurant";

//    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/Reservation.php";
//    public static final String UPLOAD_KEY = "booking";

    private getOpenHours task;
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
        task = new getOpenHours();
        task.execute((Void) null);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(datePicker.getYear(), datePicker.getDayOfMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(BookTableActivity.this, "DateChanged", Toast.LENGTH_SHORT).show();

                // Open hours for this date
                OpenHours oh = new OpenHours();

                String monLunchOpen = oh.getMonday_lunch_open();
                String monLunchClose = oh.getMonday_lunch_close();

                openingHoursButtons(monLunchOpen, monLunchClose);
            }
        });
    }

    public void openingHoursButtons(String o, String c) {

        int open = Integer.parseInt(o);
        int close = Integer.parseInt(c);

        int length = ((close - open) * 2)/100 ;
        int swing = 0;

        LinearLayout ll = (LinearLayout)findViewById(R.id.timeButtonsView);
        ll.removeAllViews();

        for(int i = 0; i < length+1; i++) {

            String openTime = Integer.toString(open);

            Button myButton = new Button(BookTableActivity.this);
            myButton.setText(openTime);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    BookTableFragment dialogFragment = new BookTableFragment();
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

    // Get opening hours for restaurant
    public class getOpenHours extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String updateString = InfoActivity.id;

            Log.d("IED", "updateString" +  updateString);

            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, updateString); // UPLOAD_KEY = "username", keyword for server POST request

            String result = service.sendPostRequest(UPLOAD_URL, data);

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            Log.d("IED", s);
        }
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
}
