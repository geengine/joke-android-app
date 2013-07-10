package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.dao.ReplyDao;
import com.wxk.jokeandroidapp.ui.adapter.ReplysAdapter;
import com.wxk.jokeandroidapp.ui.adapter.JokesAdapter.ViewHolder;
import com.wxk.jokeandroidapp.ui.listener.OperateClickListener;
import com.wxk.jokeandroidapp.ui.util.DisplayUtil;
import com.wxk.jokeandroidapp.ui.util.ImageViewAsyncTask;
import com.wxk.util.GsonUtils;
import com.wxk.util.LogUtil;
import com.wxk.util.BitmapUtil.WrapDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class JokeDetailFragment extends Fragment {
	protected ProgressBar pbLoad;
	protected TextView txtvPageTitle;
	protected ImageButton imgbAppIcon;
	protected ViewGroup titleBar;
	private int jokeid = 0;
	private JokeBean jokeBean;
	private boolean isReplying = false;
	private EditText etxtReplyContent;
	private Handler listViewHandler;
	private ViewHolder viewHolder;
	private ImageButton imgbRef;
	private final static String BEAN_JSON_DATA_EXTRA = "bean_data_extra";
	protected final String TAG = "JokeDetailFragment";

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
		args.putString(BEAN_JSON_DATA_EXTRA, bean_json);
		f.setArguments(args);

		return f;
	}

	private class BaseOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_app_icon:
				AppManager.getInstance().finishActivity();
				break;
			case R.id.btn_submit:
				doReply();
				break;
			}

		}

	}

	private void doReply() {
		if (!AppContext.isNetworkConnected()) {
			showToast(R.string.error_no_network);
			return;
		}
		if (!isReplying) {
			isReplying = true;
			String content = etxtReplyContent.getText().toString();
			if ("".equals(content)) {
				isReplying = false;
				showToast(R.string.toast_reply_empty);
				return;
			}
			(new DoReplyTask(jokeid, content)).execute();
		} else {
			showToast(R.string.toast_reply_exists);
		}

	}

	private class DoReplyTask extends AsyncTask<Void, Void, Boolean> {

		private int jokeid;
		private String content;

		public DoReplyTask(int jokeid, String content) {
			this.jokeid = jokeid;
			this.content = content;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ReplyDao dao = new ReplyDao();
			return dao.doReply(jokeid, content);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			showToast(result ? R.string.toast_reply_success
					: R.string.toast_reply_failure);
			isReplying = result;
			if (result) {
				// refresh list view
				viewHolder.btnComment.setText(""
						+ (Integer.parseInt(viewHolder.btnComment.getText()
								.toString()) + 1));
				listViewHandler.sendEmptyMessage(Constant.REFURBISH);
			}
			imgbRef.setVisibility(View.VISIBLE);

			pbLoad.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imgbRef.setVisibility(View.GONE);
			pbLoad.setVisibility(View.VISIBLE);
		}

	}

	private void initTitleBar(View v) {
		titleBar = (ViewGroup) v.findViewById(R.id.titlebar);
		if (titleBar != null) {
			pbLoad = (ProgressBar) titleBar
					.findViewById(R.id.titlebar_progress);
			txtvPageTitle = (TextView) titleBar
					.findViewById(R.id.titlebar_text);
			imgbAppIcon = (ImageButton) titleBar
					.findViewById(R.id.titlebar_app_icon);
		}

	}

	private void initBtnClick(View v) {

		BaseOnClickListener l = new BaseOnClickListener();
		ImageButton imgbSubmitReply = (ImageButton) v
				.findViewById(R.id.btn_submit);
		etxtReplyContent = (EditText) v.findViewById(R.id.etxt_reply);
		imgbRef = (ImageButton) titleBar.findViewById(R.id.titlebar_ref); // listener
																			// adapter
																			// ref
		imgbAppIcon.setOnClickListener(l);
		imgbSubmitReply.setOnClickListener(l);
	}

	private void initJokeDetailView(View v, JokeBean bean) {
		viewHolder = new ViewHolder();
		View headerDetail = AppManager.getInstance().getInflater()
				.inflate(R.layout.joke_detail, null);
		// View footer =
		// AppManager.getInstance().getInflater().inflate(R.layout.list_view_footer,
		// null);
		viewHolder.txtContent = (TextView) headerDetail
				.findViewById(R.id.txt_jokeContent);
		viewHolder.imgvJokePic = (ImageView) headerDetail
				.findViewById(R.id.imgv_jokeImg);
		txtvPageTitle.setText(bean.getTitle());
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
				viewHolder.imgHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						switch (msg.what) {
						case View.INVISIBLE:
							viewHolder.imgvJokePic.setImageDrawable(null);
							break;
						case View.GONE:
							viewHolder.imgvJokePic.setVisibility(View.GONE);
							break;
						case View.VISIBLE:

							WrapDrawable drawable = (WrapDrawable) msg.obj;
							int w = DisplayUtil
									.getScreenWidth(JokeDetailFragment.this
											.getActivity());// imgv.getWidth();
							float bl = (float) drawable.height
									/ (float) drawable.width;
							ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
									w, (int) (w * bl));
							viewHolder.imgvJokePic.setLayoutParams(params);
							LogUtil.d(TAG, "VISIBLE: w=" + w + ",h=" + w * bl
									+ "," + drawable);
							viewHolder.imgvJokePic
									.setImageDrawable(drawable.drawable);

							viewHolder.imgvJokePic.setVisibility(View.VISIBLE);
							break;
						}
					}

				};
				String url = Constant.BASE_URL + bean.getImgUrl();
				ImageViewAsyncTask imgTask = new ImageViewAsyncTask(
						viewHolder.imgHandler);
				if (!imgTask.showCacheDrawableUrl(url))
					imgTask.execute(url);
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
		// ListView
		ListView listView = (ListView) v.findViewById(R.id.lv_detailList);
		final ReplysAdapter adapter = new ReplysAdapter(bean.getId(), listView,
				headerDetail, null/* footer */, R.layout.reply_item) {
			class LoadingDataTask extends UtilAsyncTask {
				private int jokeId;

				public LoadingDataTask(int jokeId) {
					this.jokeId = jokeId;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					imgbRef.setVisibility(View.GONE);
					pbLoad.setVisibility(View.VISIBLE);
				}

				@Override
				protected PagerBean<ReplyBean> doInBackground(Integer... arg0) {
					int page = arg0[0];
					int size = 20;
					boolean isDbCache = arg0[1] == 0;
					ReplyDao dao = new ReplyDao();
					List<ReplyBean> result = dao.getReplys(jokeId, page, size,
							isDbCache);
					PagerBean<ReplyBean> pager = new PagerBean<ReplyBean>();
					// TODO no page data
					pager.setIndex(1);
					pager.setTotalPage(1);
					pager.setTotalSize(size);
					pager.setResult(result);

					LogUtil.i(TAG, "doInBackground() page=" + page);
					return pager;
				}

				@Override
				protected void onPostExecute(PagerBean<ReplyBean> result) {
					super.onPostExecute(result);
					imgbRef.setVisibility(View.VISIBLE);
					pbLoad.setVisibility(View.GONE);
				}

			}

			@Override
			public boolean loadingData(int page, boolean isDbCache) {
				if (!isLoadingData) {
					isLoadingData = true;
					(new LoadingDataTask(getJokeId())).execute(page,
							isDbCache ? 0 : 1);
				}
				return isLoadingData;
			}

			@Override
			public boolean preLoadData() {
				// TODO Auto-generated method stub
				return false;
			}

		};
		listView.setAdapter(adapter);
		adapter.initListView();
		listViewHandler = adapter.getHandler();
		imgbRef.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.refreshingData();
			}

		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String bean_json = getArguments() != null ? getArguments().getString(
				BEAN_JSON_DATA_EXTRA) : null;
		if (bean_json != null)
			jokeBean = GsonUtils.fromJson(bean_json, JokeBean.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.joke_detail_page, container,
				false);
		initTitleBar(v);
		initBtnClick(v);
		initJokeDetailView(v, jokeBean);
		return v;
	}

}
