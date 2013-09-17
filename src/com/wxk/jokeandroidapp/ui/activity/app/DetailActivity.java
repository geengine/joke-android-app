package com.wxk.jokeandroidapp.ui.activity.app;

import java.util.List;

import com.wxk.jokeandroidapp.App;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.client.ReplyClient;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.jokeandroidapp.services.ReplyService;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;
import com.wxk.jokeandroidapp.ui.fragment.app.JokeDetailFragment;
import com.wxk.util.GsonUtils;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class DetailActivity extends BaseActivity implements OnClickListener {

	private boolean isReplying = false;
	private EditText etxtReplyContent;
	private ViewPager mPager;
	private JokePagerAdapter mAdapter;
	public static final String EXTRA_JOKE_ID = "52lxh:joke_id";
	public long mJokeID;
	private JokeBean jokeBean;

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
				mJokeID = bean.getId();
				isReplying = false;
				DetailActivity.this.setTitle(bean.getTitle());
			}
		});
		// final int extraCurrentItem = getIntent().getIntExtra(EXTRA_JOKE, -1);
		mJokeID = getIntent().getLongExtra(EXTRA_JOKE_ID, -1);
		final int extraCurrentItem = (int) db.getCount("id>=?",
				new String[] { "" + mJokeID });
		if (extraCurrentItem > 0) {
			mPager.setCurrentItem(extraCurrentItem - 1);
			jokeBean = db.getOne(mJokeID);
			setTitle(jokeBean.getTitle());
		}

		final ActionBar actionBar = getActionBar();
		// Hide title text and set home as up
		// actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.mOptionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detail_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_refresh:
			setRefreshActionButtonState(true);
			final Intent startService = new Intent(this, ReplyService.class);
			startService.putExtra(ReplyService.ARG_JOKE_ID, mJokeID);
			startService.putExtra(ReplyService.ARG_PAGE, 1);
			startService.setAction(ReplyService.GET_REPLY_DATA_INTENT);

			startService(startService);
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
			jokeBean = null;
			List<JokeBean> list = db.getList(position + 1, 1);
			jokeBean = list.get(0);
			return JokeDetailFragment.newInstance(GsonUtils.toJson(jokeBean));
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private class BaseOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_submit:
				doReply();
				break;
			}

		}
	}

	private void doReply() {
		if (!App.isNetworkConnected()) {
			showToast(R.string.toast_error_network);
			return;
		}
		if (!isReplying) {
			isReplying = true;
			String content = etxtReplyContent.getText().toString();
			if ("".equals(content)) {
				isReplying = false;
				showToast(R.string.toast_reply_empty);
				return;
			}
			(new DoReplyTask(mJokeID, content)).execute();
		} else {
			showToast(R.string.toast_reply_exists);
		}

	}

	private class DoReplyTask extends AsyncTask<Void, Void, Boolean> {

		private long jokeid;
		private String content;

		public DoReplyTask(long jokeid, String content) {
			this.jokeid = jokeid;
			this.content = content;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ReplyClient dao = new ReplyClient();
			try {
				return dao.doReply(jokeid, content).getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			showToast(result ? R.string.toast_reply_success
					: R.string.toast_reply_failure);
			isReplying = result;

			if (result) {
				final Intent startService = new Intent(DetailActivity.this,
						ReplyService.class);
				startService.putExtra(ReplyService.ARG_JOKE_ID, mJokeID);
				startService.putExtra(ReplyService.ARG_PAGE, 1);
				startService.putExtra(ReplyService.ARG_REPLY_COUNT_ADD, 1);
				startService.setAction(ReplyService.GET_REPLY_DATA_INTENT);

				DetailActivity.this.startService(startService);
				// refresh list view
				// viewHolder.btnComment.setText(""
				// + (Integer.parseInt(viewHolder.btnComment.getText()
				// .toString()) + 1));
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private void initBtnClick() {

		BaseOnClickListener l = new BaseOnClickListener();
		ImageButton imgbSubmitReply = (ImageButton) findViewById(R.id.btn_submit);
		etxtReplyContent = (EditText) findViewById(R.id.etxt_reply);

		imgbSubmitReply.setOnClickListener(l);
	}

	public void setTitle(String title) {
		final ActionBar bar = getActionBar();
		bar.setTitle(title);
		initBtnClick();
		etxtReplyContent.setText("");
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
