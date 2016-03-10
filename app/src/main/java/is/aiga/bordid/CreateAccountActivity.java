package is.aiga.bordid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com//PhotoUpload/CreateUser.php";
    public static final String UPLOAD_KEY = "username";

    private EditText mUsernameView, mPasswordView;
    private UserCreateTask mAuthTask = null;
    private boolean CREATE_SUCCESSFUL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.setTitle("Create Account");

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsernameView = (EditText) findViewById(R.id.username_create);
        mPasswordView = (EditText) findViewById(R.id.password_create);

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
        String userName = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        mAuthTask = new UserCreateTask(userName, password);
        mAuthTask.execute((Void) null);
    }

    public class UserCreateTask extends AsyncTask<Void, Void, String> {

        private final String mUserName;
        private final String mPassword;

        UserCreateTask(String email, String password) {
            CREATE_SUCCESSFUL = false;
            mUserName = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String loginCode = mUserName + ":" + mPassword;
            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, loginCode); // UPLOAD_KEY = "username", keyword for server POST request

            String result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456
            Log.d("IED", result);

            return result;
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            //showProgress(false);

            String s = success.split(":")[0];
            String id = success.split(":")[1];

            if (s.equals("Account Created")) {
                Toast.makeText(CreateAccountActivity.this, s, Toast.LENGTH_SHORT).show();
                SaveSharedPreference.setUserId(CreateAccountActivity.this, id);
                SaveSharedPreference.setUserName(CreateAccountActivity.this, mUserName);
                Intent i = new Intent(CreateAccountActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
                Toast.makeText(CreateAccountActivity.this, success, Toast.LENGTH_SHORT).show();
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
