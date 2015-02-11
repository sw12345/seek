package com.compgc02.team26.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
import com.compgc02.team26.seek.Controller;
import com.compgc02.team26.seek.GPStracker;
import com.compgc02.team26.seek.SessionManager;


public class ContactResultsRadius extends SherlockFragmentActivity implements LocationListener {

	private LocationManager locationmanager;
	private String latitude,longitude;
	GPStracker gps;

	private static final String TAG_UID = "userId";

	// Log tag
	private static final String TAG = ContactResultsRadius.class.getSimpleName();

	// json URL
	private static final String url_radius_above18 = "http://seek-app.wc.lt/search_contact_radius_above18.php";
	private static final String url_radius_under18 = "http://seek-app.wc.lt/search_contact_radius_under18.php";

	private static final String INTENT_KEY = "intentkey";
	private ProgressDialog pd;
	private List<Contact> contactList = new ArrayList<Contact>();
	private ListView lv;
	private ContactCustomListAdapter adapter;

	String userId;
	String under18;
	String age;
	int user_age;

	SessionManager session;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_userlist);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// GPS
		locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		gps = new GPStracker(ContactResultsRadius.this);

		// Get user's current location
		latitude = gps.getLatitude();
		longitude = gps.getLongitude();

		lv = (ListView) findViewById(R.id.list);
		adapter = new ContactCustomListAdapter(this, contactList);
		lv.setAdapter(adapter);

		pd = new ProgressDialog(this);
		pd.setMessage("Loading..."); // Show this while the list is loading
		pd.show();

		session = new SessionManager(getApplicationContext());

		// Check whether user is logged in or not
		session.checkLogin();

		// Get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userId = user.get(SessionManager.KEY_ID);
		under18 = user.get(SessionManager.KEY_UNDER18);
		age = user.get(SessionManager.KEY_AGE);

		user_age = Integer.parseInt(age);

		// Search events within xx radius from user's current location
		Intent intent = getIntent();

		String radius = intent.getStringExtra(INTENT_KEY);		

		if ((under18.equals("false") || under18.equals("true")) && user_age >18) {
			radiusContact_above18(radius, latitude, longitude, userId);
		} else if (under18.equals("false") && user_age <18){
			radiusContact_above18(radius, latitude, longitude, userId);
		} else if (under18.equals("true") && user_age <18){
			radiusContact_under18(radius, latitude, longitude, userId);
		}

		// On selecting single contact, launch edit contact activity
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Getting values from selected ListItem
				userId = ((TextView) view.findViewById(R.id.userId)).getText().toString();

				// Starting new intent
				Intent intent = new Intent(getApplicationContext(), ContactDetails.class);
				// Sending venueId to next activity
				intent.putExtra(TAG_UID, userId);

				// Starting new activity and expecting some response back
				startActivityForResult(intent, 50);
			}
		});
	}

	// Search events within xx radius from user's current location (display user above 18)
	private void radiusContact_above18(final String radius, final String latitude, final String longitude, final String userId) {

		StringRequest postReq = new StringRequest(Request.Method.POST, url_radius_above18, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());
				hidePDialog();

				// Parsing json
				try {
					JSONArray arr = new JSONArray(response);

					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						Contact contact = new Contact();
						TextView hideText = (TextView) findViewById(R.id.noList);
						hideText.setVisibility(View.GONE);
						contact.setId(obj.getString("userId"));
						contact.setName(obj.getString("firstname")+" "+obj.getString("lastname"));
						contact.setAge(obj.getString("age"));
						contact.setInterests(obj.getString("interesttags"));
						contact.setDistance(obj.getInt("distance"));

						// Adding contact to array
						contactList.add(contact);	
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();	
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
				params.put("radius", radius);
				params.put("latitude", latitude);
				params.put("longitude", longitude);
				params.put("user_id", userId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq);
	}

	// Search events within xx radius from user's current location (display user of all age)
	private void radiusContact_under18(final String radius, final String latitude, final String longitude, final String userId) {

		StringRequest postReq = new StringRequest(Request.Method.POST, url_radius_under18, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());
				hidePDialog();

				// Parsing json
				try {
					JSONArray arr = new JSONArray(response);

					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						Contact contact = new Contact();
						TextView hideText = (TextView) findViewById(R.id.noList);
						hideText.setVisibility(View.GONE);
						contact.setId(obj.getString("userId"));
						contact.setName(obj.getString("firstname")+" "+obj.getString("lastname"));
						contact.setAge(obj.getString("age"));
						contact.setInterests(obj.getString("interesttags"));
						contact.setDistance(obj.getInt("distance"));

						// Adding contact to array
						contactList.add(contact);	
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();	
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
				params.put("radius", radius);
				params.put("latitude", latitude);
				params.put("longitude", longitude);
				params.put("user_id", userId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}

	public void eventDetails1(View v) {
		Intent intent = new Intent(this, ContactDetails.class);
		startActivity(intent);
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

	@Override
	public void onLocationChanged(Location location) {
		latitude=String.valueOf(location.getLatitude());
		longitude=String.valueOf(location.getLongitude());

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do nothing here

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getApplicationContext(),"GPS enabled", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getApplicationContext(),"GPS Disable", Toast.LENGTH_LONG).show();

	}        
}