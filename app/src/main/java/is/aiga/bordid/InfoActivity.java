package is.aiga.bordid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {


    public static TextView name, address, phoneNumber, distance, url, about;
    public static RatingBar rating;
    public Button buttonOrderTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TextViews found
        name = (TextView) findViewById(R.id.RestName);
        address = (TextView) findViewById(R.id.RestAddress);
        phoneNumber = (TextView) findViewById(R.id.RestPhone);
        distance = (TextView) findViewById(R.id.RestDistance);
        url = (TextView) findViewById(R.id.RestUrl);
        about = (TextView) findViewById(R.id.aboutRest);

        // Filling TextViews with user information
        name.setText(SaveSharedPreference.getRestaurantName(this));
        address.setText(SaveSharedPreference.getAddress(this));
        phoneNumber.setText(SaveSharedPreference.getRestaurantPhoneNumber(this));
        //distance.setText(SaveSharedPreference.getRestaurantDistance(this));
        url.setText(SaveSharedPreference.getUrl(this));
        //about.setText(SaveSharedPreference.getRestaurantDiscription(this));

        // RatingView
        rating = (RatingBar) findViewById(R.id.ratingBar);

        // Buttons initialized
        buttonOrderTable = (Button) findViewById(R.id.order_table);
        buttonOrderTable.setOnClickListener(this);

        init();
    }

    public void init() {
        Button button = (Button)findViewById(R.id.order_table);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // Check if user is logged in, if not, tell him to login
            case R.id.order_table:
                if(SaveSharedPreference.getUserName(this).length() < 1){
                    Toast.makeText(InfoActivity.this, "Login Please", Toast.LENGTH_SHORT).show();
                }
                // else if the user is logged in we go to the booking activity
                else {
                    Intent i = new Intent(this, BookTableActivity.class);
                    startActivity(i);
                }
                break;
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
