/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AppVersionManager.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.manager
 * @Description: 版本自升级管理
 * @author: zhaoqy
 * @date: 2017年6月19日 下午11:28:40
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.manager;

import com.mtq.bus.freighthelper.api.center.AppCenterAPI;
import com.mtq.ols.api.CldKAppCenterAPI.IUpgradeListener;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfoNew;

public class AppVersionManager {

	private static AppVersionManager mManager = null;

	public static AppVersionManager getInstance() {
		if (mManager == null) {
			synchronized (AppVersionManager.class) {
				if (mManager == null) {
					mManager = new AppVersionManager();
				}
			}
		}
		return mManager;
	}

	public AppVersionManager() {
	}

	public void init() {

	}

	public void checkVersion(final IAppVersionListener listener) {
		AppCenterAPI.getInstance().getUpgrade(new IUpgradeListener() {

			@Override
			public void onResult(int errCode, MtqAppInfoNew result) {
				if (listener != null) {
					listener.onResult(errCode, result);
				}
			}
		});
	}

	public static interface IAppVersionListener {

		/**
		 * 结果回调
		 * 
		 * @param errCode
		 * @param jsonString
		 */
		public void onResult(int errCode, MtqAppInfoNew result);
	}
}
