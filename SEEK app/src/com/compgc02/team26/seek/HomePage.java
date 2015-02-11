package com.compgc02.team26.seek;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.contact.ContactSearch;
import com.compgc02.team26.event.EventActivity;
import com.compgc02.team26.venue.VenueActivity;

public class HomePage extends SherlockFragmentActivity {

	SessionManager session;
	// url to create session
	private String url_session = "http://seek-app.wc.lt/get_session.php";
	private static final String TAG = HomePage.class.getSimpleName();
	
	String userId;
	String email;
	String currloc;
	String under18;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.home_page);
		getSupportActionBar().hide();

		// Session class instance
		session = new SessionManager(getApplicationContext());
		
		Intent i = getIntent();
		email = i.getStringExtra("email");
		//storeSession(email);

	}

	/**
	 * Create Session by getting data from database
	 *//*
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
				params.put("email_address", email);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);

	}*/

	public void profileButtonOnClick(View v) {

		Intent intent = new Intent(this, UserProfile.class);

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// userId
		userId = user.get(SessionManager.KEY_ID);

		intent.putExtra("userId", userId);
		startActivity(intent);
	}

	public void contactButtonOnClick(View v) {
		Intent intent = new Intent(this, ContactSearch.class);
		startActivity(intent);
	}

	public void eventButtonOnClick(View v) {
		Intent intent = new Intent(this, EventActivity.class);
		startActivity(intent);
	}

	public void venueButtonOnClick(View v) {
		Intent intent = new Intent(this, VenueActivity.class);
		startActivity(intent);
	}

	public void mediaButtonOnClick(View v) {
		Intent intent = new Intent(this, Media.class);
		startActivity(intent);
	}

	public void messageButtonOnClick(View v) {
		Intent intent = new Intent(this, Message.class);
		startActivity(intent);
	}

	public void adminButtonOnClick(View v) {
		Intent intent = new Intent(this, Admin.class);
		startActivity(intent);
	}

	public void logoutButtonOnClick(View v) {
		session.logoutUser();
		;

	}

}