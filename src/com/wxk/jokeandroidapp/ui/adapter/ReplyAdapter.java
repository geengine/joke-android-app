package com.wxk.jokeandroidapp.ui.adapter;

import java.util.Collection;
import java.util.List;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.util.UniqueList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReplyAdapter extends BaseAdapter {

	private static final String TAG = "52lxh:ReplyAdapter";
	private Activity mContext;
	private List<ReplyBean> mItems;

	private static class ViewHolder {
		public TextView txtvDate;
		public TextView txtvContext;
		public TextView txtvFloor;
	}

	public ReplyAdapter(final Activity context) {
		super();
		mContext = context;
		mItems = new UniqueList<ReplyBean>();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public ReplyBean getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ReplyBean item = getItem(position);
		Log.i(TAG, "getView() ::=> " + item.getContent());
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.reply_item, null);
			holder = new ViewHolder();
			holder.txtvContext = (TextView) convertView
					.findViewById(R.id.txtv_reply_content);
			holder.txtvDate = (TextView) convertView
					.findViewById(R.id.txtv_reply_date);
			holder.txtvFloor = (TextView) convertView
					.findViewById(R.id.txtv_floor);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (holder.txtvContext != null)
			holder.txtvContext.setText(item.getContent());

		if (holder.txtvDate != null)
			holder.txtvDate.setText(item.getActiveDate());

		if (holder.txtvFloor != null)
			holder.txtvFloor.setText("#" + (position + 1));
		return convertView;
	}

	public void fillWithItems(Collection<ReplyBean> items) {
		Log.i(TAG, String.format("::fillWithItems(%s)", items.size()));
		mItems.clear();
		mItems.addAll(items);
	}

	public void appendWithItems(Collection<ReplyBean> items) {
		Log.i(TAG, String.format("::appendWithItems(%s)", items.size()));
		mItems.addAll(mItems.size() - 1, items);
	}
}
