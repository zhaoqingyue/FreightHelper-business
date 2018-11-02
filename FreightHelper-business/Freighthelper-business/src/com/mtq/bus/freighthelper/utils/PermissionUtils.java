/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: PermissionUtils.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.utils
 * @Description: 权限管理工具
 * @author: zhaoqy
 * @date: 2017年6月7日 下午6:12:16
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

public class PermissionUtils {

	public static final int CALL_PHONE = 0x11;
	public static final int CAMERA = 0x12;

	/**
	 * 
	 * @Title: isNeedPermission
	 * @Description: 是否需要权限
	 * @param context
	 * @param permission
	 * @param requestCode
	 *            用于activity里面onRequestPermissionsResult 监听权限请求结果
	 * @return: boolean
	 */
	public static boolean isNeedPermission(Activity context, String permission,
			int requestCode) {
		if (!isGranted(context, permission)) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(context,
					permission)) {
			} else {
				ActivityCompat.requestPermissions(context,
						new String[] { permission }, requestCode);
			}
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("NewApi")
	public static boolean isGranted(Activity context, String permission) {
		boolean result = true;
		int targetSdkVersion = 23;
		try {
			final PackageInfo info = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (targetSdkVersion >= Build.VERSION_CODES.M) {
				// targetSdkVersion >= Android M, we can
				// use Context#checkSelfPermission
				result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
			} else {
				// targetSdkVersion < Android M, we have to use
				// PermissionChecker
				result = PermissionChecker.checkSelfPermission(context,
						permission) == PermissionChecker.PERMISSION_GRANTED;
			}
		}
		return result;
	}

	/**
	 * 是否需要弹出权限询问框 For 储存空间权限
	 */
	@SuppressLint("InlinedApi") 
	public static boolean isNeedPermissionForStorage(Activity context) {
		if (!isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				|| !isGranted(context,
						Manifest.permission.READ_EXTERNAL_STORAGE)) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(context,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			} else {
				ActivityCompat.requestPermissions(context, new String[] {
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE }, 99);
			}
			return true;
		} else {
			/**
			 * 直接执行相应操作
			 */
			return false;
		}
	}
	/**
	 * 录音播放音频的权限
	 */
	@SuppressLint("InlinedApi") 
	public static boolean isNeedPermissionForAudioRecoder(Activity context) {
		if (!isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				&& !isGranted(context,
						Manifest.permission.RECORD_AUDIO)) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(context,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
					ActivityCompat.shouldShowRequestPermissionRationale(context,
							Manifest.permission.RECORD_AUDIO) ){
			} else {
				ActivityCompat.requestPermissions(context, new String[] {
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.RECORD_AUDIO }, 99);
			}
			return true;
		} else {
			/**
			 * 直接执行相应操作
			 */
			return false;
		}
	}
}
