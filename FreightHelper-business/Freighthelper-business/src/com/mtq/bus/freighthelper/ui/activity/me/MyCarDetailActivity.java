/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MyCarDetailActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me
 * @Description: 车辆详情
 * @author: zhaoqy
 * @date: 2017年6月16日 下午9:53:06
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me;

import java.util.ArrayList;
import java.util.List;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.DropDownAdapter;
import com.mtq.bus.freighthelper.ui.customview.ShadowDrawable;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.CarUtils;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarDataDetailListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarDataDetail;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqGroup;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MyCarDetailActivity extends BaseActivity implements
		OnClickListener, IVCListener {

	private ImageView mBack;
	private TextView mTitle;
	private LinearLayout mFailed;
	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mLayout;
	private VerticalCard mLicense;
	private VerticalCard mSource;
	private VerticalCard mGroup;
	private VerticalCard mMdriver;
	private VerticalCard mSdriver;
	private VerticalCard mCarModel;
	private VerticalCard mCarType;
	private VerticalCard mDeviceName;
	private VerticalCard mDeviceModel;
	private VerticalCard mDeviceIdsn;
	private VerticalCard mIdphone;
	private MtqCarDataDetail mCarDetail;
	private List<MtqGroup> mGroups;
	private String mCurMobile = "";

	private PopupWindow mPop;
	private DropDownAdapter mAdapter;
	private List<DropDown> mDropList;

	private int xoff = 0;
	private int mCarId = 0;
	private int mShadowWidth = 0;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_mycar_detail;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mFailed = (LinearLayout) findViewById(R.id.mycar_detail_failed);
		mNetAbnormal = (LinearLayout) findViewById(R.id.mycar_detail_net_abnormal);
		mLoading = (LinearLayout) findViewById(R.id.mycar_detail_laoding);
		mLayout = (LinearLayout) findViewById(R.id.mycar_detail_layout);
		mLicense = (VerticalCard) findViewById(R.id.mycar_detail_license);
		mSource = (VerticalCard) findViewById(R.id.mycar_detail_source);
		mGroup = (VerticalCard) findViewById(R.id.mycar_detail_group);
		mMdriver = (VerticalCard) findViewById(R.id.mycar_detail_mdriver);
		mSdriver = (VerticalCard) findViewById(R.id.mycar_detail_sdriver);
		mCarModel = (VerticalCard) findViewById(R.id.mycar_detail_carmodel);
		mCarType = (VerticalCard) findViewById(R.id.mycar_detail_cartype);
		mDeviceName = (VerticalCard) findViewById(R.id.mycar_detail_device_name);
		mDeviceModel = (VerticalCard) findViewById(R.id.mycar_detail_device_model);
		mDeviceIdsn = (VerticalCard) findViewById(R.id.mycar_detail_device_idsn);
		mIdphone = (VerticalCard) findViewById(R.id.mycar_detail_device_idphone);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		findViewById(R.id.failed_refresh).setOnClickListener(this);
		findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		mGroup.setOnVCListener(this);
		mMdriver.setOnVCListener(this);
		mSdriver.setOnVCListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.mycar_detail);
		mBack.setVisibility(View.VISIBLE);
		mGroups = new ArrayList<MtqGroup>();

		mDropList = new ArrayList<DropDown>();
		mAdapter = new DropDownAdapter(this, mDropList);
		mCarId = getIntent().getIntExtra(ConstantUtils.CAR_ID, 0);

		mShadowWidth = DensityUtils.getDedaultSize(this);
		xoff = (int) (DensityUtils.getWidth(this) * 0.60);
	}

	@Override
	protected void loadData() {
		if (CldPhoneNet.isNetConnected()) {
			getCarDataDetail(mCarId);
		} else {
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
		}
	}

	@Override
	protected void updateUI() {
		if (mCarDetail != null) {
			mLicense.setContent(mCarDetail.carlicense);
			mSource.setContent(CarUtils.getSource(mCarDetail.sourceid));
			if (mGroups != null && !mGroups.isEmpty()) {
				String group_name = mGroups.get(0).group_name;
				mGroup.setContent(group_name);
			}
			if (!TextUtils.isEmpty(mCarDetail.mdriver)) {
				mMdriver.setTitle(mCarDetail.mdriver);
			} else {
				mMdriver.setTitle("主司机");
			}
			mMdriver.setContent(mCarDetail.mphone);
			if (!TextUtils.isEmpty(mCarDetail.sdriver)) {
				mSdriver.setTitle(mCarDetail.sdriver);
			} else {
				mSdriver.setTitle("副司机");
			}
			mSdriver.setContent(mCarDetail.sphone);
			mCarModel.setContent(mCarDetail.carmodel);
			mCarType.setContent(CarUtils.getCarType(mCarDetail.cartype));
			mDeviceName.setContent(mCarDetail.dtypename);
			mDeviceModel.setContent(mCarDetail.model);
			mDeviceIdsn.setContent(mCarDetail.idsn);
			mIdphone.setContent(mCarDetail.idphone);
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
			finish();
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

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.mycar_detail_group: {
			if (mGroup.isExpend()) {
				mAdapter.notifyDataSetChanged();
				dropDown();
			}
			break;
		}
		case R.id.mycar_detail_mdriver: {
			if (mCarDetail != null && !TextUtils.isEmpty(mCarDetail.mphone)) {
				mCurMobile = mCarDetail.mphone;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		case R.id.mycar_detail_sdriver: {
			if (mCarDetail != null && !TextUtils.isEmpty(mCarDetail.sphone)) {
				mCurMobile = mCarDetail.sphone;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void dropDown() {
		if (mPop == null) {
			ListView listView = new ListView(this);
			listView.setCacheColorHint(0x00000000);
			listView.setBackgroundColor(getResources().getColor(R.color.white));
			Drawable drawable = getResources().getDrawable(
					R.drawable.divider_bg);
			listView.setDivider(drawable);
			listView.setDividerHeight(1);
			listView.setAdapter(mAdapter);

			ShadowDrawable.Builder.on(listView)
					.bgColor(getResources().getColor(R.color.white))
					.shadowColor(Color.parseColor("#000000"))
					.shadowRange(mShadowWidth).offsetBottom(mShadowWidth)
					.offsetTop(mShadowWidth).offsetLeft(mShadowWidth).create();

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mPop.dismiss();
					itemClick(position);
				}
			});
			int width = (int) (DensityUtils.getWidth(this) * 0.40);
			mPop = new PopupWindow(listView, width + mShadowWidth,
					LayoutParams.WRAP_CONTENT, true);
			mPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			mPop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mPop.dismiss();
					mGroup.closeExpend();
				}
			});
		}
		mPop.showAsDropDown(mGroup, xoff - mShadowWidth, -mShadowWidth);
	}

	protected void itemClick(int position) {
		if (mGroups != null && !mGroups.isEmpty()) {
			String group = mGroups.get(position).group_name;
			mGroup.setContent(group);
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
	 * 车辆数据详情
	 */
	private void getCarDataDetail(int carid) {
		if (carid <= 0)
			return;

		DeliveryBusAPI.getInstance().getCarDataDetail(carid,
				new IMtqCarDataDetailListener() {

					@Override
					public void onResult(int errCode, MtqCarDataDetail car,
							List<MtqGroup> groups) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}
						
						if (errCode == 0 && car != null) {
							if (groups != null && !groups.isEmpty()) {
								mGroups.clear();
								mGroups.addAll(groups);
								int len = mGroups.size();
								for (int i = 0; i < len; i++) {
									DropDown drop = new DropDown();
									drop.id = mGroups.get(i).group_id;
									drop.name = mGroups.get(i).group_name;
									mDropList.add(drop);
								}
							}
							mCarDetail = car;
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

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_GET_MYCAR_DETAIL_SUCCESS: {
			updateUI();
			if (mGroups != null && !mGroups.isEmpty()) {
				int len = mGroups.size();
				for (int i = 0; i < len; i++) {
					DropDown drop = new DropDown();
					drop.id = mGroups.get(i).group_id;
					drop.name = mGroups.get(i).group_name;
					mDropList.add(drop);
				}
			}
			break;
		}
		case MsgId.MSGID_GET_MYCAR_DETAIL_FAILED: {
			Toast.makeText(this, "获取车辆详情失败", Toast.LENGTH_SHORT).show();
			break;
		}
		default:
			break;
		}
	}
}
