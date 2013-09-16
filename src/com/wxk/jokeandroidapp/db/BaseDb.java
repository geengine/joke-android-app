package com.wxk.jokeandroidapp.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wxk.jokeandroidapp.AppContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class BaseDb<E> {

	protected static final String TAG = "52lxh:sqlite";
	private Context context;
	private DbHelper dbHelper;
	private SQLiteDatabase db;

	public BaseDb(Context context) {
		this.context = context;
		dbHelper = DbHelper.newInstance(this.context);
	}

	public BaseDb() {
		this(AppContext.context);
	}

	public abstract String getTableName();

	public DbHelper getDbHelper() {
		return dbHelper;
	}

	protected SQLiteDatabase getDb() {
		db = getDbHelper().getWritableDatabase();
		if (!db.isOpen() || db == null)
			db = getDbHelper().getWritableDatabase();
		return db;
	}

	public boolean add(E e) {
		if (contains(e))
			return false;

		SQLiteDatabase db = getDb();

		long r = db.insert(getTableName(), null, getContentValues(e));

		// db.close();
		Log.d(TAG, String.format("::add(%s) => %s", e.getClass().getName(),
				(r > 0)));
		return r > 0;
	};

	public abstract boolean update(E e);

	public boolean addAll(Collection<E> collection) {
		if (null != collection) {
			boolean isAdd = false;
			Iterator<E> iterator = collection.iterator();
			while (iterator.hasNext()) {
				if (this.add(iterator.next())) {
					isAdd = true;
				}
			}
			return isAdd;
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
		//db.close();
		return count;
	};

	public List<E> getList(int page, int size) {
		return getList(null, null, null, page, size);
	}

	public List<E> getList(String where, String[] whereArgs, String orderBy,
			int page, int size) {
		List<E> list = null;
		SQLiteDatabase db = getDb();
		if (db.isOpen()) {
			Cursor cursor = db.query(getTableName(), null, where, whereArgs,
					null, null, orderBy, ((page - 1) * size) + "," + size);

			list = getEntity(cursor);

			cursor.close();
			//db.close();
		}
		return list;
	}

	public abstract E getOne(long id);

	abstract ContentValues getContentValues(E e);

	abstract List<E> getEntity(Cursor c);

}
