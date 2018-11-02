/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BFApplication.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.application
 * @Description: Application
 * @author: zhaoqy
 * @date: 2017年6月1日 上午11:12:39
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.application;

import android.app.Application;
import android.content.Context;

import com.cld.base.CldBase;
import com.cld.base.CldBaseParam;
import com.cld.log.CldLog;
import com.cld.nv.frame.AppProperty;
import com.cld.nv.frame.CldNvBaseManager;
import com.mtq.bus.freighthelper.db.MsgAlarmTable;
import com.mtq.bus.freighthelper.db.MsgSysTable;
import com.mtq.bus.freighthelper.utils.Config;
import com.mtq.bus.freighthelper.utils.FileUtils;
import com.mtq.bus.freighthelper.utils.SPUtils;
import com.mtq.ols.api.CldKAccountAPI;
import com.mtq.ols.api.CldOlsBase;
import com.mtq.ols.api.CldOlsBase.CldOlsParam;
import com.mtq.ols.api.CldOlsBase.IInitListener;
import com.tencent.bugly.crashreport.CrashReport;

public class BFApplication extends Application {

	public static Context mContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		BFContext.setApplication(this);
		BFContext.setContext(getApplicationContext());
		SPUtils.init(this);

		/**
		 * 初始化ols库
		 */
		initOlsLib(new IInitListener() {

			@Override
			public void onUpdateConfig() {
				CldLog.e("ols", "ConfigUpdated!");
			}

			@Override
			public void onInitDuid() {
				long duid = CldKAccountAPI.getInstance().getDuid();
				CldLog.e("ols", "duid:" + duid);
			}
		});

		/**
		 * 初始化文件路径
		 */
		FileUtils.initFile();
		/**
		 * 删除缓存消息
		 */
		MsgAlarmTable.getInstance().delete();
		MsgSysTable.getInstance().delete();

		/**
		 * 初始化Bugly
		 */
		CrashReport.initCrashReport(mContext, "4ffe1923ac", true);
	}

	public static void initOlsLib(IInitListener listener) {
		// 初始化cldbase
		CldBaseParam param = new CldBaseParam();
		param.ctx = mContext;
		CldBase.init(param);

		CldLog.setLogCat(true);
		CldOlsParam initParam = new CldOlsParam();
		initParam.appver = "M3478-L7032-3723J0Q";
		initParam.isTestVersion = false;
		initParam.isDefInit = false;

		initParam.apptype = Config.apptype;
		initParam.appid = Config.appid;
		initParam.bussinessid = Config.bussinessid;

		initParam.cid = 1060;
		initParam.mapver = "37200B13J0Q010A1";
		CldOlsBase.getInstance().init(initParam, listener);
		AppProperty appProperty = new AppProperty();
		appProperty.appId = initParam.appid;
		appProperty.appType = initParam.apptype;
		appProperty.busssinessId = initParam.bussinessid;
		CldNvBaseManager.getInstance().setAppProperty(appProperty);
	}
}
