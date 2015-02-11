package com.compgc02.team26.event;

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

public class EventUserList extends Fragment {

	SessionManager session;

	// Create JSON Parser object
	JSONParser jParser = new JSONParser();

	// Log tag
	private static final String TAG = EventUserList.class.getSimpleName();

	// Venue json URL
	private static final String url = "http://seek-app.wc.lt/get_userlist_event.php";
	private ProgressDialog pd;
	private List<Event> eventList = new ArrayList<Event>();
	private ListView lv;
	private EventCustomListAdapter adapter;	

	// JSON nodes name
	private static final String TAG_EID = "eventId";

	// JSONArray events
	JSONArray event = null;

	String userId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.event_userlist, container, false);

		// Session class instance
		session = new SessionManager(getActivity());

		// Check whether user is logged in or not
		session.checkLogin();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// name
		userId = user.get(SessionManager.KEY_ID);

		lv = (ListView) rootView.findViewById(R.id.list);
		adapter = new EventCustomListAdapter(getActivity(), eventList);
		lv.setAdapter(adapter);

		pd = new ProgressDialog(getActivity());
		pd.setMessage("Loading..."); // Show this while the list is loading
		pd.show();

		// Create volley request object
		JsonArrayRequest eventReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d(TAG, response.toString());
				hidePDialog();



				// Parsing json
				for (int i = 0; i < response.length(); i++) {
					try {						

						JSONObject obj = response.getJSONObject(i);
						Event event = new Event();
						if(obj.getString("userId").equals(userId)) {
							TextView editEvent = (TextView) getActivity().findViewById(R.id.noList);
							editEvent.setVisibility(View.GONE);
							event.setId(obj.getString("eventId"));
							event.setTitle(obj.getString("name"));
							event.setPostcode(obj.getString("postCode"));
							// event.setDistance(distance); --> query result

							// Adding event to array
							eventList.add(event);	
						} else {
							TextView noList = (TextView) getActivity().findViewById(R.id.noList);
							noList.setText("You have not created any event");
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
		Controller.getInstance().addToRequestQueue(eventReq);

		// On selecting single event, launch edit event activity
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Getting values from selected ListItem
				String eventId = ((TextView) view.findViewById(R.id.eventId)).getText().toString();

				// Starting new intent
				Intent intent = new Intent(getActivity(), EventEdit.class);
				// Sending venueId to next activity
				intent.putExtra(TAG_EID, eventId);

				// Starting new activity and expecting some response back
				startActivityForResult(intent, 50);
			}
		});
		return rootView;
	}
	// Response from Edit Event Activity
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 50) {
			/**
			 *  If result code 50 is received,
			 *  means user edited/deleted event,
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

