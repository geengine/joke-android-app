package com.wxk.jokeandroidapp;

import java.io.File;
import java.util.Hashtable;

import com.wxk.jokeandroidapp.db.DbHelper;
import com.wxk.util.LogUtil;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class App extends Application {
	public static final String TAG = "HYY_APP";
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	private static final int CACHE_TIME = 60 * 60000 * 5;// 缓存失效时间 5 分钟

	public static Context context;
	public static ConnectivityManager connectMg;

	private static Hashtable<String, Long> memCacheFingerMark = new Hashtable<String, Long>();

	/**
	 * @param key
	 */
	public static void setMemCacheFinger(String key) {
		Long finger = System.currentTimeMillis();
		memCacheFingerMark.put(key, finger);
		LogUtil.d(TAG, "Set Cache finger [ " + key + " ] : " + finger);
	}

	/**
	 * @param key
	 * @return
	 */
	public static Long getMemCacheFinger(String key) {
		if (memCacheFingerMark.containsKey(key))
			return memCacheFingerMark.get(key);
		return 0L;
	}

	/**
	 * @param key
	 * @return
	 */
	public static boolean isCacheDataFailure(String key) {
		boolean failure = false;
		Long data = getMemCacheFinger(key);
		if (data > 0L && (System.currentTimeMillis() - data) > CACHE_TIME) {
			failure = true;
			LogUtil.d(TAG, "Cache failure [ " + key + " ] : " + failure);
		} else if (data == 0L) {
			failure = true;
			LogUtil.d(TAG, "Cache finger [ " + key + " ] : " + data);
		}

		return failure;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		connectMg = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * network is connection
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected() {
		NetworkInfo ni = connectMg.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * clear cache data
	 */
	public static void clearCacheData() {
		DbHelper db = new DbHelper(App.context);
		db.deleteDataTables();
		memCacheFingerMark.clear();
		// clear cache file
		File cacheDir = App.context.getCacheDir();
		if (cacheDir.exists())
			cacheDir.delete();
	}
}
