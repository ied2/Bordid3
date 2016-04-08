package is.aiga.bordid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {


    public static TextView name, address, phoneNumber, distance, url, description;
    public static RatingBar rating;
    public Button buttonOrderTable;
    public ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TextViews found
        name = (TextView) findViewById(R.id.RestName);
        phoneNumber = (TextView) findViewById(R.id.RestPhone);
        url = (TextView) findViewById(R.id.RestUrl);
        description = (TextView) findViewById(R.id.About);
        address = (TextView) findViewById(R.id.RestAddress);
        image = (ImageView) findViewById(R.id.RestImage);

        // RatingView
        rating = (RatingBar) findViewById(R.id.ratingBar);

        // Buttons initialized
        buttonOrderTable = (Button) findViewById(R.id.order_table);
        buttonOrderTable.setOnClickListener(this);

        init();
    }

    public void init() {
        Button button = (Button) findViewById(R.id.order_table);
        button.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String logo = extras.getString("logo");

            // Filling TextViews with user information
            name.setText(extras.getString("name"));
            address.setText(extras.getString("address"));
            phoneNumber.setText(extras.getString("phoneNumber"));
            url.setText(extras.getString("url"));
            description.setText(extras.getString("description"));

            if (logo.equals("999")) Picasso.with(this).load(R.drawable.upload_image).fit().into(image);
            else Picasso.with(this).load(logo).fit().into(image);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // Check if user is logged in, if not, tell him to login
            case R.id.order_table:
                orderTable();
                break;
        }
    }

    private void orderTable() {
        if(SaveSharedPreference.getUserName(this).length() < 1){
            Toast.makeText(InfoActivity.this, "Login Please", Toast.LENGTH_SHORT).show();
        }
        // else if the user is logged in we go to the booking activity
        else {
            Intent i = new Intent(this, BookTableActivity.class);
            startActivity(i);
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
