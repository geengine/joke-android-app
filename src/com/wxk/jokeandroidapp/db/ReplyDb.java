package com.wxk.jokeandroidapp.db;

import java.util.List;

import com.wxk.jokeandroidapp.bean.ReplyBean;

import android.content.ContentValues;
import android.database.Cursor;

public class ReplyDb extends BaseDb<ReplyBean> {

	@Override
	public boolean update(ReplyBean e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(ReplyBean e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReplyBean getOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	ContentValues getContentValues(ReplyBean e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<ReplyBean> getEntity(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
