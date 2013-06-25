package com.wxk.jokeandroidapp.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.bean.PagerBean;
import com.wxk.util.LogUtil;
import com.wxk.util.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDb<E> {

	protected static final String TAG = "Db";
	private Context context;
	private DbHelper dbHelper;

	public BaseDb(Context context) {
		this.context = context;
		dbHelper = new DbHelper(this.context);
	}

	public BaseDb() {
		this(AppContext.context);
	}

	public String getTableName() {
		return null;
	}

	public DbHelper getDbHelper() {
		return dbHelper;
	}

	protected SQLiteDatabase getDb() {
		return getDbHelper().getReadableDatabase();
	}

	public boolean add(E e) {
		if (contains(e))
			return false;

		SQLiteDatabase db = getDb();

		long r = db.insert(getTableName(), null, getContentValues(e));

		db.close();

		return r > 0;
	};

	public abstract boolean update(E e);

	public boolean addAll(Collection<E> collection) {
		if (null != collection) {

			Iterator<E> iterator = collection.iterator();
			while (iterator.hasNext()) {
				this.add(iterator.next());
			}
			return true;
		}
		return false;

	};

	public abstract boolean contains(E e);

	public long getCount() {
		return getCount(null, null);
	};

	public long getCount(String where, String[] whereArgs) {
		SQLiteDatabase db = getDb();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM " + getTableName());
		Cursor cursor = null;
		if (null != where && !"".equals(where)) {
			sb.append(" WHERE " + where);
			cursor = db.rawQuery(sb.toString(), whereArgs);
		} else {
			cursor = db.rawQuery(sb.toString(), null);
		}
		long count = 0;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				count = cursor.getLong(0);
			}
		}

		cursor.close();
		db.close();
		LogUtil.d(TAG, "ROW COUNT { " + getTableName() + " } : " + count);
		return count;
	};

	public PagerBean<E> getPager(int page, int size) {
		return getPager(null, null, null, page, size);
	};

	public PagerBean<E> getPager(String where, String[] whereArgs,
			String orderBy, int page, int size) {

		PagerBean<E> pager = new PagerBean<E>(page, size, getCount(where,
				whereArgs));

		SQLiteDatabase db = getDb();
		Cursor cursor = db.query(getTableName(), null, where, whereArgs, null,
				null, orderBy, ((page - 1) * size) + "," + size);

		pager.setResult(getEntity(cursor));

		cursor.close();
		db.close();

		LogUtil.d(TAG, "WHERE { " + where + "," + StringUtil.toStr(whereArgs)
				+ " } ");
		LogUtil.d(TAG, "ORDERBY { " + orderBy + " } ");
		if (pager.getTotalSize() > 0)
			return pager;
		else
			return null;
	}

	public abstract E getOne(String id);

	abstract ContentValues getContentValues(E e);

	abstract List<E> getEntity(Cursor c);

}
