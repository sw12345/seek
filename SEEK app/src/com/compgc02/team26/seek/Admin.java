package com.compgc02.team26.seek;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.compgc02.team26.event.Event;
import com.compgc02.team26.event.EventResultsPostcode;

public class Admin extends SherlockFragmentActivity {

	SessionManager session;
	String userId;
	private ProgressDialog pd;

	// Log tag
	private static final String TAG = Admin.class.getSimpleName();

	// Deregister URL
	private static final String url_deregister = "http://seek-app.wc.lt/delete_user.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// userId
		userId = user.get(SessionManager.KEY_ID);

		final Button terms = (Button) findViewById(R.id.terms_condition);
		terms.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick(View v) {
				// Clicking this suppose to bring user to terms & conditions.
				// But no official terms & conditions from client yet.
				Uri uri = Uri.parse("https://unltd.org.uk/about_unltd/frequently-asked-questions-and-answers/");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		final Button deregister = (Button) findViewById(R.id.deregister);
		deregister.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick(View v) {
				// Ask user for deregister confirmation
				AlertDialog.Builder alertSet = new AlertDialog.Builder(Admin.this);
				alertSet.setTitle("Delete confirmation");
				alertSet.setMessage("All your data will be deleted and you will no longer be able to use the app's functions. Are you sure you want to deregister?");
				alertSet.setCancelable(false);
				alertSet.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {						
						deleteUser(userId);
						pd = new ProgressDialog(Admin.this);
						pd.setMessage("Loading..."); // Show this while the list is loading
						pd.show();
						session.logoutUser();
					}
				});

				alertSet.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alert = alertSet.create();
				alert.show();
			}
		});
	}

	// Delete user
	private void deleteUser(final String userId) {

		StringRequest postReq2 = new StringRequest(Request.Method.POST, url_deregister, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, response.toString());
				hidePDialog();

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
