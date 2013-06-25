package com.wxk.jokeandroidapp.ui.adapter;

import com.wxk.jokeandroidapp.bean.JokeBean;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

public abstract class JokesAdapter extends UtilAdapter<JokeBean> {

	public JokesAdapter(ListView listView, View header, View footer,
			int itemLayout, Handler handler) {
		super(listView, header, footer, itemLayout, handler);
	}

}
