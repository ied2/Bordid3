package is.aiga.bordid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the calendar day, month, year
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Create the date picker
        DatePickerDialog myDatePicker =
                new DatePickerDialog(getActivity(), this, year, month, day);

        return myDatePicker;
    }

    // Method is executed when user clicks OK on DatePicker
    // Fragment closes
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String suffix = Utils.getSuffix(dayOfMonth);
        String date =
                String.valueOf(dayOfMonth) + suffix + " "
                        + Utils.monthsArray[monthOfYear] + " "
                        + String.valueOf(year);

        Toast.makeText(getActivity(), date , Toast.LENGTH_SHORT).show();

        BookTableActivity.date.setText(date);
    }
}
