package com.wxk.jokeandroidapp.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DrawerAdapter extends BaseAdapter {
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	protected int mSelectedPosition = -1;// 选中的位置
	private List<DrawerItemData> mItems;
	private int mTitleLayoutId = 0;
	private int mItemLayoutId = 0;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Integer> mSeparatorsSet = new ArrayList<Integer>();

	public DrawerAdapter(Context context, List<DrawerItemData> datas,
			int itemLayoutId, int titleLayoutId) {
		this.mContext = context;
		this.mItemLayoutId = itemLayoutId;
		this.mTitleLayoutId = titleLayoutId;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.mItems = datas;
	}

	public DrawerAdapter(Context context, int itemLayoutId, int titleLayoutId) {
		this(context, new ArrayList<DrawerItemData>(), itemLayoutId,
				titleLayoutId);
	}

	public DrawerAdapter(Context context, int itemLayoutId) {
		this(context, new ArrayList<DrawerItemData>(), itemLayoutId, 0);
	}

	public void setSelectedPosition(int position) {
		this.mSelectedPosition = position;
	}

	public void addItem(final DrawerItemData item) {
		this.mItems.add(item);
		this.notifyDataSetChanged();
	}

	public void addSeparatorItem(final DrawerItemData item) {
		this.mItems.add(item);
		// save separator position
		this.mSeparatorsSet.add(this.mItems.size() - 1);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return !this.mSeparatorsSet.contains(position);
	}

	@Override
	public int getItemViewType(int position) {
		return this.mSeparatorsSet.contains(position) ? TYPE_SEPARATOR
				: TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		DrawerItemData item = mItems.get(position);
		int type = this.getItemViewType(position);
		switch (type) {
		case TYPE_SEPARATOR:
			if (convertView == null) {
				convertView = this.mInflater.inflate(this.mTitleLayoutId, null);
				Object viewHolder = initViewHolder(item, convertView, position);
				convertView.setTag(viewHolder);
			} else {
				Object viewHolder = convertView.getTag();
				initViewHolder(item, convertView, viewHolder, position);
			}
			return convertView;
		default:
			if (convertView == null) {
				convertView = this.mInflater.inflate(this.mItemLayoutId, null);
				Object viewHolder = initViewHolder(item, convertView, position);
				convertView.setTag(viewHolder);
			} else {
				Object viewHolder = convertView.getTag();
				initViewHolder(item, convertView, viewHolder, position);
			}
			return convertView;
		}

	}

	/**
	 * @param bean
	 * @param view
	 * @param viewHolder
	 * @param position
	 * @return
	 */
	protected abstract Object initViewHolder(DrawerItemData bean, View view,
			Object viewHolder, int position);

	protected Object initViewHolder(DrawerItemData bean, View view, int position) {

		return initViewHolder(bean, view, null, position);
	}

	public static class DrawerItemData {
		private boolean IsTitle;
		private String title;
		private String target;
		public int topic;
		private int iconId;

		public DrawerItemData(String title, boolean isTitle, String mTarget,
				int iconId) {
			this.setTitle(title);
			this.setIsTitle(isTitle);
			this.setTarget(mTarget);
			this.setIconId(iconId);
		}

		public DrawerItemData(String title, boolean isTitle) {
			this(title, isTitle, null, 0);
		}

		public DrawerItemData(String title, int topic) {
			this(title, false, null, 0);
			this.topic = topic;
		}

		public boolean isIsTitle() {
			return IsTitle;
		}

		public void setIsTitle(boolean isTitle) {
			IsTitle = isTitle;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public int getIconId() {
			return iconId;
		}

		public void setIconId(int iconId) {
			this.iconId = iconId;
		}
	}

}
