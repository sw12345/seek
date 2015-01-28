package com.compgc02.team26.venue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.compgc02.samsudin.seek.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class VenueActivity extends SherlockFragmentActivity {

	//title of the tab
	private static final String[] TITLE = new String[] { "Search", "Create", "Edit" };

	FragmentAdapter mAdapter; //show fragment on the page of the selected tab
	ViewPager mPager; //allow user to flip left & right of the page
	PageIndicator mIndicator; //shows what page the user currently on

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tabbed);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mAdapter = new FragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	class FragmentAdapter extends FragmentPagerAdapter {	    
		private int mCount = TITLE.length;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0: return new VenueSearch();
			case 1: return new VenueCreate();
			case 2: return new VenueUserList();
			default: return null;
			} 
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position];
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
