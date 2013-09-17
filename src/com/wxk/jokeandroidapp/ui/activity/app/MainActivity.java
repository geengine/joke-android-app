package com.wxk.jokeandroidapp.ui.activity.app;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.db.Topics;
import com.wxk.jokeandroidapp.services.JokeService;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.adapter.DrawerAdapter;
import com.wxk.jokeandroidapp.ui.adapter.DrawerAdapter.DrawerItemData;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeListFragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private static final String TAG = "52lxh:MainActivity";
	private static final String STATE_DRAWER_SELECTION = "52lxh:drawer_selection";
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private BaseAdapter mDrawerAdapter;
	private int mTopic;
	private int oldDrawerSelection = -1;
	private int nowDrawerSelection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "::onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPlanetTitles = Topics.getInstance().getTopicKeys();
		initDrawerNav(savedInstanceState);
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
		R.drawable.action_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(nowDrawerSelection);
		}
	}

	protected FragmentTransaction openFragmentTransaction() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		return fragmentManager.beginTransaction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "::onCreateOptionsMenu()");
		super.mOptionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		MenuItem refresh = menu.findItem(R.id.action_refresh);
		if (refresh != null)
			refresh.setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.i(TAG, "::onPostCreate()");
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG, "::onConfigurationChanged()");
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
		Log.i(TAG, String.format("::selectItem(%s)", position));
		if (position != oldDrawerSelection) {
			// set actionbar refresh
			setRefreshActionButtonState(true);

			mTopic = Topics.getInstance().getTopicID(position);
			Fragment fragment = new JokeListFragment();
			Bundle args = new Bundle();
			args.putInt(JokeListFragment.ARG_JOKE_TOPIC, mTopic);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			((DrawerAdapter) mDrawerAdapter).setSelectedPosition(position);

			mDrawerList.setItemChecked(position, true);

			setTitle(mPlanetTitles[position]);

			oldDrawerSelection = position;
		}
		// auto
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "::onOptionsItemSelected()");
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {

		case R.id.action_refresh:
			Log.i(TAG, "onOptionsItemSelected::action_refresh");
			setRefreshActionButtonState(true);
			final Intent startService = new Intent(this, JokeService.class);
			startService.putExtra(JokeService.ARG_JOKE_TOPIC, mTopic);
			startService.putExtra(JokeService.ARG_JOKE_PAGE, 1);
			startService.putExtra(JokeService.EXTRA_APPEND, false);
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
		oldDrawerSelection = savedInstanceState.getInt(STATE_DRAWER_SELECTION);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "::onSaveInstanceState()");
		if (oldDrawerSelection > 0)
			outState.putInt(STATE_DRAWER_SELECTION, oldDrawerSelection);
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
}
