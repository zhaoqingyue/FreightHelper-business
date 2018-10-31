/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MsgSysTable.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.db
 * @Description: 系统消息Table
 * @author: zhaoqy
 * @date: 2017年6月20日 下午10:16:59
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.db;

import java.util.ArrayList;
import java.util.List;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MsgSysTable {

	public static final String TAG = "MsgSysTable";
	public static final String MSG_SYS_TABLE = "msg_sys_table";
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://"
			+ DbManager.AUTHORITY + "/" + MSG_SYS_TABLE);

	public static final String ID = "_id";
	public static final String SYS_SERIALID = "sys_serialid";
	public static final String SYS_TIME = "sys_time";
	public static final String SYS_TITLE = "sys_title";
	public static final String SYS_CONTENT = "sys_content";
	public static final String SYS_PUBLISHER = "sys_publisher";
	public static final String SYS_READSTATUS = "sys_readstatus";

	private static MsgSysTable mInstance = null;

	public static MsgSysTable getInstance() {
		if (mInstance == null) {
			synchronized (MsgSysTable.class) {
				if (mInstance == null) {
					mInstance = new MsgSysTable();
				}
			}
		}
		return mInstance;
	}

	public String getCreateSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(MSG_SYS_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(SYS_SERIALID);
		sb.append(" Integer,");
		sb.append(SYS_TIME);
		sb.append(" Integer,");
		sb.append(SYS_TITLE);
		sb.append(" TEXT,");
		sb.append(SYS_CONTENT);
		sb.append(" TEXT,");
		sb.append(SYS_PUBLISHER);
		sb.append(" TEXT,");
		sb.append(SYS_READSTATUS);
		sb.append(" Integer");
		sb.append(");");

		return sb.toString();
	}

	public String getUpgradeSql() {
		String string = "DROP TABLE IF EXISTS " + MSG_SYS_TABLE;
		return string;
	}

	public void insert(List<MtqMsgSys> msgList) {
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
			MtqMsgSys msg = msgList.get(i);
			if (query(msg.serialid) != null) {
				delete(msg.serialid);
			}
			ContentValues values = new ContentValues();
			values.put(SYS_SERIALID, msg.serialid);
			values.put(SYS_TIME, msg.time);
			values.put(SYS_TITLE, msg.title);
			values.put(SYS_CONTENT, msg.content);
			values.put(SYS_PUBLISHER, msg.publisher);
			values.put(SYS_READSTATUS, msg.readstatus);
			db.insert(MSG_SYS_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 更新本地消息阅读状态
	 */
	public void update(List<MtqMsgSys> msgList) {
		if (DbManager.mDbHelper == null)
			return;

		if (msgList == null || msgList.isEmpty())
			return;

		int len = msgList.size();
		for (int i = 0; i < len; i++) {
			MtqMsgSys msg = msgList.get(i);
			update(msg);
		}
	}

	/**
	 * 更新本地消息阅读状态
	 */
	public void update(MtqMsgSys msg) {
		if (DbManager.mDbHelper == null || msg == null)
			return;

		String where = null;
		where = SYS_SERIALID + "=" + msg.serialid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));

		ContentValues values = new ContentValues();
		values.put(SYS_READSTATUS, 1); // 已读
		db.update(MSG_SYS_TABLE, values, where, null);
	}

	public MtqMsgSys query(int serialid) {
		if (DbManager.mDbHelper == null)
			return null;

		MtqMsgSys msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		where = SYS_SERIALID + "=" + serialid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db
				.query(MSG_SYS_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgSys();
				msg.serialid = cursor.getInt(cursor
						.getColumnIndex(SYS_SERIALID));
				msg.time = cursor.getInt(cursor.getColumnIndex(SYS_TIME));
				msg.title = cursor.getString(cursor.getColumnIndex(SYS_TITLE));
				msg.content = cursor.getString(cursor
						.getColumnIndex(SYS_CONTENT));
				msg.publisher = cursor.getString(cursor
						.getColumnIndex(SYS_PUBLISHER));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(SYS_READSTATUS));
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msg;
	}

	public List<MtqMsgSys> query() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqMsgSys> msgList = new ArrayList<MtqMsgSys>();
		MtqMsgSys msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db
				.query(MSG_SYS_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgSys();
				msg.serialid = cursor.getInt(cursor
						.getColumnIndex(SYS_SERIALID));
				msg.time = cursor.getInt(cursor.getColumnIndex(SYS_TIME));
				msg.title = cursor.getString(cursor.getColumnIndex(SYS_TITLE));
				msg.content = cursor.getString(cursor
						.getColumnIndex(SYS_CONTENT));
				msg.publisher = cursor.getString(cursor
						.getColumnIndex(SYS_PUBLISHER));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(SYS_READSTATUS));
				msgList.add(msg);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return msgList;
	}

	public List<MtqMsgSys> queryUnRead() {
		if (DbManager.mDbHelper == null)
			return null;

		List<MtqMsgSys> msgList = new ArrayList<MtqMsgSys>();
		MtqMsgSys msg = null;
		Cursor cursor = null;
		String where = null;
		String orderBy = null;
		orderBy = ID + " DESC";
		where = SYS_READSTATUS + "=" + 0;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db
				.query(MSG_SYS_TABLE, null, where, null, null, null, orderBy);
		if (cursor != null) {
			cursor.setNotificationUri(DbManager.mContext.getContentResolver(),
					CONTENT_SORT_URI);
			int len = cursor.getCount();
			for (int i = 0; i < len; i++) {
				cursor.moveToPosition(i);
				msg = new MtqMsgSys();
				msg.serialid = cursor.getInt(cursor
						.getColumnIndex(SYS_SERIALID));
				msg.time = cursor.getInt(cursor.getColumnIndex(SYS_TIME));
				msg.title = cursor.getString(cursor.getColumnIndex(SYS_TITLE));
				msg.content = cursor.getString(cursor
						.getColumnIndex(SYS_CONTENT));
				msg.publisher = cursor.getString(cursor
						.getColumnIndex(SYS_PUBLISHER));
				msg.readstatus = cursor.getInt(cursor
						.getColumnIndex(SYS_READSTATUS));
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
		where = SYS_READSTATUS + "=" + 0;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		cursor = db
				.query(MSG_SYS_TABLE, null, where, null, null, null, orderBy);
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

	public void delete(int serialid) {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		where = SYS_SERIALID + "=" + serialid;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(MSG_SYS_TABLE, where, null);
	}

	public void delete() {
		if (DbManager.mDbHelper == null)
			return;

		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DbManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(MSG_SYS_TABLE, where, null);
	}
}
