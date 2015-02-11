package com.compgc02.team26.contact;

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

public class ContactDetails extends SherlockFragmentActivity {

	private TextView txtName;
	private TextView txtEmail;
	private TextView txtGender;
	private TextView txtInterests;
	private TextView txtPhone;
	private TextView txtAge;
	private ImageView callUser;
	private ImageView textUser;
	private ImageView emailUser;

	String gender;
	String interests;
	String age;
	String email;
	String phone;		
	String userId;

	private static final String TAG_UID = "userId";
	private static final String TAG = ContactDetails.class.getSimpleName();
	private static final String url_contact_details = "http://seek-app.wc.lt/get_contact_details.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		// Getting contact details from intent
		Intent intent = getIntent();

		// Getting contact id (userId) from intent
		userId = intent.getStringExtra(TAG_UID);

		// Getting complete contact details in background thread
		getEventDetails(userId);

		// call owner of venue
		callUser = (ImageView) findViewById(R.id.callUser);
		callUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);          
				callIntent.setData(Uri.parse("tel:"+ phone));          
				startActivity(callIntent);
			}
		});

		// text owner of venue
		textUser = (ImageView) findViewById(R.id.textUser);
		textUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);          
				smsIntent.setData(Uri.parse("sms:"+ phone));          
				startActivity(smsIntent);
			}
		});

		// email owner of venue
		emailUser = (ImageView) findViewById(R.id.emailUser);
		emailUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);          
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				startActivity(Intent.createChooser(emailIntent, "Send email to user"));
			}
		});


	}

	/**
	 * Getting contact details in background thread
	 * */
	private void getEventDetails(final String user_id) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_contact_details, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());

				try {

					JSONObject contact = new JSONObject(response);
					String firstname = contact.getString("firstname");
					String lastname = contact.getString("lastname");
					email = contact.getString("email");
					gender = contact.getString("gender");
					interests = contact.getString("interests");					
					phone = contact.getString("phone");
					age = contact.getString("age");

					// Found contact with this userId
					txtName = (TextView) findViewById(R.id.txtName);
					txtEmail = (TextView) findViewById(R.id.txtEmail);
					txtGender = (TextView) findViewById(R.id.txtGender);
					txtInterests = (TextView) findViewById(R.id.txtInterests);
					txtPhone = (TextView) findViewById(R.id.txtPhone);
					txtAge = (TextView) findViewById(R.id.txtAge);

					// Set edit text
					txtName.setText(firstname + " " + lastname);
					txtEmail.setText(email);
					txtGender.setText(gender);
					txtInterests.setText(interests);
					txtPhone.setText(phone);
					txtAge.setText(age);

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
