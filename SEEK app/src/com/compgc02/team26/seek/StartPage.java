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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.compgc02.samsudin.seek.R;

public class StartPage extends SherlockFragmentActivity implements OnClickListener {

	public void loginClick(View v) {
		Intent intent = new Intent(this, HomePage.class);
		startActivity(intent);
	}

	public void newUserOnClick (View v) {
		Intent intent = new Intent(this, RegisterPage.class);
		startActivity(intent);
	}

	SessionManager session;
	JSONParser jParser = new JSONParser();

	private static final String TAG = StartPage.class.getSimpleName();
	
	private ProgressDialog pDialog;

	EditText inputEmail;
	EditText inputPassword;
	String email_address;
	String user_pass;
	String userId;
	String email;
	String currloc;
	String under18;

	// url to log user in
	private static String url_login = "http://seek-app.wc.lt/login.php";
	

	// url to create session
	private String url_session = "http://seek-app.wc.lt/get_session.php";

	// JSON nodes names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_page);
		getSupportActionBar().hide();

		// User input
		inputEmail = (EditText) findViewById(R.id.emailInput);
		inputPassword = (EditText) findViewById(R.id.passwordInput);

		// Session class instance
		session = new SessionManager(getApplicationContext());
		
		email_address = inputEmail.getText().toString();

		// Login button
		final Button loginButton = (Button) findViewById(R.id.loginButton);

		// Button click login
		loginButton.setOnClickListener(this);

	}


	// Login button clicked, logging new user in background thread
	@Override
	public void onClick (View v) {
		if (v.getId() == R.id.loginButton){
			new Login().execute();
		}

	}

	/**
	 * Background Async Task to log new user
	 * */
	class Login extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(StartPage.this);
			pDialog.setMessage("Attempting to log in..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Logging new user in
		 * */
		protected String doInBackground(String... args) {

			//capture user input
			email_address = inputEmail.getText().toString();
			user_pass = inputPassword.getText().toString();

			// Building parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email_address", email_address));
			params.add(new BasicNameValuePair("user_pass", user_pass));
			

			// Getting JSON Object
			// Login user url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);

			// Check log cat from response
			Log.d("Login Response", json.toString());

			// Check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {					
					// Successfully logged user	
					Log.d("Login successful!", json.toString());
					Intent intent = new Intent(getApplicationContext(), HomePage.class);
					storeSession(email_address);
					startActivity(intent);
					finish();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login failed!", json.toString());
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
			// Dismiss the dialog once done
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(StartPage.this, file_url, Toast.LENGTH_LONG).show();
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

}
