package com.wxk.jokeandroidapp.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.wxk.jokeandroidapp.bean.JokeBean;

public class JokeDb extends BaseDb<JokeBean>{

	@Override
	public boolean update(JokeBean e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(JokeBean e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JokeBean getOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	ContentValues getContentValues(JokeBean e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<JokeBean> getEntity(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

}
