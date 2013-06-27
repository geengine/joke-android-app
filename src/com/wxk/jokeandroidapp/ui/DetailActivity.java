package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.dao.ReplyDao;
import com.wxk.jokeandroidapp.ui.adapter.ReplysAdapter;
import com.wxk.util.LogUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

	private int jokeid = 0;
	private boolean isReplying = false;
	private EditText etxtReplyContent;
	private Handler listViewHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_detail_page);
		initTitleBar();
		initJokeDetailView(getDetailBean());
		initBtnClick();
	}

	private JokeBean getDetailBean() {
		JokeBean bean = new JokeBean();
		Intent intent = getIntent();
		bean.setId(intent.getIntExtra("id", 0));
		bean.setTitle(intent.getStringExtra("title"));
		bean.setContent(intent.getStringExtra("content"));
		bean.setClickCount(intent.getIntExtra("clicks", 0));
		bean.setReplyCount(intent.getIntExtra("replys", 0));
		bean.setGooodCount(intent.getIntExtra("goods", 0));
		bean.setBadCount(intent.getIntExtra("bads", 0));
		jokeid = bean.getId();
		return bean;
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
				listViewHandler.sendEmptyMessage(Constant.REFURBISH);
			}
			pbLoad.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbLoad.setVisibility(View.VISIBLE);
		}

	}

	private void initBtnClick() {
		BaseOnClickListener l = new BaseOnClickListener();
		ImageButton imgbSubmitReply = (ImageButton) findViewById(R.id.btn_submit);
		etxtReplyContent = (EditText) findViewById(R.id.etxt_reply);
		imgbAppIcon.setOnClickListener(l);
		imgbSubmitReply.setOnClickListener(l);
	}

	private void initJokeDetailView(JokeBean bean) {
		View headerDetail = AppManager.getInstance().getInflater()
				.inflate(R.layout.joke_detail, null);
		// View footer =
		// AppManager.getInstance().getInflater().inflate(R.layout.list_view_footer,
		// null);
		TextView txtContent = (TextView) headerDetail
				.findViewById(R.id.txt_jokeContent);
		ImageView imgvJokePic = (ImageView) headerDetail
				.findViewById(R.id.imgv_jokeImg);
		txtvPageTitle.setText(bean.getTitle());
		if (txtContent != null)
			txtContent.setText(bean.getContent());
		if (imgvJokePic != null) {
			if (bean.getImgUrl() != null && !"".equals(bean.getImgUrl())) {
				imgvJokePic.setVisibility(View.GONE);
			} else {
				imgvJokePic.setVisibility(View.VISIBLE);
				// TODO loading image file from url
			}
		}
		// operate button
		Button btnGood = (Button) headerDetail.findViewById(R.id.btn_good);
		Button btnBad = (Button) headerDetail.findViewById(R.id.btn_bad);
		Button btnComment = (Button) headerDetail
				.findViewById(R.id.btn_comment);
		if (btnGood != null) {
			btnGood.setText("" + bean.getGooodCount());
		}
		if (btnBad != null) {
			btnBad.setText("" + bean.getBadCount());
		}
		if (btnComment != null) {
			btnComment.setText("" + bean.getReplyCount());
		}
		// ListView
		ListView listView = (ListView) findViewById(R.id.lv_detailList);
		ReplysAdapter adapter = new ReplysAdapter(bean.getId(), listView,
				headerDetail, null/* footer */, R.layout.reply_item) {
			class LoadingDataTask extends UtilAsyncTask {
				private int jokeId;

				public LoadingDataTask(int jokeId) {
					this.jokeId = jokeId;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
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

		};
		listView.setAdapter(adapter);
		adapter.initListView();
		listViewHandler = adapter.getHandler();
	}
}