package com.compgc02.team26.seek;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.compgc02.team26.datepicker.DatePickerFragment;

public class UserProfile extends SherlockFragmentActivity implements OnDateSetListener {

	private ProgressDialog pDialog;
	SessionManager session;
	Button saveButton;
	String userId;

	private EditText inputFirstName;
	private EditText inputLastName;
	private EditText inputPhone;
	private EditText inputPostCode;
	private EditText inputAddress;
	private EditText inputInterests;
	private EditText inputBirthDate;
	private RadioGroup inputGender;
	private RadioButton inputMale;
	private RadioButton inputFemale;
	private CheckBox inputCurrLoc;
	//private CheckBox inputSeeCurrLoc;
	private CheckBox inputContDetails;
	private CheckBox inputUnder18;

	// Create JSON Parser object
	JSONParser jsonParser = new JSONParser();

	private static final String TAG = UserProfile.class.getSimpleName();

	// url to edit profile
	private static final String url_get_userprofile = "http://seek-app.wc.lt/get_userprofile.php";

	// url to update profile
	private static final String url_update_userprofile = "http://seek-app.wc.lt/update_userprofile.php";

	// JSON node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// Check whether user is logged in or not
		session.checkLogin();

		// Getting user profile from intent
		Intent intent = getIntent();

		// Getting user id (userId) from intent
		userId = intent.getStringExtra("userId");

		// Getting complete user profile in background thread
		getUserProfile(userId);

		saveButton = (Button) findViewById(R.id.saveButton);

