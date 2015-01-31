package com.compgc02.team26.event;

import java.util.List;

import com.compgc02.samsudin.seek.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Event> eventItems;

	public EventCustomListAdapter(Activity activity, List<Event> eventItems){
		this.activity = activity;
		this.eventItems = eventItems;
	}

	@Override
	public int getCount(){
		return eventItems.size();
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
			convertView = inflater.inflate(R.layout.event_rowlist, parent, false);

		TextView eventId = (TextView) convertView.findViewById(R.id.eventId);
		TextView title = (TextView) convertView.findViewById(R.id.name);
		TextView postcode = (TextView) convertView.findViewById(R.id.postcode);
		TextView distance = (TextView) convertView.findViewById(R.id.distance);

		// Gets venue data
		Event e = eventItems.get(position);

		// Event title
		eventId.setText(e.getId());

		// Event title
		title.setText(e.getTitle());

		// Event postcode
		postcode.setText("Post Code: " + e.getPostcode());

		// Event distance
		distance.setText("Distance: " + String.valueOf(e.getDistance()) + " km");

		return convertView;

	}

}
