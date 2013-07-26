package com.wxk.jokeandroidapp.ui.util;

import java.io.File;
import java.lang.ref.SoftReference;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.util.BitmapUtil;
import com.wxk.util.BitmapUtil.WrapDrawable;
import com.wxk.util.FileUtil;
import com.wxk.util.LogUtil;
import com.wxk.util.StringUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ImageViewAsyncTask extends AsyncTask<String, Integer, Boolean> {
	final String TAG = "AsyncTask";
	protected File cacheDir;
	protected String cacheFileName;
	protected Context context;
	protected String src = "";
	protected File cacheFile;
	protected Handler handler;

	public ImageViewAsyncTask(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;
		this.cacheDir = this.context.getCacheDir();
	}

	public ImageViewAsyncTask(Handler handler) {
		this(handler, AppContext.context);
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		String url = arg0[0];
		cacheFileName = StringUtil.hashKeyForDisk(url);
		if (showCacheDrawable(cacheFileName)) {
			return true;
		}
		cacheFile = new File(cacheDir, cacheFileName);

		if (!cacheFile.exists())
			return FileUtil.downLoadFile(url, cacheFile);
		return cacheFile.exists();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result && cacheFile != null) {

			WrapDrawable drawable = BitmapUtil.getImgDrawable(cacheFile);
			setImageViewDrawable(drawable);
			// cache
			if (cacheFile != null) {
				AppContext.imgCache.put(cacheFile.getName(),
						new SoftReference<WrapDrawable>(drawable));
			}

		} else if (!result || cacheFile == null) {
			handler.sendEmptyMessage(View.GONE);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		LogUtil.i(TAG, cacheFile.getPath() + ": " + values[0]);
	}

	public boolean showCacheDrawableUrl(String url) {
		String cacheFileName = StringUtil.hashKeyForDisk(url);
		return showCacheDrawable(cacheFileName);
	}

	private boolean showCacheDrawable(String cacheFileName) {

		// SoftReference cache
		if (AppContext.imgCache.containsKey(cacheFileName)) {
			SoftReference<WrapDrawable> softReference = AppContext.imgCache
					.get(cacheFileName);

			if (null != softReference && softReference.get() != null) {
				WrapDrawable drawable = softReference.get();
				LogUtil.d(TAG, "showCacheDrawable( " + cacheFileName + " )"
						+ drawable);
				setImageViewDrawable(drawable);
				return true;
			}
		}
		File temp = new File(cacheDir, cacheFileName);
		if (temp.exists()) {
			WrapDrawable drawable = BitmapUtil.getImgDrawable(temp);
			setImageViewDrawable(drawable);
			return true;
		}

		return false;
	}

	private void setImageViewDrawable(WrapDrawable drawable) {

		if (drawable != null && drawable.drawable != null) {

			LogUtil.d(TAG, "setImageViewDrawable( " + drawable + " )");
			Message msg = new Message();
			msg.what = View.VISIBLE;
			msg.obj = drawable;
			handler.sendMessage(msg);

		} else {
			handler.sendEmptyMessage(View.GONE);
		}
	}
}
