package com.wxk.jokeandroidapp.ui.adapter;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.ui.DetailActivity;
import com.wxk.jokeandroidapp.ui.listener.OperateClickListener;
import com.wxk.jokeandroidapp.ui.util.ImageFetcher;
import com.wxk.util.BitmapUtil.WrapDrawable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class JokesAdapter extends UtilAdapter<JokeBean> {

	private ImageFetcher mImageFetcher;

	public JokesAdapter(ListView listView, View header, View footer,
			int itemLayout, ImageFetcher imageFetcher) {
		super(listView, header, footer, itemLayout);
		this.mImageFetcher = imageFetcher;
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
				final Intent intentDetail = new Intent(AppContext.context,
						DetailActivity.class);
				intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentDetail.putExtra(DetailActivity.EXTRA_JOKE, position);
				// intentDetail.putExtra("id", bean.getId());
				// intentDetail.putExtra("title", bean.getTitle());
				// intentDetail.putExtra("content", bean.getContent());
				// intentDetail.putExtra("replys", bean.getReplyCount());
				// intentDetail.putExtra("clicks", bean.getClickCount());
				// intentDetail.putExtra("goods", bean.getGooodCount());
				// intentDetail.putExtra("bads", bean.getBadCount());
				// intentDetail.putExtra("date", bean.getActiveDate());
				// intentDetail.putExtra("imgurl", bean.getImgUrl());
				AppContext.context.startActivity(intentDetail);
			}

		});
	}

	@Override
	protected Object initViewHolder(JokeBean bean, View view, Object obj,
			int position) {
		ViewHolder viewHolder;
		if (obj == null) {
			viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) view
					.findViewById(R.id.txt_jokeTitle);
			viewHolder.txtContent = (TextView) view
					.findViewById(R.id.txt_jokeContent);
			viewHolder.imgvJokePic = (ImageView) view
					.findViewById(R.id.imgv_jokeImg);
			// operate button
			viewHolder.btnGood = (Button) view.findViewById(R.id.btn_good);
			viewHolder.btnBad = (Button) view.findViewById(R.id.btn_bad);
			viewHolder.btnComment = (Button) view
					.findViewById(R.id.btn_comment);
		} else {
			viewHolder = (ViewHolder) obj;
		}

		if (viewHolder.txtTitle != null)
			viewHolder.txtTitle.setText(bean.getTitle());

		if (viewHolder.txtContent != null) {
			if (bean.getContent() != null && !"".equals(bean.getContent())) {
				viewHolder.txtContent.setVisibility(View.VISIBLE);
				viewHolder.txtContent.setText(bean.getContent());
			} else {
				viewHolder.txtContent.setVisibility(View.GONE);
			}
		}

		if (viewHolder.imgvJokePic != null) {

			if (bean.getImgUrl() != null && !"".equals(bean.getImgUrl())) {
				// viewHolder.imgvJokePic.setImageDrawable(null);
				String url = Constant.BASE_URL + bean.getImgUrl();
				viewHolder.imgvJokePic.setVisibility(View.VISIBLE);
				mImageFetcher.loadImage(url, viewHolder.imgvJokePic);
				/*
				 * ImageViewAsyncTask imgTask = new ImageViewAsyncTask(
				 * viewHolder.imgHandler); if
				 * (!imgTask.showCacheDrawableUrl(url)) imgTask.execute(url);
				 */

			} else {
				viewHolder.imgvJokePic.setVisibility(View.GONE);
			}
		}

		OperateClickListener ocl = new OperateClickListener(viewHolder, bean);
		if (viewHolder.btnGood != null) {
			viewHolder.btnGood.setText("" + bean.getGooodCount());
			viewHolder.btnGood.setOnClickListener(ocl);
		}
		if (viewHolder.btnBad != null) {
			viewHolder.btnBad.setText("" + bean.getBadCount());
			viewHolder.btnBad.setOnClickListener(ocl);
		}
		if (viewHolder.btnComment != null) {
			viewHolder.btnComment.setText("" + bean.getReplyCount());
			viewHolder.btnComment.setOnClickListener(ocl);
		}
		return viewHolder;
	}

	@SuppressLint("HandlerLeak")
	public static class ViewHolder {
		public TextView txtTitle;
		public TextView txtContent;
		public ImageView imgvJokePic;
		public Button btnGood;
		public Button btnBad;
		public Button btnComment;
		public Handler imgHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {

				case View.GONE:
					imgvJokePic.setVisibility(View.GONE);
					break;
				case View.VISIBLE:
					imgvJokePic.setVisibility(View.VISIBLE);
					WrapDrawable drawable = (WrapDrawable) msg.obj;
					Bitmap newBitmap = null;
					if (drawable != null && drawable.drawable != null) {
						newBitmap = ((BitmapDrawable) drawable.drawable)
								.getBitmap();
					}
					if (imgvJokePic.getDrawable() != null) {
						Bitmap oldbitmap = ((BitmapDrawable) imgvJokePic
								.getDrawable()).getBitmap();
						imgvJokePic.setImageDrawable(null);
						if (oldbitmap != newBitmap) {
							if (!oldbitmap.isRecycled()) {
								if (oldbitmap != null) {
									oldbitmap.recycle();
									oldbitmap = null;
								}
							}
						}
					}

					if (drawable != null && drawable.drawable != null) {
						imgvJokePic.setImageDrawable(drawable.drawable);
					}
					break;
				}
			}
		};
	}
}
