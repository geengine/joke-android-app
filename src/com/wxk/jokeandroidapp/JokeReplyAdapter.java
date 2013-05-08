package com.wxk.jokeandroidapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxk.tools.AsyncImageLoader;
import com.wxk.tools.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JokeReplyAdapter extends BaseAdapter {

	private List<Map<String, String>> replyList;
	private Map<Integer, View> replyRowViews = new HashMap<Integer, View>();
	private Context context = null;

	public JokeReplyAdapter(List<Map<String, String>> replyList, Context context) {
		this.replyList = replyList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return replyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return replyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = replyRowViews.get(position);
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.reply_item, null);
			TextView txtLouceng = (TextView) rowView
					.findViewById(R.id.txt_reply_louceng);
			TextView txtReplyTime=(TextView)rowView.findViewById(R.id.txt_reply_time);
			TextView txtReplyContent=(TextView)rowView.findViewById(R.id.txt_reply_content);

			txtLouceng.setText("#"+(position+1)+" £º");
			txtReplyTime.setText(replyList.get(position).get("replytime"));
			txtReplyContent.setText(replyList.get(position).get("content"));
			replyRowViews.put(position, rowView);
		}
		return rowView;
	}

}
