package com.compgc02.team26.seek;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.compgc02.samsudin.seek.R;

public class Media extends SherlockFragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f58021")));
	}

	public void onClick1(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/getting-started/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick2(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/finance/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick3(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/legal-resources/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick4(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/marketing-resources/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick5(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/mentoring-resources/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick6(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/staff-and-recruitment/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick7(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/growing-your-venture/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick8(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/working-with-social-entrepreneurs/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void onClick9(View v) {
		Uri uri = Uri.parse("https://unltd.org.uk/category/resources/webinar-2014/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
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


