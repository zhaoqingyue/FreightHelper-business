/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: HandleErrManager.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.manager
 * @Description: 错误码处理
 * @author: zhaoqy
 * @date: 2017年8月2日 上午10:34:31
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.application.BFContext;
import com.mtq.bus.freighthelper.ui.activity.start.LoginActivity;
import com.mtq.bus.freighthelper.utils.AppInfo;

public class HandleErrManager {

	private static HandleErrManager mManager = null;

	public static HandleErrManager getInstance() {
		if (mManager == null) {
			synchronized (HandleErrManager.class) {
				if (mManager == null) {
					mManager = new HandleErrManager();
				}
			}
		}
		return mManager;
	}
	
	public void handleErrCode(int errcode) {
		switch (errcode) {
		case 1001: {
			/**
			 * 必填项缺少类
			 */
			break;
		}
		case 1002: {
			/**
			 * 参数内容格式不合法类
			 */
			break;
		}
		case 1003: {
			/**
			 * 用户未登录或登录失败类
			 * （用户未登录类包含登录态记录不存在、登录已过期等；登录失败类包含用户名不存在、密码错误等）
			 */
			break;
		}
		case 1004: {
			/**
			 * 无权限访问类
			 */
			break;
		}
		case 1005: {
			/**
			 * Sign签名失败类
			 */
			break;
		}
		case 1006: {
			/**
			 * 登录失败次数过多类
			 */
			break;
		}	
		case 1007: {
			/**
			 * 企业帐号到期类
			 *（包含登录失败次数提醒、帐号冻结等）
			 */
			break;
		}	
		case 1008: {
			/**
			 * 其它设备登录类
			 */
			if (mHandler != null) {
				mHandler.sendEmptyMessage(1008);
			}
			break;
		}	
		case 1009: {
			/**
			 * 已存在类
			 *（包含司机已被邀请加入车队）
			 */
			break;
		}	
		case 1900: {
			/**
			 * 系统错误类
			 *（包含入库失败、接口异常等）
			 */
			break;
		}	
		case 2000: {
			/**
			 * 频繁重复提交类
			 *（包含重复记录入库、相同用户接口请求过于频繁等）
			 */
			break;
		}
		default:
			break;
		}
	}
	
	public static Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1008: {
				DeliveryBusAPI.getInstance().logout();
				DeliveryBusAPI.getInstance().uninit();
				CarStateCountManager.getInstance().unInit();
			
				Context context = BFContext.getContext();
				String str = context.getResources().getString(
						R.string.account_invalid);
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				if (AppInfo.isForeground(context)) {
					Intent intent = new Intent(context, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
				ActivityManager.getInstance().finishAllActivity();
				break;
			}
			default:
				break;
			}
		}
	};
}
