/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: CarDetailActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.car
 * @Description: 车辆实时详情
 * @author: zhaoqy
 * @date: 2017年6月16日 下午11:29:50
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.car;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.cld.mapapi.map.CldMap;
import com.cld.mapapi.map.MapView;
import com.cld.mapapi.map.MarkerOptions;
import com.cld.mapapi.map.OverlayOptions;
import com.cld.mapapi.model.LatLng;
import com.cld.mapapi.search.geocode.GeoCodeResult;
import com.cld.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.cld.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cld.navisdk.routeplan.CldRoutePlaner;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.api.map.MapViewAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.TaskNaviAdapter;
import com.mtq.bus.freighthelper.ui.adapter.TaskStoreAdapter;
import com.mtq.bus.freighthelper.ui.customview.CarCompartTemp;
import com.mtq.bus.freighthelper.ui.customview.CarStateCard;
import com.mtq.bus.freighthelper.ui.customview.DropDownText;
import com.mtq.bus.freighthelper.ui.customview.DropDownText.IDdtListener;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.ui.customview.pull.PullToZoomScrollViewEx;
import com.mtq.bus.freighthelper.ui.dialog.WaybillDialog;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.CarUtils;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateDetailListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskNaviListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskStoreListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCar;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskNavi;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskStore;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class CarDetailActivity extends BaseActivity implements OnClickListener,
		IVCListener, IDdtListener, OnItemClickListener {

	public static final String TAG = "CarDetailActivity";
	public static final int PAGESIZE = 10;

	public static final int TASK_STORE = 1;
	public static final int TASK_NAVI = 2;

	private ImageView mBack;
	private TextView mTitle;

	private ScrollView mScrollView;
	private FrameLayout mViewGroup;
	private MapView mMapView;

	/**
	 * 详情
	 */
	private TextView mPos;
	private TextView mMileage;
	private TextView mCarStatus;

	private VerticalCard mMdriver;
	private VerticalCard mSdriver;

	private View mDetailLayout;
	private DropDownText mDetailVc;
	private View mDetailList;
	/**
	 * 实时数据
	 */
	private CarStateCard remain_fuel;
	private CarStateCard battery_voltage;
	private CarStateCard engine_speed;
	private CarStateCard car_speed;
	private CarStateCard coolant_temperature;
	private CarStateCard moment_fuel;
	/**
	 * 车厢温度
	 */
	private View temperatureLayout;
	private CarCompartTemp temperature0;
	private CarCompartTemp temperature1;
	private CarCompartTemp temperature2;
	/**
	 * 当日运单数据
	 */
	private DropDownText mStoreVc;
	private View mStoreLayout;
	private ListView mStoreListView;
	private TextView mStoreEmpty;
	private TaskStoreAdapter mStoreAdapter;
	private List<MtqTaskStore> mTaskStores;
	/**
	 * 当日点火熄火数据
	 */
	private DropDownText mNaviVc;
	private View mNaviLayout;
	private ListView mNaviListView;
	private TextView mNaviEmpty;
	private TaskNaviAdapter mNaviAdapter;
	private List<MtqTaskNavi> mTotalNavis;
	private List<MtqTaskNavi> mTaskNavis;

	private View mFooter;
	private View mFooterParent;
	private TextView mFooterText;
	private ProgressBar mFooterBar;

	private MtqCar mMtqCar;
	private MtqState mMtqState;
	private MtqCarState mCarState;

	private int mStorePage = 0;
	private int mNaviPage = 0;
	private int mCarID = 0;
	private long mCurX = 0;
	private long mCurY = 0;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_car_detail;
	}

	private void loadViewForScroll() {
		PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
		View headView = LayoutInflater.from(this).inflate(
				R.layout.layout_pull_head, null, false);
		View zoomView = LayoutInflater.from(this).inflate(
				R.layout.layout_pull_zoom, null, false);
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.layout_pull_content, null, false);
		scrollView.setHeaderView(headView);
		scrollView.setZoomView(zoomView);
		scrollView.setScrollContentView(contentView);
		scrollView.setZoomEnabled(true);
	}

	@Override
	protected void initViews() {
		// loadViewForScroll();

		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);

		mScrollView = (ScrollView) findViewById(R.id.car_detail_scrollview);
		mViewGroup = (FrameLayout) findViewById(R.id.car_detail_mapview);
		mPos = (TextView) findViewById(R.id.car_detail_pos);
		mMileage = (TextView) findViewById(R.id.car_detail_mileage);
		mCarStatus = (TextView) findViewById(R.id.car_detail_status);

		mMdriver = (VerticalCard) findViewById(R.id.car_detail_mdriver);
		mSdriver = (VerticalCard) findViewById(R.id.car_detail_sdriver);

		mDetailLayout = findViewById(R.id.car_detail_task_detail_layout);
		mDetailVc = (DropDownText) findViewById(R.id.car_detail_task_detail);
		mDetailList = findViewById(R.id.car_detail_task_detail_list);

		/**
		 * 实时数据
		 */
		remain_fuel = (CarStateCard) findViewById(R.id.car_state_remain_fuel);
		battery_voltage = (CarStateCard) findViewById(R.id.car_state_battery_voltage);
		engine_speed = (CarStateCard) findViewById(R.id.car_state_engine_speed);
		car_speed = (CarStateCard) findViewById(R.id.car_state_car_speed);
		coolant_temperature = (CarStateCard) findViewById(R.id.car_state_coolant_temperature);
		moment_fuel = (CarStateCard) findViewById(R.id.car_state_moment_fuel);
		/**
		 * 车厢温度
		 */
		temperatureLayout = findViewById(R.id.car_detail_temperature_layout);
		temperature0 = (CarCompartTemp) findViewById(R.id.car_detail_temperature0);
		temperature1 = (CarCompartTemp) findViewById(R.id.car_detail_temperature1);
		temperature2 = (CarCompartTemp) findViewById(R.id.car_detail_temperature2);

		/**
		 * 当日运单数据
		 */
		mStoreVc = (DropDownText) findViewById(R.id.car_detail_task_store);
		mStoreLayout = findViewById(R.id.car_detail_task_store_layout);
		mStoreListView = (ListView) findViewById(R.id.car_detail_task_store_list);
		mStoreEmpty = (TextView) findViewById(R.id.car_detail_task_store_empty);

		/**
		 * 当日点火熄火数据
		 */
		mNaviVc = (DropDownText) findViewById(R.id.car_detail_task_navi);
		mNaviLayout = findViewById(R.id.car_detail_task_navi_layout);
		mNaviListView = (ListView) findViewById(R.id.car_detail_task_navi_list);
		mNaviEmpty = (TextView) findViewById(R.id.car_detail_task_navi_empty);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		findViewById(R.id.car_detail_schedule).setOnClickListener(this);
		findViewById(R.id.car_detail_track).setOnClickListener(this);
		mMdriver.setOnVCListener(this);
		mSdriver.setOnVCListener(this);

		mDetailVc.setOnDdtListener(this);
		mStoreVc.setOnDdtListener(this);
		mNaviVc.setOnDdtListener(this);

		mStoreListView.setOnItemClickListener(this);
		mNaviListView.setOnItemClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initData() {
		mBack.setVisibility(View.VISIBLE);
		mCarState = (MtqCarState) getIntent().getSerializableExtra(
				ConstantUtils.MTQ_CAR_STATE);
		if (mCarState != null) {
			mTitle.setText(mCarState.carlicense);
			String mileageHint = getResources().getString(
					R.string.car_detail_mileage);
			/**
			 * 四舍五入
			 */
			int mi = (int) Math.round(mCarState.mileage / 1000.0);
			String mileage = String.format(mileageHint, mi);
			mMileage.setText(mileage);
			mCarStatus.setText(CarUtils.getCarStatus(mCarState.carstatus));
			int colorid = CarUtils.getStatusColor(mCarState.carstatus);
			int color = getResources().getColor(colorid);
			mCarStatus.setTextColor(color);
		}

		mMapView = MapViewAPI.getInstance().createMapView(this);
		mMapView.getMap().setTrafficEnabled(true);
		mViewGroup.addView(mMapView);

		// mTaskStores = TestUtils.getTaskStore();
		mTaskStores = new ArrayList<MtqTaskStore>();
		mStoreAdapter = new TaskStoreAdapter(this, mTaskStores);
		mStoreListView.setAdapter(mStoreAdapter);
		mStoreAdapter.setListViewHeightBasedOnChildren(mStoreListView);
		mStoreListView.setEmptyView(mStoreEmpty);

		// mTaskNavis = TestUtils.getTaskNavi();
		mTotalNavis = new ArrayList<MtqTaskNavi>();
		mTaskNavis = new ArrayList<MtqTaskNavi>();
		mNaviAdapter = new TaskNaviAdapter(this, mTaskNavis);
		mNaviListView.setAdapter(mNaviAdapter);
		mNaviAdapter.setListViewHeightBasedOnChildren(mNaviListView);
		mNaviListView.setEmptyView(mNaviEmpty);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMapView != null) {
			mMapView.onResume();
			MapViewAPI.getInstance().setLocationIconEnabled(mMapView.getMap(), false);
			try {
				CldRoutePlaner.getInstance().clearRoute();
			} catch (Exception e) {
				CldLog.e(TAG, "Exception: " + e.toString());
			}
			if (mCarState != null) {
				showPoi(mCarState.x, mCarState.y, mCarState.gpstime);
			}
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
	private void showPoi(long x, long y, long gpstime) {
		CldLog.e(TAG, "x: " + x + ", y: " + y);
		CldLog.e(TAG, "mCurX: " + mCurX + ", mCurY: " + mCurY);
		
		mCurX = x;
		mCurY = y;
		CldMap map = mMapView.getMap();
		MapViewAPI.getInstance().setLocationIconEnabled(mMapView.getMap(), false);
		MapViewAPI.getInstance().removeAllOverlay(map);

		LatLng lng = new LatLng();
		lng.longitude = x;
		lng.latitude = y;
		if (x == 0 || y == 0) {
			/**
			 * 如果坐标无效，则定位到创建大厦，不显示在线/离线图标
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
		if (TimeUtils.isOnLine(gpstime)) {
			int picId = R.drawable.icon_car_online;
			img.setImageResource(picId);
		} else {
			int picId = R.drawable.icon_car_offline;
			img.setImageResource(picId);
		}
		// 选中放在最上面画，不被其他的盖住
		selectOption = new MarkerOptions().position(lng).layout(view);

		// 选中放在最上面画，不被其他的盖住
		options.add(selectOption);
		MapViewAPI.getInstance().addOverlay(map, options);
		/************************** 显示当前坐标位置 ************************/

		/**
		 * 设置地图中心坐标
		 */
		MapViewAPI.getInstance().showScaleControl(mMapView, false);
		MapViewAPI.getInstance().showZoomControls(mMapView, false);
		MapViewAPI.getInstance().setMapCenter(map, lng);
	}

	@Override
	protected void loadData() {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			if (mCarState != null) {
				mCarID = mCarState.carid;
				/**
				 * 获取车辆实时状态数据
				 */
				getCarStateDetail();
				
				/**
				 * 获取车辆当日运单数据
				 */
				Handler handler0 = new Handler();
				handler0.postDelayed(new Runnable() {

					@Override
					public void run() {
						mStorePage = 1;
						getTaskStore();
					}
				}, 200);
				
				/**
				 * 获取车辆当日行程数据
				 */
				Handler handler1 = new Handler();
				handler1.postDelayed(new Runnable() {

					@Override
					public void run() {
						mNaviPage = 1;
						getTaskNavi();
					}
				}, 400);
			}
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
			finish();
			break;
		}
		case R.id.car_detail_schedule: {
			if (!DeliveryBusAPI.getInstance().hasSchedulePermission()) {
				Toast.makeText(this, "当前账号无车辆调度权限", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(this, ScheduleActivity.class);
			if (mCarState != null) {
				int carid = mCarState.carid;
				String carlicense = mCarState.carlicense;
				intent.putExtra("carid", carid);
				intent.putExtra("carlicense", carlicense);
			}
			if (mMtqCar != null) {
				intent.putExtra("mdriverid", mMtqCar.mdriverid);
				intent.putExtra("mdriver", mMtqCar.mdriver);
			}
			startActivity(intent);
			break;
		}
		case R.id.car_detail_track: {
			if (!DeliveryBusAPI.getInstance().hasTrackPermission()) {
				Toast.makeText(this, "当前账号无轨迹回放权限", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (mCarState != null) {
				Intent intent = new Intent(this, TrackActivity.class);
				int carid = mCarState.carid;
				intent.putExtra("carid", carid);
				startActivity(intent);
			}
			break;
		}
		default:
			break;
		}
	}

	private String mCurMobile = "";

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.car_detail_mdriver: {
			if (mMtqCar != null && !TextUtils.isEmpty(mMtqCar.mphone)) {
				mCurMobile = mMtqCar.mphone;
				CallUtils.makeCall(this, mCurMobile);
			}
			break;
		}
		case R.id.car_detail_sdriver: {
			if (mMtqCar != null && !TextUtils.isEmpty(mMtqCar.sphone)) {
				mCurMobile = mMtqCar.sphone;
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
				Toast.makeText(this, "请开启拨打电话权限", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			super.onRequestPermissionsResult(requestCode, permissions,
					grantResults);
			break;
		}
	}

	@Override
	public void onDdtClick(View view) {
		switch (view.getId()) {
		case R.id.car_detail_task_detail: {
			if (mDetailVc.isExpend()) {
				mDetailList.setVisibility(View.VISIBLE);
			} else {
				mDetailList.setVisibility(View.GONE);
			}
			break;
		}
		case R.id.car_detail_task_store: {
			if (mStoreVc.isExpend()) {
				mStoreLayout.setVisibility(View.VISIBLE);
			} else {
				mStoreLayout.setVisibility(View.GONE);
			}
			break;
		}
		case R.id.car_detail_task_navi: {
			if (mNaviVc.isExpend()) {
				mNaviLayout.setVisibility(View.VISIBLE);
			} else {
				mNaviLayout.setVisibility(View.GONE);
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.car_detail_task_store_list: {
			if (mTaskStores != null && !mTaskStores.isEmpty()) {
				if (mFooterText != null && mFooterText.isShown()) {
					if (position == mTaskStores.size()) {
						loadMore(TASK_STORE);
						return;
					}
				}

				MtqTaskStore taskStore = mTaskStores.get(position);
				String orderid = taskStore.cut_orderid;
				String send_regionname = taskStore.send_regionname;
				String send_addr = taskStore.send_address;
				String receive_regionname = taskStore.receive_regionname;
				String receive_addr = taskStore.receive_address;
				int orderstatus = taskStore.orderstatus;

				String date = TimeUtils.stampToYmdHm(taskStore.receive_date);
				String receive_date = date;
				float weight = taskStore.goods_weight;
				float volume = taskStore.goods_volume;
				String driver = taskStore.driver_name;
				String phone = taskStore.driver_phone;

				WaybillDialog dialog = new WaybillDialog(this, orderid,
						send_regionname, send_addr, receive_regionname,
						receive_addr, orderstatus, receive_date, weight,
						volume, driver, phone);
				dialog.show();
			}
			break;
		}
		case R.id.car_detail_task_navi_list: {
			if (mFooterText != null && mFooterText.isShown()) {
				if (position == mTaskNavis.size()) {
					loadMore(TASK_NAVI);
					return;
				}
			}
			break;
		}
		default:
			break;
		}
	}

	private boolean isInvalidMtqState(MtqState mtqState) {
		if (mtqState == null)
			return true;

		if (mtqState != null) {
			if (mtqState.time == 0 && mtqState.surplusoil == 0
					&& mtqState.batteryvoltage == 0
					&& mtqState.enginespeed == 0 && mtqState.carspeed == 0
					&& mtqState.enginecoolcent == 0 && mtqState.mileage == 0
					&& mtqState.instantfuel == 0 && mtqState.maxtempalarm == 0
					&& mtqState.mintempalarm == 0 && mtqState.temperature1 == 0
					&& mtqState.temperature2 == 0 && mtqState.temperature3 == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取车辆实时状态数据
	 */
	private void getCarStateDetail() {
		if (mCarID <= 0)
			return;

		DeliveryBusAPI.getInstance().getCarStateDetail(mCarID,
				new IMtqCarStateDetailListener() {

					@Override
					public void onResult(int errCode, MtqCar mtqCar,
							MtqState mtqState) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							mMtqCar = mtqCar;
							mMtqState = mtqState;
							updateCarDetail();
						}
					}
				});
	}

	/**
	 * 获取车辆当日运单数据
	 */
	private void getTaskStore() {
		if (mCarID <= 0)
			return;

		DeliveryBusAPI.getInstance().getTaskStore(mCarID, mStorePage, PAGESIZE,
				new IMtqTaskStoreListener() {

					@Override
					public void onResult(int errCode, List<MtqTaskStore> data,
							int total) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							if (data != null && !data.isEmpty()) {
								int local_size = mTaskStores.size() + data.size();
								if (local_size < total) {
									showFooter(TASK_STORE);
								} else if (local_size == total) {
									hideFooter();
								}
								mTaskStores.addAll(data);
								mStoreAdapter.notifyDataSetChanged();
								return;
							}
						}
						if (mStorePage > 1) {
							mStorePage--;
						}
					}
				});
	}

	/**
	 * 获取车辆当日行程数据
	 */
	private void getTaskNavi() {
		if (mCarID <= 0)
			return;

		DeliveryBusAPI.getInstance().getTaskNavi(mCarID, mNaviPage, PAGESIZE,
				new IMtqTaskNaviListener() {

					@Override
					public void onResult(int errCode, List<MtqTaskNavi> data,
							int total) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							if (data != null && !data.isEmpty()) {
								int local_size = mTotalNavis.size() + data.size();
								if (local_size < total) {
									showFooter(TASK_NAVI);
								} else if (local_size == total) {
									hideFooter();
								}
								/**
								 * mTotalNavis: 用来计算当前总数
								 */
								mTotalNavis.addAll(data);
								/**
								 * 过滤掉取不到熄火时间的数据
								 */
								int len = data.size();
								for (int i = 0; i < len; i++) {
									MtqTaskNavi navi = data.get(i);
									if (navi.endtime > 0
											&& navi.endtime > navi.starttime) {
										mTaskNavis.add(navi);
									}
								}
								mNaviAdapter.notifyDataSetChanged();
								return;
							}
						}
						if (mNaviPage > 1) {
							mNaviPage--;
						}
					}
				});
	}

	/********************* ListView中动态显示和隐藏Footer *********************/
	protected void showFooter(int type) {
		if (mFooterParent == null) {
			mFooterParent = (LinearLayout) LayoutInflater.from(this).inflate(
					R.layout.layout_listview_footer_load_more, null);
			mFooter = mFooterParent.findViewById(R.id.footer_load_more_layout);
			mFooterBar = (ProgressBar) mFooterParent
					.findViewById(R.id.footer_load_more_bar);
			mFooterText = (TextView) mFooterParent
					.findViewById(R.id.footer_load_more);
			if (type == TASK_STORE) {
				mStoreListView.addFooterView(mFooterParent);
			} else {
				mNaviListView.addFooterView(mFooterParent);
			}
		}
		mFooterText.setVisibility(View.VISIBLE);
		mFooterText.setText("加载更多");
		mFooterBar.setVisibility(View.GONE);
	}

	protected void hideFooter() {
		if (mFooter != null) {
			mFooter.setVisibility(View.GONE);
		}
	}

	protected void loadMore(int type) {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
			return;
		}

		mFooterText.setText("正在加载中...");
		mFooterBar.setVisibility(View.VISIBLE);
		switch (type) {
		case TASK_STORE: {
			mStorePage++;
			getTaskStore();
			break;
		}
		case TASK_NAVI: {
			mNaviPage++;
			getTaskNavi();
			break;
		}
		default:
			break;
		}
	}

	/********************* ListView中动态显示和隐藏Footer *********************/

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	private static String location = "";

	@SuppressWarnings("deprecation")
	private void updateCarDetail() {
		if (mMtqCar != null) {
			mTitle.setText(mMtqCar.carlicense);
			String mileageHint = getResources().getString(
					R.string.car_detail_mileage);
			int mi = (int) Math.round(mMtqCar.mileage / 1000.0);
			String mileage = String.format(mileageHint, mi);
			mMileage.setText(mileage);
			mCarStatus.setText(CarUtils.getCarStatus(mMtqCar.carstatus));
			int colorid = CarUtils.getStatusColor(mCarState.carstatus);
			int color = getResources().getColor(colorid);
			mCarStatus.setTextColor(color);

			CldLog.e(TAG, "mdriver: " + mMtqCar.mdriver);
			CldLog.e(TAG, "sdriver: " + mMtqCar.sdriver);
			if (!TextUtils.isEmpty(mMtqCar.mdriver)) {
				mMdriver.setTitle(mMtqCar.mdriver);
			} else {
				mMdriver.setTitle("主司机");
			}
			mMdriver.setContent(mMtqCar.mphone);
			if (!TextUtils.isEmpty(mMtqCar.sdriver)) {
				mSdriver.setTitle(mMtqCar.sdriver);
			} else {
				mSdriver.setTitle("副司机");
			}
			mSdriver.setContent(mMtqCar.sphone);
			
			if (mMtqCar.x != 0 && mMtqCar.x == mCurX && 
					mMtqCar.y != 0 && mMtqCar.y == mCurY) {
				/**
				 * 如果坐标没变，则无须重新刷新
				 */
			} else {
				/**
				 * 重新刷新当前位置
				 */
				showPoi(mMtqCar.x, mMtqCar.y, mMtqCar.gpstime);
			}
			locationPos();
		} else {
			/**
			 * 获取车辆信息失败
			 */
			mPos.setText("暂无位置");
		}

		if (mMtqState != null) {
			if (!isInvalidMtqState(mMtqState)) {
				mDetailLayout.setVisibility(View.VISIBLE);
				mDetailVc.setExpend(true);
				mDetailList.setVisibility(View.VISIBLE);
				
				if (mMtqState.surplusoil != -512) {
					remain_fuel.setContent(mMtqState.surplusoil + "%");
					/**
					 * 当剩余油量低于10%时，警示显示
					 */
					if (mMtqState.surplusoil < 10) {
						int color = getResources().getColor(R.color.red);
						remain_fuel.setContentColor(color);
					} else {
						int color = getResources().getColor(
								R.color.text_highlight_color);
						remain_fuel.setContentColor(color);
					}
				} else {
					remain_fuel.setContent("暂无数据");
					int color = getResources().getColor(
							R.color.text_highlight_color);
					remain_fuel.setContentColor(color);
				}

				if (mMtqState.batteryvoltage != -512) {
					battery_voltage.setContent(mMtqState.batteryvoltage + "V");
				} else {
					battery_voltage.setContent("暂无数据");
				}

				if (mMtqState.enginespeed != -512) {
					engine_speed.setContent(mMtqState.enginespeed + "rpm");
				} else {
					engine_speed.setContent("暂无数据");
				}

				if (mMtqState.carspeed != -512) {
					car_speed.setContent(mMtqState.carspeed + "km/h");
				} else {
					car_speed.setContent("暂无数据");
				}

				if (mMtqState.enginecoolcent != -512) {
					coolant_temperature.setContent(mMtqState.enginecoolcent
							+ "℃");
				} else {
					coolant_temperature.setContent("暂无数据");
				}

				if (mMtqState.instantfuel != -512) {
					moment_fuel.setContent(mMtqState.instantfuel + "L/100km");
				} else {
					moment_fuel.setContent("暂无数据");
				}

				/**
				 * 车厢温度
				 */
				temperatureLayout.setVisibility(View.VISIBLE);
				if (mMtqState.temperature1 != -512) {
					temperature0.setContent(mMtqState.temperature1 + "℃");
					temperature0.setMaxCount(30);
					temperature0.setCurCount(mMtqState.temperature1);
				} else {
					temperature0.setContent("暂无数据");
				}

				if (mMtqState.temperature2 != -512) {
					temperature1.setContent(mMtqState.temperature2 + "℃");
					temperature1.setMaxCount(30);
					temperature1.setCurCount(mMtqState.temperature2);
				} else {
					temperature1.setContent("暂无数据");
				}

				if (mMtqState.temperature3 != -512) {
					temperature2.setContent(mMtqState.temperature3 + "℃");
					temperature2.setMaxCount(30);
					temperature2.setCurCount(mMtqState.temperature3);
				} else {
					temperature2.setContent("暂无数据");
				}
			} else {
				/**
				 * mMtqState对象无效
				 */
				mDetailLayout.setVisibility(View.GONE);
			}
		} else {
			/**
			 * 获取车辆状态失败
			 */
			mDetailLayout.setVisibility(View.GONE);
		}
	}

	private void locationPos() {
		if (mMtqCar.x == 0 || mMtqCar.y == 0) {
			location = "暂无位置";
			mHadler.sendEmptyMessage(0);
			return;
		}

		MapViewAPI.getInstance().getGeoCodeResult(mMtqCar.x, mMtqCar.y,
				new OnGetGeoCoderResultListener() {

					@Override
					public void onGetGeoCodeResult(GeoCodeResult arg0) {

					}

					@Override
					public void onGetReverseGeoCodeResult(
							ReverseGeoCodeResult result) {
						if (result != null) {
							location = result.address;
							//CldLog.e(TAG, "address: " + result.address);
							CldLog.e(TAG, "location: " + location);
						} else {
							location = "暂无位置";
						}
						mHadler.sendEmptyMessage(0);
					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHadler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				mPos.setText(location);
				break;
			}
			default:
				break;
			}
		};
	};
}
