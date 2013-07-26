package com.wxk.jokeandroidapp.dao;

import com.wxk.jokeandroidapp.AppContext;

public class BaseDao {
	protected final String TAG = "DAO";

	/**
	 * @param key
	 */
	protected void setMemCacheFinger(String key) {
		AppContext.setMemCacheFinger(key);
	}

	/**
	 * @param key
	 * @return
	 */
	protected Long getMemCacheFinger(String key) {
		return AppContext.getMemCacheFinger(key);
	}

	/**
	 * @param key
	 * @return
	 */
	protected boolean isCacheDataFailure(String key) {
		return AppContext.isCacheDataFailure(key);
	}
}
