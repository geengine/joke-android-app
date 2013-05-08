package com.wxk.jokeandroidapp;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.wxk.tools.Common;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class MApplication extends Application {

	public static Context mcontext;
	public static int dWidth;
	public static int currentVesion=1;
	public static Map<String,SoftReference<Drawable>> imgCache= new HashMap<String, SoftReference<Drawable>>();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.mcontext = getApplicationContext();
		dWidth = Common.getScreenWidth(this);
	}
}
