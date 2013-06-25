package com.wxk.util;

import java.lang.ref.SoftReference;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.wxk.jokeandroidapp.AppContext;

public class AsyncImageLoader {

	public void LoadDrawable(final String imgUrl, final ImageCallback callback) {
		if(imgUrl==null||imgUrl.length()<=3){
			return;
		}
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				callback.ImageLoaded((Drawable) msg.obj);
			}
		};
		// �ж�ͼƬ�Ƿ��ڻ�����
		if (AppContext.imgCache.containsKey(imgUrl)) {
			SoftReference<Drawable> softReference = AppContext.imgCache
					.get(imgUrl);
			if (softReference.get() != null) {
				Message message = handler.obtainMessage(0, softReference.get());
				handler.sendMessage(message);
				return;
			}
		}
		// �ж�ͼƬ�Ƿ���sdͼƬ������
		final String imageName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1,
				imgUrl.length());
		Bitmap bm = FilesHelper.GetImage("/52lxh/cacheimages",imageName);
		if (bm != null) {
			Drawable drawable = new BitmapDrawable(bm);
			AppContext.imgCache.put(imgUrl, new SoftReference<Drawable>(
					drawable));
			Message message = handler.obtainMessage(0, drawable);
			handler.sendMessage(message);
			return;
		}
		new Thread() {
			public void run() {
				try {
					Drawable drawable = LoadImageFromUrl("http://www.52lxh.com"
							+ imgUrl);
					AppContext.imgCache.put(imgUrl,
							new SoftReference<Drawable>(drawable));
					FilesHelper.CreateFile("/52lxh/cacheimages",imageName,Common.drawableToBitmap(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ����������ͼƬ
	 * 
	 * @param url
	 *            ͼƬ��ַ
	 * @return
	 */
	private Drawable LoadImageFromUrl(String url) {
		try {
			return Drawable.createFromStream(new URL(url).openStream(), "src");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public interface ImageCallback {
		public void ImageLoaded(Drawable imgDrawable);
	}
}
