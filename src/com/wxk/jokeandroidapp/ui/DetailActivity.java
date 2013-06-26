package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.dao.ReplyDao;
import com.wxk.jokeandroidapp.ui.adapter.ReplysAdapter;
import com.wxk.util.LogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

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
		return bean;
	}

	private static class BaseOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_app_icon:
				AppManager.getInstance().finishActivity();
				break;
			}

		}

	}

	private void initBtnClick() {
		BaseOnClickListener l = new BaseOnClickListener();
		imgbAppIcon.setOnClickListener(l);
	}

	private void initJokeDetailView(JokeBean bean) {
		View headerDetail = AppManager.getInstance().getInflater()
				.inflate(R.layout.joke_detail, null);
		View footer = AppManager.getInstance().getInflater()
				.inflate(R.layout.list_view_footer, null);
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

		ListView listView = (ListView) findViewById(R.id.lv_detailList);
		ReplysAdapter adapter = new ReplysAdapter(bean.getId(), listView,
				headerDetail, footer, R.layout.reply_item) {
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
	}
}
