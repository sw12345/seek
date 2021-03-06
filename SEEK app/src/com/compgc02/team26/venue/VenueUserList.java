package com.compgc02.team26.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.compgc02.samsudin.seek.R;
import com.compgc02.team26.seek.Controller;
import com.compgc02.team26.seek.JSONParser;
import com.compgc02.team26.seek.SessionManager;

public class VenueUserList extends Fragment {

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	// Log tag
	private static final String TAG = VenueUserList.class.getSimpleName();

	// Venue json URL
	private static final String url = "http://seek-app.wc.lt/get_userlist_venue.php";
	private ProgressDialog pd;
	private List<Venue> venueList = new ArrayList<Venue>();
	private ListView lv;
	private VenueCustomListAdapter adapter;	

	// JSON nodes name
	private static final String TAG_VID = "venueId";

	// JSONArray venues
	JSONArray venue = null;

	SessionManager session;
	String userId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.venue_userlist, container, false);

		// Session class instance
		session = new SessionManager(getActivity());

		// Check whether user is logged in or not
		session.checkLogin();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// name
		userId = user.get(SessionManager.KEY_ID);

		lv = (ListView) rootView.findViewById(R.id.list);
		adapter = new VenueCustomListAdapter(getActivity(), venueList);
		lv.setAdapter(adapter);

		pd = new ProgressDialog(getActivity());
		pd.setMessage("Loading..."); // Show this while the list is loading
		pd.show();

		// Create volley request object
		JsonArrayRequest venueReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d(TAG, response.toString());
				hidePDialog();
								
				// Parsing json
				for (int i = 0; i < response.length(); i++) {
					try {

						JSONObject obj = response.getJSONObject(i);
						Venue venue = new Venue();
						if(obj.getString("userId").equals(userId)) {
							TextView editEvent = (TextView) getActivity().findViewById(R.id.noList);
							editEvent.setVisibility(View.GONE);					
							venue.setId(obj.getString("venueId"));
							venue.setTitle(obj.getString("name"));
							venue.setType(obj.getString("venueType"));
							venue.setPostcode(obj.getString("postCode"));
							venue.setMaxcap(obj.getString("maxCap"));
							// venue.setDistance(distance); --> query result

							// Adding venue to array
							venueList.add(venue);	
						} else {
							TextView noList = (TextView) getActivity().findViewById(R.id.noList);
							noList.setText("You have not created any venue");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				// notifying list adapter about data changes
				// so that it renders the list view with updated data
				adapter.notifyDataSetChanged();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				hidePDialog();

			}
		});

		// Adding request to request queue
		Controller.getInstance().addToRequestQueue(venueReq);

		// On selecting single venue, launch edit venue activity
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Getting values from selected ListItem
				String venueId = ((TextView) view.findViewById(R.id.venueId)).getText().toString();

				// Starting new intent
				Intent intent = new Intent(getActivity(), VenueEdit.class);
				// Sending venueId to next activity
				intent.putExtra(TAG_VID, venueId);

				// Starting new activity and expecting some response back
				startActivityForResult(intent, 100);
			}
		});
		return rootView;
	}

	// Response from Edit Venue Activity
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 100) {
			/**
			 *  If result code 100 is received,
			 *  means user edited/deleted venue,
			 *  reload this screen again
			 */			 
			Intent intent = getActivity().getIntent();
			getActivity().finish();
			startActivity(intent);
		}

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

}

