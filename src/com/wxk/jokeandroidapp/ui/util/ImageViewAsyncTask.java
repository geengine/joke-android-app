package com.wxk.jokeandroidapp.ui.util;

import java.io.File;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.util.BitmapUtil;
import com.wxk.util.FileUtil;
import com.wxk.util.StringUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

public class ImageViewAsyncTask extends AsyncTask<String, Integer, Boolean> {

	final String TAG = "AsyncTask";
	protected File cacheDir;

	protected Context context;
	protected ImageView imgv;
	protected String src = "";
	protected File cacheFile;

	public ImageViewAsyncTask(ImageView imgv, String src, Context context) {
		this.imgv = imgv;
		this.src = src;
		this.context = context;
		cacheDir = this.context.getCacheDir();
	}

	public ImageViewAsyncTask(ImageView imgv, String src) {
		this(imgv, src, AppContext.context);
	}

	public ImageViewAsyncTask(ImageView imgv) {
		this(imgv, null, AppContext.context);
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		String url = arg0[0];
		String cacheFileName = StringUtil.hashKeyForDisk(url);
		cacheFile = new File(cacheDir, cacheFileName);
		if (!cacheFile.exists())
			return FileUtil.downLoadFile(url, cacheFile);
		return cacheFile.exists();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			Drawable drawable = BitmapUtil.getImgDrawable(cacheFile);
			if (drawable != null)
				imgv.setImageDrawable(drawable);
		} else {
			imgv.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
