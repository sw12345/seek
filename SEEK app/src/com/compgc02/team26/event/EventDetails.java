package com.compgc02.team26.event;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventDetails extends SherlockFragmentActivity {

	private TextView txtName;
	private TextView txtStartdate;
	private TextView txtEnddate;
	private TextView txtMaxcap;
	private TextView txtReglink;
	private TextView txtDescription;
	private TextView txtAddress;
	private GoogleMap map;

	String eventId;
	String name;
	String lat;
	String lang;

	private static final String TAG_EID = "eventId";
	private static final String TAG = EventDetails.class.getSimpleName();
	private static final String url_event_details = "http://seek-app.wc.lt/get_event_details.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// Getting event details from intent
		Intent intent = getIntent();

		// Getting event id (eventId) from intent
		eventId = intent.getStringExtra(TAG_EID);

		/**
		 * Getting event details in background thread
		 * */
		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_event_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject event = new JSONObject(response);
					name = event.getString("name");
					String startdate = event.getString("startdate");
					String enddate = event.getString("enddate");
					String regLink = event.getString("regLink");
					String maxCap = event.getString("maxCap");
					String address = event.getString("address");
					String description = event.getString("description");
					lat = event.getString("latitude");
					lang = event.getString("longitude");
					try {
						// Loading map
						initilizeMap();
						
						double latitude = Double.parseDouble(lat);
						double longitude = Double.parseDouble(lang);
						// create marker
						MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);

						// adding marker
						map.addMarker(marker);
						CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build();
						map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

					} catch (Exception e) {
						e.printStackTrace();
					}

					// Found event with this eventId
					txtName = (TextView) findViewById(R.id.txtName);
					txtStartdate = (TextView) findViewById(R.id.txtStartdate);
					txtEnddate = (TextView) findViewById(R.id.txtEnddate);
					txtMaxcap = (TextView) findViewById(R.id.txtMaxcap);
					txtAddress = (TextView) findViewById(R.id.txtAddress);
					txtReglink =(TextView) findViewById(R.id.txtReglink);
					txtDescription = (TextView) findViewById(R.id.txtDescription);

					// Set edit text
					txtName.setText(name);
					txtStartdate.setText(startdate);
					txtEnddate.setText(enddate);
					txtAddress.setText(address);
					txtReglink.setText(regLink);
					txtReglink.setMovementMethod(LinkMovementMethod.getInstance());
					txtMaxcap.setText(maxCap);
					txtDescription.setText(description);
					
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
	 * Load google map
	 */
	private void initilizeMap() {
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (map == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
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
