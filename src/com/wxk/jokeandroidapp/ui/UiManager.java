package com.wxk.jokeandroidapp.ui;

import java.util.Stack;

import com.wxk.jokeandroidapp.App;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

//import android.widget.ImageView;

public class UiManager {
	private static UiManager uniqueInstance = null;
	private Stack<Activity> activityStack;
	private Boolean isExit = false;
	private LayoutInflater inflater;

	public LayoutInflater getInflater() {
		if (null == inflater)
			inflater = LayoutInflater.from(App.context);
		return inflater;
	}

	public Boolean getIsExit() {
		return isExit;
	}

	public void setIsExit(Boolean isExit) {
		this.isExit = isExit;
	}

	public static UiManager getInstance() {
		if (null == uniqueInstance)
			uniqueInstance = new UiManager();
		return uniqueInstance;
	}

	public Integer getAppSize() {
		if (null != activityStack)
			return activityStack.size();
		return 0;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		isExit = false;
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void appExit(Context context) {
		try {
			finishAllActivity();
			System.exit(0);
		} catch (Exception e) {
		}
	}

}
