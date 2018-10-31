/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: TimeUtils.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.utils
 * @Description: 时间工具类
 * @author: zhaoqy
 * @date: 2017年6月21日 下午3:52:43
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;

public class TimeUtils {

	public static final int MAX_DAY_IN_MONTH = 31;
	public static final int DAY_IN_MONTH = 30;
	public static final int SECONDS_IN_DAY = 60 * 60 * 24;
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	/**
	 * @Title: getStarttime
	 * @Description: 获取开始时间
	 * @param day
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStarttimeForYmd(int day) {
		String res = "";
		long timeStamp = System.currentTimeMillis() - TimeUtils.MILLIS_IN_DAY
				* day;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timeStamp);
		res = dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: getEndtime
	 * @Description: 获取结束时间
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getEndtimeForYmd() {
		String res = "";
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timeStamp);
		res = dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: getStartTimeForStamp
	 * @Description: 获取开始时间时间戳
	 * @param day
	 * @return: long
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getStartTimeForStamp(int day) {
		long timeStamp = System.currentTimeMillis() - TimeUtils.MILLIS_IN_DAY
				* day;
		return timeStamp / 1000;
	}

	/**
	 * @Title: getEndtime
	 * @Description: 获取结束时间
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getEndTimeForStamp() {
		long timeStamp = System.currentTimeMillis();
		return timeStamp / 1000;
	}

	/**
	 * @Title: getCurTimeForYmd
	 * @Description: 获取yyyy-MM-dd格式的当前时间
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurTimeForYmd() {
		String res = "";
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timeStamp);
		res = dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: getCurTimeForYmdHm
	 * @Description: 获取yyyy-MM-dd HH:mm格式的当前时间
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurTimeForYmdHm() {
		String res = "";
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(timeStamp);
		res = dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: getStartTimeForYmdHm
	 * @Description: 获取yyyy-MM-dd HH:mm格式的开始时间
	 * @param day
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStartTimeForYmdHm(int day) {
		String res = "";
		long timeStamp = System.currentTimeMillis() - TimeUtils.MILLIS_IN_DAY
				* day;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(timeStamp);
		res = dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: stampToYmd
	 * @Description: 将时间戳转换为yyyy-MM-dd格式的时间
	 * @param stamp
	 * @return: String
	 */
	@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
	public static String stampToYmd(long stamp) {
		String res = "";
		String ftime = "";
		SimpleDateFormat dateFormat = null;
		long timeStamp = new Long(stamp * 1000);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date(timeStamp);
		res = ftime + dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: stampToYmdHm
	 * @Description: 将时间戳转换为yyyy-MM-dd HH:mm格式的时间
	 * @param stamp
	 * @return: String
	 */
	@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
	public static String stampToYmdHm(long stamp) {
		String res = "";
		String ftime = "";
		SimpleDateFormat dateFormat = null;
		long timeStamp = new Long(stamp * 1000);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date date = new Date(timeStamp);
		res = ftime + dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: stampToYmdHms
	 * @Description: 将时间戳转换为yyyy-MM-dd HH:mm:ss格式的时间
	 * @param stamp
	 * @return: String
	 */
	@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
	public static String stampToYmdHms(long stamp) {
		String res = "";
		String ftime = "";
		SimpleDateFormat dateFormat = null;
		long timeStamp = new Long(stamp * 1000);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = new Date(timeStamp);
		res = ftime + dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: stampToHm
	 * @Description: 将时间戳转换为HH:mm格式的时间
	 * @param stamp
	 * @return: String
	 */
	@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
	public static String stampToHm(long stamp) {
		String res = "";
		String ftime = "";
		SimpleDateFormat dateFormat = null;
		long timeStamp = new Long(stamp * 1000);
		dateFormat = new SimpleDateFormat("HH:mm");

		Date date = new Date(timeStamp);
		res = ftime + dateFormat.format(date);
		return res;
	}

	/**
	 * @Title: getStampFromYdh
	 * @Description: 将 yyyy-MM-dd格式的时间转化成时间戳
	 * @param formattime
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getStampFromYdh(String formattime) {
		long stamp = 0;
		if (!TextUtils.isEmpty(formattime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = format.parse(formattime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date != null) {
				stamp = date.getTime() / 1000;
			}
		}
		return stamp;
	}

	/**
	 * @Title: getStampFromYdhHm
	 * @Description: 将 yyyy-MM-dd HH:mm格式的时间转化成时间戳
	 * @param formattime
	 * @return: String
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getStampFromYdhHm(String formattime) {
		long stamp = 0;
		if (!TextUtils.isEmpty(formattime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = null;
			try {
				date = format.parse(formattime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date != null) {
				stamp = date.getTime() / 1000;
			}
		}
		return stamp;
	}
	
	/**
	 * 判断时间是不是今天
	 */
	@SuppressLint("SimpleDateFormat") 
	public static boolean isToday(String formattime) {
		if (!TextUtils.isEmpty(formattime)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			/**
			 * 当前日期
			 */
			Date curDate = new Date();
			String curDay = sf.format(curDate);

			Date formatDate = null;
			try {
				formatDate = sf.parse(formattime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String formatDay = sf.format(formatDate);
			return curDay.equals(formatDay);
		}
		return false;
	}
	
	public static boolean isOnLine(long stamp) {
		long curStamp = System.currentTimeMillis() / 1000;
		long diff = curStamp - stamp;
		/**
		 * 与系统时间相差在5分钟范围内为在线，否则为离线
		 */
		if (diff <= 5 * 60) {
			return true;
		}
		return false;
	}

	public static String long2String(long time) {

		// 毫秒转秒
		int sec = (int) time / 1000;

		sec = sec % 60; // 秒
		if (sec < 10) { // 秒补0
			return "0" + sec;
		} else {
			return sec + "";
		}
	}

}
