package com.wxk.jokeandroidapp.db;

import java.util.List;

import com.wxk.jokeandroidapp.bean.ReplyBean;

import android.content.ContentValues;
import android.database.Cursor;

public class ReplyDb extends BaseDb<ReplyBean> {

	final String TABLE = "t_reply";

	@Override
	public String getTableName() {
		return TABLE;
	}

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
	public ReplyBean getOne(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ReplyBean> getList(int jokeId, int page, int size) {
		return this.getList("j_id", new String[] { "" + jokeId },
				"active_date DESC", page, size);
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
