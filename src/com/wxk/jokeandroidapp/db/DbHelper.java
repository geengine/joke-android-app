package com.wxk.jokeandroidapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author fengpengbin@gmail.com
 * 
 */
public class DbHelper extends SQLiteOpenHelper {

	private static final int VERSION = 2; // database version
	private static final String DB_NAME = "52_jokes_db"; // database name
	private static DbHelper uniqeInstance;

	public static DbHelper newInstance(Context context) {
		if (uniqeInstance == null) {
			uniqeInstance = new DbHelper(context);
		}
		return uniqeInstance;
	}

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DbHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create database table
		createDataTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// update database version
		if (oldVersion == 1 && newVersion == VERSION) {
			db.execSQL("ALERT TABLE t_joke ADD COLUMN topic INTEGER DEFAULT 0");
		}

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// on after open database running
	}

	/**
	 * create database tables
	 * 
	 * @param db
	 */
	public void createDataTables(SQLiteDatabase db) {
		// jokes
		db.execSQL("CREATE TABLE IF NOT EXISTS t_joke (id INTEGER PRIMARY KEY,title VARCHAR,content TEXT,imgurl VARCHAR,replys INTEGER,clicks INTEGER,goods INTEGER,bads INTEGER,active_date VARCHAR,topic INTEGER DEFAULT 0)");
		// reply/comment
		db.execSQL("CREATE TABLE IF NOT EXISTS t_reply (id,INTEGER PRIMAYR KEY,j_id INTEGER,content VARCHAR,active_date VARCHAR)");
	}

	/**
	 * delete data tables
	 */
	public void deleteDataTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("t_joke", null, null);
		db.delete("t_reply", null, null);
		db.close();
		this.close();
	}

}
