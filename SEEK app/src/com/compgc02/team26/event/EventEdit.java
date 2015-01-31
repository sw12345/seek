package com.compgc02.team26.event;

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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.compgc02.team26.datepicker.DatePickerFragment2;
import com.compgc02.team26.datepicker.DatePickerFragment3;
import com.compgc02.team26.seek.Controller;
import com.compgc02.team26.seek.JSONParser;

public class EventEdit extends SherlockFragmentActivity {

	private ProgressDialog pDialog;
	
	private EditText inputEventName;
	private EditText inputStartDate;
	private EditText inputEndDate;
	private EditText inputAddress;
	private EditText inputPostcode;
	private EditText inputMaxCap;
	private EditText inputRegLink;
	private EditText inputDescription;
	
	Button saveButton;
	Button deleteButton;
	String eventId;

	// Create JSON Parser object
	JSONParser jsonParser = new JSONParser();
	
	private static final String TAG = EventEdit.class.getSimpleName();

	// url to edit event
	private static final String url_event_details = "http://seek-app.wc.lt/get_event_details.php";

	// url to update event
	private static final String url_update_event = "http://seek-app.wc.lt/update_event.php";

	// url to delete event
	private static final String url_delete_event = "http://seek-app.wc.lt/delete_event.php";

	// JSON node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_EID = "eventId";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_edit);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		saveButton = (Button) findViewById(R.id.saveButton);

		deleteButton = (Button) findViewById(R.id.deleteButton);

		// Getting event details from intent
		Intent intent = getIntent();

		// Getting event id (eventId) from intent
		eventId = intent.getStringExtra(TAG_EID);

		// Getting complete event details in background thread
		getEventDetails(eventId);

		// Save button click event
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting background task to update event
				new SaveEventDetails().execute();
			}
		});

		// Delete button click event
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Deleting event in background thread
				new DeleteEvent().execute();
			}
		});

	}

	public void startDatePicker (View view) {
        DialogFragment DatePickerFragment = new DatePickerFragment2();
        DatePickerFragment.show(getSupportFragmentManager(), "datePicker");	
    }
	public void endDatePicker (View view) {
        DialogFragment DatePickerFragment = new DatePickerFragment3();
        DatePickerFragment.show(getSupportFragmentManager(), "datePicker");	
    }
	
	/**
	 * Getting event details in background thread
	 * */
	private void getEventDetails(final String event_id) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_event_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject event = new JSONObject(response);
					String name = event.getString("name");
					String startdate = event.getString("startdate");
					String enddate = event.getString("enddate");
					String regLink = event.getString("regLink");
					String maxCap = event.getString("maxCap");
					String address = event.getString("address");
					String post_code = event.getString("postcode");
					String description = event.getString("description");

					// Found event with this eventId
					inputEventName = (EditText) findViewById(R.id.eventTitle_input);
					inputStartDate = (EditText) findViewById(R.id.startDate_input);
					inputEndDate = (EditText) findViewById(R.id.endDate_input);
					inputRegLink = (EditText) findViewById(R.id.urlLink_input);
					inputAddress = (EditText) findViewById(R.id.address);
					inputPostcode = (EditText) findViewById(R.id.postcode);
					inputMaxCap = (EditText) findViewById(R.id.maxCap_input);
					inputDescription = (EditText) findViewById(R.id.eventDescription_input);

					// Set edit text
					inputEventName.setText(name);
					inputStartDate.setText(startdate);
					inputEndDate.setText(enddate);
					inputRegLink.setText(regLink);
					inputAddress.setText(address);
					inputPostcode.setText(post_code);
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
				params.put("event_id", eventId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);
	}
	
	/**
	 * Background Async Task to save event details
	 * */
	class SaveEventDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EventEdit.this);
			pDialog.setMessage("Saving event ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving event
		 * */
		protected String doInBackground(String... args) {

			// Getting updated data from EditTexts
			String name = inputEventName.getText().toString();
			String startdate = inputStartDate.getText().toString();
			String enddate = inputEndDate.getText().toString();
			String regLink = inputRegLink.getText().toString();
			String address = inputAddress.getText().toString();
			String post_code = inputPostcode.getText().toString();
			String maxCap = inputMaxCap.getText().toString();
			String description = inputDescription.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("event_id", eventId));
			params.add(new BasicNameValuePair("e_name", name));
			params.add(new BasicNameValuePair("start_date", startdate));
			params.add(new BasicNameValuePair("end_date", enddate));
			params.add(new BasicNameValuePair("reg_link", regLink));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("post_code", post_code));
			params.add(new BasicNameValuePair("max_cap", maxCap));
			params.add(new BasicNameValuePair("e_desc", description));

			// Sending updated data through http request
			// Update event url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_event, "POST", params);

			// Check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// Successfully updated
					Intent intent = getIntent();
					// Send result code 50 to notify about event update
					setResult(50, intent);
					finish();
				} else {
					// Failed to update event
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
			// Dismiss the dialog once event updated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to delete event
	 * */
	class DeleteEvent extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EventEdit.this);
			pDialog.setMessage("Deleting Event...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting event
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("event_id", eventId));

				// Getting event details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(url_delete_event, "POST", params);

				// Check log for json response
				Log.d("Delete Event", json.toString());

				// JSON success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// Event successfully deleted
					// Notify previous activity by sending code 100
					Intent intent = getIntent();
					// Send result code 100 to notify about event deletion
					setResult(50, intent);
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
			// Dismiss the dialog once event deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
			}
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