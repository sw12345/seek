package com.compgc02.team26.venue;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.seek.JSONParser;
import com.compgc02.team26.seek.SessionManager;

public class VenueCreate extends Fragment implements OnClickListener {

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	SessionManager session;

	private ProgressDialog pDialog;

	private EditText inputVenueName;
	private EditText inputPostCode;
	private EditText inputAddress;
	private EditText inputMaxCap;
	private EditText inputDescription;
	private Spinner inputSpinner;

	String userId;

	// url to create venue
	private static String url_create_venue = "http://seek-app.wc.lt/create_venue.php";

	// JSON nodes names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.venue_create, container, false);

		// Session class instance
		session = new SessionManager(getActivity());

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// userId
		userId = user.get(SessionManager.KEY_ID);

		// User input
		inputVenueName = (EditText)rootView.findViewById(R.id.venueTitle_input);
		inputPostCode = (EditText)rootView.findViewById(R.id.postcode);
		inputAddress = (EditText)rootView.findViewById(R.id.address);
		inputMaxCap = (EditText)rootView.findViewById(R.id.maxCap_input);
		inputDescription = (EditText)rootView.findViewById(R.id.eventDescription_input);
		inputSpinner = (Spinner)rootView.findViewById(R.id.typeSpinner1);

		// Create button
		final Button createButton = (Button)rootView.findViewById(R.id.createButton);

		// Button click venue
		createButton.setOnClickListener(this);

		return rootView;

	}

	// Create button clicked, create new venue in background thread
	@Override
	public void onClick (View v) {
		if (v.getId() == R.id.createButton){
			new CreateNewVenue().execute();
		}

	}

	/**
	 * Background Async Task to create new venue
	 * */
	class CreateNewVenue extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(VenueCreate.this.getActivity());
			pDialog.setMessage("Creating new venue..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating new venue
		 * */
		protected String doInBackground(String... args) {
			//capture user input
			String v_name = inputVenueName.getText().toString();
			String v_type = inputSpinner.getSelectedItem().toString();
			String address = inputAddress.getText().toString();
			String post_code = inputPostCode.getText().toString().toUpperCase(Locale.getDefault());
			String max_cap = inputMaxCap.getText().toString();
			String v_desc = inputDescription.getText().toString();

			// Building parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("v_name", v_name));
			params.add(new BasicNameValuePair("v_type", v_type));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("post_code", post_code));
			params.add(new BasicNameValuePair("max_cap", max_cap));
			params.add(new BasicNameValuePair("v_desc", v_desc));
			params.add(new BasicNameValuePair("user_id", userId));

			// Getting JSON Object
			// Create venue url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_create_venue, "POST", params);

			// Check log cat from response
			Log.d("Create Response", json.toString());

			// Check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {					
					// Successfully created venue	
					Intent intent = new Intent(VenueCreate.this.getActivity(), VenueActivity.class);
					startActivity(intent);
					getActivity().finish();
					return json.getString(TAG_MESSAGE);
				} else {
					// Failed to create venue
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
