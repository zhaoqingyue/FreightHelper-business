/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DTypeTable.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 设备类型Table
 * @author: zhaoqy
 * @date: 2017年6月21日 下午10:50:23
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import java.util.ArrayList;
import java.util.List;

import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDeviceDType;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DTypeTable {

	public static final String TAG = "DTypeTable";
	public static final String DEVICE_DTYPE_TABLE = "device_dtype_table";
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://"
			+ DbManager.AUTHORITY + "/" + DEVICE_DTYPE_TABLE);

	public static final String ID = "_id";
	public static final String DEVICE_DTYPE = "device_dtype";
	public static final String DEVICE_DNAME = "device_dname";

	private static DTypeTable mInstance = null;

	public static DTypeTable getInstance() {
		if (mInstance == null) {
			synchronized (DTypeTable.class) {
				if (mInstance == null) {
					mInstance = new DTypeTable();
				}
			}
		}
		return mInstance;
	}

	public String getCreateSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(DEVICE_DTYPE_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(DEVICE_DTYPE);
		sb.append(" Integer,");
		sb.append(DEVICE_DNAME);
		sb.append(" TEXT");
		sb.append(");");

		return sb.toString();
	}

	public String getUpgradeSql() {
		String string = "DROP TABLE IF EXISTS " + DEVICE_DTYPE_TABLE;
		return string;
	}

	public void insert(List<MtqDeviceDType> dtypeList) {
		if (DbManager.mDbHelper == null)
			return;

		if (dtypeList == null || dtypeList.isEmpty())
			return;

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		db.beginTransaction();
		delete();
		int len = dtypeList.size();
		for (int i = 0; i < len; i++) {
			MtqDeviceDType dtype = dtypeList.get(i);
			ContentValues values = new ContentValues();
			values.put(DEVICE_DTYPE, dtype.dtype);
			values.put(DEVICE_DNAME, dtype.dname);
			db.insert(DEVICE_DTYPE_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<MtqDeviceDType> query() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqDeviceDType> dtypeList = new ArrayList<MtqDeviceDType>();
		MtqDeviceDType dtype = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " ASC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(DEVICE_DTYPE_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				dtype = new MtqDeviceDType();
				dtype.dtype = cursor
						.getInt(cursor.getColumnIndex(DEVICE_DTYPE));
				dtype.dname = cursor.getString(cursor
						.getColumnIndex(DEVICE_DNAME));
				dtypeList.add(dtype);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return dtypeList;
	}

	public void delete(int dtype) {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		where = DEVICE_DTYPE + "=" + dtype;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(DEVICE_DTYPE_TABLE, where, null);
	}

	public void delete() {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(DEVICE_DTYPE_TABLE, where, null);
	}
}
