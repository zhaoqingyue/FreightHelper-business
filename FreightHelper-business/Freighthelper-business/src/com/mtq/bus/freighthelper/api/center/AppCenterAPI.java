/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AppCenterAPI.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.api.center
 * @Description: kgo相关API
 * @author: zhaoqy
 * @date: 2017年6月19日 下午11:25:16
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.api.center;

import com.mtq.ols.api.CldKAppCenterAPI;
import com.mtq.ols.api.CldKAppCenterAPI.IUpgradeListener;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfoNew;

public class AppCenterAPI {

	private static AppCenterAPI mAppCenterAPI = null;

	public static AppCenterAPI getInstance() {
		if (mAppCenterAPI == null) {
			synchronized (AppCenterAPI.class) {
				if (mAppCenterAPI == null) {
					mAppCenterAPI = new AppCenterAPI();
				}
			}
		}
		return mAppCenterAPI;
	}

	/**
	 * @Title: getMtqAppInfo
	 * @Description: 获取应用版本信息
	 * @return: MtqAppInfo
	 */
	public MtqAppInfoNew getMtqAppInfo() {
		return CldKAppCenterAPI.getInstance().getMtqAppInfo();
	}

	/**
	 * @Title: hasNewVersion
	 * @Description: 是否存在新版本
	 * @return: boolean
	 */
	public boolean hasNewVersion() {
		return CldKAppCenterAPI.getInstance().hasNewVersion();
	}

	/**
	 * @Title: clearAppVersion
	 * @Description: 清除版本信息
	 * @return: void
	 */
	public void clearAppVersion() {
		CldKAppCenterAPI.getInstance().clearAppVersion();
	}

	public void getUpgrade(IUpgradeListener listener) {
		CldKAppCenterAPI.getInstance().getUpgrade(listener);
	}
}
