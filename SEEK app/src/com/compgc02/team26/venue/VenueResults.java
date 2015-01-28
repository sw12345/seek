package com.compgc02.team26.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.seek.JSONParser;
import com.compgc02.team26.venue.VenueResults.LoadAllVenues;

public class VenueResults extends SherlockFragmentActivity  {

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> venueList;

	/**
	 * URL containing database search. THIS NEEDS TO CHANGE. CREATE ANOTHER PHP FILE FOR SEARCH FUNCTION!!
	 * Try to modify the PHP file for search function.
	 */
	final String url_all_venues = "http://seek.wc.lt/seek/get_userlist_venue.php";

	private ProgressDialog pDialog;

	// JSON nodes name
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_VENUE = "venue";
	private static final String TAG_VID = "venueId";
	private static final String TAG_NAME = "name";
	private static final String TAG_TYPE = "venueType";
	private static final String TAG_MAXCAP = "maxCap";

	// JSONArray venues
	JSONArray venue = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.venue_rowlist);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Hashmap for ListView
		venueList = new ArrayList<HashMap<String, String>>();

		// Loading venues in Background Thread
		new LoadAllVenues().execute();

		// Get listview
		ListView lv = (ListView) findViewById(R.id.list);

		/**
		 * Click list to go for venue details. Please change accordingly.
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Getting values from selected ListItem
				String venueId = ((TextView) view.findViewById(R.id.venueId)).getText().toString();

				// Starting new intent
				Intent intent = new Intent(getApplicationContext(), VenueDetails.class);
				// Sending venueId to next activity
				intent.putExtra(TAG_VID, venueId);

				// Starting new activity
				startActivity(intent);
			}
		});
	}


	/**
	 * Background Async Task to Load all venues by making HTTP Request
	 * */
	class LoadAllVenues extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(VenueResults.this);
			pDialog.setMessage("Loading venues. Please wait....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All venues from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {


				// Getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_all_venues, "GET", params);

				// Check log cat for JSON response
				Log.d("All Venues: ", json.toString());


				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// Venues found
					// Getting array of venues
					venue = json.getJSONArray(TAG_VENUE);

					// Looping through the venues
					for (int i = 0; i < venue.length(); i++) {
						JSONObject c = venue.getJSONObject(i);

						// Storing each json item in a variable
						String venueId = c.getString(TAG_VID);
						String name = c.getString(TAG_NAME);
						String venueType = c.getString(TAG_TYPE);
						String maxCap = c.getString(TAG_MAXCAP);

						// Creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// Adding each child node to HashMap key => value
						map.put(TAG_VID, venueId);
						map.put(TAG_NAME, name);
						map.put(TAG_TYPE, venueType);
						map.put(TAG_MAXCAP, maxCap);

						// adding HashList to ArrayList
						venueList.add(map);
					}
				} else {
					// No venues found
					// Launch create venue activity
					Intent intent = new Intent(getApplicationContext(), VenueCreate.class);

					// Closing all previous activities
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog after getting all venues
			pDialog.dismiss();
			// Updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListView lv = (ListView) findViewById(R.id.list);
					ListAdapter adapter = new SimpleAdapter(
							VenueResults.this, venueList,
							R.layout.venue_rowlist, new String[] {TAG_VID, TAG_NAME, TAG_TYPE, TAG_MAXCAP},
							new int[] { R.id.venueId, R.id.name, R.id.type});
					// Updating listview
					lv.setAdapter(adapter);
				}
			});

		}

	}


	}
