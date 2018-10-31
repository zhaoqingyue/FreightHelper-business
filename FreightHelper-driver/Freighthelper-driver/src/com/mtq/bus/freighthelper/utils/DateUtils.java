/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DateUtils.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.utils
 * @Description: 日期工具类
 * @author: zhaoqy
 * @date: 2017年6月17日 下午4:53:36
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	/**
	 * @Title: getYear
	 * @Description: 返回当前年份
	 * @return: int
	 */
	public static int getCurYear() {
		Date date = new Date();
		String year = new SimpleDateFormat("yyyy").format(date);
		return Integer.parseInt(year);
	}

	/**
	 * @Title: getMonth
	 * @Description: 返回当前月份
	 * @return: int
	 */
	public static int getCurMonth() {
		Date date = new Date();
		String month = new SimpleDateFormat("MM").format(date);
		return Integer.parseInt(month);
	}
	
	/**
	 * @Title: getCurMonthDay
	 * @Description: 当前月的当天
	 * @return: int
	 */
	public static int getCurMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @Title: isLeap
	 * @Description: 判断闰年
	 * @param year
	 * @return: boolean
	 */
	public static boolean isLeap(int year) {
		if (((year % 100 == 0) && year % 400 == 0)
				|| ((year % 100 != 0) && year % 4 == 0)) 
			return true;
		else
			return false;
	}

	/**
	 * @Title: getDays
	 * @Description: 返回当月天数
	 * @param year
	 * @param month
	 * @return: int
	 */
	public static int getDays(int year, int month) {
		int days;
		int FebDay = 28;
		if (isLeap(year))
			FebDay = 29;
		
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			days = FebDay;
			break;
		default:
			days = 0;
			break;
		}
		return days;
	}
	
	public static int START_YEAR = 2015;

	public static List<String> getYaerList() {
		List<String> list = new ArrayList<String>();
		int curyear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = START_YEAR; i <= curyear; i++) {
			list.add(i + "年");
		}
		return list;
	}

	public static List<String> getMounthList() {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			list.add(i + "月");
		}
		return list;
	}

	public static List<String> getDayList(int year, int month) {
		List<String> list = new ArrayList<String>();
		int days = getDays(year, month);
		for (int i = 1; i <= days; i++) {
			list.add(i + "日");
		}
		return list;
	}
	
	public static List<String> getHourList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				list.add("0" + i + ":00");
			} else {
				list.add(i + ":00");
			}
		}
		return list;
	}
}
