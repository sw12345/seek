package com.compgc02.team26.datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.compgc02.samsudin.seek.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * to instantiate Date Picker in EventCreate.java to allow user to select the start date of an event.
 * @author User
 *
 */
public class DatePickerFragment3 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	private EditText inputEndDate;
	private SimpleDateFormat dateFormatter;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    	final Calendar myCalendar = Calendar.getInstance();
		myCalendar.set(Calendar.YEAR, year);
		myCalendar.set(Calendar.MONTH, month);
		myCalendar.set(Calendar.DAY_OF_MONTH, day);
		String myFormat = "yyyy-MM-dd"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

		inputEndDate = (EditText) getActivity().findViewById(R.id.endDate_input);
		inputEndDate.setText(sdf.format(myCalendar.getTime()));
		}
}