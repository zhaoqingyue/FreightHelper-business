/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MyDriverDetailActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me
 * @Description: 司机详情
 * @author: zhaoqy
 * @date: 2017年6月16日 下午9:41:20
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.DriverUtils;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDriverDataDetailListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqInviteDriverListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriverDetail;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MyDriverDetailActivity extends BaseActivity implements
		OnClickListener, IVCListener {

	private ImageView mBack;
	private TextView mTitle;
	private LinearLayout mFailed;
	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mLayout;
	private VerticalCard mDriverName;
	private VerticalCard mDriverPhone;
	private VerticalCard mInviteStatus;
	private VerticalCard mIdentityNum;
	private VerticalCard mContact0;
	private VerticalCard mContact1;
	private Button mInvite;
	private MtqDriverDetail mDriverDetail;
	private String mCurMobile = "";
	private int mDriverId = 0;
	private int mCurInviteStatus = 0;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_mydriver_detail;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mFailed = (LinearLayout) findViewById(R.id.mydriver_detail_failed);
		mNetAbnormal = (LinearLayout) findViewById(R.id.mydriver_detail_net_abnormal);
		mLoading = (LinearLayout) findViewById(R.id.mydriver_detail_laoding);
		mLayout = (LinearLayout) findViewById(R.id.mydriver_detail_layout);
		mDriverName = (VerticalCard) findViewById(R.id.mydriver_detail_name);
		mDriverPhone = (VerticalCard) findViewById(R.id.mydriver_detail_mobile);
		mInviteStatus = (VerticalCard) findViewById(R.id.mydriver_detail_status);
		mIdentityNum = (VerticalCard) findViewById(R.id.mydriver_detail_identity_num);
		mContact0 = (VerticalCard) findViewById(R.id.mydriver_detail_emergency_contact0);
		mContact1 = (VerticalCard) findViewById(R.id.mydriver_detail_emergency_contact1);
		mInvite = (Button) findViewById(R.id.mydriver_detail_invite);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		findViewById(R.id.failed_refresh).setOnClickListener(this);
		findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		mDriverPhone.setOnVCListener(this);
		mContact0.setOnVCListener(this);
		mContact1.setOnVCListener(this);
		mInvite.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.mydriver_detail);
		mBack.setVisibility(View.VISIBLE);
		mDriverId = getIntent().getIntExtra(ConstantUtils.DRIVER_ID, 0);
	}

	@Override
	protected void loadData() {
		if (CldPhoneNet.isNetConnected()) {
			getDriverDataDetail(mDriverId);
		} else {
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void updateUI() {
		if (mDriverDetail != null) {
			mDriverName.setContent(mDriverDetail.driver_name);
			mDriverPhone.setContent(mDriverDetail.phone);
			int invitestatus = mDriverDetail.invitestatus;
			mCurInviteStatus = invitestatus;
			mInviteStatus.setContent(DriverUtils.getInviteStatus(invitestatus));
			int colorid = DriverUtils.getStatusColor(invitestatus);
			int color = getResources().getColor(colorid);
			mInviteStatus.setContentColor(color);

			String id_number = mDriverDetail.id_number;
			if (!TextUtils.isEmpty(id_number)) {
				String temp = id_number;
				temp = id_number.substring(0, 4) + "**********"
						+ id_number.substring(14, 18);
				mIdentityNum.setContent(temp);
			}
			if (!TextUtils.isEmpty(mDriverDetail.emergency_man1)) {
				mContact0.setTitle(mDriverDetail.emergency_man1);
			} else {
				mContact0.setTitle("紧急联系人1");
			}
			mContact0.setContent(mDriverDetail.emergency_phone1);
			if (!TextUtils.isEmpty(mDriverDetail.emergency_man2)) {
				mContact1.setTitle(mDriverDetail.emergency_man2);
			} else {
				mContact1.setTitle("紧急联系人2");
			}
			mContact1.setContent(mDriverDetail.emergency_phone2);
			if (isUninvited(invitestatus)) {
				mInvite.setVisibility(View.VISIBLE);
			} else {
				mInvite.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			if (mLoading != null && mLoading.isShown()) {
				mNetAbnormal.setVisibility(View.VISIBLE);
				mLoading.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			back();
			break;
		}
		case R.id.mydriver_detail_invite: {
			makeAnInvite();
			break;
		}
		case R.id.failed_refresh: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(this, R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				loadData();
			}
			break;
		}
		case R.id.net_abnormal_refresh: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(this, R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				loadData();
			}
			break;
		}
		default:
			break;
		}
	}

	private void makeAnInvite() {
		String titleHint = getResources().getString(R.string.dialog_invite);
		String title = String.format(titleHint, mDriverDetail.driver_name);
		String cancel = getResources().getString(R.string.dialog_cancel);
		String sure = getResources().getString(R.string.dialog_sure);
		PromptDialog dialog = new PromptDialog(this, title, "", cancel, sure,
				new IPromptListener() {

					@Override
					public void OnCancel() {

					}

					@Override
					public void OnSure() {
						startInvite();
					}
				});
		dialog.show();
	}

	protected void startInvite() {
		if (!DeliveryBusAPI.getInstance().hasDriverInvitePermission()) {
			Toast.makeText(this, "当前账号无邀请司机权限", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			if (mDriverId <= 0)
				return;
			
			String str = getResources().getString(R.string.tip_invitting);
			ProgressDialog.showProgress(this, str, null);

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					invite(mDriverId);
				}
			}, 500);
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		back();
	}

	private void back() {
		/**
		 * 返回前，判断邀请状态是否发生变化
		 */
		if (mDriverDetail != null
				&& mCurInviteStatus != mDriverDetail.invitestatus) {
			/**
			 * 说明邀请状态发生变化, 返回到司机列表页面时，更新该项邀请状态
			 */
			Intent intent = getIntent();
			intent.putExtra("invitestatus", mCurInviteStatus);
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.mydriver_detail_mobile: {
			if (mDriverDetail != null
					&& !TextUtils.isEmpty(mDriverDetail.phone)) {
				mCurMobile = mDriverDetail.phone;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		case R.id.mydriver_detail_emergency_contact0: {
			if (mDriverDetail != null
					&& !TextUtils.isEmpty(mDriverDetail.emergency_phone1)) {
				mCurMobile = mDriverDetail.emergency_phone1;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		case R.id.mydriver_detail_emergency_contact1: {
			if (mDriverDetail != null
					&& !TextUtils.isEmpty(mDriverDetail.emergency_phone2)) {
				mCurMobile = mDriverDetail.emergency_phone2;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case PermissionUtils.CALL_PHONE: {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				if (!TextUtils.isEmpty(mCurMobile)) {
					CallUtils.call(this, mCurMobile);
				}
			} else {
				/**
				 * 无权限
				 */
				Toast.makeText(this, "请打开拨打电话权限", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			super.onRequestPermissionsResult(requestCode, permissions,
					grantResults);
			break;
		}
	}

	/**
	 * 获取司机详情
	 */
	private void getDriverDataDetail(int driverid) {
		if (driverid <= 0)
			return;

		DeliveryBusAPI.getInstance().getDriverDataDetail(driverid,
				new IMtqDriverDataDetailListener() {

					@Override
					public void onResult(int errCode, MtqDriverDetail data) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0 && data != null) {
							mDriverDetail = data;
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mFailed.setVisibility(View.GONE);
							mLayout.setVisibility(View.VISIBLE);
							updateUI();
						} else {
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mFailed.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	/**
	 * 邀请司机加入车队
	 */
	private void invite(int driverid) {
		DeliveryBusAPI.getInstance().inviteDriver(driverid,
				new IMtqInviteDriverListener() {

					@Override
					public void onResult(int errCode, int invitestatus) {
						if (ProgressDialog.isShowProgress()) {
							ProgressDialog.cancelProgress();
						}
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							Toast.makeText(MyDriverDetailActivity.this, "邀请成功",
									Toast.LENGTH_SHORT).show();
							mCurInviteStatus = invitestatus;
							/**
							 * 将1-未读邀请消息，2-已读邀请消息归类为已邀请
							 */
							if (invitestatus == 1) {
								mCurInviteStatus = 2;
							}
							mInviteStatus.setContent(DriverUtils
									.getInviteStatus(mCurInviteStatus));
							int colorid = DriverUtils
									.getStatusColor(mCurInviteStatus);
							@SuppressWarnings("deprecation")
							int color = getResources().getColor(colorid);
							mInviteStatus.setContentColor(color);
							if (isUninvited(mCurInviteStatus)) {
								mInvite.setVisibility(View.VISIBLE);
							} else {
								mInvite.setVisibility(View.GONE);
							}
						} else {
							Toast.makeText(MyDriverDetailActivity.this, "邀请失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	private boolean isUninvited(int status) {
		boolean isUninvited = false;
		switch (status) {
		case 1:
		case 2:
		case 4:
		case 5: {
			isUninvited = true;
			break;
		}
		case 3: {
			isUninvited = false;
			break;
		}
		default:
			break;
		}
		return isUninvited;
	}
}
