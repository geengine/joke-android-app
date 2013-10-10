package com.wxk.jokeandroidapp.ui.activity;

import com.baidu.mobstat.StatService;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.ui.UiManager;
import com.wxk.jokeandroidapp.ui.activity.app.AboutActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	protected final String TAG = "activity";
	protected GestureDetector gestureDetector;
	protected Menu mOptionsMenu;

	protected void showToast(String showText) {
		Toast toast = Toast.makeText(this, showText, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void showToast(int resId) {
		showToast(this.getString(resId));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:

			break;
		case R.id.action_about:
			final Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (mOptionsMenu != null) {
			final MenuItem refreshItem = mOptionsMenu
					.findItem(R.id.action_refresh);
			if (refreshItem != null) {
				if (refreshing) {
					Log.i(TAG,
							"::setRefreshActionButtonState() => Action refresh...");
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					Log.i(TAG,
							"::setRefreshActionButtonState() => Action unrefresh");
					refreshItem.setActionView(null);
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureDetector = new GestureDetector(this, onGestureListener);
		UiManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UiManager.getInstance().finishActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	protected void onFlingLeft() {
	}

	protected void onFlingRight() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	OnGestureListener onGestureListener = new OnGestureListener() {
		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e0, MotionEvent e1, float v0,
				float v1) {
			if (null == e0 || e1 == null)
				return false;
			//
			float x = e0.getX() - e1.getX();
			float y = e0.getY() - e1.getY();
			// 滑动最小距离
			float x_limit = 50;
			float y_limit = 50;
			float abs_x = Math.abs(x);
			float abs_y = Math.abs(y);
			if (abs_x >= abs_y) {
				// left or right
				if (x > x_limit || x < -x_limit) {
					if (x > 0) {
						// left
						onFlingLeft();
						return true;
					} else {
						// right
						onFlingRight();
						return true;
					}
				}
			} else {
				if (y > y_limit || y < -y_limit) {
					if (y > 0) {
						// up
						Log.v(TAG, "up");
					} else
						// down
						Log.v(TAG, "down");
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	};
}