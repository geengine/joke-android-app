package com.wxk.jokeandroidapp.ui.fragment.app;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.db.Topics;
import com.wxk.jokeandroidapp.ui.activity.app.DetailActivity;
import com.wxk.jokeandroidapp.ui.activity.app.MainActivity;
import com.wxk.jokeandroidapp.ui.adapter.JokeListAdapter;
import com.wxk.jokeandroidapp.ui.fragment.BaseListFragment;
import com.wxk.jokeandroidapp.ui.loader.JokeLoader;
import com.wxk.jokeandroidapp.services.JokeService;
import com.wxk.util.LogUtil;

public class JokeListFragment extends BaseListFragment implements
		OnScrollListener, LoaderManager.LoaderCallbacks<List<JokeBean>> {
	public static final String ARG_PLANET_NUMBER = "planet_number";
	private static final String TAG = "52lxh:JokeListFragment";
	private BaseAdapter mAdapter;
	private List<JokeBean> mJokeItems;
	private JokeListReceiver mJokeListReceiver;
	private int mPage = 1;
	private int mTopic = 0;
	private boolean mIsAppend = false;

	private class JokeListReceiver extends BroadcastReceiver {
		private static final String TAG = "52lxh:JokeListFragment>>JokeListReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "::onReceive()");
			if (isDetached() || isRemoving()) {
				return;
			}
			final boolean isAppend = intent.getBooleanExtra(
					JokeService.EXTRA_APPEND, false);
			final boolean isCached = intent.getBooleanExtra(
					JokeService.EXTRA_CACHED, false);
			final boolean isError = intent.getBooleanExtra(
					JokeService.EXTRA_SERVER_ERROR, false);
			List<JokeBean> fetched = JokeService.loadJokeFromCache(
					getActivity(), mTopic, mPage);
			if (isError) {
				Toast.makeText(getActivity(), "No network connection.",
						Toast.LENGTH_SHORT).show();
			}
			if (isCached) {
				Log.i(TAG, "cached");
			}
			if (fetched != null) {
				if (isAppend && mAdapter != null) {
					mJokeItems = new ArrayList<JokeBean>();
					mJokeItems.addAll(fetched);

					((JokeListAdapter) mAdapter).appendWithItems(mJokeItems);
					mAdapter.notifyDataSetChanged();
					mIsAppend = !isAppend;
				} else {
					mAdapter = new JokeListAdapter(getActivity());
					mJokeItems = new ArrayList<JokeBean>();
					mJokeItems.addAll(fetched);
					((JokeListAdapter) mAdapter).fillWithItems(mJokeItems);
					mAdapter.notifyDataSetChanged();
					setListAdapter(mAdapter);
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "::onCreate()");
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
		final Bundle args = getArguments();
		if (args != null) {
			final int i = args.getInt(ARG_PLANET_NUMBER);
			mTopic = Topics.getInstance().getTopicID(i);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "::onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
		if (MainActivity.class.isInstance(getActivity())) {
		}
		getListView().setOnScrollListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		// getLoaderManager().restartLoader(mTopic, null, this);

		Log.i(TAG, "::onStart()");
		if (mJokeListReceiver == null) {
			mJokeListReceiver = new JokeListReceiver();
		}
		IntentFilter refreshFilter = new IntentFilter(
				JokeService.REFRESH_JOKE_UI_INTENT);
		getActivity().registerReceiver(mJokeListReceiver, refreshFilter);

		startService();
	}

	private void startService() {
		final Intent startService = new Intent(getActivity(), JokeService.class);
		startService.putExtra(JokeService.ARG_JOKE_TOPIC, mTopic);
		startService.putExtra(JokeService.ARG_JOKE_PAGE, mPage);
		startService.putExtra(JokeService.EXTRA_APPEND, mIsAppend);
		startService.setAction(JokeService.GET_JOKE_DATA_INTENT);

		getActivity().startService(startService);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "::onCreateView()");
		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_joke_list, null);
		return view;
	}

	@Override
	public void onPause() {
		Log.i(TAG, "::onPause()");
		super.onPause();

	}

	@Override
	public void onStop() {
		Log.i(TAG, "::onStop()");
		super.onStop();
		try {
			if (mJokeListReceiver != null) {
				getActivity().unregisterReceiver(mJokeListReceiver);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Loader<List<JokeBean>> onCreateLoader(int arg0, Bundle arg1) {
		Log.i(TAG, "::onCreateLoader()");

		return new JokeLoader(getActivity(), mTopic);
	}

	@Override
	public void onLoadFinished(Loader<List<JokeBean>> listLoader,
			List<JokeBean> items) {
		Log.i(TAG, "::onLoadFinished()");
		mJokeItems = new ArrayList<JokeBean>();
		mJokeItems.addAll(items);
		mAdapter = new JokeListAdapter(this.getActivity());
		((JokeListAdapter) mAdapter).fillWithItems(mJokeItems);
		mAdapter.notifyDataSetChanged();
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i(TAG, String.format("position=%s, id=%s", position, id));
		Intent intentDetail = new Intent();
		intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentDetail.putExtra(DetailActivity.EXTRA_JOKE_ID,
				mAdapter.getItemId(position));

		intentDetail.setClass(AppContext.context, DetailActivity.class);
		AppContext.context.startActivity(intentDetail);
	}

	@Override
	public void onLoaderReset(Loader<List<JokeBean>> arg0) {
		Log.i(TAG, "::onLoaderReset()");
		mJokeItems.clear();
	}

	private void loadingMore() {
		if (!mIsAppend) {
			mIsAppend = true;
			mPage = mPage + 1;
			startService();
		}
	}

	private int mLastItem;
	private int mFirstItem;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItem = firstVisibleItem;// - (isFooter ? 1 : 0) - (isHeader ? 1 :
										// 0);
		mLastItem = firstVisibleItem + visibleItemCount;

		// LogUtil.i(TAG, "firstItem=" + firstItem + " ,lastItem=" + lastItem);
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				&& mLastItem >= mAdapter.getCount() - 4) {
			LogUtil.i(TAG, "loading more ...");
			loadingMore();
		}
		if (scrollState == SCROLL_STATE_IDLE && mFirstItem == 0) {
		}
	}

}