		// Save button click profile
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting background task to update profile
				new UpdateUserProfile().execute();
			}
		});
	}

	// Update date of birth
	public void onDateSet(DatePicker view, int year, int month, int day) {
		final Calendar myCalendar = Calendar.getInstance();
		myCalendar.set(Calendar.YEAR, year);
		myCalendar.set(Calendar.MONTH, month);
		myCalendar.set(Calendar.DAY_OF_MONTH, day);
		String myFormat = "yyyy-MM-dd"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
		inputBirthDate = (EditText) findViewById(R.id.birthInput);
		inputBirthDate.setText(sdf.format(myCalendar.getTime()));
	}

	public void datepickerClick (View view) {
		DialogFragment DatePickerFragment = new DatePickerFragment();
		DatePickerFragment.show(getSupportFragmentManager(), "datePicker");
	}

	/**
	 * Getting user details in background thread
	 * */
	private void getUserProfile(final String user_id) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_get_userprofile, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject users = new JSONObject(response);
					String firstname = users.getString("firstname");
					String lastname = users.getString("lastname");
					String phone = users.getString("phone");
					String postcode = users.getString("postcode");
					String address = users.getString("address");
					String birthdate = users.getString("birthdate");
					String under18 = users.getString("under18");
					String gender = users.getString("gender");
					String interests = users.getString("interests");
					String currloc = users.getString("currloc");
					//String seecurrloc = users.getString("seecurrloc");
					String contdetails = users.getString("contdetails");

					// Found users with this userId
					inputFirstName = (EditText) findViewById(R.id.firstNameInput);
					inputLastName = (EditText) findViewById(R.id.lastNameInput);
					inputPhone = (EditText) findViewById(R.id.phoneInput);
					inputPostCode = (EditText) findViewById(R.id.postcode);
					inputAddress = (EditText) findViewById(R.id.address);
					inputBirthDate = (EditText) findViewById(R.id.birthInput);
					inputUnder18 = (CheckBox) findViewById(R.id.under18Check);
					inputGender = (RadioGroup) findViewById(R.id.radioSex);
					inputMale = (RadioButton) findViewById(R.id.maleButton);
					inputFemale = (RadioButton) findViewById(R.id.femaleButton);
					inputInterests = (EditText) findViewById(R.id.interestTagsInput);
					inputCurrLoc = (CheckBox) findViewById(R.id.useCurrentLocation);
					//inputSeeCurrLoc = (CheckBox) findViewById(R.id.othersSeeCrntLocation);
					inputContDetails = (CheckBox) findViewById(R.id.othersSeeCntctDetails);

					// Set edit text
					inputFirstName.setText(firstname);
					inputLastName.setText(lastname);
					inputPhone.setText(phone);
					inputAddress.setText(address);
					inputPostCode.setText(postcode);
					inputAddress.setText(address);
					inputBirthDate.setText(birthdate);
					inputInterests.setText(interests);


					if (currloc.equals("true")) {
						inputCurrLoc.setChecked(true);
					} else {
						inputCurrLoc.setChecked(false);
					}

					/*if (seecurrloc.equals("true")) {
						inputSeeCurrLoc.setChecked(true);
					} else {
						inputSeeCurrLoc.setChecked(false);
					}*/

					if (contdetails.equals("true")) {
						inputContDetails.setChecked(true);
					} else {
						inputContDetails.setChecked(false);
					}

					if (under18.equals("true")) {
						inputUnder18.setChecked(true);
					} else {
						inputUnder18.setChecked(false);
					}

					if (gender.equals("Male")) {
						inputMale.setChecked(true);
						inputFemale.setChecked(false);
					} else {
						inputMale.setChecked(false);
						inputFemale.setChecked(true);
					}					

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
				params.put("user_id", userId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);

	}

	/**
	 * Background Async Task to save user profile
	 * */
	class UpdateUserProfile extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UserProfile.this);
			pDialog.setMessage("Saving profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving profile
		 * */
		protected String doInBackground(String... args) {

			// Declare radio group
			inputGender = (RadioGroup) findViewById(R.id.radioSex);

			// Get selected radio button from RadioGroup
			RadioGroup inputGender = (RadioGroup) findViewById(R.id.radioSex);
			int selectedId = inputGender.getCheckedRadioButtonId();

			// Find radio button by selectedId
			RadioButton myOption = (RadioButton) findViewById(selectedId);

			StringBuffer responseText = new StringBuffer();

			// Getting updated data from EditTexts
			String firstname = inputFirstName.getText().toString();
			String lastname = inputLastName.getText().toString();
			String phone = inputPhone.getText().toString();
			String address = inputAddress.getText().toString();
			String postcode = inputPostCode.getText().toString().toUpperCase(Locale.getDefault());			
			String gender = responseText.append(myOption.getText()).toString();
			String interests = inputInterests.getText().toString();
			String birthdate = inputBirthDate.getText().toString();	
			
			boolean bool_under18 = inputUnder18.isChecked();
			String under18 = Boolean.toString(bool_under18);

			boolean bool_currloc = inputCurrLoc.isChecked();
			String currloc = Boolean.toString(bool_currloc);
			
			boolean bool_contdetails = inputContDetails.isChecked();
			String contdetails = Boolean.toString(bool_contdetails);
			
			//String seecurrloc = inputSeeCurrLoc.getText().toString();
			

			// get user data from sessionnn
			HashMap<String, String> user = session.getUserDetails();

			// name
			String userId = user.get(SessionManager.KEY_ID);
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", userId));
			params.add(new BasicNameValuePair("first_name", firstname));
			params.add(new BasicNameValuePair("last_name", lastname));
			params.add(new BasicNameValuePair("phone_nmbr", phone));
			params.add(new BasicNameValuePair("address", address));
			params.add(new BasicNameValuePair("post_code", postcode));
			params.add(new BasicNameValuePair("under_18", under18));
			params.add(new BasicNameValuePair("gender", gender));
			params.add(new BasicNameValuePair("int_tags", interests));
			params.add(new BasicNameValuePair("use_currloc", currloc));
			//params.add(new BasicNameValuePair("see_currloc", seecurrloc));
			params.add(new BasicNameValuePair("see_details", contdetails));
			params.add(new BasicNameValuePair("birth_date", birthdate));

			// Sending updated data through http request
			// Update profile url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_userprofile, "POST", params);

			// Check log cat from response
			Log.d("Create Response", json.toString());

			// Check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// Successfully updated
					Intent intent = getIntent();
					// Send result code 50 to notify about profile update
					setResult(50, intent);
					finish();
					return json.getString(TAG_MESSAGE);
				} else {
					// Failed to update profile
					return json.getString(TAG_MESSAGE);
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
			// Dismiss the dialog once profile updated
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