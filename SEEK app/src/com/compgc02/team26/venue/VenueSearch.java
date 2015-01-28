package com.compgc02.team26.venue;

import com.compgc02.samsudin.seek.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class VenueSearch extends Fragment {

	private EditText inputRadius;
	private EditText inputPostcode;
	private Button searchBtn;
	private static final String INTENT_KEY = "intentkey";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.venue_search, container, false);

		inputRadius = (EditText) rootView.findViewById(R.id.inputRadius);
		inputPostcode = (EditText) rootView.findViewById(R.id.postcode);

		final RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupVenue);
		searchBtn = (Button) rootView.findViewById(R.id.searchButton);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selected = rg.getCheckedRadioButtonId();
				Bundle b = new Bundle();
				switch (selected) {

				case R.id.radiusRadiobutton:
					// pass the radius to another activity to display the results
					String radius = inputRadius.getText().toString();
					Intent intent2 = new Intent(getActivity(), VenueResultsRadius.class);
					intent2.putExtra(INTENT_KEY, radius);
					startActivity(intent2);
					break;

				case R.id.postcodeRadiobutton:
					// pass the postcode to another activity to display the results
					String postcode = inputPostcode.getText().toString().replace(" ", "");
					Intent intent3 = new Intent(getActivity(), VenueResultsPostcode.class);
					intent3.putExtra(INTENT_KEY, postcode);
					startActivity(intent3);
					break;
				}
			}
		});

		return rootView;
	}


}