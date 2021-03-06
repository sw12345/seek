package com.compgc02.team26.seek;

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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterPage extends SherlockFragmentActivity implements OnClickListener {

	// Register JSON Parser object
	JSONParser jParser = new JSONParser();

	private ProgressDialog pDialog;

	private EditText inputFirstName;
	private EditText inputLastName;
	private EditText inputEmail;
	private EditText inputEmailCfrm;
	private EditText inputPassword;
	private EditText inputPasswordCfrm;
	private CheckBox checkBox;

	String email_address;
	String userId;
	String email;
	String currloc;
	String under18;

	// url to register user
	private static String url_register = "http://seek-app.wc.lt/register.php";

	private static final String TAG = RegisterPage.class.getSimpleName();

	// url to create session
	private String url_session = "http://seek-app.wc.lt/get_session.php";

	// JSON nodes names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	SessionManager session;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// User input
		inputFirstName = (EditText) findViewById(R.id.firstName);
		inputLastName = (EditText) findViewById(R.id.lastName);
		inputEmail = (EditText) findViewById(R.id.emailInput);
		inputEmailCfrm = (EditText) findViewById(R.id.emailConfirm);
		inputPassword = (EditText) findViewById(R.id.passwordInput);
		inputPasswordCfrm = (EditText) findViewById(R.id.passwordConfirm);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);

		// Open link to the UnLtd privacy & policy of the app
		String checkBoxText = "I agree to all the <a href='https://unltd.org.uk/about_unltd/frequently-asked-questions-and-answers/' > Terms and Conditions</a>";

		checkBox.setText(Html.fromHtml(checkBoxText));
		checkBox.setMovementMethod(LinkMovementMethod.getInstance());

		// Register button
		final Button registerButton = (Button) findViewById(R.id.registerButton);

		// Button click register
		registerButton.setOnClickListener(this);

	}

	// Register button clicked, register new user in background thread
	@Override
	public void onClick (View v) {
		if (v.getId() == R.id.registerButton){
			new RegisterNewUser().execute();
		}

	}

	/**
	 * Background Async Task to register new user
	 * */
	class RegisterNewUser extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterPage.this);
			pDialog.setMessage("Registering new user..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Registering new user
		 * */
		protected String doInBackground(String... args) {

			StringBuffer responseText = new StringBuffer();

			//capture user input
			String first_name = inputFirstName.getText().toString();
			String last_name = inputLastName.getText().toString();
			email_address = inputEmail.getText().toString();
			String email_address_cnfrm = inputEmailCfrm.getText().toString();
			String user_pass = inputPassword.getText().toString();
			String user_pass_cnfrm = inputPasswordCfrm.getText().toString();			
			String terms_cond = responseText.append(checkBox.isChecked()).toString();


			// Building parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("first_name", first_name));
			params.add(new BasicNameValuePair("last_name", last_name));
			params.add(new BasicNameValuePair("email_address", email_address));
			params.add(new BasicNameValuePair("email_address_cnfrm", email_address_cnfrm));
			params.add(new BasicNameValuePair("user_pass", user_pass));
			params.add(new BasicNameValuePair("user_pass_cnfrm", user_pass_cnfrm));
			params.add(new BasicNameValuePair("terms_cond", terms_cond));

			// Getting JSON Object
			// Register user url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_register, "POST", params);

			// Check log cat from response
			Log.d("Register Response", json.toString());

			// Check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {					
					// Successfully registered user	

					storeSession(email_address);
					// get user data from session
					HashMap<String, String> user = session.getUserDetails();

					// userId
					userId = user.get(SessionManager.KEY_ID);
					Intent intent = new Intent(getApplicationContext(), StartPage.class);			
					intent.putExtra("userId", userId);
					startActivity(intent);
					finish();
					return json.getString(TAG_MESSAGE);

				} else {
					// Failed to register user
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
				Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * Create Session by getting data from database
	 */
	private void storeSession(final String email_address) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_session, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject person = new JSONObject(response);
					userId = person.getString("userId");
					email = person.getString("email");
					currloc = person.getString("currentloc");
					under18 = person.getString("under18");

					// Store session
					session.createLoginSession(userId, email, currloc, under18);

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
				params.put("email_address", email_address);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);

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

