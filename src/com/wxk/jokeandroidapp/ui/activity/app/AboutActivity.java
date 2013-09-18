package com.wxk.jokeandroidapp.ui.activity.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.ui.UiManager;
import com.wxk.jokeandroidapp.ui.activity.BaseActivity;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			UiManager.getInstance().finishActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
