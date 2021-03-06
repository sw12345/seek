package com.compgc02.team26.event;

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


public class EventResultsRadius extends SherlockFragmentActivity implements LocationListener {

	private LocationManager locationmanager;
	private String latitude,longitude;
	GPStracker gps;

	private static final String TAG_EID = "eventId";

	// Log tag
	private static final String TAG = EventResultsRadius.class.getSimpleName();

	// Event json URL
	private static final String url_radius = "http://seek-app.wc.lt/search_event_radius.php";

	private static final String INTENT_KEY = "intentkey";
	private ProgressDialog pd;
	private List<Event> eventList = new ArrayList<Event>();
	private ListView lv;
	private EventCustomListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_userlist);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// GPS
		locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		gps = new GPStracker(EventResultsRadius.this);

		// Get user's current location
		latitude = gps.getLatitude();
		longitude = gps.getLongitude();

		lv = (ListView) findViewById(R.id.list);
		adapter = new EventCustomListAdapter(this, eventList);
		lv.setAdapter(adapter);

		pd = new ProgressDialog(this);
		pd.setMessage("Loading..."); // Show this while the list is loading
		pd.show();

		// Search events within xx radius from user's current location
		Intent intent = getIntent();

		String radius = intent.getStringExtra(INTENT_KEY);
		radiusEvent(radius, latitude, longitude);

		// On selecting single event, launch edit event activity
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Getting values from selected ListItem
				String eventId = ((TextView) view.findViewById(R.id.eventId)).getText().toString();

				// Starting new intent
				Intent intent = new Intent(getApplicationContext(), EventDetails.class);
				// Sending eventId to next activity
				intent.putExtra(TAG_EID, eventId);

				// Starting new activity and expecting some response back
				startActivityForResult(intent, 50);
			}
		});
	}

	// Search events within xx radius from user's current location
	private void radiusEvent(final String radius, final String latitude, final String longitude) {

		StringRequest postReq = new StringRequest(Request.Method.POST, url_radius, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());
				hidePDialog();				

				// Parsing json
				try {
					JSONArray arr = new JSONArray(response);

					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						TextView hideText = (TextView) findViewById(R.id.noList);
						hideText.setVisibility(View.GONE);
						Event event = new Event();						
						event.setId(obj.getString("eventId").toString());
						event.setTitle(obj.getString("name").toString());
						event.setPostcode(obj.getString("postcode").toString());
						event.setDistance(obj.getInt("distance"));

						// Adding event to array
						eventList.add(event);	
					} 
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();	
				}
			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error2: " + error.getMessage());
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
			}

		})  {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("radius", radius);
				params.put("latitude", latitude);
				params.put("longitude", longitude);
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
		Intent intent = new Intent(this, EventDetails.class);
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
		Toast.makeText(getApplicationContext(),"GPS Disable. You need to enable GPS to use this function", Toast.LENGTH_LONG).show();

	}        
}