package com.wxk.jokeandroidapp.db;

import java.util.List;

import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.util.LogUtil;
import com.wxk.util.UniqueList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReplyDb extends BaseDb<ReplyBean> {

	final String TABLE = "t_reply";

	@Override
	public String getTableName() {
		return TABLE;
	}

	@Override
	public boolean update(ReplyBean e) {
		SQLiteDatabase db = getDb();
		ContentValues values = getContentValues(e);
		LogUtil.d(TAG, "UPDATE " + TABLE + " SET " + values.toString());
		long r = db.update(TABLE, values, "id=?",
				new String[] { e.getId() + "" });
		db.close();
		LogUtil.d(TAG, "RESULT: " + (r > 0));
		return r > 0;
	}

	@Override
	public boolean contains(ReplyBean e) {
		if (null != e) {
			ReplyBean b = getOne(e.getId());
			if (null != b && b.equals(e)) {
				if (!b.equalsAll(e)) {
					update(e);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public ReplyBean getOne(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ReplyBean> getList(long jokeId, int page, int size) {
		return this.getList("j_id=?", new String[] { "" + jokeId },
				"active_date DESC", page, size);
	}

	@Override
	ContentValues getContentValues(ReplyBean e) {
		ContentValues values = new ContentValues();
		values.put("id", e.getId());
		values.put("j_id", e.getJokeID());
		values.put("content", e.getContent());
		values.put("active_date", e.getActiveDate());
		return values;
	}

	@Override
	List<ReplyBean> getEntity(Cursor c) {
		List<ReplyBean> list = null;
		if (null != c) {
			list = new UniqueList<ReplyBean>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				ReplyBean bean = new ReplyBean();
				bean.setId(c.getInt(c.getColumnIndex("id")));
				bean.setJokeID(c.getInt(c.getColumnIndex("j_id")));
				bean.setContent(c.getString(c.getColumnIndex("content")));
				bean.setActiveDate(c.getString(c.getColumnIndex("active_date")));
				list.add(bean);
			}
		}
		return list;
	}

}
