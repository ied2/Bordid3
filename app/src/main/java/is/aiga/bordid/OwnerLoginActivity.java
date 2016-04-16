package is.aiga.bordid;

import static android.Manifest.permission.READ_CONTACTS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OwnerLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/ValidateUser.php";
    public static final String UPLOAD_KEY = "username";
    public boolean LOGIN_SUCCESSFUL;

    // Id to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTitle("Sign In");

        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.userName);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

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
            showProgress(true);
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String userName) {
        return userName.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(OwnerLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    // Asynchronous login task
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mUserName;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            LOGIN_SUCCESSFUL = false;
            mUserName = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String loginCode = mUserName + ":" + mPassword;
            Log.d("IED", loginCode);
            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, loginCode);

            String result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String data) {
            mAuthTask = null;
            showProgress(false);

            // Creating a JSONObject from data, received from server
            try {
                JSONObject nodeRoot  = new JSONObject(data);
                if(nodeRoot.getString("Success").equals("TRUE")) {
                    String s1 = nodeRoot.getString("OwnerId");
                    String s2 = nodeRoot.getString("OwnerUsername");
                    String s3 = nodeRoot.getString("OwnerName");
                    String s4 = nodeRoot.getString("OwnerEmail");
                    String s5 = nodeRoot.getString("OwnerPhoneNumber");
                    String s6 = nodeRoot.getString("RestId");
                    String s7 = nodeRoot.getString("RestName");
                    String s8 = nodeRoot.getString("RestAddress");
                    String s9 = nodeRoot.getString("RestZip");
                    String s10 = nodeRoot.getString("RestCity");
                    String s11 = nodeRoot.getString("RestLatitude");
                    String s12 = nodeRoot.getString("RestLongitude");
                    String s13 = nodeRoot.getString("RestPhoneNumber");
                    String s14 = nodeRoot.getString("RestUrl");
                    String s15 = nodeRoot.getString("RestNumSeats");
                    String s16 = nodeRoot.getString("RestEmail");
                    String s17 = nodeRoot.getString("ProfileImageUrl");

                    // Save login information
                    SaveSharedPreference.setUserId(OwnerLoginActivity.this, s1);
                    SaveSharedPreference.setUserName(OwnerLoginActivity.this, s2);
                    SaveSharedPreference.setName(OwnerLoginActivity.this, s3);
                    SaveSharedPreference.setEmail(OwnerLoginActivity.this, s4);
                    SaveSharedPreference.setPhoneNumber(OwnerLoginActivity.this, s5);
                    SaveSharedPreference.setRestaurantId(OwnerLoginActivity.this, s6);
                    SaveSharedPreference.setRestaurantName(OwnerLoginActivity.this, s7);
                    SaveSharedPreference.setAddress(OwnerLoginActivity.this, s8);
                    SaveSharedPreference.setZip(OwnerLoginActivity.this, s9);
                    SaveSharedPreference.setCity(OwnerLoginActivity.this, s10);
                    SaveSharedPreference.setLatitude(OwnerLoginActivity.this, s11);
                    SaveSharedPreference.setLongitude(OwnerLoginActivity.this, s12);
                    SaveSharedPreference.setRestaurantPhoneNumber(OwnerLoginActivity.this, s13);
                    SaveSharedPreference.setUrl(OwnerLoginActivity.this, s14);
                    SaveSharedPreference.setNumSeats(OwnerLoginActivity.this, s15);
                    SaveSharedPreference.setRestaurantEmail(OwnerLoginActivity.this, s16);
                    SaveSharedPreference.setProfileImage(OwnerLoginActivity.this, s17);

                    // Start Profile Activity
                    Intent i = new Intent(OwnerLoginActivity.this, ProfileActivity.class);
                    startActivity(i);
                    finish(); // Shut down current activity

                } else {
                    // Tell user if password is incorrect
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
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


