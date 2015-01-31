package com.compgc02.team26.seek;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.volley.RequestQueue;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	private RequestQueue q;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file userId
	private static final String PREF_NAME = "SeekPref";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User userId (make variable public to access from outside)
	public static final String KEY_ID = "userId";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String userId, String email){
		
		
		

		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing userId in pref
		editor.putString(KEY_ID, userId);
		
		// Storing email in pref
		editor.putString(KEY_EMAIL, email);
		
		// commit changes
		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to start page
			Intent i = new Intent(_context, StartPage.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user userId
		user.put(KEY_ID, pref.getString(KEY_ID, null));
		
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to start page
		Intent i = new Intent(_context, StartPage.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
