package com.wxk.jokeandroidapp.ui.adapter;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.ui.DetailActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class JokesAdapter extends UtilAdapter<JokeBean> {

	public JokesAdapter(ListView listView, View header, View footer,
			int itemLayout) {
		super(listView, header, footer, itemLayout);
	}

	@Override
	public void preInitListView() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ignore footer view
				if (view == footer)
					return;
				// ignore null data
				if (null == datas || datas.size() < 1)
					return;

				JokeBean bean = datas.get(position);
				Intent intentDetail = new Intent();
				intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentDetail.putExtra("id", bean.getId());
				intentDetail.putExtra("title", bean.getTitle());
				intentDetail.putExtra("content", bean.getContent());
				intentDetail.putExtra("replys", bean.getReplyCount());
				intentDetail.putExtra("clicks", bean.getClickCount());
				intentDetail.putExtra("goods", bean.getGooodCount());
				intentDetail.putExtra("bads", bean.getBadCount());
				intentDetail.putExtra("date", bean.getActiveDate());

				intentDetail.setClass(AppContext.context, DetailActivity.class);
				AppContext.context.startActivity(intentDetail);
			}

		});
	}

	@Override
	protected Object initViewHolder(JokeBean bean, View view, Object obj) {
		ViewHolder viewHolder;
		if (obj == null)
			viewHolder = new ViewHolder();
		else
			viewHolder = (ViewHolder) obj;

		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txt_jokeTitle);
		if (viewHolder.txtTitle != null)
			viewHolder.txtTitle.setText(bean.getTitle());
		viewHolder.txtContext = (TextView) view
				.findViewById(R.id.txt_jokeContent);
		if (viewHolder.txtContext != null)
			viewHolder.txtContext.setText(bean.getContent());
		viewHolder.imgvJokePic = (ImageView) view
				.findViewById(R.id.imgv_jokeImg);
		if (viewHolder.imgvJokePic != null) {
			if (bean.getImgUrl() == null || "".equals(bean.getImgUrl())) {
				viewHolder.imgvJokePic.setVisibility(View.GONE);
			} else {
				viewHolder.imgvJokePic.setVisibility(View.VISIBLE);
				// TODO loading image to url
			}
		}
		return viewHolder;
	}

	private static class ViewHolder {
		public TextView txtTitle;
		public TextView txtContext;
		public ImageView imgvJokePic;
		// public Button btnGood;
		// public Button btnBad;
		// public Button btnComment;
	}
}
