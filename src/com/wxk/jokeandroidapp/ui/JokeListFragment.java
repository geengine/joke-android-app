package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.dao.JokeDao;
import com.wxk.jokeandroidapp.ui.adapter.JokesAdapter;
import com.wxk.jokeandroidapp.ui.util.ImageFetcher;
import com.wxk.util.LogUtil;

import android.app.Fragment;
import android.view.View;
import android.widget.ListView;

public class JokeListFragment extends Fragment {
	public final static String TAG = "JokeListFragment";
	private ListView listView;
	private JokesAdapter adapter;
	private ImageFetcher mImageFetcher;

	private void initListView(ListView listView) {
		if (null == listView) {
			throw new NullPointerException("ListView is null");
		}
		View footer = (View) AppManager.getInstance().getInflater()
				.inflate(R.layout.list_view_footer, null);
		// View header = (View) AppManager.getInstance().getInflater()
		// .inflate(R.layout.list_view_header, null);

		adapter = new JokesAdapter(listView, null/* header */, footer,
				R.layout.joke_list_item, mImageFetcher) {
			class LoadingDataTask extends UtilAsyncTask {
				private JokeDao dao = new JokeDao();
				private int pageSize = 5;

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					preLoadData();
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
	}
}
