package com.wxk.jokeandroidapp.ui.activity.app;

import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.TopicBean;
import com.wxk.jokeandroidapp.services.JokeService;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeListFragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {

	private static final String TAG = "52lxh:MainActivity";
	private int mTopic;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "::onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "::onCreateOptionsMenu()");
		super.mOptionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.i(TAG, "::onPostCreate()");
		// Sync the toggle state after onRestoreInstanceState has occurred.
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG, "::onConfigurationChanged()");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "::onOptionsItemSelected()");

		switch (item.getItemId()) {

		case R.id.action_refresh:
			Log.i(TAG, "onOptionsItemSelected::action_refresh");
			final Intent startService = new Intent(this, JokeService.class);
			startService.putExtra(JokeService.EXTRA_CACHED, false);
			startService.putExtra(JokeService.ARG_JOKE_TOPIC, mTopic);
			startService.putExtra(JokeService.ARG_JOKE_PAGE, 1);
			startService.putExtra(JokeService.EXTRA_REFRESH, true);
			startService.setAction(JokeService.GET_JOKE_DATA_INTENT);

			startService(startService);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "::onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "::onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		Log.i(TAG, "::onResume()");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "::onPause()");
		super.onPause();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "::onDestroy()");
		super.onDestroy();
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final List<TopicBean> topics = TopicBean.getTopics();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return topics.get(position).name;
		}

		@Override
		public int getCount() {
			return topics.size();
		}

		@Override
		public Fragment getItem(int position) {
			return JokeListFragment.newInstance(topics.get(position).id);
		}

	}
}
