/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DbManager.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 数据库管理器
 * @author: zhaoqy
 * @date: 2017年6月20日 下午10:14:17
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DbManager extends ContentProvider {
	public static final String AUTHORITY = "com.mtq.bus.freighthelper.db.DbManager";
	public static final String DB_NAME = "busfreighthelper.db";
	public static final int DB_VERSION = 1;
	public static SQLiteDatabase mSqlDB;
	public static DatabaseHelper mDbHelper;
	public static Context mContext;

	@Override
	public boolean onCreate() {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(getContext());
			mContext = getContext();
		}
		return (mDbHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		qb.setTables(uri.getPathSegments().get(0));

		Cursor c = qb.query(db, projection, selection, null, null, null,
				sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String s, String[] as) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		return db.delete(uri.getPathSegments().get(0), s, as);
	}

	@Override
	public int update(Uri uri, ContentValues values, String s, String[] as) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		return db.update(uri.getPathSegments().get(0), values, s, as);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MsgAlarmTable.getInstance().getCreateSql());
			db.execSQL(MsgSysTable.getInstance().getCreateSql());
			
			db.execSQL(DTypeTable.getInstance().getCreateSql());
			db.execSQL(CarStateTable.getInstance().getCreateSql());
			db.execSQL(DriverTable.getInstance().getCreateSql());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(MsgAlarmTable.getInstance().getUpgradeSql());
			db.execSQL(MsgSysTable.getInstance().getUpgradeSql());
			
			db.execSQL(DTypeTable.getInstance().getUpgradeSql());
			db.execSQL(CarStateTable.getInstance().getUpgradeSql());
			db.execSQL(DriverTable.getInstance().getUpgradeSql());
			onCreate(db);
		}
	}
}
