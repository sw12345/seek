package com.compgc02.team26.datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.compgc02.team26.seek.UserProfile;

/**
 * to instantiate Date Picker in UserProfile.java to allow user to select their birthdate
 * @author User
 *
 */
public class DatePickerFragment extends DialogFragment {

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
        return new DatePickerDialog(getActivity(), (UserProfile)getActivity(), year, month, day);

    }

    

}