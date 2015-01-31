package com.compgc02.team26.contact;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.compgc02.samsudin.seek.R;

public class ContactSearch extends SherlockFragmentActivity {

	private EditText inputRadius;
	private EditText inputPostcode;
	private Button searchBtn;
	private static final String INTENT_KEY = "intentkey";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_search);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));

		inputRadius = (EditText) findViewById(R.id.inputRadius);
		inputPostcode = (EditText) findViewById(R.id.postcode);

		final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupContact);
		searchBtn = (Button) findViewById(R.id.searchButton);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selected = rg.getCheckedRadioButtonId();
				switch (selected) {

				case R.id.radiusRadiobutton:
					// pass the radius to EventResults.class to display the results
					String radius = inputRadius.getText().toString();
					Intent intent2 = new Intent(getApplicationContext(), ContactResultsRadius.class);
					intent2.putExtra(INTENT_KEY, radius);
					startActivity(intent2);
					break;

				case R.id.postcodeRadiobutton:
					// pass the postcode to EventResults.class to display the results
					String postcode = inputPostcode.getText().toString().replace(" ", "");
					Intent intent3 = new Intent(getApplicationContext(), ContactResultsPostcode.class);
					intent3.putExtra(INTENT_KEY, postcode);
					startActivity(intent3);
					break;
				}
			}
		});
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
