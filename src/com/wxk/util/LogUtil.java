package com.wxk.util;

import android.util.Log;

public final class LogUtil {
	static boolean isLogD = true;
	static boolean isLogI = true;
	static boolean isLogW = true;
	static boolean isLogE = true;

	public static void d(String TAG, String msg) {
		if (isLogD)
			Log.d(TAG, msg);
	}

	public static void i(String TAG, String msg) {
		if (isLogI)
			Log.i(TAG, msg);
	}

	public static void w(String TAG, String msg) {
		if (isLogW)
			Log.w(TAG, msg);
	}

	public static void e(String TAG, String msg) {
		if (isLogE)
			Log.e(TAG, msg);
	}

}
