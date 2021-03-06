package is.aiga.bordid;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static ImageView profile_image;
    public Button buttonLogout, buttonConfigure, buttonRestaurantConfigure, buttonReservations;
    public static TextView name, email, phoneNumber, restaurant_name;
    public static int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TextViews found
        name = (TextView) findViewById(R.id.profile_name);
        email = (TextView) findViewById(R.id.profile_email);
        phoneNumber = (TextView) findViewById(R.id.profile_phoneNumber);
        restaurant_name = (TextView) findViewById(R.id.profile_restaurant_name);

        // Filling TextViews with user information
        name.setText(SaveSharedPreference.getName(this));
        email.setText(SaveSharedPreference.getEmail(this));
        phoneNumber.setText(SaveSharedPreference.getPhoneNumber(this));
        restaurant_name.setText(SaveSharedPreference.getRestaurantName(this));

        // Checking if user is not a owner we hide restaurant change view
        if(SaveSharedPreference.getRestaurantName(this).length() == 0) {
            View view = findViewById(R.id.restaurant_layout);
            view.setVisibility(View.GONE);
        }

        // Buttons initialized
        buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);
        buttonConfigure = (Button) findViewById(R.id.profile_configure);
        buttonConfigure.setOnClickListener(this);
        buttonReservations = (Button) findViewById(R.id.profile_reservations);
        buttonReservations.setOnClickListener(this);
        buttonRestaurantConfigure = (Button) findViewById(R.id.profile_configure_restaurant);
        buttonRestaurantConfigure.setOnClickListener(this);
        profile_image = (ImageView) findViewById(R.id.profile_photo);
        if(!SaveSharedPreference.getProfileImage(this).equals("")) Picasso.with(this).load(SaveSharedPreference.getProfileImage(this)).into(profile_image);
        Log.d("IED", SaveSharedPreference.getProfileImage(this));
        profile_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Logout
            case R.id.button_logout:
                logout();
                break;

            // Start a dialog to configure the profile
            case R.id.profile_configure:
                FragmentManager fm = getFragmentManager();
                UserConfigureFragment dialogFragment = new UserConfigureFragment();
                dialogFragment.show(fm, "Sample Fragment");
                break;

            // Start a dialog to configure the profile
            case R.id.profile_configure_restaurant:
                FragmentManager fm2 = getFragmentManager();
                RestaurantConfigureFragment dialogFragment2 = new RestaurantConfigureFragment();
                dialogFragment2.show(fm2, "Sample Fragment");
                break;

            // Start a dialog to change profile image
            case R.id.profile_photo:
                FragmentManager fm3 = getFragmentManager();
                UploadPhotoFragment dialogFragment3 = new UploadPhotoFragment();
                dialogFragment3.show(fm3, "Sample Fragment");
                break;

            // Start an activity to display reservations for user
            case R.id.profile_reservations:
                Intent i = new Intent(ProfileActivity.this, ReservationsActivity.class);
                startActivity(i);
                break;

            default:
        }
    }

    private void logout() {
        SaveSharedPreference.clearUser(this);
        finish();
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
