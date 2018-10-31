/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MsgAlarmTable.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 警情消息
 * @author: zhaoqy
 * @date: 2017年6月20日 下午11:22:45
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;

public class MsgAlarmTable {

	public static final String TAG = "MsgAlarmTable";
	public static final String MSG_ALARM_TABLE = "msg_alarm_table";
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://"
			+ DbManager.AUTHORITY + "/" + MSG_ALARM_TABLE);

	public static final String ID = "_id";
	public static final String ALARM_SERIALID = "alarm_serialid";
	public static final String ALARM_CARID = "alarm_carid";
	public static final String ALARM_CARLICENSE = "alarm_carlicense";
	public static final String ALARM_X = "alarm_x";
	public static final String ALARM_Y = "alarm_y";
	public static final String ALARM_SPEED = "alarm_speed";
	public static final String ALARM_DIRECTION = "alarm_direction";
	public static final String ALARM_ALARMID = "alarm_alarmid";
	public static final String ALARM_EVENTJSON = "alarm_eventjson";
	public static final String ALARM_LOCATTIME = "alarm_locattime";
	public static final String ALARM_DRIVER_NAME = "alarm_driver_name";
	public static final String ALARM_DRIVER_PHONE = "alarm_driver_phone";
	public static final String ALARM_MDRIVER = "alarm_mdriver";
	public static final String ALARM_MPHONE = "alarm_mphone";
	public static final String ALARM_SDRIVER = "alarm_sdriver";
	public static final String ALARM_SPHONE = "alarm_sphone";
	public static final String ALARM_READSTATUS = "alarm_readstatus";
	public static final String ALARM_TYPE = "alarm_type";

	private static MsgAlarmTable mInstance = null;

	public static MsgAlarmTable getInstance() {
		if (mInstance == null) {
			synchronized (MsgAlarmTable.class) {
				if (mInstance == null) {
					mInstance = new MsgAlarmTable();
				}
			}
		}
		return mInstance;
	}

	public String getCreateSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(MSG_ALARM_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(ALARM_SERIALID);
		sb.append(" TEXT,");
		sb.append(ALARM_CARID);
		sb.append(" Integer,");
		sb.append(ALARM_CARLICENSE);
		sb.append(" TEXT,");
		sb.append(ALARM_X);
		sb.append(" Integer,");
		sb.append(ALARM_Y);
		sb.append(" Integer,");
		sb.append(ALARM_SPEED);
		sb.append(" Integer,");
		sb.append(ALARM_DIRECTION);
		sb.append(" Integer,");
		sb.append(ALARM_ALARMID);
		sb.append(" Integer,");
		sb.append(ALARM_EVENTJSON);
		sb.append(" TEXT,");
		sb.append(ALARM_LOCATTIME);
		sb.append(" Integer,");
		sb.append(ALARM_DRIVER_NAME);
		sb.append(" TEXT,");
		sb.append(ALARM_DRIVER_PHONE);
		sb.append(" TEXT,");
		sb.append(ALARM_MDRIVER);
		sb.append(" TEXT,");
		sb.append(ALARM_MPHONE);
		sb.append(" TEXT,");
		sb.append(ALARM_SDRIVER);
		sb.append(" TEXT,");
		sb.append(ALARM_SPHONE);
		sb.append(" TEXT,");
		sb.append(ALARM_READSTATUS);
		sb.append(" Integer,");
		sb.append(ALARM_TYPE);
		sb.append(" Integer");
		sb.append(");");

		return sb.toString();
	}

	public String getUpgradeSql() {
		String string = "DROP TABLE IF EXISTS " + MSG_ALARM_TABLE;
		return string;
	}

	public void insert(List<MtqMsgAlarm> msgList) {
		if (DbManager.mDbHelper == null)
			return;

		if (msgList == null || msgList.isEmpty())
			return;

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		db.beginTransaction();
		int len = msgList.size();
		for (int i = 0; i < len; i++) {
			MtqMsgAlarm msg = msgList.get(i);
			if (query(msg.id) != null) {
				delete(msg.id);
			}
			ContentValues values = new ContentValues();
			values.put(ALARM_SERIALID, msg.id);
			values.put(ALARM_CARID, msg.carid);
			values.put(ALARM_CARLICENSE, msg.carlicense);
			values.put(ALARM_X, msg.x);
			values.put(ALARM_Y, msg.y);
			values.put(ALARM_SPEED, msg.speed);
			values.put(ALARM_DIRECTION, msg.direction);
			values.put(ALARM_ALARMID, msg.alarmid);
			values.put(ALARM_EVENTJSON, msg.eventjson);
			values.put(ALARM_LOCATTIME, msg.locattime);
			values.put(ALARM_DRIVER_NAME, msg.driver_name);
			values.put(ALARM_DRIVER_PHONE, msg.driver_phone);
			values.put(ALARM_MDRIVER, msg.mdriver);
			values.put(ALARM_MPHONE, msg.mphone);
			values.put(ALARM_SDRIVER, msg.sdriver);
			values.put(ALARM_SPHONE, msg.sphone);
			values.put(ALARM_READSTATUS, msg.readstatus);
			values.put(ALARM_TYPE, msg.alarmType);
			db.insert(MSG_ALARM_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 更新本地消息阅读状态
	 */
	public void update(List<MtqMsgAlarm> msgList) {
		if (DbManager.mDbHelper == null)
			return;

		if (msgList == null || msgList.isEmpty())
			return;

		int len = msgList.size();
		for (int i = 0; i < len; i++) {
			MtqMsgAlarm msg = msgList.get(i);
			update(msg);
		}
	}

	/**
	 * 更新本地消息阅读状态
	 */
	public void update(MtqMsgAlarm msg) {
		if (DbManager.mDbHelper == null || msg == null)
			return;

		String where = null;
		where = ALARM_SERIALID + "=\"" + msg.id + "\"";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		ContentValues values = new ContentValues();
		values.put(ALARM_READSTATUS, 1); // 已读
		db.update(MSG_ALARM_TABLE, values, where, null);
	}

	public MtqMsgAlarm query(String serialid) {
		if (DbManager.mDbHelper == null)
			return null;

		MtqMsgAlarm msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		where = ALARM_SERIALID + "=\"" + serialid + "\"";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(MSG_ALARM_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgAlarm();
				msg.id = cursor
						.getString(cursor.getColumnIndex(ALARM_SERIALID));
				msg.carid = cursor.getInt(cursor.getColumnIndex(ALARM_CARID));
				msg.carlicense = cursor.getString(cursor
						.getColumnIndex(ALARM_CARLICENSE));
				msg.x = cursor.getInt(cursor.getColumnIndex(ALARM_X));
				msg.y = cursor.getInt(cursor.getColumnIndex(ALARM_Y));
				msg.speed = cursor.getInt(cursor.getColumnIndex(ALARM_SPEED));
				msg.direction = cursor.getInt(cursor
						.getColumnIndex(ALARM_DIRECTION));
				msg.alarmid = cursor.getInt(cursor
						.getColumnIndex(ALARM_ALARMID));
				msg.locattime = cursor.getInt(cursor
						.getColumnIndex(ALARM_LOCATTIME));
				/**
				 * eventjson
				 */
				msg.eventjson = cursor.getString(cursor
						.getColumnIndex(ALARM_EVENTJSON));
				msg.driver_name = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_NAME));
				msg.driver_phone = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_PHONE));
				msg.mdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_MDRIVER));
				msg.mphone = cursor.getString(cursor
						.getColumnIndex(ALARM_MPHONE));
				msg.sdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_SDRIVER));
				msg.sphone = cursor.getString(cursor
						.getColumnIndex(ALARM_SPHONE));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(ALARM_READSTATUS));
				msg.alarmType = cursor
						.getInt(cursor.getColumnIndex(ALARM_TYPE));
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msg;
	}

	public List<MtqMsgAlarm> query() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqMsgAlarm> msgList = new ArrayList<MtqMsgAlarm>();
		MtqMsgAlarm msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(MSG_ALARM_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgAlarm();
				msg.id = cursor
						.getString(cursor.getColumnIndex(ALARM_SERIALID));
				msg.carid = cursor.getInt(cursor.getColumnIndex(ALARM_CARID));
				msg.carlicense = cursor.getString(cursor
						.getColumnIndex(ALARM_CARLICENSE));
				msg.x = cursor.getInt(cursor.getColumnIndex(ALARM_X));
				msg.y = cursor.getInt(cursor.getColumnIndex(ALARM_Y));
				msg.speed = cursor.getInt(cursor.getColumnIndex(ALARM_SPEED));
				msg.direction = cursor.getInt(cursor
						.getColumnIndex(ALARM_DIRECTION));
				msg.alarmid = cursor.getInt(cursor
						.getColumnIndex(ALARM_ALARMID));
				msg.locattime = cursor.getInt(cursor
						.getColumnIndex(ALARM_LOCATTIME));
				/**
				 * eventjson
				 */
				msg.eventjson = cursor.getString(cursor
						.getColumnIndex(ALARM_EVENTJSON));
				msg.driver_name = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_NAME));
				msg.driver_phone = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_PHONE));
				msg.mdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_MDRIVER));
				msg.mphone = cursor.getString(cursor
						.getColumnIndex(ALARM_MPHONE));
				msg.sdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_SDRIVER));
				msg.sphone = cursor.getString(cursor
						.getColumnIndex(ALARM_SPHONE));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(ALARM_READSTATUS));
				msg.alarmType = cursor
						.getInt(cursor.getColumnIndex(ALARM_TYPE));
				msgList.add(msg);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msgList;
	}

	public List<MtqMsgAlarm> queryUnRead() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqMsgAlarm> msgList = new ArrayList<MtqMsgAlarm>();
		MtqMsgAlarm msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		where = ALARM_READSTATUS + "=" + 0;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(MSG_ALARM_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgAlarm();
				msg.id = cursor
						.getString(cursor.getColumnIndex(ALARM_SERIALID));
				msg.id = cursor
						.getString(cursor.getColumnIndex(ALARM_SERIALID));
				msg.carid = cursor.getInt(cursor.getColumnIndex(ALARM_CARID));
				msg.carlicense = cursor.getString(cursor
						.getColumnIndex(ALARM_CARLICENSE));
				msg.x = cursor.getInt(cursor.getColumnIndex(ALARM_X));
				msg.y = cursor.getInt(cursor.getColumnIndex(ALARM_Y));
				msg.speed = cursor.getInt(cursor.getColumnIndex(ALARM_SPEED));
				msg.direction = cursor.getInt(cursor
						.getColumnIndex(ALARM_DIRECTION));
				msg.alarmid = cursor.getInt(cursor
						.getColumnIndex(ALARM_ALARMID));
				msg.locattime = cursor.getInt(cursor
						.getColumnIndex(ALARM_LOCATTIME));
				/**
				 * eventjson
				 */
				msg.eventjson = cursor.getString(cursor
						.getColumnIndex(ALARM_EVENTJSON));
				msg.driver_name = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_NAME));
				msg.driver_phone = cursor.getString(cursor
						.getColumnIndex(ALARM_DRIVER_PHONE));
				msg.mdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_MDRIVER));
				msg.mphone = cursor.getString(cursor
						.getColumnIndex(ALARM_MPHONE));
				msg.sdriver = cursor.getString(cursor
						.getColumnIndex(ALARM_SDRIVER));
				msg.sphone = cursor.getString(cursor
						.getColumnIndex(ALARM_SPHONE));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(ALARM_READSTATUS));
				msg.alarmType = cursor
						.getInt(cursor.getColumnIndex(ALARM_TYPE));
				msgList.add(msg);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msgList;
	}

	public int queryUnReadCount() {
		if (DbManager.mDbHelper == null)
			return 0;

		int count = 0;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		where = ALARM_READSTATUS + "=" + 0;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db.query(MSG_ALARM_TABLE, null, where, null, null, null,
				orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			count = cursor.getCount();
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return count;
	}

	public void delete(String serialid) {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		where = ALARM_SERIALID + "=\"" + serialid + "\"";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(MSG_ALARM_TABLE, where, null);
	}

	public void delete() {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(MSG_ALARM_TABLE, where, null);
	}
}
