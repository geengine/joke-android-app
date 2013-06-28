package com.wxk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtil {
	final static String TAG = "BitmapUtil";

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPX = bitmap.getWidth() / 2;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return outBitmap;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions() {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTempStorage = new byte[100 * 1024]; // 100k cache
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888; // setting color
		opts.inPurgeable = true; // auto recycle
		opts.inInputShareable = true; // if isPurgeable=false ignored this
		// opts.inSampleSize = 2; // zoom size
		return opts;
	}

	public static WrapDrawable getImgDrawable(File cacheFile) {
		return getImgDrawable(cacheFile, 0, 0);
	}

	public static class WrapDrawable {
		public Drawable drawable;
		public int width;
		public int height;
	}

	public static WrapDrawable getImgDrawable(File cacheFile, Integer width,
			Integer height) {
		WrapDrawable drawable = null;

		if (cacheFile != null && cacheFile.exists()) {
			InputStream inputStream = null;
			Bitmap bm = null;
			try {
				inputStream = new FileInputStream(cacheFile);

				bm = BitmapFactory.decodeStream(inputStream, null,
						getBitmapFactoryOptions());
				if (width == 0)
					width = bm.getWidth();
				if (height == 0)
					height = bm.getHeight();
				Bitmap useThisBitmap = Bitmap.createScaledBitmap(bm, width,
						height, true);
				drawable = new WrapDrawable();
				drawable.width = width;
				drawable.height = height;
				drawable.drawable = new BitmapDrawable(null, useThisBitmap);

			} catch (Exception e) {
				LogUtil.e(TAG,
						"getImgDrawable from " + cacheFile.getAbsolutePath()
								+ " error !");
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
				if (bm != null) {
					bm.recycle();
				}
			}
		}
		return drawable;
	}
}
