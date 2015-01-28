package com.compgc02.team26.contact;

import java.util.List;

import com.compgc02.samsudin.seek.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Contact> contactItems;

	public ContactCustomListAdapter(Activity activity, List<Contact> contactItems){
		this.activity = activity;
		this.contactItems = contactItems;
	}

	@Override
	public int getCount(){
		return contactItems.size();
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
			convertView = inflater.inflate(R.layout.contact_rowlist, parent, false);

		TextView userId = (TextView) convertView.findViewById(R.id.userId);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView age = (TextView) convertView.findViewById(R.id.age);
		TextView interests = (TextView) convertView.findViewById(R.id.interests);
		TextView distance = (TextView) convertView.findViewById(R.id.distance);

		// Gets contact data
		Contact c = contactItems.get(position);

		// Contact name
		userId.setText(c.getId());

		// Contact name
		name.setText(c.getName());

		// Contact age
		interests.setText("Interests: " + c.getInterests());

		// Contact distance
		distance.setText("Distance: " + String.valueOf(c.getDistance()) + " km");

		return convertView;

	}

}
