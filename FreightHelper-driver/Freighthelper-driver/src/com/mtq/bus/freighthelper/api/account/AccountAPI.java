/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AccountAPI.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.api.account
 * @Description: 账号相关API
 * @author: zhaoqy
 * @date: 2017年6月19日 下午11:24:56
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.api.account;

import com.mtq.ols.api.CldKAccountAPI;
import com.mtq.ols.api.CldKConfigAPI;

public class AccountAPI {

	private static AccountAPI instance = null;

	public static AccountAPI getInstance() {
		if (instance == null) {
			synchronized (AccountAPI.class) {
				if (instance == null) {
					instance = new AccountAPI();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取当前设备的duid
	 */
	public long getDuid() {
		return CldKAccountAPI.getInstance().getDuid();
	}

	/**
	 * 是否是手机号
	 * 
	 * @param phone
	 *            传入手机号
	 */
	public boolean isPhoneNum(String phone) {
		return CldKConfigAPI.getInstance().isPhoneNum(phone);
	}

}
