package com.wxk.jokeandroidapp.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.util.LogUtil;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
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
			int itemLayout, Handler handler) {

		this.datas = new ArrayList<E>();
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

	public void setOnScrollListener(OnScrollListener osl) {

		this.listView.setOnScrollListener(osl);
		loadingData(cur_page);
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
	public abstract boolean loadingData(final int page);

	public void refreshingData() {
		isRefreshingData = true;
		loadingData(1);
	}

	public void loadingMoreData() {

		if (cur_page < total_pager) {
			showListFooter();
			cur_page++;
			loadingData(cur_page);
			LogUtil.d(TAG, "Loading page : " + cur_page);
		} else {
			LogUtil.d(TAG, "Loading page : " + cur_page);
		}
	}

	public void bindDatas(List<E> data) {
		if (isRefreshingData) {
			boolean ret = this.datas.addAll(0, data);
			if (ret) {
				this.notifyDataSetInvalidated();
			}
			isRefreshingData = false;
		} else {
			boolean ret = this.datas.addAll(data);
			if (ret)
				this.notifyDataSetChanged();
		}
		if (datas == null || datas.size() == 0 || cur_page == total_pager) {
			hideListFooter();
		} else {
			showListFooter();
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

	public abstract class UtilAsyncTask extends
			AsyncTask<Integer, Integer, PagerBean<E>> {

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
			super.onPostExecute(result);
		}
	}
}
