package com.wxk.jokeandroidapp.ui.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
}
