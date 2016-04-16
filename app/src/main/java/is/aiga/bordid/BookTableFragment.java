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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class BookTableFragment extends DialogFragment {

    private TextView numberOfSeats;
    private static EditText name, email, phoneNumber;
    private Button confirm, cancel, increment, decrement;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book_table, container, false);
        getDialog().setTitle("Book Table");

        // Initialize few things
        init();

        return rootView;
    }

    private void init() {

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

                Toast.makeText(getActivity(), "Table Booked", Toast.LENGTH_LONG).show();
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
}
