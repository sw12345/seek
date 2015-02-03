package com.compgc02.team26.venue;

import java.util.List;

import com.compgc02.samsudin.seek.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VenueCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Venue> venueItems;

	public VenueCustomListAdapter(Activity activity, List<Venue> venueItems){
		this.activity = activity;
		this.venueItems = venueItems;
	}

	@Override
	public int getCount(){
		return venueItems.size();
	}

	@Override
	public Object getItem(int location) {
		return location;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.venue_rowlist, parent, false);

		TextView venueId = (TextView) convertView.findViewById(R.id.venueId);
		venueId.setVisibility(View.GONE); //hidden from interface. Use only for passing data to get details of venue
		TextView title = (TextView) convertView.findViewById(R.id.name);
		TextView type = (TextView) convertView.findViewById(R.id.type);
		TextView maxcap = (TextView) convertView.findViewById(R.id.maxcap);
		TextView distance = (TextView) convertView.findViewById(R.id.distance);

		// Gets venue data
		Venue v = venueItems.get(position);

		// Venue title
		venueId.setText(v.getId());

		// Venue title
		title.setText(v.getTitle());

		// Venue type
		type.setText("Type: " + v.getType());

		// Venue max cap
		maxcap.setText("Max Capacity: " + v.getMaxcap());

		// Venue distance
		distance.setText("Distance: " + String.valueOf(v.getDistance()));

		return convertView;

	}

}
