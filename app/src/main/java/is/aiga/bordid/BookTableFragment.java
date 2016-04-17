package is.aiga.bordid;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class BookTableFragment extends DialogFragment {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/Server/Reservation.php";
    public static final String UPLOAD_KEY = "booking";

    private ProgressDialog loading;
    private TextView numberOfSeats, date, timeOfDay;
    private Button confirm, cancel, increment, decrement;
    private View rootView;
    private String time, day, year;
    private int month;
    private String[] months = {"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí", "Ágúst", "September", "Október", "Nóvember", "Desember"};



    public BookTableFragment(String time, int day, int month, int year) {
        this.time = time;
        this.day = String.valueOf(day);
        this.month = month;
        this.year = String.valueOf(year);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book_table, container, false);
        getDialog().setTitle("Book Table");

        // Initialize few things
        init();

        return rootView;
    }

    private void init() {

        date = (TextView)rootView.findViewById(R.id.date);
        date.setText(day + ". " + months[month] + " " + year);
        timeOfDay = (TextView)rootView.findViewById(R.id.timeOfDay);
        timeOfDay.setText(time);


        numberOfSeats = (TextView) rootView.findViewById(R.id.numberOfSeats);

        increment = (Button) rootView.findViewById(R.id.increment);
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iTemp = Integer.parseInt((String) numberOfSeats.getText());
                if(iTemp < 10) iTemp += 1;
                numberOfSeats.setText(Integer.toString(iTemp));
            }
        });

        decrement = (Button) rootView.findViewById(R.id.decrement);
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dTemp = Integer.parseInt((String) numberOfSeats.getText());
                if(dTemp != 1) dTemp -= 1;
                numberOfSeats.setText(Integer.toString(dTemp));
            }
        });

        confirm = (Button) rootView.findViewById(R.id.dialogButtonConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = month+1;
                String date = year + "-0" + m +"-" + day + " " + time + ":00";
                BookTable booktable = new BookTable(SaveSharedPreference.getUserId(getActivity()), RestaurantInfoActivity.id, numberOfSeats.getText().toString(), date);
                booktable.execute((Void) null);
                loading = ProgressDialog.show(getActivity(),"Loading...","Please Wait...",true,true);
                Thread timerThread = new Thread() {
                    public void run() {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            loading.dismiss();
                        }
                    }
                };
                timerThread.start();
                Intent i = new Intent(getActivity(), ReservationsActivity.class);
                startActivity(i);
                dismiss();

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

    // Asynchronous login task
    public class BookTable extends AsyncTask<Void, Void, String> {

        private final String custId;
        private final String restId;
        private final String numberOfSeats;
        private final String resDate;


        BookTable(String custId, String restId, String numberOfSeats, String resDate) {

            this.custId = custId;
            this.restId = restId;
            this.numberOfSeats = numberOfSeats;
            this.resDate = resDate;
        }

        @Override
        protected String doInBackground(Void... params) {

            Service service = new Service(); // Service class is used to validate username and password

            String str = custId +":"+ restId +":"+ numberOfSeats +":"+ resDate;
            HashMap<String,String> data = new HashMap<>();
            data.put(UPLOAD_KEY, str);
            Log.d("IED", "Send String: " + str);

            String result = service.sendPostRequest(UPLOAD_URL, data); // Posts a String to server, String created by HashMap, eg. username=john:123456

            return result;
        }

        @Override
        protected void onPostExecute(final String data) {
            Log.d("IED", "result: " + data);
            }
        }
}
