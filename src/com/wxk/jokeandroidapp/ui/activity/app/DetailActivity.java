package com.wxk.jokeandroidapp.ui.activity.app;

import java.util.List;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeDetailFragment;
import com.wxk.util.GsonUtils;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class DetailActivity extends BaseActivity implements OnClickListener {

	private ViewPager mPager;
	private JokePagerAdapter mAdapter;
	public static final String EXTRA_JOKE_ID = "52lxh:joke_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_detail_pager);

		//
		mAdapter = new JokePagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(2);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				JokeBean bean = null;
				List<JokeBean> list = db.getList(position + 1, 1);
				bean = list.get(0);
				DetailActivity.this.setTitle(bean.getTitle());
			}
		});
		// final int extraCurrentItem = getIntent().getIntExtra(EXTRA_JOKE, -1);
		final long jokeId = getIntent().getLongExtra(EXTRA_JOKE_ID, -1);
		final int extraCurrentItem = (int) db.getCount("id>=?",
				new String[] { "" + jokeId });
		if (extraCurrentItem > 0) {
			mPager.setCurrentItem(extraCurrentItem - 1);
			JokeBean bean = db.getOne(jokeId);
			setTitle(bean.getTitle());
		}

		final ActionBar actionBar = getActionBar();
		// Hide title text and set home as up
		// actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private JokeDb db = new JokeDb();

	private class JokePagerAdapter extends FragmentStatePagerAdapter {
		public JokePagerAdapter(FragmentManager fm) {
			super(fm);
			mSize = (int) db.getCount();
		}

		private final int mSize;

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

	public void setTitle(String title) {
		final ActionBar bar = getActionBar();
		bar.setTitle(title);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
