package com.wxk.util;

import com.wxk.jokeandroidapp.AppContext;

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
	 * �õ���Ļ�Ŀ�
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
	 * ��ʾһ��Toast
	 */
	public static void ShowDialog(String showText) {
		Toast toast=Toast.makeText(AppContext.context, showText,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * ��ȡ�ַ�
	 * @param content ԭʼ����
	 * @param length ����Ϊֹ
	 * @return
	 */
	public static String GetSubString(String content, int length) {
		return content.length() >= length ? content.substring(0, length - 1)
				: content;
	}
}
