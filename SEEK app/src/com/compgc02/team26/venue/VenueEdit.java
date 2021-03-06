package com.compgc02.team26.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.seek.Controller;
import com.compgc02.team26.seek.JSONParser;

public class VenueEdit extends SherlockFragmentActivity {

	private ProgressDialog pDialog;

	private EditText inputVenueName;
	private EditText inputAddress;
	private EditText inputPostCode;
	private EditText inputMaxCap;
	private EditText inputDescription;
	private Spinner inputType;
	Button saveButton;
	Button deleteButton;

	String venueId;

	// Create JSON Parser object
	JSONParser jsonParser = new JSONParser();

	private static final String TAG = VenueEdit.class.getSimpleName();

	// url to edit venue
	private static final String url_venue_details = "http://seek-app.wc.lt/get_venue_details.php";

	// url to update venue
	private static final String url_update_venue = "http://seek-app.wc.lt/update_venue.php";

	// url to delete venue
	private static final String url_delete_venue = "http://seek-app.wc.lt/delete_venue.php";

	// JSON node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_VID = "venueId";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue_edit);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		saveButton = (Button) findViewById(R.id.saveButton1);

		deleteButton = (Button) findViewById(R.id.deleteButton);

		// Getting venue details from intent
		Intent intent = getIntent();

		// Getting venue id (venueId) from intent
		venueId = intent.getStringExtra(TAG_VID);

		// Getting complete venue details in background thread
		getVenueDetails(venueId);

		// Save button click event
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting background task to update venue
				new SaveVenueDetails().execute();
			}
		});

		// Delete button click event
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Deleting venue in background thread
				new DeleteVenue().execute();
			}
		});

	}

	/**
	 * Getting venue details in background thread
	 * */
	private void getVenueDetails(final String venue_id) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_venue_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject venue = new JSONObject(response);
					String name = venue.getString("name");
					String maxCap = venue.getString("maxCap");
					String address = venue.getString("address");
					String postcode = venue.getString("postcode");
					String description = venue.getString("description");

					// Found venue with this venueId
					inputVenueName = (EditText) findViewById(R.id.venueTitle_input);
					inputAddress = (EditText) findViewById(R.id.address);
					inputPostCode = (EditText) findViewById(R.id.postcode);
					inputMaxCap = (EditText) findViewById(R.id.maxCap_input);
					inputDescription = (EditText) findViewById(R.id.venueDescription_input);

					// Set edit text
					inputVenueName.setText(name);
					//inputType.setSelectedItem().toString();
					inputAddress.setText(address);
					inputPostCode.setText(postcode);
					inputMaxCap.setText(maxCap);
					inputDescription.setText(description);

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();				
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
			}

		})  {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("venue_id", venueId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);
	}

	/**
	 * Background Async Task to save venue details
	 * */
	class SaveVenueDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(VenueEdit.this);
			pDialog.setMessage("Saving venue ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving venue
		 * */
		protected String doInBackground(String... args) {
			
			inputType = (Spinner) findViewById(R.id.typeSpinner1);

			// Getting updated data from EditTexts
			String name = inputVenueName.getText().toString();
			String type = inputType.getSelectedItem().toString();
			String address = inputAddress.getText().toString();
			String postcode = inputPostCode.getText().toString();
			String maxCap = inputMaxCap.getText().toString();
			String description = inputDescription.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("venue_id", venueId));
			params.add(new BasicNameValuePair("v_name", name));
			params.add(new BasicNameValuePair("v_type", type));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("post_code", postcode));
			params.add(new BasicNameValuePair("max_cap", maxCap));
			params.add(new BasicNameValuePair("v_desc", description));

			// Sending updated data through http request
			// Update venue url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_venue, "POST", params);

			// Check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// Successfully updated
					Intent intent = getIntent();
					// Send result code 100 to notify about venue update
					setResult(100, intent);
					finish();
				} else {
					// Failed to update venue
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}


		/**
		 * After completing background task dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog once venue updated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to delete venue
	 * */
	class DeleteVenue extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(VenueEdit.this);
			pDialog.setMessage("Deleting Venue...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting venue
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("venue_id", venueId));

				// Getting venue details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(url_delete_venue, "POST", params);

				// Check log for json response
				Log.d("Delete Venue", json.toString());

				// JSON success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// Venue successfully deleted
					// Notify previous activity by sending code 100
					Intent intent = getIntent();
					// Send result code 100 to notify about venue deletion
					setResult(100, intent);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog once venue deleted
			pDialog.dismiss();

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}  
}

