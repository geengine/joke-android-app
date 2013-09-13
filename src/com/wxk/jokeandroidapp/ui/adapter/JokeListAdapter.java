package com.wxk.jokeandroidapp.ui.adapter;

import java.util.ArrayList;
import java.util.Collection;

import com.androidquery.AQuery;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.util.UniqueList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class JokeListAdapter extends BaseAdapter {

	private static class ViewHolder {
		public TextView txtTitle;
		public TextView txtContent;
		public ImageView imgvJokePic;
		public Button btnGood;
		public Button btnBad;
		public Button btnComment;
	}

	private static final String TAG = "52lxh:JokeListAdapter";

	private ArrayList<JokeBean> mItems;
	private Activity mContext;

	public JokeListAdapter(final Activity context) {
		super();
		mContext = context;
		mItems = new UniqueList<JokeBean>();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public JokeBean getItem(int i) {
		return mItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		final JokeBean item = getItem(position);
		ViewHolder holder;
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.jokerow, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) converView
					.findViewById(R.id.txt_jokeTitle);
			holder.txtContent = (TextView) converView
					.findViewById(R.id.txt_jokeContent);
			holder.imgvJokePic = (ImageView) converView
					.findViewById(R.id.imgv_jokeImg);
			// operate button
			holder.btnGood = (Button) converView.findViewById(R.id.btn_good);
			holder.btnBad = (Button) converView.findViewById(R.id.btn_bad);
			holder.btnComment = (Button) converView
					.findViewById(R.id.btn_comment);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		holder.txtTitle.setText(item.getTitle());
		if (item.getContent() != null && !item.getContent().equals("")
				&& item.getContent().length() > 6) {
			holder.txtContent.setText(item.getContent());
			holder.txtContent.setVisibility(View.VISIBLE);
		} else {
			holder.txtContent.setVisibility(View.GONE);
		}

		holder.btnGood.setText("" + item.getGooodCount());
		holder.btnBad.setText("" + item.getBadCount());
		holder.btnComment.setText("" + item.getReplyCount());
		final String imgURL = item.getImgUrl();
		if (imgURL != null && !imgURL.equals("") && imgURL.length() > 6) {
			Log.i(TAG, String.format("Loading image: %s", imgURL));
			final AQuery aq = new AQuery(mContext);
			aq.id(holder.imgvJokePic).image(Constant.BASE_URL + imgURL, true,
					true, 800, 0, null, AQuery.FADE_IN_NETWORK);
			holder.imgvJokePic.setVisibility(View.VISIBLE);
		} else {
			holder.imgvJokePic.setVisibility(View.GONE);
		}
		return converView;
	}

	public void fillWithItems(Collection<JokeBean> items) {
		mItems.clear();
		mItems.addAll(items);
	}

	public void appendWithItems(Collection<JokeBean> items) {
		mItems.addAll(mItems.size() - 1, items);
	}
}
