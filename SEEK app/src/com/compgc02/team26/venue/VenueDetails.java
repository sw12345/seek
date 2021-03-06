package com.compgc02.team26.venue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
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

public class VenueDetails extends SherlockFragmentActivity {

	private TextView txtName;
	private TextView txtType;
	private TextView txtAddress;
	private TextView txtMaxcap;
	private TextView txtDescription;
	private TextView txtEmail;
	private TextView txtPhone;
	private ImageView callOwner;
	private ImageView textOwner;
	private ImageView emailOwner;
	private GoogleMap map;

	String name;
	String type;
	String address;
	String maxCap;
	String description;
	String email;
	String phone;	
	String venueId;
	String eventId;
	String lat;
	String lang;


	private static final String TAG_VID = "venueId";
	private static final String TAG = VenueDetails.class.getSimpleName();
	private static final String url_venue_details = "http://seek-app.wc.lt/get_venue_details.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// Getting venue details from intent
		Intent intent = getIntent();

		// Getting venue id (venueId) from intent
		venueId = intent.getStringExtra(TAG_VID);

		// Getting complete venue details in background thread
		getVenueDetails(venueId);

		// call owner of venue
		callOwner = (ImageView) findViewById(R.id.callOwner);
		callOwner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);          
				callIntent.setData(Uri.parse("tel:"+ phone));          
				startActivity(callIntent);
			}
		});

		// text owner of venue
		textOwner = (ImageView) findViewById(R.id.textOwner);
		textOwner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);          
				smsIntent.setData(Uri.parse("sms:"+ phone));          
				startActivity(smsIntent);
			}
		});

		// email owner of venue
		emailOwner = (ImageView) findViewById(R.id.emailOwner);
		emailOwner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);          
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				startActivity(Intent.createChooser(emailIntent, "Send email to owner"));
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
					name = venue.getString("name");
					type = venue.getString("type");
					address = venue.getString("address");
					maxCap = venue.getString("maxCap");
					description = venue.getString("description");
					email = venue.getString("email");
					phone = venue.getString("phone");
					lat = venue.getString("latitude");
					lang = venue.getString("longitude");
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

					// Found venue with this venueId
					txtName = (TextView) findViewById(R.id.txtName);
					txtType = (TextView) findViewById(R.id.txtType);
					txtAddress = (TextView) findViewById(R.id.txtAddress);
					txtMaxcap = (TextView) findViewById(R.id.txtMaxcap);
					txtAddress = (TextView) findViewById(R.id.txtAddress);
					txtDescription = (TextView) findViewById(R.id.txtDescription);
					txtEmail = (TextView) findViewById(R.id.txtEmail);
					txtPhone = (TextView) findViewById(R.id.txtPhone);

					// Set edit text
					txtName.setText(name);
					txtType.setText(type);
					txtAddress.setText(address);
					txtMaxcap.setText(maxCap);
					txtDescription.setText(description);
					txtEmail.setText(email);
					txtPhone.setText(phone);

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
