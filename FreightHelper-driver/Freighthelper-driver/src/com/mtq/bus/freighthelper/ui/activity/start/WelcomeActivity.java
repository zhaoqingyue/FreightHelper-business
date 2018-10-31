/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: WelcomeActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.start
 * @Description: 启动页
 * @author: zhaoqy
 * @date: 2017年6月1日 下午8:23:12
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.start;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.mapapi.map.CldMap.NaviInitListener;
import com.cld.navisdk.CldNaviAuthManager;
import com.cld.navisdk.CldNaviAuthManager.CldAuthManagerListener;
import com.cld.navisdk.utils.CldNaviSdkUtils;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.api.map.NavigateAPI;
import com.mtq.bus.freighthelper.bean.eventbus.LoginEvent;
import com.mtq.bus.freighthelper.ui.activity.MainActivity;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.bus.freighthelper.utils.SPUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class WelcomeActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 是否是首次启动app
		 */
		boolean isFirstOpen = SPUtils
				.getBoolean(ConstantUtils.FIRST_OPEN, true);
		if (isFirstOpen) {
			SPUtils.put(ConstantUtils.FIRST_OPEN, false);
			/**
			 * 申请存储、定位、电话权限
			 */
			if (!PermissionUtils.isGranted(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)
					|| !PermissionUtils.isGranted(this,
							Manifest.permission.ACCESS_FINE_LOCATION)
					|| !PermissionUtils.isGranted(this,
							Manifest.permission.READ_PHONE_STATE)) {
				List<String> reqlist = new ArrayList<String>();
				if (!PermissionUtils.isGranted(this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					reqlist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
				}

				if (!PermissionUtils.isGranted(this,
						Manifest.permission.ACCESS_FINE_LOCATION))
					reqlist.add(Manifest.permission.ACCESS_FINE_LOCATION);

				if (!PermissionUtils.isGranted(this,
						Manifest.permission.READ_PHONE_STATE))
					reqlist.add(Manifest.permission.READ_PHONE_STATE);

				String[] reqarray = new String[reqlist.size()];
				for (int i = 0; i < reqlist.size(); i++) {
					reqarray[i] = (String) reqlist.get(i);
				}

				ActivityCompat.requestPermissions(this, reqarray, 119);
				Toast.makeText(WelcomeActivity.this, "请授予权限以便更好地使用地图功能",
						Toast.LENGTH_SHORT).show();
			} else {
				/**
				 * 初始化导航sdk(有存储权限，才会初始化成功)
				 */
				initSDK();
				
				Intent intent = new Intent(this, GuideActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			/**
			 * 初始化导航sdk(有存储权限，才会初始化成功)
			 */
			initSDK();
			jump();
		}
	}

	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		if (requestCode == 119) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& permissions[0]
							.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				Log.e("授予权限", " " + grantResults[0]);
			}
			
			/**
			 * 初始化导航sdk(有存储权限，才会初始化成功)
			 */
			initSDK();
			
			/**
			 * 不管是否接受还是拒绝，都往下走
			 */
			Intent intent = new Intent(this, GuideActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void jump() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				onJump();
			}
		}, 2000);
	}

	protected void onJump() {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		/**
		 * 是否登录
		 */
		boolean isLogin = DeliveryBusAPI.getInstance().getLoginStatus();
		if (isLogin) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		} else if (!DeliveryBusAPI.getInstance().getUserName().isEmpty()
				&& !DeliveryBusAPI.getInstance().getPassword().isEmpty()) {
			/**
			 * 自动登录
			 */
			DeliveryBusAPI.getInstance().autoLogin();
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(LoginEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_LOGIN_SUCCESS: {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case MsgId.MSGID_LOGIN_FAILED: {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
	}

	private void initSDK() {
		NavigateAPI.getInstance().init(this, new NaviInitListener() {

			@Override
			public void onAuthResult(int status, String arg1) {
				// 初始化结果状态判断
				String str;
				if (0 == status) {
					str = "key校验成功!";
				} else {
					str = "key校验失败!";
				}

				if (status != 0) {
					authenticate();
				}
				//Toast.makeText(WelcomeActivity.this, str, Toast.LENGTH_SHORT).show();
				Log.e("check", str + " " + arg1);
			}

			@Override
			public void initSuccess() {
				Log.e("check", "initsuccess");
			}

			@Override
			public void initStart() {
				Log.e("check", "initstart");
			}

			@Override
			public void initFailed(String arg0) {
				Log.e("check", arg0);
			}
		});
	}

	static int mEnticateCnt = 0;
	static boolean mIsAuthStatus = false;
	static boolean mIsAuthing = false; // 是否鉴权中

	private void authenticate() {
		mEnticateCnt = 0;
		mIsAuthStatus = false;

		Runnable r = new Runnable() {

			@Override
			public void run() {

				while (mEnticateCnt < 3 && !mIsAuthStatus) {
					// 鉴权不成功且鉴权次数少于3次
					if (mIsAuthing) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}

					if (mEnticateCnt < 3 && !mIsAuthStatus) {
						mIsAuthing = true;
						CldNaviAuthManager.getInstance().authenticate(
								new CldAuthManagerListener() {

									@Override
									public void onAuthResult(int i, String s) {
										mIsAuthing = false;
										mEnticateCnt++;
										if (i == 0) {
											mIsAuthStatus = true;
										}
									}
								},
								CldNaviSdkUtils
										.getAuthValue(getApplicationContext()));
					} else {
						break;
					}
				}
			}
		};

		new Thread(r).start();
	}
}
