package com.compgc02.team26.seek;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.contact.ContactSearch;
import com.compgc02.team26.event.EventActivity;
import com.compgc02.team26.venue.VenueActivity;

public class HomePage extends SherlockFragmentActivity {

	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.home_page);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// Check whether user is logged in or not
		session.checkLogin();
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// name
		String userId = user.get(SessionManager.KEY_ID);

		// email
		String email = user.get(SessionManager.KEY_EMAIL);

		TextView lblName = (TextView) findViewById(R.id.userid);
		TextView lblEmail = (TextView) findViewById(R.id.email);
		lblName.setText(userId);
		lblEmail.setText(email);

	}

	public void profileButtonOnClick(View v) {
		String userId = ((TextView) findViewById(R.id.userid)).getText().toString();		
		Intent intent = new Intent(this, UserProfile.class);
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