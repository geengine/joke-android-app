package com.wxk.jokeandroidapp.ui.util;

import com.wxk.jokeandroidapp.AppContext;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(displayMetrics);
		// final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		return width;
	}

	public static int getScreenWidth() {
		return getScreenWidth(AppContext.context);
	}
}
