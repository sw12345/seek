package com.compgc02.team26.seek;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.contact.ContactSearch;
import com.compgc02.team26.event.EventActivity;
import com.compgc02.team26.venue.VenueActivity;

public class HomePage extends SherlockFragmentActivity {

	SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.home_page);
		getSupportActionBar().hide();

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// Check whether user is logged in or not
		session.checkLogin();

	}

	public void profileButtonOnClick(View v) {
	
		Intent intent = new Intent(this, UserProfile.class);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// userId
		String userId = user.get(SessionManager.KEY_ID);

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