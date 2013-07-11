package com.wxk.jokeandroidapp.ui;

import java.util.List;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.util.GsonUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class DetailActivity extends FragmentActivity implements OnClickListener {

	private ViewPager mPager;
	private JokePagerAdapter mAdapter;
	public static final String EXTRA_JOKE = "extra_joke";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_detail_pager);
		mAdapter = new JokePagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(2);

		final int extraCurrentItem = getIntent().getIntExtra(EXTRA_JOKE, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
		}
	}

	private class JokePagerAdapter extends FragmentStatePagerAdapter {
		public JokePagerAdapter(FragmentManager fm) {
			super(fm);
			mSize = (int) db.getCount();
		}

		private final int mSize;
		private JokeDb db = new JokeDb();

		@Override
		public int getCount() {
			return mSize;
		}

		@Override
		public Fragment getItem(int position) {
			JokeBean jokeBean = null;
			List<JokeBean> list = db.getList(position + 1, 1);
			jokeBean = list.get(0);
			return JokeDetailFragment.newInstance(GsonUtils.toJson(jokeBean));
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
