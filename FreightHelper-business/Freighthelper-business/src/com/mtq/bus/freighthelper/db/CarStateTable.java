/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: CarStateTable.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 车辆状态
 * @author: zhaoqy
 * @date: 2017年6月24日 下午4:43:07
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import java.util.ArrayList;
import java.util.List;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CarStateTable {

	public static final String TAG = "CarStateTable";
	public static final String CAR_STATE_TABLE = "car_state_table";
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://"
			+ DbManager.AUTHORITY + "/" + CAR_STATE_TABLE);

	public static final String ID = "_id";
	public static final String CAR_STATE_ID = "carid";
	public static final String CAR_STATE_LICENSE = "carlicense";
	public static final String CAR_STATE_STATUS = "carstatus";
	public static final String CAR_STATE_X = "x";
	public static final String CAR_STATE_Y = "y";
	public static final String CAR_STATE_GPSTIME = "gpstime";
	public static final String CAR_STATE_MILEAGE = "mileage";

	private static CarStateTable mInstance = null;

	public static CarStateTable getInstance() {
		if (mInstance == null) {
			synchronized (CarStateTable.class) {
				if (mInstance == null) {
					mInstance = new CarStateTable();
				}
			}
		}
		return mInstance;
	}

	public String getCreateSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(CAR_STATE_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(CAR_STATE_ID);
		sb.append(" Integer,");
		sb.append(CAR_STATE_LICENSE);
		sb.append(" TEXT,");
		sb.append(CAR_STATE_STATUS);
		sb.append(" Integer,");
		sb.append(CAR_STATE_X);
		sb.append(" Integer,");
		sb.append(CAR_STATE_Y);
		sb.append(" Integer,");
		sb.append(CAR_STATE_GPSTIME);
		sb.append(" Integer,");
		sb.append(CAR_STATE_MILEAGE);
		sb.append(" Integer");
		sb.append(");");

		return sb.toString();
	}

	public String getUpgradeSql() {
		String string = "DROP TABLE IF EXISTS " + CAR_STATE_TABLE;
		return string;
	}

	public void insert(List<MtqCarState> carStateList) {
		if (DbManager.mDbHelper == null)
			return;

		if (carStateList == null || carStateList.isEmpty())
			return;

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		db.beginTransaction();
		
		int len = carStateList.size();
		for (int i = 0; i < len; i++) {
			MtqCarState carState = carStateList.get(i);
			if (query(carState.carid) != null) {
				delete(carState.carid);
			}
			ContentValues values = new ContentValues();
			values.put(CAR_STATE_ID, carState.carid);
			values.put(CAR_STATE_LICENSE, carState.carlicense);
			values.put(CAR_STATE_STATUS, carState.carstatus);
			values.put(CAR_STATE_X, carState.x);
			values.put(CAR_STATE_Y, carState.y);
			values.put(CAR_STATE_GPSTIME, carState.gpstime);
			values.put(CAR_STATE_MILEAGE, carState.mileage);
			db.insert(CAR_STATE_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<MtqCarState> query() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqCarState> carStateList = new ArrayList<MtqCarState>();
		MtqCarState carState = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(CAR_STATE_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				carState = new MtqCarState();
				carState.carid = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_ID));
				carState.carlicense = cursor.getString(cursor
						.getColumnIndex(CAR_STATE_LICENSE));
				carState.carstatus = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_STATUS));
				carState.x = cursor.getInt(cursor.getColumnIndex(CAR_STATE_X));
				carState.y = cursor.getInt(cursor.getColumnIndex(CAR_STATE_Y));
				carState.gpstime = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_GPSTIME));
				carState.mileage = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_MILEAGE));
				carStateList.add(carState);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return carStateList;
	}

	public List<MtqCarState> queryByStatus(int carstatus) {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqCarState> carStateList = new ArrayList<MtqCarState>();
		MtqCarState carState = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		where = CAR_STATE_STATUS + "=" + carstatus;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(CAR_STATE_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				carState = new MtqCarState();
				carState.carid = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_ID));
				carState.carlicense = cursor.getString(cursor
						.getColumnIndex(CAR_STATE_LICENSE));
				carState.carstatus = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_STATUS));
				carState.x = cursor.getInt(cursor.getColumnIndex(CAR_STATE_X));
				carState.y = cursor.getInt(cursor.getColumnIndex(CAR_STATE_Y));
				carState.gpstime = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_GPSTIME));
				carState.mileage = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_MILEAGE));
				carStateList.add(carState);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return carStateList;
	}

	public MtqCarState query(int carid) {
		if (DbManager.mDbHelper == null)
			return null;

		MtqCarState carState = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		where = CAR_STATE_ID + "=" + carid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(CAR_STATE_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				carState = new MtqCarState();
				carState.carid = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_ID));
				carState.carlicense = cursor.getString(cursor
						.getColumnIndex(CAR_STATE_LICENSE));
				carState.carstatus = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_STATUS));
				carState.x = cursor.getInt(cursor.getColumnIndex(CAR_STATE_X));
				carState.y = cursor.getInt(cursor.getColumnIndex(CAR_STATE_Y));
				carState.gpstime = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_GPSTIME));
				carState.mileage = cursor.getInt(cursor
						.getColumnIndex(CAR_STATE_MILEAGE));
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return carState;
	}

	public void delete(int carid) {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		where = CAR_STATE_ID + "=" + carid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(CAR_STATE_TABLE, where, null);
	}

	public void delete() {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(CAR_STATE_TABLE, where, null);
	}
}
