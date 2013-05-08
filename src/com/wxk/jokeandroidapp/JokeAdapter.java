package com.wxk.jokeandroidapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxk.tools.AsyncImageLoader;
import com.wxk.tools.AsyncImageLoader.ImageCallback;
import com.wxk.tools.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JokeAdapter extends BaseAdapter {

	private List<Map<String, String>> jokeList;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;

	public JokeAdapter(List<Map<String, String>> jokeList, Context context) {
		this.jokeList = jokeList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jokeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jokeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = rowViews.get(position);
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.joke_list_item, null);
			TextView title = (TextView) rowView
					.findViewById(R.id.txt_jokeTitle);
			
			Button reply = (Button) rowView.findViewById(R.id.btn_comment);
			Button good = (Button) rowView.findViewById(R.id.btn_good);
			Button bad = (Button) rowView.findViewById(R.id.btn_bad);
			TextView content = (TextView) rowView
					.findViewById(R.id.joke_content);
			ImageView jokeImg = (ImageView) rowView
					.findViewById(R.id.img_jokeImg);

			title.setText(jokeList.get(position).get("title"));
			reply.setText(jokeList.get(position).get("replyCount"));
			good.setText(jokeList.get(position).get("haoxiao"));
			bad.setText(jokeList.get(position).get("haoleng"));
			String jokeContent = jokeList.get(position).get("content");
			if (jokeContent.trim().length() == 0) {
				content.setVisibility(View.GONE);
			} else {
				content.setText(Common.GetSubString(jokeContent, 100));
			}
			String imgUrl = jokeList.get(position).get("img");
			if (imgUrl != "") {
				LoadImg(imgUrl, jokeImg);
			} else {
				jokeImg.setVisibility(View.GONE);
			}
			rowViews.put(position, rowView);
		}
		return rowView;
	}

	private AsyncImageLoader loader = new AsyncImageLoader();

	private void LoadImg(final String imgUrl, ImageView imgView) {
		ImageCallback callBack = new ImageLoadImpI(imgView);
		loader.LoadDrawable(imgUrl, callBack);
	}
}
