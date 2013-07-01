package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.dao.JokeDao;
import com.wxk.jokeandroidapp.ui.adapter.JokesAdapter;
import com.wxk.util.LogUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

	private ListView listView;
	private ImageButton imgbRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitleBar();
		initListView();
	}

	private void initListView() {
		imgbRef = (ImageButton) titleBar.findViewById(R.id.titlebar_ref);
		listView = (ListView) findViewById(R.id.list);
		View footer = (View) AppManager.getInstance().getInflater()
				.inflate(R.layout.list_view_footer, null);
		// View header = (View) AppManager.getInstance().getInflater()
		// .inflate(R.layout.list_view_header, null);
		final JokesAdapter adapter = new JokesAdapter(listView,
				null/* header */, footer, R.layout.joke_list_item) {
			class LoadingDataTask extends UtilAsyncTask {
				private JokeDao dao = new JokeDao();
				private int pageSize = 5;

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					preLoadData();
					imgbRef.setVisibility(View.GONE);
					pbLoad.setVisibility(View.VISIBLE);
				}

				@Override
				protected PagerBean<JokeBean> doInBackground(Integer... arg0) {
					int page = arg0[0];
					boolean isDbCahce = arg0[1] == 0;

					PagerBean<JokeBean> pager = new PagerBean<JokeBean>(page,
							pageSize, Integer.MAX_VALUE);
					List<JokeBean> result = dao.getJokes(page, pageSize,
							isDbCahce);
					pager.setResult(result);

					LogUtil.i(TAG, "doInBackground() page=" + page);
					return pager;
				}

				@Override
				protected void onPostExecute(PagerBean<JokeBean> result) {
					super.onPostExecute(result);
					LogUtil.i(TAG, "onPostExecute() " + result);
					imgbRef.setVisibility(View.VISIBLE);
					pbLoad.setVisibility(View.GONE);
				}

			}

			@Override
			public boolean loadingData(int page, boolean isDbCache) {
				if (!isLoadingData) {
					isLoadingData = true;
					LogUtil.i("adapter", "Loading data page " + page);
					(new LoadingDataTask()).execute(page, isDbCache ? 0 : 1);
				}
				return isLoadingData;
			}

			@Override
			public boolean preLoadData() {
				JokeDao dao = new JokeDao();
				bindDatas(dao.getJokesDbCache(1, 5));
				return false;
			}

		};

		// set adapter
		listView.setAdapter(adapter);

		adapter.initListView();
		imgbRef.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				adapter.refreshingData();
			}

		});
	}
}
