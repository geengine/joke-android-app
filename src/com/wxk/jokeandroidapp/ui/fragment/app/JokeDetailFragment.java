package com.wxk.jokeandroidapp.ui.fragment.app;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.wxk.jokeandroidapp.Constants;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.services.ReplyService;
import com.wxk.jokeandroidapp.ui.UiManager;
import com.wxk.jokeandroidapp.ui.activity.app.DetailActivity;
import com.wxk.jokeandroidapp.ui.adapter.JokeAdapter.ViewHolder;
import com.wxk.jokeandroidapp.ui.adapter.ReplyAdapter;
import com.wxk.jokeandroidapp.ui.fragment.BaseListFragment;
import com.wxk.jokeandroidapp.ui.listener.OperateClickListener;
import com.wxk.util.GsonUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JokeDetailFragment extends BaseListFragment {
	private JokeBean mJokeBean;

	private ViewHolder viewHolder;
	private final static String EXTRA_JOKE_JSON = "52lxh:extra_joke_json_data";
	private static final String TAG = "52lxh:JokeDetailFragment";
	private JokeReplyReceiver mReplyListReceiver;
	private BaseAdapter mAdapter;
	private List<ReplyBean> mReplyItems;
	private int mPage = 1;
	private boolean mIsAppend;

	private class JokeReplyReceiver extends BroadcastReceiver {
		private static final String TAG = "52lxh:JokeReplyReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "::onReceive()");
			if (isDetached() || isRemoving()) {
				return;
			}
			final boolean isAppend = intent.getBooleanExtra(
					ReplyService.EXTRA_APPEND, false);
			final boolean isCached = intent.getBooleanExtra(
					ReplyService.EXTRA_CACHED, false);
			final boolean isRefresh = intent.getBooleanExtra(
					ReplyService.EXTRA_REFRESH, false);
			final boolean isError = intent.getBooleanExtra(
					ReplyService.EXTRA_ERROR, false);
			final long jokeId = intent
					.getLongExtra(ReplyService.ARG_JOKE_ID, 0);
			final int countAdd = intent.getIntExtra(
					ReplyService.ARG_REPLY_COUNT_ADD, 0);
			if (isError) {
				Toast.makeText(getActivity(), "No network connection.",
						Toast.LENGTH_SHORT).show();
			}
			if (isCached) {
				Log.i(TAG, "cached");
			}
			if (isRefresh) {
				Log.i(TAG, "refresh");
			}
			List<ReplyBean> fetched = ReplyService.loadReplyFromCache(
					getActivity(), jokeId, mPage, 50);
			try {
				if (fetched != null
						&& JokeDetailFragment.this.getListView() != null) {
					Log.i(TAG, "Fetched items:" + fetched.size());
					if (isAppend && mAdapter != null) {
						mReplyItems = new ArrayList<ReplyBean>();
						mReplyItems.addAll(fetched);
						((ReplyAdapter) mAdapter).appendWithItems(mReplyItems);
						mAdapter.notifyDataSetChanged();
						mIsAppend = !isAppend;
					} else {
						mAdapter = new ReplyAdapter(getActivity());
						mReplyItems = new ArrayList<ReplyBean>();
						mReplyItems.addAll(fetched);
						((ReplyAdapter) mAdapter).fillWithItems(mReplyItems);
						if (JokeDetailFragment.this.getListView()
								.getHeaderViewsCount() == 0) {
							JokeDetailFragment.this
									.getListView()
									.addHeaderView(getJokeDetailView(mJokeBean));
						}
						setListAdapter(mAdapter);
						// mAdapter.notifyDataSetChanged();
					}
					if (viewHolder != null) {
						viewHolder.btnComment.setText(""
								+ (mJokeBean.getReplyCount() + countAdd));
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "::onReceive() error= " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onStart() {
		Log.i(TAG, "::onStart()");
		super.onStart();
		if (mReplyListReceiver == null) {
			mReplyListReceiver = new JokeReplyReceiver();
		}
		IntentFilter refreshFilter = new IntentFilter(
				ReplyService.REFRESH_REPLY_UI_INTENT + mJokeBean.getId());
		getActivity().registerReceiver(mReplyListReceiver, refreshFilter);
		getListView().addHeaderView(getJokeDetailView(mJokeBean));
		startService();
	}

	private void startService() {
		final Intent startService = new Intent(getActivity(),
				ReplyService.class);
		startService.putExtra(ReplyService.ARG_JOKE_ID, mJokeBean.getId());
		startService.putExtra(ReplyService.ARG_PAGE, mPage);
		startService.putExtra(ReplyService.EXTRA_APPEND, mIsAppend);
		startService.setAction(ReplyService.GET_REPLY_DATA_INTENT);

		getActivity().startService(startService);
	}

	public JokeDetailFragment() {

	}

	protected void showToast(String showText) {
		Toast toast = Toast.makeText(this.getActivity(), showText,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void showToast(int resId) {
		showToast(this.getString(resId));
	}

	public static JokeDetailFragment newInstance(String bean_json) {
		final JokeDetailFragment f = new JokeDetailFragment();

		final Bundle args = new Bundle();
		args.putString(EXTRA_JOKE_JSON, bean_json);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "::onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
		if (DetailActivity.class.isInstance(getActivity())) {

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "::onCreate()");
		super.onCreate(savedInstanceState);
		String bean_json = getArguments() != null ? getArguments().getString(
				EXTRA_JOKE_JSON) : null;
		if (bean_json != null)
			mJokeBean = GsonUtils.fromJson(bean_json, JokeBean.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_joke_detail,
				container, false);
		Log.i(TAG, "::onCreateView()");
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "::onPause()");
	}

	@Override
	public void onStop() {
		Log.i(TAG, "::onStop()");
		super.onStop();
		try {
			if (mReplyListReceiver != null) {
				getActivity().unregisterReceiver(mReplyListReceiver);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (viewHolder != null && viewHolder.imgvJokePic != null) {
			viewHolder.imgvJokePic.setImageDrawable(null);
		}
	}

	/* ######################################## */

	private View getJokeDetailView(JokeBean bean) {
		viewHolder = new ViewHolder();
		View headerDetail = UiManager.getInstance().getInflater()
				.inflate(R.layout.part_joke_detail, null);
		// View footer =
		// AppManager.getInstance().getInflater().inflate(R.layout.list_view_footer,
		// null);
		viewHolder.txtContent = (TextView) headerDetail
				.findViewById(R.id.txt_jokeContent);
		viewHolder.imgvJokePic = (ImageView) headerDetail
				.findViewById(R.id.imgv_jokeImg);
		if (viewHolder.txtContent != null) {
			if (bean.getContent() != null && !"".equals(bean.getContent())) {
				viewHolder.txtContent.setVisibility(View.VISIBLE);
				viewHolder.txtContent.setText(bean.getContent());
			} else {
				viewHolder.txtContent.setVisibility(View.GONE);
			}
		}

		if (viewHolder.imgvJokePic != null) {
			if (bean.getImgUrl() != null && !"".equals(bean.getImgUrl())) {

				AQuery aq = new AQuery(getActivity());
				aq = aq.id(viewHolder.imgvJokePic).image(
						Constants.BASE_URL + bean.getImgUrl(), true, true, 0,
						0, null, 0, AQuery.RATIO_PRESERVE);

			} else {
				viewHolder.imgvJokePic.setVisibility(View.GONE);
			}
		}
		// operate button
		viewHolder.btnGood = (Button) headerDetail.findViewById(R.id.btn_good);
		viewHolder.btnBad = (Button) headerDetail.findViewById(R.id.btn_bad);
		viewHolder.btnComment = (Button) headerDetail
				.findViewById(R.id.btn_comment);
		OperateClickListener ocl = new OperateClickListener(viewHolder, bean);
		if (viewHolder.btnGood != null) {
			viewHolder.btnGood.setText("" + bean.getGooodCount());
			viewHolder.btnGood.setOnClickListener(ocl);
		}
		if (viewHolder.btnBad != null) {
			viewHolder.btnBad.setText("" + bean.getBadCount());
			viewHolder.btnBad.setOnClickListener(ocl);
		}
		if (viewHolder.btnComment != null) {
			viewHolder.btnComment.setText("" + bean.getReplyCount());
			// viewHolder.btnComment.setOnClickListener(ocl);
		}
		// set header view
		Log.i(TAG, "=>::getJokeDetailView()");
		headerDetail.setVisibility(View.VISIBLE);
		return headerDetail;
	}
}
