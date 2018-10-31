/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DriverTable.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 司机列表Table
 * @author: zhaoqy
 * @date: 2017年6月27日 上午11:47:50
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import java.util.ArrayList;
import java.util.List;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DriverTable {

	public static final String TAG = "DriverTable";
	public static final String DRIVER_TABLE = "driver_table";
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://"
			+ DbManager.AUTHORITY + "/" + DRIVER_TABLE);

	public static final String ID = "_id";
	public static final String DRIVER_ID = "driverid";
	public static final String DRIVER_NAME = "driver_name";
	public static final String DRIVER_PHONE = "driver_phone";
	public static final String DRIVER_STATUS = "driver_invitestatus";

	private static DriverTable mInstance = null;

	public static DriverTable getInstance() {
		if (mInstance == null) {
			synchronized (DriverTable.class) {
				if (mInstance == null) {
					mInstance = new DriverTable();
				}
			}
		}
		return mInstance;
	}

	public String getCreateSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(DRIVER_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(DRIVER_ID);
		sb.append(" Integer,");
		sb.append(DRIVER_NAME);
		sb.append(" TEXT,");
		sb.append(DRIVER_PHONE);
		sb.append(" TEXT,");
		sb.append(DRIVER_STATUS);
		sb.append(" Integer");
		sb.append(");");

		return sb.toString();
	}

	public String getUpgradeSql() {
		String string = "DROP TABLE IF EXISTS " + DRIVER_TABLE;
		return string;
	}

	public void insert(List<MtqDriver> driverList) {
		if (DbManager.mDbHelper == null)
			return;

		if (driverList == null || driverList.isEmpty())
			return;

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		db.beginTransaction();
		int len = driverList.size();
		for (int i = 0; i < len; i++) {
			MtqDriver driver = driverList.get(i);
			if (query(driver.driverid) != null) {
				delete(driver.driverid);
			}

			ContentValues values = new ContentValues();
			values.put(DRIVER_ID, driver.driverid);
			values.put(DRIVER_NAME, driver.driver_name);
			values.put(DRIVER_PHONE, driver.phone);
			values.put(DRIVER_STATUS, driver.invitestatus);
			db.insert(DRIVER_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<MtqDriver> query() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqDriver> driverList = new ArrayList<MtqDriver>();
		MtqDriver driver = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " ASC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(DRIVER_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				driver = new MtqDriver();
				driver.driverid = cursor.getInt(cursor
						.getColumnIndex(DRIVER_ID));
				driver.driver_name = cursor.getString(cursor
						.getColumnIndex(DRIVER_NAME));
				driver.phone = cursor.getString(cursor
						.getColumnIndex(DRIVER_PHONE));
				driver.invitestatus = cursor.getInt(cursor
						.getColumnIndex(DRIVER_STATUS));
				driverList.add(driver);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return driverList;
	}

	public MtqDriver query(int driverid) {
		if (DbManager.mDbHelper == null)
			return null;

		MtqDriver driver = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " ASC";
		where = DRIVER_ID + "=" + driverid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(DRIVER_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				driver = new MtqDriver();
				driver.driverid = cursor.getInt(cursor
						.getColumnIndex(DRIVER_ID));
				driver.driver_name = cursor.getString(cursor
						.getColumnIndex(DRIVER_NAME));
				driver.phone = cursor.getString(cursor
						.getColumnIndex(DRIVER_PHONE));
				driver.invitestatus = cursor.getInt(cursor
						.getColumnIndex(DRIVER_STATUS));
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return driver;
	}

	public List<MtqDriver> queryByStatus(int invitestatus) {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqDriver> driverList = new ArrayList<MtqDriver>();
		MtqDriver driver = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " ASC";
		where = DRIVER_STATUS + "=" + invitestatus;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(DRIVER_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				driver = new MtqDriver();
				driver.driverid = cursor.getInt(cursor
						.getColumnIndex(DRIVER_ID));
				driver.driver_name = cursor.getString(cursor
						.getColumnIndex(DRIVER_NAME));
				driver.phone = cursor.getString(cursor
						.getColumnIndex(DRIVER_PHONE));
				driver.invitestatus = cursor.getInt(cursor
						.getColumnIndex(DRIVER_STATUS));
				driverList.add(driver);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return driverList;
	}

	public void delete(int driverid) {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		where = DRIVER_ID + "=" + driverid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(DRIVER_TABLE, where, null);
	}

	public void delete() {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(DRIVER_TABLE, where, null);
	}
}
