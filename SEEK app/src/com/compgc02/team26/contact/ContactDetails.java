package com.compgc02.team26.contact;

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

public class ContactDetails extends SherlockFragmentActivity {

	private TextView txtName;
	private TextView txtEmail;
	private TextView txtGender;
	private TextView txtInterests;

	
	String userId;

	private static final String TAG_UID = "userId";
	private static final String TAG = ContactDetails.class.getSimpleName();
	private static final String url_contact_details = "http://seek.wc.lt/seek/get_contact_details.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		// Getting event details from intent
		Intent intent = getIntent();

		// Getting event id (userId) from intent
		userId = intent.getStringExtra(TAG_UID);

		// Getting complete event details in background thread
		getEventDetails(userId);
	}

	/**
	 * Getting event details in background thread
	 * */
	private void getEventDetails(final String user_id) {

		//rq = Volley.newRequestQueue(this);

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_contact_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject event = new JSONObject(response);
					String firstname = event.getString("firstname");
					String lastname = event.getString("lastname");
					String email = event.getString("email");
					String gender = event.getString("gender");
					String interests = event.getString("interests");
					
					// Found event with this userId
					txtName = (TextView) findViewById(R.id.txtName);
					txtEmail = (TextView) findViewById(R.id.txtEmail);
					txtGender = (TextView) findViewById(R.id.txtGender);
					txtInterests = (TextView) findViewById(R.id.txtInterests);

					// Set edit text
					txtName.setText(firstname + " " + lastname);
					txtEmail.setText(email);
					txtGender.setText(gender);
					txtInterests.setText(interests);

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
