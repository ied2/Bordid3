package is.aiga.bordid;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UserConfigureFragment extends DialogFragment {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com//PhotoUpload/ConfigureUser.php";
    public static final String UPLOAD_KEY = "user";
    private ConfigureUser task;

    private String result;

    private static EditText name, email, phoneNumber;
    private Button save, cancel;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_configure, container, false);
        getDialog().setTitle("Configure");

        // Initialize few things
        init();

        return rootView;
    }

    private void init() {
        name = (EditText) rootView.findViewById(R.id.dialog_name);
        email = (EditText) rootView.findViewById(R.id.dialog_email);
        phoneNumber = (EditText) rootView.findViewById(R.id.dialog_phonenumber);

        name.setText(ProfileActivity.name.getText().toString());
        email.setText(ProfileActivity.email.getText().toString());
        phoneNumber.setText(ProfileActivity.phoneNumber.getText().toString());

        save = (Button) rootView.findViewById(R.id.dialogButtonOK);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mName = name.getText().toString();
                String mEmail = email.getText().toString();
                String mPhoneNumber = phoneNumber.getText().toString();

                // Start task to fetch information about the user
                task = new ConfigureUser(mName, mEmail, mPhoneNumber);
                task.execute((Void) null);

                ProfileActivity.name.setText(mName);
                ProfileActivity.email.setText(mEmail);
                ProfileActivity.phoneNumber.setText(mPhoneNumber);

                Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_LONG).show();
                dismiss(); // Close dialog
            }
        });

        cancel = (Button) rootView.findViewById(R.id.dialogButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close dialog
            }
        });
    }

    // Update user information
    public class ConfigureUser extends AsyncTask<Void, Void, String> {

        private String name, email, phoneNumber;

        ConfigureUser(String name, String email, String phoneNumber) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String updateString = SaveSharedPreference.getUserId(getActivity()) + ":" + name + ":" + email + ":" + phoneNumber;

            Log.d("IED", "updateString" +  updateString);

            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, updateString); // UPLOAD_KEY = "username", keyword for server POST request

            result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            SaveSharedPreference.setName(rootView.getContext(), name);
            SaveSharedPreference.setEmail(rootView.getContext(), email);
            SaveSharedPreference.setPhoneNumber(rootView.getContext(), phoneNumber);
        }
    }
}
