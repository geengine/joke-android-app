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
import android.widget.ListView;

public class MainActivity extends BaseActivity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitleBar();
		initListView();
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.list);
		View footer = (View) AppManager.getInstance().getInflater()
				.inflate(R.layout.list_view_footer, null);
		JokesAdapter adapter = new JokesAdapter(listView, null, footer,
				R.layout.joke_list_item) {
			class LoadingDataTask extends UtilAsyncTask {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();

					pbLoad.setVisibility(View.VISIBLE);
				}

				@Override
				protected PagerBean<JokeBean> doInBackground(Integer... arg0) {
					int page = arg0[0];
					boolean isDbCahce = arg0[1] == 0;
					int pageSize = 5;
					JokeDao dao = new JokeDao();
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

		};

		// set adapter
		listView.setAdapter(adapter);

		adapter.initListView();
	}
}
