package com.wxk.jokeandroidapp.ui.util;

import java.io.File;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.util.BitmapUtil;
import com.wxk.util.BitmapUtil.WrapDrawable;
import com.wxk.util.FileUtil;
import com.wxk.util.LogUtil;
import com.wxk.util.StringUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageViewAsyncTask extends AsyncTask<String, Integer, Boolean> {

	final String TAG = "AsyncTask";
	protected File cacheDir;

	protected Context context;
	protected ImageView imgv;
	protected String src = "";
	protected File cacheFile;
	protected boolean autoFill;

	public ImageViewAsyncTask(ImageView imgv, String src, Context context,
			boolean autoFill) {
		this.imgv = imgv;
		this.src = src;
		this.context = context;
		this.cacheDir = this.context.getCacheDir();
		this.autoFill = autoFill;
	}

	public ImageViewAsyncTask(ImageView imgv, String src, boolean autoFill) {
		this(imgv, src, AppContext.context, autoFill);
	}

	public ImageViewAsyncTask(ImageView imgv, String src) {
		this(imgv, src, AppContext.context, false);
	}

	public ImageViewAsyncTask(ImageView imgv, boolean autoFill) {
		this(imgv, null, AppContext.context, autoFill);
	}

	public ImageViewAsyncTask(ImageView imgv) {
		this(imgv, null, AppContext.context, false);
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
			WrapDrawable drawable = BitmapUtil.getImgDrawable(cacheFile);

			if (drawable != null) {
				if (autoFill) {
					int w = DisplayUtil.getScreenWidth(context);// imgv.getWidth();
					float bl = (float) drawable.height / (float) drawable.width;
					ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
							w, (int) (w * bl));
					imgv.setLayoutParams(params);
					LogUtil.i(TAG, cacheFile.getPath() + ": width=" + w
							+ ",height=" + w * bl + "," + bl);
				}
				imgv.setImageDrawable(drawable.drawable);
			}

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
