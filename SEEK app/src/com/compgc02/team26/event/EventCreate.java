package com.compgc02.team26.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.datepicker.DatePickerFragment;
import com.compgc02.team26.datepicker.DatePickerFragment2;
import com.compgc02.team26.datepicker.DatePickerFragment3;
import com.compgc02.team26.seek.JSONParser;

public class EventCreate extends Fragment implements OnClickListener {

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	private ProgressDialog pDialog;

	private EditText inputEventName;
	private EditText inputStartDate;
	private EditText inputEndDate;
	private EditText inputPostCode;
	private EditText inputAddress;
	private EditText inputMaxCap;
	private EditText inputRegLink;
	private EditText inputDescription;

	// url to create event
	private static String url_create_event = "http://seek-app.wc.lt/create_event.php";

	// JSON nodes names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.event_create, container, false);

		// User input
		inputEventName = (EditText)rootView.findViewById(R.id.eventTitle_input);
		inputStartDate = (EditText)rootView.findViewById(R.id.startDate_input);
		inputEndDate = (EditText)rootView.findViewById(R.id.endDate_input);
		inputPostCode = (EditText)rootView.findViewById(R.id.postcode);
		inputAddress = (EditText)rootView.findViewById(R.id.address);
		inputMaxCap = (EditText)rootView.findViewById(R.id.maxCap_input);
		inputRegLink = (EditText)rootView.findViewById(R.id.urlLink_input);
		inputDescription = (EditText)rootView.findViewById(R.id.eventDescription_input);		

		// Find create button
		final Button createButton = (Button)rootView.findViewById(R.id.createButton);

		// Button click venue
		createButton.setOnClickListener(this);

		/*createButton = (Button) rootView.findViewById(R.id.createButton);

		createButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting background task to update profile
				new CreateNewEvent().execute();
			}
		});*/

		// Date picker for event start date
		inputStartDate = (EditText) rootView.findViewById(R.id.startDate_input);
		inputStartDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick (View view) {
				DialogFragment DatePickerFragment = new DatePickerFragment2();
				DatePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");	

			}
		});

		inputEndDate = (EditText) rootView.findViewById(R.id.endDate_input);

		// Save button click profile
		inputEndDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick (View view) {
				DialogFragment DatePickerFragment = new DatePickerFragment3();
				DatePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");	

			}
		});

		return rootView;
	}	

	// Create button clicked, create new venue in background thread
	@Override
	public void onClick (View v) {
		if (v.getId() == R.id.createButton){
			new CreateNewEvent().execute();
		}
	}

	public void startDate_input (View view) {
		DialogFragment DatePickerFragment = new DatePickerFragment();
		DatePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");	
	}


	/**
	 * Background Async Task to create new event
	 * */
	class CreateNewEvent extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EventCreate.this.getActivity());
			pDialog.setMessage("Creating new event..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating new event
		 * */
		protected String doInBackground(String... args) {
			//capture user input
			String e_name = inputEventName.getText().toString();
			String start_date = inputStartDate.getText().toString();
			String end_date = inputEndDate.getText().toString();
			String address = inputAddress.getText().toString();
			String post_code = inputPostCode.getText().toString().toUpperCase(Locale.getDefault());
			String max_cap = inputMaxCap.getText().toString();
			String reg_link = inputRegLink.getText().toString();
			String e_desc = inputDescription.getText().toString();

			// Building parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start_date", start_date));
			params.add(new BasicNameValuePair("end_date", end_date));
			params.add(new BasicNameValuePair("e_name", e_name));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("post_code", post_code));
			params.add(new BasicNameValuePair("max_cap", max_cap));
			params.add(new BasicNameValuePair("reg_link", reg_link));
			params.add(new BasicNameValuePair("e_desc", e_desc));

			// Getting JSON Object
			// Create event url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_create_event, "POST", params);

			// Check log cat from response
			Log.d("Create Response", json.toString());

			// Check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {					
					// Successfully created event	
					Intent intent = new Intent(EventCreate.this.getActivity(), EventActivity.class);
					startActivity(intent);
					getActivity().finish();
					return json.getString(TAG_MESSAGE);
				} else {
					// Failed to create event
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog once done
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
			}
		}
	}
}
