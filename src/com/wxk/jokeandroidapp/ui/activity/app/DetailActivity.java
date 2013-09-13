package com.wxk.jokeandroidapp.ui.activity.app;

import java.util.List;

import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeDetailFragment;
import com.wxk.jokeandroidapp.ui.util.ImageCache;
import com.wxk.jokeandroidapp.ui.util.ImageFetcher;
import com.wxk.util.GsonUtils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class DetailActivity extends BaseActivity implements OnClickListener {

	private ViewPager mPager;
	private JokePagerAdapter mAdapter;
	public static final String EXTRA_JOKE_ID = "extra_joke_id";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_detail_pager);
		//
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;

		// final int longest = (height > width ? height : width);// / 2;

		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				this, Constant.IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		//
		mImageFetcher = new ImageFetcher(this, width, height);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageFetcher.setImageFadeIn(false);
		mImageFetcher.setFullImageShow(true);
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
		final int jokeId = getIntent().getIntExtra(EXTRA_JOKE_ID, -1);
		final int extraCurrentItem = (int) db.getCount("id>?",
				new String[] { "" + jokeId });
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
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

	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setTitle(String title) {
		final ActionBar bar = getActionBar();
		bar.setTitle(title);
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}
}
