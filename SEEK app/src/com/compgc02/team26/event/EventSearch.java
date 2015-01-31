package com.compgc02.team26.event;

import com.compgc02.samsudin.seek.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class EventSearch extends Fragment {

	private EditText inputRadius;
	private EditText inputPostcode;
	private Button searchBtn;
	private Intent intent;
	private static final String INTENT_KEY = "intentkey";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.event_search, container, false);

		inputRadius = (EditText) rootView.findViewById(R.id.inputRadius);
		inputPostcode = (EditText) rootView.findViewById(R.id.postcode);

		final RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupEvent);
		searchBtn = (Button) rootView.findViewById(R.id.searchButton);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selected = rg.getCheckedRadioButtonId();
				switch (selected) {

				case R.id.radiusRadiobutton:
					// pass the radius to another activity to display the results
					String radius = inputRadius.getText().toString();
					intent = new Intent(getActivity(), EventResultsRadius.class);
					intent.putExtra(INTENT_KEY, radius);
					startActivity(intent);
					break;

				case R.id.postcodeRadiobutton:
					// pass the postcode to another activity to display the results
					String postcode = inputPostcode.getText().toString().replace(" ", "");
					intent = new Intent(getActivity(), EventResultsPostcode.class);
					intent.putExtra(INTENT_KEY, postcode);
					startActivity(intent);
					break;
				}
			}
		});

		return rootView;
	}

}
