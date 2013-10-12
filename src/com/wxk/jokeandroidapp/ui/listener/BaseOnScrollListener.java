package com.wxk.jokeandroidapp.ui.listener;

import com.wxk.jokeandroidapp.Constants;
import com.wxk.util.LogUtil;

import android.os.Handler;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

//import com.cjiayi.hyy.util.LogUtil;

public class BaseOnScrollListener implements OnScrollListener {
	final String TAG = "OnScrollListener";
	private BaseAdapter adapter;
	private Handler handler;
	private Boolean isFooter = false;
	private Boolean isHeader = false;
	private Integer lastItem = 0;
	private Integer firstItem = 0;

	public BaseOnScrollListener(BaseAdapter adapter, Handler handler) {
		this.adapter = adapter;
		this.handler = handler;
	}

	public BaseOnScrollListener(BaseAdapter adapter, Handler handler,
			Boolean isFooter) {
		this.adapter = adapter;
		this.handler = handler;
		this.isFooter = isFooter;
	}

	public BaseOnScrollListener(BaseAdapter adapter, Handler handler,
			Boolean isFooter, Boolean isHeader) {
		this.adapter = adapter;
		this.handler = handler;
		this.isFooter = isFooter;
		this.isHeader = isHeader;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE && lastItem == adapter.getCount()) {
			handler.sendEmptyMessage(Constants.LOADING);
			LogUtil.i(TAG, "loading more ...");
		}
		if (scrollState == SCROLL_STATE_IDLE && firstItem == 0) {
			// TODO when position is index one and this item is very
			// height
			// then refresh some time
			// handler.sendEmptyMessage(Constant.REFURBISH);
			// LogUtil.i(TAG, "refurbish ...");
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstItem = firstVisibleItem;// - (isFooter ? 1 : 0) - (isHeader ? 1 :
										// 0);
		lastItem = firstVisibleItem + visibleItemCount - (isFooter ? 1 : 0)
				- (isHeader ? 1 : 0);

		// LogUtil.i(TAG, "firstItem=" + firstItem + " ,lastItem=" + lastItem);
	}

}
