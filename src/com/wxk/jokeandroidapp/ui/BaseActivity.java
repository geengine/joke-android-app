package com.wxk.jokeandroidapp.ui;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected void showToast(String showText) {
		Toast toast = Toast.makeText(this, showText, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void showToast(int resId) {
		showToast(this.getString(resId));
	}
}
