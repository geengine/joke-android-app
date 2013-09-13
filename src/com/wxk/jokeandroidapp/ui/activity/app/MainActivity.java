package com.wxk.jokeandroidapp.ui.activity.app;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.db.Topics;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.adapter.DrawerAdapter;
import com.wxk.jokeandroidapp.ui.adapter.DrawerAdapter.DrawerItemData;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeListFragment;
import com.wxk.jokeandroidapp.ui.util.ImageFetcher;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends BaseActivity {

	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private BaseAdapter mDrawerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPlanetTitles = Topics.getInstance().getTopicKeys();
		initDrawerNav(savedInstanceState);
	}

	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

	private DrawerAdapter getDeawerAdapter() {

		DrawerAdapter adapter = new DrawerAdapter(this,
				R.layout.drawer_list_item) {

			@Override
			protected Object initViewHolder(DrawerItemData bean, View view,
					Object viewHolder, int position) {
				// TODO Auto-generated method stub
				ViewHolder newHolder;
				if (viewHolder == null) {
					newHolder = new ViewHolder();
					newHolder.text = (TextView) view
							.findViewById(android.R.id.text1);
				} else {
					newHolder = (ViewHolder) viewHolder;
				}
				if (newHolder.text != null) {
					newHolder.text.setText(bean.getTitle());
				}
				if (position == mSelectedPosition) {
					newHolder.text.setSelected(true);
					newHolder.text.setBackgroundColor(MainActivity.this
							.getResources().getColor(
									R.color.navigation_drawer_item_sel_bg));
				} else {
					newHolder.text.setSelected(false);
					newHolder.text.setBackgroundColor(MainActivity.this
							.getResources().getColor(
									R.color.navigation_drawer_item_bg));
				}
				return newHolder;
			}

			class ViewHolder {
				public TextView text;
			}
		};
		for (String item : mPlanetTitles) {
			adapter.addItem(new DrawerItemData(item, false));
		}
		return adapter;
	}

	private void initDrawerNav(Bundle savedInstanceState) {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerAdapter = getDeawerAdapter();
		mDrawerList.setAdapter(mDrawerAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		//
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		//

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	protected FragmentTransaction openFragmentTransaction() {
		FragmentManager fragmentManager = getFragmentManager();
		return fragmentManager.beginTransaction();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Log.i(TAG, "selectItem: " + position);
		Fragment fragment = new JokeListFragment();
		Bundle args = new Bundle();
		args.putInt(JokeListFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		((DrawerAdapter) mDrawerAdapter).setSelectedPosition(position);
		mDrawerList.setItemChecked(position, true);

		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);

		MenuItem menuItem = menu.findItem(R.id.action_about);

		MenuItemCompat.setOnActionExpandListener(menuItem,
				new OnActionExpandListener() {

					@Override
					public boolean onMenuItemActionCollapse(MenuItem arg0) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onMenuItemActionExpand(MenuItem arg0) {
						// TODO Auto-generated method stub
						return false;
					}

				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}
}
