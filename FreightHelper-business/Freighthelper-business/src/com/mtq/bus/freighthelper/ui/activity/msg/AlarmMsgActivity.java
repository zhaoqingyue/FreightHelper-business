/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AlarmMsgActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.msg
 * @Description: 报警消息详情详情
 * @author: zhaoqy
 * @date: 2017年6月7日 下午7:42:48
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.msg;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.log.CldLog;
import com.cld.mapapi.map.CldMap;
import com.cld.mapapi.map.MapView;
import com.cld.mapapi.map.MarkerOptions;
import com.cld.mapapi.map.OverlayOptions;
import com.cld.mapapi.model.LatLng;
import com.cld.navisdk.routeplan.CldRoutePlaner;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.api.map.MapViewAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.GeoCodeUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.MsgUtils;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AlarmMsgActivity extends BaseActivity implements OnClickListener,
		IVCListener {

	public static final String TAG = "AlarmMsgActivity";
	private ImageView mBack;
	private TextView mTitle;
	private FrameLayout mViewGroup;
	private MapView mMapView;
	private TextView mSpeed;
	private VerticalCard mLicense;
	private TextView mPos;
	private VerticalCard mTime;
	private VerticalCard mType;
	private VerticalCard mContent;
	private VerticalCard mMdriver;
	private VerticalCard mSdriver;
	private MtqMsgAlarm mMsgAlarm;
	private String mCurMobile = "";
	private boolean hasSetRead = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_msg_alarm;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mViewGroup = (FrameLayout) findViewById(R.id.msg_alarm_map);
		mSpeed = (TextView) findViewById(R.id.msg_alarm_speed);
		mLicense = (VerticalCard) findViewById(R.id.msg_alarm_license);
		mPos = (TextView) findViewById(R.id.msg_alarm_pos);
		mTime = (VerticalCard) findViewById(R.id.msg_alarm_time);
		mType = (VerticalCard) findViewById(R.id.msg_alarm_type);
		mContent = (VerticalCard) findViewById(R.id.msg_alarm_content);
		mMdriver = (VerticalCard) findViewById(R.id.msg_alarm_mdriver);
		mSdriver = (VerticalCard) findViewById(R.id.msg_alarm_sdriver);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mMdriver.setOnVCListener(this);
		mSdriver.setOnVCListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.msg_alarm_detail);
		mBack.setVisibility(View.VISIBLE);
		mMsgAlarm = (MtqMsgAlarm) getIntent().getSerializableExtra(
				ConstantUtils.MTQ_MSG_ALARM);
		mMapView = MapViewAPI.getInstance().createMapView(this);
		mMapView.getMap().setTrafficEnabled(true);
		mViewGroup.addView(mMapView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMapView != null) {
			mMapView.onResume();
			try {
				CldRoutePlaner.getInstance().clearRoute();
			} catch (Exception e) {
				CldLog.e(TAG, "Exception: " + e.toString());
			}
			showPoi();
			mMapView.update();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMapView != null) {
			mMapView.onPause();
		}
	}

	/**
	 * 显示POI 为中心位置
	 */
	private void showPoi() {
		CldMap map = mMapView.getMap();
		MapViewAPI.getInstance().setLocationIconEnabled(map, false);
		MapViewAPI.getInstance().removeAllOverlay(map);

		if (mMsgAlarm == null)
			return;
			
		CldLog.e(TAG, "x: " + mMsgAlarm.x + ", y: " + mMsgAlarm.y);
		LatLng lng = new LatLng();
		lng.longitude = mMsgAlarm.x;
		lng.latitude = mMsgAlarm.y;
		if (mMsgAlarm.x == 0 || mMsgAlarm.y == 0) {
			/**
			 * 如果坐标无效，则定位到创建大厦
			 */
			lng.longitude = 114.11581083;
			lng.latitude = 22.600734167;

			MapViewAPI.getInstance().showScaleControl(mMapView, false);
			MapViewAPI.getInstance().showZoomControls(mMapView, false);
			MapViewAPI.getInstance().setMapCenter(map, lng);
			return;
		}
		
		/************************** 显示当前坐标位置 ************************/
		List<OverlayOptions> options = new ArrayList<OverlayOptions>();
		OverlayOptions selectOption = null;
		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_track_point_poi, null);
		ImageView img = (ImageView) view.findViewById(R.id.track_point_poi_img);
		int picId = R.drawable.icon_water_red;
		img.setImageResource(picId);
		TextView speed = (TextView) view
				.findViewById(R.id.track_point_poi_speed);
		speed.setText(mMsgAlarm.speed + "km/h");
		
		// 选中放在最上面画，不被其他的盖住
		selectOption = new MarkerOptions().position(lng).layout(view);
		// 选中放在最上面画，不被其他的盖住
		options.add(selectOption);
		MapViewAPI.getInstance().addOverlay(map, options);
		/************************** 显示当前坐标位置 ************************/

		MapViewAPI.getInstance().showScaleControl(mMapView, false);
		MapViewAPI.getInstance().showZoomControls(mMapView, false);
		MapViewAPI.getInstance().setMapCenter(map, lng);
	}

	@Override
	protected void loadData() {
		updateMsg();
		if (mMsgAlarm != null && mMsgAlarm.readstatus == 0) {
			/**
			 * 已读状态为0，则更新已读状态
			 */
			String id = mMsgAlarm.id;
			CldLog.e(TAG, "id: " + id);
			DeliveryBusAPI.getInstance().setMsgAlarmRead(id);
		}
	}

	private void updateMsg() {
		if (mMsgAlarm != null) {
			mSpeed.setText(mMsgAlarm.speed + "km/h");
			mLicense.setContent(mMsgAlarm.carlicense);
			mTime.setContent(TimeUtils.stampToYmdHms(mMsgAlarm.locattime));
			String alarmType = MsgUtils.getAlarmType(mMsgAlarm.alarmid);
			CldLog.e(TAG, "alarmType: " + alarmType);
			mType.setContent(alarmType);

			if (!TextUtils.isEmpty(mMsgAlarm.mdriver)) {
				mMdriver.setTitle(mMsgAlarm.mdriver);
			} else {
				mMdriver.setTitle("主司机");
			}
			mMdriver.setContent(mMsgAlarm.mphone);
			if (!TextUtils.isEmpty(mMsgAlarm.sdriver)) {
				mSdriver.setTitle(mMsgAlarm.sdriver);
			} else {
				mSdriver.setTitle("副司机");
			}
			mSdriver.setContent(mMsgAlarm.sphone);

			new GeoCodeUtils(mPos).setTextByThread(mMsgAlarm.x, mMsgAlarm.y,
					false);
			mContent.setContent(MsgUtils.getAlarmDesc(mMsgAlarm.alarmid,
					mMsgAlarm.eventjson));
		}
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
		case R.id.title_left_img: {
			back();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		/**
		 * 当注释掉super.onBackPressed()
		 */
		// super.onBackPressed();
		back();
	}

	private void back() {
		if (hasSetRead) {
			Intent intent = getIntent();
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.msg_alarm_mdriver: {
			if (mMsgAlarm != null && !TextUtils.isEmpty(mMsgAlarm.mphone)) {
				mCurMobile = mMsgAlarm.mphone;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		case R.id.msg_alarm_sdriver: {
			if (mMsgAlarm != null && !TextUtils.isEmpty(mMsgAlarm.sphone)) {
				mCurMobile = mMsgAlarm.sphone;
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
				if (TextUtils.isEmpty(mCurMobile)) {
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

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_SET_MSG_ALARM_READ_SUCCESS: {
			hasSetRead = true;
			break;
		}
		case MsgId.MSGID_SET_MSG_ALARM_READ_FAILED: {
			hasSetRead = true;
			break;
		}
		default:
			break;
		}
	}
}
