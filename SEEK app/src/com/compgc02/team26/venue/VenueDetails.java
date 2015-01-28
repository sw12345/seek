package com.compgc02.team26.venue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

public class VenueDetails extends SherlockFragmentActivity {

	private TextView txtName;
	private TextView txtType;
	private TextView txtAddress;
	private TextView txtMaxcap;
	private TextView txtDescription;
	
	String venueId;

	private static final String TAG_VID = "venueId";
	private static final String TAG = VenueDetails.class.getSimpleName();
	private static final String url_venue_details = "http://seek.wc.lt/seek/get_venue_details.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		// Getting venue details from intent
		Intent intent = getIntent();

		// Getting venue id (venueId) from intent
		venueId = intent.getStringExtra(TAG_VID);

		// Getting complete venue details in background thread
		getVenueDetails(venueId);
	}

	/**
	 * Getting venue details in background thread
	 * */
	private void getVenueDetails(final String venue_id) {

		//rq = Volley.newRequestQueue(this);

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_venue_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject venue = new JSONObject(response);
					String name = venue.getString("name");
					String type = venue.getString("type");
					String address = venue.getString("address");
					String maxCap = venue.getString("maxCap");
					String description = venue.getString("description");

					// Found venue with this venueId
					txtName = (TextView) findViewById(R.id.txtName);
					txtType = (TextView) findViewById(R.id.txtType);
					txtAddress = (TextView) findViewById(R.id.txtAddress);
					txtMaxcap = (TextView) findViewById(R.id.txtMaxcap);
					txtAddress = (TextView) findViewById(R.id.txtAddress);
					txtDescription = (TextView) findViewById(R.id.txtDescription);

					// Set edit text
					txtName.setText(name);
					txtType.setText(type);
					txtAddress.setText(address);
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
				params.put("venue_id", venueId);
				return params;
			}

		};
		Controller.getInstance().addToRequestQueue(postReq2);
		//rq.add(postReq2);

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
