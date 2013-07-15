package com.wxk.jokeandroidapp.ui.adapter;

import java.util.List;

import com.wxk.jokeandroidapp.AppManager;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.jokeandroidapp.ui.listener.BaseOnScrollListener;
import com.wxk.util.LogUtil;
import com.wxk.util.UniqueList;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @author fengpengbin@gmail.com
 * 
 * @param <E>
 */
public abstract class UtilAdapter<E> extends BaseAdapter {

	private static final String TAG = "UtilAdapter";
	protected List<E> datas;
	protected ListView listView;
	protected View header;
	protected View footer;
	protected int item_layout;
	protected long total_pager = 0;
	protected long total_count = 0;
	protected int cur_page = 1;
	protected boolean isLoadingData = false;
	protected boolean isRefreshingData = false;
	protected Handler handler;

	public UtilAdapter(ListView listView, View header, View footer,
			int itemLayout) {
		this(listView, header, footer, itemLayout, null);
	}

	public UtilAdapter(ListView listView, View header, View footer,
			int itemLayout, Handler handler) {

		this.datas = new UniqueList<E>();
		this.listView = listView;
		this.header = header;
		this.footer = footer;
		this.item_layout = itemLayout;
		this.handler = handler;

		if (null != header) {
			this.listView.addHeaderView(this.header);
		}
		if (null != footer) {
			this.listView.addFooterView(this.footer);
		}

	}

	public void initListView() {
		BaseOnScrollListener l = new BaseOnScrollListener(this,
				this.getHandler(), null != header, null != footer);
		listView.setOnScrollListener(l);
		preInitListView();
		refreshingData();
	}

	public void preInitListView() {
	}

	public Handler getHandler() {
		if (null == handler)
			handler = createHandler();
		return handler;
	}

	@SuppressLint("HandlerLeak")
	protected Handler createHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.LOADING:
					loadingMoreData();
					break;
				case Constant.REFURBISH:

					refreshingData();
					break;
				}
			}

		};
	}

	protected void showListHeader() {
		if (null != header) {
			header.setVisibility(View.VISIBLE);
		}
	}

	protected void hideListHeader() {
		if (null != header) {
			header.setVisibility(View.GONE);
		}
	}

	protected void hideListFooter() {
		if (null != footer) {
			footer.setVisibility(View.GONE);
		}
	}

	protected void showListFooter() {
		if (null != footer) {
			footer.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * @param page
	 * @return
	 */
	public abstract boolean loadingData(final int page, final boolean isDbCache);

	public abstract boolean preLoadData();

	public boolean loadingData(final int page) {
		return loadingData(page, true);
	}

	public void refreshingData() {
		if (!isRefreshingData) {
			isRefreshingData = true;
			cur_page = 1;
			loadingData(cur_page, false);
		}
	}

	public void loadingMoreData() {

		if (!this.isLoadingData) {
			if (cur_page < total_pager) {
				showListFooter();
				cur_page++;
				loadingData(cur_page, true);
				LogUtil.d(TAG, "Loading page : " + cur_page);
			} else {
				LogUtil.d(TAG, "not loading more data !");
			}
		}
	}

	public void bindDatas(List<E> data) {
		if (data != null && data.size() > 0) {
			if (isRefreshingData) {
				this.datas.clear();
				boolean ret = this.datas.addAll(data);
				if (ret) {
					this.notifyDataSetInvalidated();
				}
				isRefreshingData = false;
			} else {
				boolean ret = this.datas.addAll(data);
				if (ret)
					this.notifyDataSetChanged();
			}
		}
		if (datas == null || datas.size() == 0) {
			hideListFooter();
		}
	}

	public void removeItem(int position) {
		this.datas.remove(position);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		E bean = datas.get(position);
		if (bean != null) {
			if (convertView == null) {
				convertView = AppManager.getInstance().getInflater()
						.inflate(item_layout, null);
				Object viewHolder = initViewHolder(bean, convertView, position);
				convertView.setTag(viewHolder);
			} else {
				Object viewHolder = convertView.getTag();
				initViewHolder(bean, convertView, viewHolder, position);
			}
			return convertView;
		}
		return null;
	}

	protected abstract Object initViewHolder(E bean, View view, Object obj,
			int position);

	protected Object initViewHolder(E bean, View view, int position) {

		return initViewHolder(bean, view, null, position);
	}

	public abstract class UtilAsyncTask extends
			AsyncTask<Integer, Integer, PagerBean<E>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			preLoadData();
		}

		@Override
		protected void onPostExecute(PagerBean<E> result) {
			if (result != null && result.getTotalSize() > 0) {
				total_pager = result.getTotalPage();
				total_count = result.getTotalSize();

				bindDatas(result.getResult());
			} else {
				hideListFooter();
			}
			isLoadingData = false;
			if (isRefreshingData)
				isRefreshingData = false;
			super.onPostExecute(result);
		}

		protected void bindDatas(List<E> data) {
			UtilAdapter.this.bindDatas(data);
		}
	}
}
