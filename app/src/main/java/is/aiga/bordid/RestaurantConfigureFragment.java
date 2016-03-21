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

public class RestaurantConfigureFragment extends DialogFragment implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/EditRestaurant.php";
    public static final String UPLOAD_KEY = "restaurant";
    private ConfigureRestaurant task;

    private String result;

    private static EditText name, email, address, zip, city, phoneNumber, numSeats, url, latitude, longitude;
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
        url = (EditText) rootView.findViewById(R.id.dialog_restaurant_url);
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

        save = (Button) rootView.findViewById(R.id.rdialogButtonOK);
        cancel = (Button) rootView.findViewById(R.id.rdialogButtonCancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdialogButtonOK :
                String mName = name.getText().toString();
                String mEmail = email.getText().toString();
                String mAddress = address.getText().toString();
                String mZip = zip.getText().toString();
                String mCity = city.getText().toString();
                String mPhoneNumber = phoneNumber.getText().toString();
                String mNumSeats = numSeats.getText().toString();
                String mUrl = url.getText().toString();
                String mLatitude = latitude.getText().toString();
                String mLongitude = longitude.getText().toString();

                // Save new information about the user
                task = new ConfigureRestaurant(mName, mEmail, mAddress, mZip, mCity, mPhoneNumber, mNumSeats, mUrl, mLatitude, mLongitude);
                task.execute((Void) null);

                // Update user information in the app
                ProfileActivity.restaurant_name.setText(mName);
                SaveSharedPreference.setRestaurantName(getActivity(), mName);
                SaveSharedPreference.setRestaurantEmail(getActivity(), mEmail);
                SaveSharedPreference.setAddress(getActivity(), mAddress);
                SaveSharedPreference.setZip(getActivity(), mZip);
                SaveSharedPreference.setCity(getActivity(), mCity);
                SaveSharedPreference.setRestaurantPhoneNumber(getActivity(), mPhoneNumber);
                SaveSharedPreference.setNumSeats(getActivity(), mNumSeats);
                SaveSharedPreference.setUrl(getActivity(), mUrl);
                SaveSharedPreference.setLatitude(getActivity(), mLatitude);
                SaveSharedPreference.setLongitude(getActivity(), mLongitude);

                Toast.makeText(getActivity(), "Account Updated", Toast.LENGTH_LONG).show();
                dismiss(); // Close dialog

                break;
            case R.id.rdialogButtonCancel:
                dismiss(); // Close dialog
        }
    }

    // Update user's restaurant information
    public class ConfigureRestaurant extends AsyncTask<Void, Void, String> {

        private String cname, cemail, caddress, czip, ccity, cphoneNumber, cnumSeats, curl, clatitude, clongitude;

        ConfigureRestaurant(String name, String email, String address, String zip, String city, String phoneNumber, String numSeats, String url, String latitude, String longitude) {
            this.cname = name;
            this.cemail = email;
            this.caddress = address;
            this.czip = zip;
            this.ccity = city;
            this.cphoneNumber = phoneNumber;
            this.cnumSeats = numSeats;
            this.curl = url;
            this.clatitude = latitude;
            this.clongitude = longitude;

        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String updateString = SaveSharedPreference.getUserId(getActivity()) + ":" + cname + ":" + cemail + ":" + caddress + ":" + czip + ":" + ccity + ":" + cphoneNumber +
                                    ":" + cnumSeats + ":" + curl + ":" + clatitude + ":" + clongitude;

            Log.d("IED", "updateString" +  updateString);

            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, updateString); // UPLOAD_KEY = "restaurant", keyword for server POST request

            result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            Log.d("IED", "EditRestaurant: " + s);
        }
    }
}