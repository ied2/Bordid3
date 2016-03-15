package is.aiga.bordid;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class BookTableActivity extends AppCompatActivity implements View.OnClickListener {

    private Button pickDate, increment, decrement, buttonBook;
    public static TextView date;
    public TextView numberOfSeats;

    /** Private members of the class */
    private TextView displayTime;
    private Button pickTime;

    private int pHour;
    private int pMinute;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);

        init();

        /** Display the current time in the TextView */
        updateDisplay();
    }

    public void init() {
        numberOfSeats = (TextView) findViewById(R.id.numberOfSeats);
        numberOfSeats.setOnClickListener(this);
        increment = (Button) findViewById(R.id.increment);
        increment.setOnClickListener(this);
        decrement = (Button) findViewById(R.id.decrement);
        decrement.setOnClickListener(this);
        pickDate = (Button)findViewById(R.id.pick_date);
        pickDate.setOnClickListener(this);
        buttonBook = (Button) findViewById(R.id.button_book);
        buttonBook.setOnClickListener(this);
        date = (TextView)findViewById(R.id.date);
        date.setText(Utils.dateToday());
        displayTime = (TextView) findViewById(R.id.timeDisplay);
        pickTime = (Button) findViewById(R.id.pickTime);
        pickTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Start pick date fragment
            case R.id.pick_date:
                DialogFragment DatePicker = new DatePickerFragment();
                DatePicker.show(getSupportFragmentManager(), "datePicker");
            break;
            // decrement seat quantity
            case R.id.decrement:
                int dTemp = Integer.parseInt((String) numberOfSeats.getText());
                if(dTemp != 1) dTemp -= 1;
                numberOfSeats.setText(Integer.toString(dTemp));
                break;
            // increment seat quantity
            case R.id.increment:
                int iTemp = Integer.parseInt((String) numberOfSeats.getText());
                iTemp += 1;
                numberOfSeats.setText(Integer.toString(iTemp));
                break;
            // Confirm booking
            case R.id.button_book:
                Toast.makeText(BookTableActivity.this, "Table Booked", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.pickTime:
                showDialog(TIME_DIALOG_ID);
                break;
        }
    }

    // Callback received when the user "picks" a time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                    displayToast();
                }
            };

    // Updates the time in the TextView
    private void updateDisplay() {
        displayTime.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)));
    }

    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(displayTime.getText()),   Toast.LENGTH_SHORT).show();
    }

    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
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
