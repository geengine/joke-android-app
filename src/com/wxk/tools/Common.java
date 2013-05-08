package com.wxk.tools;

import com.wxk.jokeandroidapp.MApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

public class Common {

	/**
	 * 得到屏幕的宽
	 * 
	 * @param context
	 * @return
	 */

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/*
	 * 显示一个Toast
	 */
	public static void ShowDialog(String showText) {
		Toast toast=Toast.makeText(MApplication.mcontext, showText,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 截取字符串
	 * @param content 原始内容
	 * @param length 结束为止
	 * @return
	 */
	public static String GetSubString(String content, int length) {
		return content.length() >= length ? content.substring(0, length - 1)
				: content;
	}
}
