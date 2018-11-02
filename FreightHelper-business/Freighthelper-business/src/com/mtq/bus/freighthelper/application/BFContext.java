/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BFContext.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.application
 * @Description: 全局环境变量
 * @author: zhaoqy
 * @date: 2017年6月1日 上午11:12:10
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.application;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class BFContext {

	private static Context mContext = null;
	private static Application mApplication = null;

	/**
	 * @Title: getContext
	 * @Description: 获取Context
	 * @return: Context
	 */
	public static Context getContext() {
		return mContext;
	}

	/**
	 * @Title: setContext
	 * @Description: 设置Context
	 * @param ctx
	 * @return: void
	 */
	public static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * @Title: getApplication
	 * @Description: 获取Application
	 * @return: Application
	 */
	public static Application getApplication() {
		return mApplication;
	}

	/**
	 * @Title: setApplication
	 * @Description: 设置Application
	 * @param application
	 * @return: void
	 */
	public static void setApplication(Application application) {
		mApplication = application;
	}

	// app path
	private static String mAppPath = "";

	// app directory
	private static final String DEFAULT_DIR = "pndpersonalcenter";

	/**
	 * 获取程序路径
	 * @return String
	 */
	@SuppressLint("SdCardPath")
	public static String getAppPath() {
		if (!TextUtils.isEmpty(mAppPath)) {
			return mAppPath;
		}

		File extFile = Environment.getExternalStorageDirectory();
		if ((extFile.exists()) && (extFile.isDirectory())
				&& (extFile.canWrite())) {
			// /storage/emulated/0/pndpersonalcenter
			mAppPath = extFile.getAbsolutePath() + "/" + DEFAULT_DIR;
		} else {
			
			// /mnt/sdcard/pndpersonalcenter
			mAppPath = "/mnt/sdcard/" + DEFAULT_DIR;
		}
		return mAppPath;
	}
}
