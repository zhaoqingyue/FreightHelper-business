/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MapSelectPointActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.car
 * @Description: 地图选点
 * @author: zhaoqy
 * @date: 2017年6月13日 下午7:35:22
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.car;

import hmi.packages.HPDefine.HPWPoint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.location.CldLocation;
import com.cld.location.CldLocationClient;
import com.cld.location.CldLocationOption;
import com.cld.location.ICldLocationListener;
import com.cld.log.CldLog;
import com.cld.mapapi.map.CldMap.OnMapMovingListener;
import com.cld.mapapi.map.MapView;
import com.cld.mapapi.model.LatLng;
import com.cld.mapapi.model.PoiInfo;
import com.cld.mapapi.search.exception.IllegalSearchArgumentException;
import com.cld.mapapi.search.geocode.GeoCodeResult;
import com.cld.mapapi.search.geocode.GeoCoder;
import com.cld.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.cld.mapapi.search.geocode.ReverseGeoCodeOption;
import com.cld.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cld.mapapi.search.poi.OnPoiSearchResultListener;
import com.cld.mapapi.search.poi.PoiNearSearch;
import com.cld.mapapi.search.poi.PoiNearSearchOption;
import com.cld.mapapi.search.poi.PoiResult;
import com.cld.nv.location.CldCoordUtil;
import com.cld.nv.map.CldMapApi;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.map.MapViewAPI;
import com.mtq.bus.freighthelper.bean.AddressBean;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.utils.DebounceTool;
import com.mtq.bus.freighthelper.utils.DebounceTool.OnDebounceListener;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MapSelectPointActivity extends BaseActivity implements
		OnClickListener, OnGetGeoCoderResultListener, OnPoiSearchResultListener {

	// public static final int resultCode = 0;
	protected static final String TAG = "MapSelectPointActivity";
	private ImageView mBack;
	private TextView mTitle;
	private FrameLayout mMapLayout;
	TextView tv_position;
	TextView tv_position_detail;
	TextView tv_confirm;
	View prl_loading;
	MapView mMapView;
	GeoCoder mGeoCoder;
	PoiNearSearch mPoiSearch;
	LatLng currentlatLng;
	DebounceTool tool;
	ReverseGeoCodeResult mCurGeoResult;
	PoiInfo mCurPoi;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_map_select_point;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mMapLayout = (FrameLayout) findViewById(R.id.map_select_point_mapview);

		tv_position = (TextView) findViewById(R.id.tv_position);
		tv_position_detail = (TextView) findViewById(R.id.tv_position_detail);
		tv_confirm = (TextView) findViewById(R.id.tv_confirm);

		prl_loading = findViewById(R.id.prl_loading);

	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.schedule_map_select_point);
		mBack.setVisibility(View.VISIBLE);
		mMapView = MapViewAPI.getInstance().createMapView(this);
		mMapView.getMap().setTrafficEnabled(false);
		mMapLayout.addView(mMapView);

		MapViewAPI.getInstance().setLocationIconEnabled(mMapView.getMap(),
				false);
		HPWPoint start = CldMapApi.getNMapCenter();
		mMapView.getMap().setMapCenter(new LatLng(start.y, start.x));

		getTool().NewInput("getcenter");
		mMapView.getMap().setOnMapMovingListener(new OnMapMovingListener() {

			@Override
			public void onMapMoving() {
			}

			@Override
			public void onMapMoveStoped() {
				CldLog.e("滑动停止", "获取当前位置");
				LoadResult();
				getTool().NewInput("getcenter");
			}
		});

		getTool().setListener(new OnDebounceListener() {

			@Override
			public void AfterDebounce(Object obj) {
				getMapCenter();
			}
		});
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

	public void GeoCode(LatLng latLng) {
		ReverseGeoCodeOption option = new ReverseGeoCodeOption();
		// 设置逆地理坐标的经度值
		option.location.longitude = latLng.longitude;
		// 设置逆地理坐标的纬度值
		option.location.latitude = latLng.latitude;
		getGeoCoder().setOnGetGeoCodeResultListener(this);
		try {
			// 传入逆地理参数对象
			getGeoCoder().reverseGeoCode(option);
		} catch (IllegalSearchArgumentException e) {
			e.printStackTrace();
		}
	}

	public void getMapCenter() {
		if (mMapView != null) {
			// 获取地图中心点坐标
			mCurGeoResult = null;
			currentlatLng = mMapView.getMap().getCenterPosition();
			GeoCode(currentlatLng);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		getGeoCoder().setOnGetGeoCodeResultListener(null);
		mMapView.destroy();
	}

	public DebounceTool getTool() {
		if (tool == null)
			tool = new DebounceTool();

		return tool;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.tv_confirm: {
			if (mCurGeoResult != null) {
				Intent intent = new Intent();
				AddressBean bean = new AddressBean();
				bean.address = mCurGeoResult.address;
				bean.kcode = CldCoordUtil.cldToKCode(mCurGeoResult.location);
				bean.x = mCurGeoResult.location.longitude;
				bean.y = mCurGeoResult.location.latitude;
				CldLog.e(TAG, bean.address + " " + bean.kcode + ", x: "
						+ bean.x + ", y: " + bean.y);
				intent.putExtra("addressinfo", bean);
				if (mCurPoi != null) {
					intent.putExtra("poiname", mCurPoi.name);
					CldLog.e(TAG, "poiname: " + mCurPoi.name);
				}
				setResult(RESULT_OK, intent);
				finish();
			} else {
				if (prl_loading != null
						&& prl_loading.getVisibility() == View.VISIBLE) {
					Toast.makeText(this, "正在获取地址中，请等待", Toast.LENGTH_SHORT)
							.show();
				} else
					Toast.makeText(this, "获取地址失败，请重新获取", Toast.LENGTH_SHORT)
							.show();
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		ShowResult();
		if (result != null) {
			if (result.errorCode != 0) {
				Toast.makeText(
						this,
						result != null && !TextUtils.isEmpty(result.errorMsg) ? result.errorMsg
								: "逆地理编码失败", Toast.LENGTH_LONG).show();
				CldLog.v("SearchSDK", result.address + "\n"
						+ result.businessCircle);
				SetResult(null);
				return;
			}
			if (TextUtils.isEmpty(result.address)) {// 返回地址是否为空判断
				// mTv_Name.setText( result != null
				// && !TextUtils
				// .isEmpty(result.errorMsg) ? result.errorMsg
				// : "逆地理编码失败");

				SetResult(null);
			} else {
				if (currentlatLng == null
						|| (result.location.latitude == currentlatLng.latitude && result.location.longitude == currentlatLng.longitude)) {
					SetResult(result);
				}
			}
			// mTv_Name.setText(result.address+"\n"+result.businessCircle+"\n"
			// +result.addressDetail.province + "\n"
			// +result.addressDetail.city + "\n"
			// +result.addressDetail.district + "\n"
			// +result.addressDetail.street + "\n"
			// +result.addressDetail.streetNumber + "\n");
		}
	}

	public void SetResult(final ReverseGeoCodeResult result) {
		mCurGeoResult = result;
		MapSelectPointActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result == null) {
					tv_position.setText("获取位置失败");
					tv_position_detail.setText("");

				} else {
					LoadResult();
					// result.businessCircle
					// mCurResult.addressDetail.street +
					// mCurResult.addressDetail.streetNumber
					tv_position.setText("");
					tv_position_detail.setText(result.address);

					searchNearPoi();
					// tv_position_detail.setText(mCurResult.address);
					// tv_position_detail.setText(
					// result.addressDetail.province + result.addressDetail.city
					// +
					// result.addressDetail.district
					// + result.addressDetail.street +
					// result.addressDetail.streetNumber);
				}
			}
		});
	}

	public void ShowResult() {
		if (prl_loading != null) {
			MapSelectPointActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					prl_loading.setVisibility(View.GONE);
					// tv_confirm.setClickable(true);
					tv_confirm.setEnabled(true);
				}
			});
		}
	}

	public void LoadResult() {
		if (prl_loading != null) {
			MapSelectPointActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					prl_loading.setVisibility(View.VISIBLE);
					// tv_confirm.setClickable(false);
					tv_confirm.setEnabled(false);
				}
			});
		}
	}

	protected void searchNearPoi() {
		PoiNearSearchOption option = new PoiNearSearchOption();
		option.pageNum = 1;// 得到页总数
		option.pageCapacity = 5;// 单页显示数
		// option.keyword = mEt_Keyword.getText().toString();// 搜索关键字

		option.radius = 500;// 默认为100

		option.location.longitude = mCurGeoResult.location.longitude;

		option.location.latitude = mCurGeoResult.location.latitude;

		getPoiSearch().setOnPoiSearchListner(this);
		getPoiSearch().searchNearby(option);// 传入查找参数
	}

	/**
	 * Poi搜索结果
	 */
	@Override
	public void onGetPoiSearchResult(int errorCode, PoiResult result) {

		ShowResult();

		// poiResult = result;
		if (errorCode == 0 && result != null && result.totalCount != 0
				&& result.getPoiInfos() != null
				&& result.getPoiInfos().size() > 0) {// 判断是否有数据

			mCurPoi = result.getPoiInfos().get(0);
			tv_position.setText("在" + mCurPoi.name + "附近");
		} else {

			mCurPoi = null;

			tv_position.setText("附近未找到相关的点");
		}
	}

	public GeoCoder getGeoCoder() {

		if (mGeoCoder == null)
			mGeoCoder = GeoCoder.newInstance();

		return mGeoCoder;

	}

	public PoiNearSearch getPoiSearch() {

		if (mPoiSearch == null)
			mPoiSearch = PoiNearSearch.newInstance();

		return mPoiSearch;

	}

	// 定位终端
	private CldLocationClient locationManager;

	/**
	 * 定位
	 * 
	 * @param locationMode
	 *            定位类型 参考类LocationMode
	 * @param spanMs
	 *            定位频率 单位毫秒
	 * @return void
	 * @author Huagx
	 * @date 2016-1-28 上午9:11:40
	 */
	private void location(int locationMode, int spanMs) {
		if (null == locationManager) {
			locationManager = new CldLocationClient(this);
		}
		// 如果已开启定位，先停掉
		if (locationManager.isStarted()) {
			locationManager.stop();
		}
		// 设置定位选项
		CldLocationOption option = new CldLocationOption();
		option.setLocationMode(locationMode);// 设置定位模式
		option.setNetworkScanSpan(spanMs);// 定位扫描时间
		locationManager.setLocOption(option);
		locationManager.registerLocationListener(new ICldLocationListener() {

			@Override
			public void onReceiveLocation(CldLocation location) {
				if (null != location) {
					double altitude = location.getAltitude();
					// 定位到的经纬度
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					float accuracy = location.getAccuracy();
					float bearing = location.getBearing();
					float speed = location.getSpeed();
					long time = location.getTime();
					String addr = location.getAddress();
					String adCode = location.getAdCode();
					String dist = location.getDistrict();
					String city = location.getCity();
					String cityCode = location.getCityCode();
					String province = location.getProvince();
					String locInfo = "lat:" + latitude + ",lon:" + longitude
							+ "alt:" + altitude + ",acc:" + accuracy + ",bear:"
							+ bearing + ",spd:" + speed + ",time:" + time
							+ ",provice:" + province + "city:" + city
							+ ",code:" + cityCode + ",dist:" + dist + ",addr:"
							+ addr + ",adcode:" + adCode;
					CldLog.i("location", locInfo);
					mMapView.getMap().setNMapCenter(
							new LatLng(location.getLatitude(), location
									.getLongitude()));

				}
			}
		});
		locationManager.start();
	}

	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		// 关闭定位
		if (locationManager != null) {
			locationManager.stop();
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

}
