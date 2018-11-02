/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: ActivityManager.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.manager
 * @Description: Activity管理
 * @author: zhaoqy
 * @date: 2017年6月1日 上午11:23:26
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.manager;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;

public class ActivityManager {
	
	private static Stack<Activity> activityStack;
	private static ActivityManager instance;

	private ActivityManager() {
	}

	/**
	 * 单一实例
	 */
	public static ActivityManager getInstance() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity getCurActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		int len = activityStack.size();
		for (int i = 0; i < len; i++) {
			Activity activity = activityStack.get(i);
			if (activity != null) {
				activity.finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void exitApp(Context context) {
		try {
			finishAllActivity();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {
		}
	}
}
