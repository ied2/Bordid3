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

public class EditRestDialogFragment extends DialogFragment {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com//PhotoUpload/ConfigureUser.php";
    public static final String UPLOAD_KEY = "user";
    private ConfigureUser task;

    private String result;

    private static EditText name, email, address, zip, city, phoneNumber, numSeats, latitude, longitude;
    private Button save, cancel;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_restaurant_configure, container, false);
        getDialog().setTitle("Configure");

        // Initialize few things
        init();

        return rootView;
    }

    private void init() {
        name = (EditText) rootView.findViewById(R.id.dialog_restaurant_name);
        email = (EditText) rootView.findViewById(R.id.dialog_restaurant_email);
        address = (EditText) rootView.findViewById(R.id.dialog_restaurant_address);
        zip = (EditText) rootView.findViewById(R.id.dialog_restaurant_zip);
        city = (EditText) rootView.findViewById(R.id.dialog_restaurant_city);
        phoneNumber = (EditText) rootView.findViewById(R.id.dialog_restaurant_phonenumber);
        numSeats = (EditText) rootView.findViewById(R.id.dialog_restaurant_numSeats);
        latitude = (EditText) rootView.findViewById(R.id.dialog_restaurant_latitude);
        longitude = (EditText) rootView.findViewById(R.id.dialog_restaurant_longitude);

        name.setText(SaveSharedPreference.getRestaurantName(rootView.getContext()));
        email.setText(SaveSharedPreference.getRestaurantEmail(rootView.getContext()));
        address.setText(SaveSharedPreference.getAddress(rootView.getContext()));
        zip.setText(SaveSharedPreference.getZip(rootView.getContext()));
        city.setText(SaveSharedPreference.getCity(rootView.getContext()));
        phoneNumber.setText(SaveSharedPreference.getRestaurantPhoneNumber(rootView.getContext()));
        numSeats.setText(SaveSharedPreference.getNumSeats(rootView.getContext()));
        latitude.setText(SaveSharedPreference.getLatitude(rootView.getContext()));
        longitude.setText(SaveSharedPreference.getLongitude(rootView.getContext()));

//        save = (Button) rootView.findViewById(R.id.dialogButtonOK);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String mName = name.getText().toString();
//                String mEmail = email.getText().toString();
//                String mPhoneNumber = phoneNumber.getText().toString();
//
//                // Start task to fetch information about the user
//                task = new ConfigureUser(mName, mEmail, mPhoneNumber);
//                task.execute((Void) null);
//
//                ProfileActivity.name.setText(mName);
//                ProfileActivity.email.setText(mEmail);
//                ProfileActivity.phoneNumber.setText(mPhoneNumber);
//
//                Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_LONG).show();
//                dismiss(); // Close dialog
//            }
//        });
//
//        cancel = (Button) rootView.findViewById(R.id.dialogButtonCancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss(); // Close dialog
//            }
//        });
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

            String updateString = ProfileActivity.id + ":" + name + ":" + email + ":" + phoneNumber;

            Log.d("IED", "updateString" +  updateString);

            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, updateString); // UPLOAD_KEY = "username", keyword for server POST request

            result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            Log.d("IED", "GetUser: " + s);

        }
    }
}
