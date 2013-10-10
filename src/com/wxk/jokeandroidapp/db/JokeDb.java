package com.wxk.jokeandroidapp.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.util.LogUtil;
import com.wxk.util.UniqueList;

public class JokeDb extends BaseDb<JokeBean> {

	final String TABLE = "t_joke";

	@Override
	public String getTableName() {
		return TABLE;
	}

	@Override
	public boolean update(JokeBean e) {
		SQLiteDatabase db = getDb();
		ContentValues values = getContentValues(e);
		LogUtil.i(TAG, "UPDATE " + TABLE + " SET " + values.toString());
		long r = db.update(TABLE, values, "id=?",
				new String[] { e.getId() + "" });
		db.close();
		LogUtil.i(TAG, "RESULT: " + (r > 0));
		return r > 0;
	}

	public boolean updateGood(long id, int good) {
		ContentValues values = new ContentValues();

		values.put("goods", good);
		SQLiteDatabase db = getDb();
		long r = db.update(TABLE, values, "id=?", new String[] { "" + id });
		db.close();
		return r > 0;
	}

	public boolean updateBad(long id, int bad) {
		ContentValues values = new ContentValues();

		values.put("bads", bad);
		SQLiteDatabase db = getDb();
		long r = db.update(TABLE, values, "id=?", new String[] { "" + id });
		db.close();
		return r > 0;
	}

	public boolean updateReplyPlusPlus(long id) {
		ContentValues values = new ContentValues();

		values.put("bads", "bads+1");
		SQLiteDatabase db = getDb();
		long r = db.update(TABLE, values, "id=?", new String[] { "" + id });
		db.close();
		return r > 0;
	}

	@Override
	public List<JokeBean> getList(int page, int size) {

		return super.getList(null, null, "id DESC", page, size);
	}

	public List<JokeBean> getList(int page, int size, int topic) {
		if (topic == 0) { // no filter
			return this.getList(page, size);
		}
		return super.getList("topic=?", new String[] { "" + topic }, "id DESC",
				page, size);
	}

	@Override
	public boolean contains(JokeBean e) {
		if (null != e) {
			JokeBean b = getOne(e.getId());
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
	public JokeBean getOne(long id) {
		List<JokeBean> list = super.getList("id=?", new String[] { "" + id },
				"id DESC", 1, 5);

		return null != list && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	ContentValues getContentValues(JokeBean e) {
		ContentValues values = new ContentValues();

		values.put("id", e.getId());
		values.put("title", e.getTitle());
		values.put("content", e.getContent());
		values.put("clicks", e.getClickCount());
		values.put("replys", e.getReplyCount());
		values.put("goods", e.getGooodCount());
		values.put("bads", e.getBadCount());
		values.put("imgurl", e.getImgUrl());
		values.put("active_date", e.getActiveDate());
		if (e.getTopic() > 0)
			values.put("topic", e.getTopic());
		return values;
	}

	@Override
	List<JokeBean> getEntity(Cursor c) {
		List<JokeBean> list = null;
		if (null != c) {
			list = new UniqueList<JokeBean>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				JokeBean bean = new JokeBean();
				bean.setId(c.getInt(c.getColumnIndex("id")));
				bean.setTitle(c.getString(c.getColumnIndex("title")));
				bean.setContent(c.getString(c.getColumnIndex("content")));
				bean.setClickCount(c.getInt(c.getColumnIndex("clicks")));
				bean.setReplyCount(c.getInt(c.getColumnIndex("replys")));
				bean.setGooodCount(c.getInt(c.getColumnIndex("goods")));
				bean.setBadCount(c.getInt(c.getColumnIndex("bads")));
				bean.setActiveDate(c.getString(c.getColumnIndex("active_date")));
				bean.setImgUrl(c.getString(c.getColumnIndex("imgurl")));
				list.add(bean);
			}
		}
		return list;
	}

}
