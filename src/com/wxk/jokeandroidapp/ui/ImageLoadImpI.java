package com.wxk.jokeandroidapp.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.util.Common;
import com.wxk.util.FilesHelper;
import com.wxk.util.AsyncImageLoader.ImageCallback;

public class ImageLoadImpI implements ImageCallback {

	private ImageView imgView;
	private int showWidth=AppContext.dWidth-20;

	public ImageLoadImpI(ImageView img) {
		super();
		imgView = img;
	}

	@Override
	public void ImageLoaded(Drawable imgDrawable) {
		// TODO Auto-generated method stub
		if (imgDrawable == null) {
			return;
		}
		Bitmap bp = Common.drawableToBitmap(imgDrawable);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				showWidth, getHeight(bp.getWidth(), bp.getHeight()));
		imgView.setLayoutParams(params);
		imgView.setImageDrawable(imgDrawable);
	}

	private int getHeight(int width, int height) {
		return (int) ((float) height / width * showWidth);
	}
}
