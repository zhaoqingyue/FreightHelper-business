/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: UserInfoActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me
 * @Description:我的信息
 * @author: zhaoqy
 * @date: 2017年6月13日 下午4:54:48
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.ActivityManager;
import com.mtq.bus.freighthelper.manager.CarStateCountManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.activity.start.LoginActivity;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqLogin;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class UserInfoActivity extends BaseActivity implements OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private VerticalCard mUserName;
	private VerticalCard mOrgName;
	private VerticalCard mName;
	private MtqLogin mMtqLogin;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_userinfo;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mUserName = (VerticalCard) findViewById(R.id.userinfo_username);
		mOrgName = (VerticalCard) findViewById(R.id.userinfo_org_name);
		mName = (VerticalCard) findViewById(R.id.userinfo_name);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		findViewById(R.id.userinfo_pwd).setOnClickListener(this);
		findViewById(R.id.userinfo_logout).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.me_userinfo);
		mBack.setVisibility(View.VISIBLE);
	}

	@Override
	protected void loadData() {
		mMtqLogin = DeliveryBusAPI.getInstance().getMtqLogin();
	}

	@Override
	protected void updateUI() {
		/**
		 * 设置个人信息
		 */
		if (mMtqLogin != null) {
			mUserName.setContent(mMtqLogin.user_name);
			mOrgName.setContent(mMtqLogin.org_name);
			mName.setContent(mMtqLogin.user_alias);
		}
	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.userinfo_pwd: {
			lostPwd();
			break;
		}
		case R.id.userinfo_logout: {
			logout();
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
		PromptDialog dialog = new PromptDialog(this, title, "", "", sure, null);
		dialog.show();
	}

	private void logout() {
		String title = getResources().getString(R.string.dialog_exit);
		String cancel = getResources().getString(R.string.dialog_cancel);
		String sure = getResources().getString(R.string.dialog_sure);
		PromptDialog dialog = new PromptDialog(this, title, "", cancel, sure,
				new IPromptListener() {

					@Override
					public void OnCancel() {

					}

					@Override
					public void OnSure() {
						/**
						 * 退出登录
						 */
						DeliveryBusAPI.getInstance().logout();
						DeliveryBusAPI.getInstance().uninit();
						CarStateCountManager.getInstance().unInit();

						/**
						 * 退出之后，跳转至登录界面
						 */
						ActivityManager.getInstance().finishAllActivity();
						Intent intent = new Intent(UserInfoActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				});
		dialog.show();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_LOGOUT_SUCCESS: {
			break;
		}
		case MsgId.MSGID_LOGOUT_FAILED: {
			break;
		}
		default:
			break;
		}
	}
}
