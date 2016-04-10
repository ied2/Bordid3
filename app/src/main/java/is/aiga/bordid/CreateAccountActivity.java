package is.aiga.bordid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/CreateUser.php";
    public static final String UPLOAD_KEY = "username";

    private EditText mUserNameView, mPasswordView, mFullName, mRestaurantName, mPhoneNumber, mEmail;
    private UserCreateTask mAuthTask = null;
    private boolean CREATE_SUCCESSFUL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.setTitle("Create Account");

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserNameView = (EditText) findViewById(R.id.username_create);
        mPasswordView = (EditText) findViewById(R.id.password_create);
        mFullName = (EditText) findViewById(R.id.full_name);
        mRestaurantName = (EditText) findViewById(R.id.restaurant_name);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mEmail = (EditText) findViewById(R.id.email);


        Button mEmailSignInButton = (Button) findViewById(R.id.create_account);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
                //Toast.makeText(CreateAccountActivity.this, "Account to be created :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateAccount() {

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fullName = mFullName.getText().toString();
        String restaurantName = mRestaurantName.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String email = mEmail.getText().toString();

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isUsernameValid(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            mAuthTask = new UserCreateTask(userName, password, fullName, restaurantName, phoneNumber, email);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String userName) {
        return userName.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    public class UserCreateTask extends AsyncTask<Void, Void, String> {

        private final String mUserName;
        private final String mPassword;
        private final String mFullName;
        private final String mRestaurantName;
        private final String mPhoneNumber;
        private final String mEmail;

        UserCreateTask(String userName, String password, String fullName, String restaurantName, String phoneNumber, String email) {
            this.mUserName = userName;
            this.mPassword = password;
            this.mFullName = fullName;
            this.mRestaurantName = restaurantName;
            this.mPhoneNumber = phoneNumber;
            this.mEmail = email;
            CREATE_SUCCESSFUL = false;

        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String loginCode = mUserName + ":" + mPassword + ":" + mFullName + ":" + mRestaurantName + ":" + mPhoneNumber + ":" + mEmail;
            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, loginCode); // UPLOAD_KEY = "username", keyword for server POST request

            String result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456
            Log.d("IED", result);

            return result;
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;

            if(success.equals("Username Already Exists")) {
                Toast.makeText(CreateAccountActivity.this, success, Toast.LENGTH_SHORT).show();
                return;
            }

            String s = success.split(":")[0];
            String id = success.split(":")[1];

            if (s.equals("Account Created")) {
                Toast.makeText(CreateAccountActivity.this, s, Toast.LENGTH_SHORT).show();
                SaveSharedPreference.setUserId(CreateAccountActivity.this, id);
                SaveSharedPreference.setUserName(CreateAccountActivity.this, mUserName);
                SaveSharedPreference.setName(CreateAccountActivity.this, mFullName);
                SaveSharedPreference.setRestaurantName(CreateAccountActivity.this, mRestaurantName);
                SaveSharedPreference.setPhoneNumber(CreateAccountActivity.this, mPhoneNumber);
                SaveSharedPreference.setEmail(CreateAccountActivity.this, mEmail);
                Intent i = new Intent(CreateAccountActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
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
