package com.wxk.jokeandroidapp.ui.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class WrapBitmapDrawable extends BitmapDrawable {
	private int width;
	private int height;

	public WrapBitmapDrawable(Resources res, Bitmap bitmap) {
		super(res, bitmap);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
