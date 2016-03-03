package is.aiga.bordid;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com//PhotoUpload/GetUser.php";
    public static final String UPLOAD_KEY = "username";

    private Button buttonLogout, buttonConfigure, buttonRestaurantConfigure;
    public static TextView name, email, phoneNumber;

    public static int id;
    public static String userName;
    private UserInformation task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            userName = extras.getString("username");
//            Toast.makeText(ProfileActivity.this, "Logged in as: " + userName, Toast.LENGTH_LONG).show();
//        }
//
//        // TextViews to be configured by user
//        name = (TextView) findViewById(R.id.profile_name);
//        email = (TextView) findViewById(R.id.profile_email);
//        phoneNumber = (TextView) findViewById(R.id.profile_phoneNumber);
//
//        // Buttons
//        buttonLogout = (Button) findViewById(R.id.button_logout);
//        buttonLogout.setOnClickListener(this);
//        buttonConfigure = (Button) findViewById(R.id.profile_configure);
//        buttonConfigure.setOnClickListener(this);
//        buttonRestaurantConfigure = (Button) findViewById(R.id.profile_configure_restaurant);
//        buttonRestaurantConfigure.setOnClickListener(this);
//
//        // Start task to fetch information about the user
//        task = new UserInformation(userName);
//        task.execute((Void) null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                SaveSharedPreference.clearUserName(this);
                finish();
                break;

                // Start a dialog to configure the profile
            case R.id.profile_configure:
                FragmentManager fm = getFragmentManager();
                MyDialogFragment dialogFragment = new MyDialogFragment ();
                dialogFragment.show(fm, "Sample Fragment");

                // Start a dialog to configure the profile
            case R.id.profile_configure_restaurant:
                FragmentManager fm2 = getFragmentManager();
                MyDialogFragment dialogFragment2 = new MyDialogFragment ();
                dialogFragment2.show(fm2, "Sample Fragment");
            default:
                break;
        }
    }

    // Get user information
    public class UserInformation extends AsyncTask<Void, Void, String> {

        private final String mUserName;

        UserInformation(String username) {
            mUserName = username;
        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, mUserName); // UPLOAD_KEY = "username", keyword for server POST request

            String result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            Log.d("IED", "GetUser: " + s);

            int i = Integer.parseInt(s.split(":")[0]);
            String n = s.split(":")[1];
            String e = s.split(":")[2];
            String p = s.split(":")[3];

            id = i;
            name.setText(n);
            email.setText(e);
            phoneNumber.setText(p);

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
