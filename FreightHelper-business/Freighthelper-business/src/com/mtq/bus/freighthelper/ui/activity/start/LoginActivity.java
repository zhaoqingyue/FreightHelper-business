/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: LoginActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.start
 * @Description: 登录界面
 * @author: zhaoqy
 * @date: 2017年6月16日 上午10:19:40
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.LoginEvent;
import com.mtq.bus.freighthelper.ui.activity.MainActivity;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.UserUtils;
import com.mtq.bus.freighthelper.utils.UserUtils.InputError;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "LoginActivity";
	private EditText mUserEdit;
	private EditText mPwdEdit;
	private TextView mUserCancel;
	private TextView mPwdCancel;
	private Button mLogin;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_login;
	}

	@Override
	protected void initViews() {
		mUserEdit = (EditText) findViewById(R.id.login_edit_username);
		mPwdEdit = (EditText) findViewById(R.id.login_edit_pwd);
		mUserCancel = (TextView) findViewById(R.id.login_edit_username_cancel);
		mPwdCancel = (TextView) findViewById(R.id.login_edit_pwd_cancel);
		mLogin = (Button) findViewById(R.id.login_btn_login);
	}

	@Override
	protected void setListener() {
		mLogin.setOnClickListener(this);
		mUserCancel.setOnClickListener(this);
		mPwdCancel.setOnClickListener(this);
		findViewById(R.id.login_lost_pwd).setOnClickListener(this);
		setTextChangedListener();
	}

	@Override
	protected void initData() {
		String username = DeliveryBusAPI.getInstance().getUserName();
		String password = DeliveryBusAPI.getInstance().getPassword();
		if (!TextUtils.isEmpty(username)) {
			mUserEdit.setText(username);
			mUserEdit.setSelection(username.length());
		}
		
		/**
		 * 调试用
		 */
		boolean debug = false;
		if (debug) {
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
				mUserEdit.setText(username);
				mPwdEdit.setText(password);
				mUserEdit.setSelection(username.length());
			} else {
				username = "zhangxuan";
				password = "123456";
				mUserEdit.setText(username);
				mPwdEdit.setText(password);
				mUserEdit.setSelection(username.length());
			}
		}
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	protected void updateUI() {

	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn_login: {
			login();
			break;
		}
		case R.id.login_edit_username_cancel: {
			mUserEdit.setText("");
			mUserEdit.setSelection(0);
			break;
		}
		case R.id.login_edit_pwd_cancel: {
			mPwdEdit.setText("");
			mPwdEdit.setSelection(0);
			break;
		}
		case R.id.login_lost_pwd: {
			lostPwd();
			break;
		}
		default:
			break;
		}
	}

	@SuppressLint("DefaultLocale") 
	private void login() {
		final String username = mUserEdit.getText().toString().toLowerCase();
		final String pwd = mPwdEdit.getText().toString();
		CldLog.i(TAG, "username: " + username + ", pwd: " + pwd);
		InputError errorCode = UserUtils.checkInputIsValid(username, pwd);
		switch (errorCode) {
		case eERROR_ACCOUNT_EMPTY: {
			Toast.makeText(this, R.string.account_account_empty,
					Toast.LENGTH_SHORT).show();
			break;
		}
		case eERROR_PASSWORD_EMPTY: {
			Toast.makeText(this, R.string.account_pasword_empty,
					Toast.LENGTH_SHORT).show();
			break;
		}
		case eERROR_ACCOUNT_INPUT: {
			Toast.makeText(this, R.string.account_account_error,
					Toast.LENGTH_SHORT).show();
			break;
		}
		case eERROR_PASSWORD_INPUT: {
			Toast.makeText(this, R.string.account_password_error,
					Toast.LENGTH_SHORT).show();
			break;
		}
		case eERROR_EMAIL_INPUT: {
			Toast.makeText(this, R.string.account_email_error,
					Toast.LENGTH_SHORT).show();
			break;
		}
		case eERROR_NONE: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(this, R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				String str = getResources().getString(
						R.string.tip_logining);
				ProgressDialog.showProgress(this, str, null);
				
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						/**
						 * 账号登录
						 */
						DeliveryBusAPI.getInstance().login(username, pwd);
					}
				}, 500);
			}
			break;
		}
		default:
			break;
		}
	}

	private void lostPwd() {
		String title = getResources().getString(
				R.string.dialog_contact_administrator);
		String sure = getResources().getString(R.string.dialog_know);
		PromptDialog dialog = new PromptDialog(this, title, "", "", sure,
				new IPromptListener() {

					@Override
					public void OnCancel() {

					}

					@Override
					public void OnSure() {
					}
				});
		dialog.show();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(LoginEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_LOGIN_SUCCESS: {
			if (ProgressDialog.isShowProgress()) {
				ProgressDialog.cancelProgress();
			}
			
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case MsgId.MSGID_LOGIN_FAILED: {
			if (ProgressDialog.isShowProgress()) {
				ProgressDialog.cancelProgress();
			}
			
			String errmsg = "";
			CldLog.i(TAG, "errCode: " + event.errCode);
			switch (event.errCode) {
			case 1001: {
				// 必填项缺少
				errmsg = "必填项缺少";
				break;
			}
			case 1002: {
				// 参数内容格式不合法
				errmsg = "参数内容格式不合法";
				break;
			}
			case 1003: {
				// 用户未登录或登录失败
				errmsg = "用户未登录或登录失败";
				break;
			}
			case 10004: {
				// 解析异常
				errmsg = "解析异常";
				break;
			}
			case 1006: {
				// 登录失败次数过多
				errmsg = "登录失败次数过多";
				break;
			}
			case 1007: {
				// 企业帐号到期
				errmsg = "企业帐号到期";
				break;
			}
			case 1800: {
				// 其它设备登录
				errmsg = "其它设备登录";
				break;
			}
			case 1900: {
				// 系统升级维护
				errmsg = "系统升级维护";
				break;
			}
			case 2000: {
				// 频繁重复提交
				errmsg = "频繁重复提交";
				break;
			}
			default:
				break;
			}
			Toast.makeText(this, event.errMsg, Toast.LENGTH_SHORT).show();
			break;
		}
		default:
			break;
		}
	}

	private void setTextChangedListener() {
		mUserEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String username = mUserEdit.getText().toString();
				String pwd = mPwdEdit.getText().toString();
				if (!TextUtils.isEmpty(username)) {
					mUserCancel.setVisibility(View.VISIBLE);
				} else {
					mUserCancel.setVisibility(View.GONE);
				}

				if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
					mLogin.setEnabled(true);
				} else {
					mLogin.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mPwdEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String username = mUserEdit.getText().toString();
				String pwd = mPwdEdit.getText().toString();
				if (!TextUtils.isEmpty(pwd)) {
					mPwdCancel.setVisibility(View.VISIBLE);
				} else {
					mPwdCancel.setVisibility(View.GONE);
				}
				
				if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
					mLogin.setEnabled(true);
				} else {
					mLogin.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
