/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: CallUtils.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.utils
 * @Description: 拨打电话工具
 * @author: zhaoqy
 * @date: 2017年6月7日 下午5:58:41
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;

public class CallUtils {

	/**
	 * @Title: makeCall
	 * @Description: 响应电话号码的点击
	 * @param context
	 * @param mobile
	 * @return: void
	 */
	public static void makeCall(final Activity context, final String mobile) {
		String title = context.getResources().getString(R.string.dialog_call);
		String cancel = context.getResources()
				.getString(R.string.dialog_cancel);
		String sure = context.getResources().getString(R.string.dialog_sure);
		PromptDialog dialog = new PromptDialog(context, title, mobile, cancel,
				sure, new IPromptListener() {

					@Override
					public void OnCancel() {

					}

					@Override
					public void OnSure() {
						checkPermission(context, mobile);
					}
				});
		dialog.show();
	}

	/**
	 * @Title: checkPermission
	 * @Description: 检查是否有权限
	 * @param context
	 * @param mobile
	 * @return: void
	 */
	public static void checkPermission(Activity context, String mobile) {
		boolean result = PermissionUtils.isNeedPermission(context,
				Manifest.permission.CALL_PHONE, PermissionUtils.CALL_PHONE);
		Log.e("zhaoqy", " result: " + result);
		if (!result) {
			call(context, mobile);
		}
	}

	/**
	 * @Title: call
	 * @Description: 拨打电话
	 * @param context
	 * @param mobile
	 * @return: void
	 */
	public static void call(Activity context, String mobile) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		Uri data = Uri.parse("tel:" + mobile);
		intent.setData(data);
		context.startActivity(intent);
	}
}
