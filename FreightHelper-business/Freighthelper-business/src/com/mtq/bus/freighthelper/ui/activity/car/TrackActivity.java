/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: TrackActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.car
 * @Description: 轨迹回放
 * @author: zhaoqy
 * @date: 2017年6月17日 下午5:28:53
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.car;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.cld.mapapi.map.MapView;
import com.cld.mapapi.map.MarkerOptions;
import com.cld.mapapi.map.OverlayOptions;
import com.cld.mapapi.map.PolyLineOptions;
import com.cld.mapapi.model.LatLng;
import com.cld.nv.map.overlay.Overlay;
import com.cld.nv.map.overlay.impl.MapPolyLine;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.api.map.MapViewAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.activity.me.SelectTimeActivity;
import com.mtq.bus.freighthelper.ui.customview.ShadowDrawable;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTrackHistoryListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqPosData;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTrackHistory;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class TrackActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "TrackActivity";
	public static final int REQUEST_TRACK = 2;
	private ImageView mBack;
	private TextView mTitle;
	private FrameLayout mViewGroup;
	private MapView mMapView;

	private View mNoData;
	private View mDetail;
	private TextView mMileage;
	private TextView mMaxSpeed;
	private TextView mAlarmNum;
	//private TextView mCurSpeed;

	private TextView mStart;
	private TextView mEnd;
	private LinearLayout mSpeedLayout;
	private TextView mSpeedOne;
	private TextView mSpeedTwo;
	private TextView mSpeedThree;
	private TextView mSpeedFour;
	private ImageView mPlayImg;
	private TextView mPlayText;
	private ProgressBar mPlayBar;

	private List<MtqTrackHistory> mTrackList;
	private List<MtqPosData> mPosDataList;
	private long mCurStart = 0;
	private long mCurEnd = 0;
	private int mCarId = 0;
	private int mSpeedRate = 1;
	private boolean mIsPlay = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_track;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mViewGroup = (FrameLayout) findViewById(R.id.track_mapview);

		mNoData = findViewById(R.id.track_no_data);
		mDetail = findViewById(R.id.track_detail);
		mMileage = (TextView) findViewById(R.id.track_detail_mileage);
		mMaxSpeed = (TextView) findViewById(R.id.track_detail_max_speed);
		mAlarmNum = (TextView) findViewById(R.id.track_detail_alarm_num);
		//mCurSpeed = (TextView) findViewById(R.id.track_cur_speed);

		mStart = (TextView) findViewById(R.id.track_time_start);
		mEnd = (TextView) findViewById(R.id.track_time_end);
		mSpeedLayout = (LinearLayout) findViewById(R.id.track_speed_layout);
		mSpeedOne = (TextView) findViewById(R.id.track_speed_one);
		mSpeedTwo = (TextView) findViewById(R.id.track_speed_two);
		mSpeedThree = (TextView) findViewById(R.id.track_speed_three);
		mSpeedFour = (TextView) findViewById(R.id.track_speed_four);
		mPlayImg = (ImageView) findViewById(R.id.track_play_img);
		mPlayText = (TextView) findViewById(R.id.track_play_text);
		mPlayBar = (ProgressBar) findViewById(R.id.track_play_progressbar);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mStart.setOnClickListener(this);
		mEnd.setOnClickListener(this);
		mSpeedOne.setOnClickListener(this);
		mSpeedTwo.setOnClickListener(this);
		mSpeedThree.setOnClickListener(this);
		mSpeedFour.setOnClickListener(this);
		mPlayImg.setOnClickListener(this);
		findViewById(R.id.track_time_yesterday).setOnClickListener(this);
		findViewById(R.id.track_time_custom).setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initData() {
		mTitle.setText(R.string.car_track);
		mBack.setVisibility(View.VISIBLE);
		mSpeedOne.setSelected(true);
		mCarId = getIntent().getIntExtra("carid", 0);

		mTrackPointList = new ArrayList<LatLng>();
		mMapView = MapViewAPI.getInstance().createMapView(this);
		mMapView.getMap().setTrafficEnabled(false);
		mViewGroup.addView(mMapView);
		mTrackList = new ArrayList<MtqTrackHistory>();
		mPosDataList = new ArrayList<MtqPosData>();

		int mShadowWidth = DensityUtils.getDedaultSize(this);
		ShadowDrawable.Builder.on(mDetail)
				.bgColor(getResources().getColor(R.color.white))
				.shadowColor(Color.parseColor("#000000"))
				.shadowRange(mShadowWidth).offsetBottom(mShadowWidth)
				.offsetTop(mShadowWidth).offsetLeft(mShadowWidth)
				.offsetRight(mShadowWidth).create();
	}

	@Override
	protected void loadData() {
		/**
		 * 默认前一天，当前时间往前24小时的时间范围
		 */
		yesterday();
	}

	@Override
	protected void updateUI() {

	}

	@Override
	protected void onConnectivityChange() {

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mMapView != null) {
			mMapView.onPause();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		stopPlay();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopPlay();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//CldLog.e(TAG, "onResume");
		if (mMapView != null) {
			mMapView.onResume();
			mMapView.update();
			MapViewAPI.getInstance().setLocationIconEnabled(mMapView.getMap(),
					false);
			if (mPosDataList != null && mPosDataList.size() > 0) {
				showTrack();
			} else {
				MapViewAPI.getInstance().setZoomLevel(mMapView.getMap(), 16);
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
		case R.id.track_time_yesterday: {
			yesterday();
			break;
		}
		case R.id.track_time_custom: {
			Intent intent = new Intent(this, SelectTimeActivity.class);
			intent.putExtra(SelectTimeActivity.SELECT_TIME_FROM,
					SelectTimeActivity.SELECT_TIME_FROM_TRACK);
			startActivityForResult(intent, REQUEST_TRACK);
			break;
		}
		case R.id.track_speed_one: {
			if (!mPosDataList.isEmpty()) {
				if (!mSpeedOne.isSelected()) {
					mSpeedOne.setSelected(true);
					mSpeedTwo.setSelected(false);
					mSpeedThree.setSelected(false);
					mSpeedFour.setSelected(false);
					mSpeedRate = 1;
				}
			}
			break;
		}
		case R.id.track_speed_two: {
			if (!mPosDataList.isEmpty()) {
				if (!mSpeedTwo.isSelected()) {
					mSpeedOne.setSelected(false);
					mSpeedTwo.setSelected(true);
					mSpeedThree.setSelected(false);
					mSpeedFour.setSelected(false);
					mSpeedRate = 2;
				}
			}
			break;
		}
		case R.id.track_speed_three: {
			if (!mPosDataList.isEmpty()) {
				if (!mSpeedThree.isSelected()) {
					mSpeedOne.setSelected(false);
					mSpeedTwo.setSelected(false);
					mSpeedThree.setSelected(true);
					mSpeedFour.setSelected(false);
					mSpeedRate = 3;
				}
			}
			break;
		}
		case R.id.track_speed_four: {
			if (!mPosDataList.isEmpty()) {
				if (!mSpeedFour.isSelected()) {
					mSpeedOne.setSelected(false);
					mSpeedTwo.setSelected(false);
					mSpeedThree.setSelected(false);
					mSpeedFour.setSelected(true);
					mSpeedRate = 4;
				}
			}
			break;
		}
		case R.id.track_play_img: {
			if (!mPosDataList.isEmpty() && mPlayIndex < mPosDataList.size() - 1) {
				if (mIsPlay) {
					/**
					 * 暂停
					 */
					mHandler.removeMessages(PLAY);
					mIsPlay = false;
					mPlayImg.setImageResource(R.drawable.selector_btn_pause_bg);
					mPlayText.setText("播放");
				} else {
					/**
					 * 播放
					 */
					mHandler.sendEmptyMessage(PLAY);
					mIsPlay = true;
					mPlayImg.setImageResource(R.drawable.selector_btn_play_bg);
					mPlayText.setText("暂停");
				}
			}
			break;
		}
		default:
			break;
		}
	}

	private void yesterday() {
		String starttime = TimeUtils.getStartTimeForYmdHm(1);
		String endtime = TimeUtils.getCurTimeForYmdHm();
		CldLog.e(TAG, "starttime: " + starttime);
		CldLog.e(TAG, "endtime: " + endtime);
		mStart.setText(starttime);
		mEnd.setText(endtime);
		long start = TimeUtils.getStampFromYdhHm(starttime);
		long end = TimeUtils.getStampFromYdhHm(endtime);

		updateTrack(start, end);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_TRACK) {
				//mDetail.setVisibility(View.GONE);
				//mNoData.setVisibility(View.GONE);
				
				Bundle bundle = data.getExtras();
				String starttime = bundle.getString("starttime");
				String endtime = bundle.getString("endtime");
				CldLog.e(TAG, "starttime: " + starttime);
				CldLog.e(TAG, "endtime: " + endtime);
				long start = TimeUtils.getStampFromYdhHm(starttime);
				long end = TimeUtils.getStampFromYdhHm(endtime);
				mStart.setText(starttime);
				mEnd.setText(endtime);

				updateTrack(start, end);
			}
		}
	}

	private void updateTrack(long start, long end) {
		if (mCarId <= 0)
			return;

		if (start == mCurStart && end == mCurEnd)
			return;

		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			String str = getResources().getString(R.string.tip_loading);
			ProgressDialog.showProgress(this, str, null);
			/**
			 * 获取历史轨迹
			 */
			getTrackHistory(start, end);
		}
	}

	/**
	 * 获取历史轨迹
	 */
	private void getTrackHistory(long start, long end) {
		mCurStart = start;
		mCurEnd = end;
		mHandler.sendEmptyMessageDelayed(TIMEOUT, 60 * 1000);
		timeOut = false;
		DeliveryBusAPI.getInstance().getTrackHistory(mCarId, start, end,
				new IMtqTrackHistoryListener() {

					@Override
					public void onResult(int errCode,
							List<MtqTrackHistory> data, int alarmnum) {
						mHandler.removeMessages(TIMEOUT);
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

						mTrackList.clear();
						mPosDataList.clear();
						if (!timeOut) {
							if (errCode == 0) {
								if (data != null && !data.isEmpty()) {
									// mTrackList.clear();
									mTrackList.addAll(data);
									// mPosDataList.clear();
									for (int i = 0; i < data.size(); i++) {
										List<MtqPosData> pos_data = data.get(i).pos_data;
										mPosDataList.addAll(pos_data);
									}

									if (!mPosDataList.isEmpty()) {
										showTrack();
										update(alarmnum);
										mDetail.setVisibility(View.VISIBLE);
										mNoData.setVisibility(View.GONE);
										mPlayImg.setEnabled(true);
										updatePlayStatus();
									} else {
										mTrackList.clear();
										mDetail.setVisibility(View.GONE);
										mNoData.setVisibility(View.VISIBLE);
										mPlayImg.setEnabled(false);
										updatePlayStatus();
										/**
										 * 地图恢复成默认状态
										 */
										mMapView.getMap().removeAllOverlay();
										MapViewAPI.getInstance().setZoomLevel(
												mMapView.getMap(), 16);
									}
								} else {
									mTrackList.clear();
									mDetail.setVisibility(View.GONE);
									mNoData.setVisibility(View.VISIBLE);
									mPlayImg.setEnabled(false);
									updatePlayStatus();
									/**
									 * 地图恢复成默认状态
									 */
									mMapView.getMap().removeAllOverlay();
									MapViewAPI.getInstance().setZoomLevel(
											mMapView.getMap(), 16);
								}
							} else {
								Toast.makeText(TrackActivity.this, "加载失败",
										Toast.LENGTH_SHORT).show();
								mPlayImg.setEnabled(false);
								updatePlayStatus();
								/**
								 * 地图恢复成默认状态
								 */
								mMapView.getMap().removeAllOverlay();
								MapViewAPI.getInstance().setZoomLevel(
										mMapView.getMap(), 16);
							}
						}
					}
				});
	}

	protected void updatePlayStatus() {
		if (!mPosDataList.isEmpty()) {
			mSpeedLayout.setVisibility(View.VISIBLE);
			//mCurSpeed.setVisibility(View.VISIBLE);
			mHandler.removeMessages(PLAY);
			mIsPlay = true;
			mPlayImg.setImageResource(R.drawable.selector_btn_play_bg);
			mPlayText.setText("暂停");
			mPlayIndex = 0;
			playTrack(mPlayIndex);
			mPlayBar.setMax(mPosDataList.size() - 1);
			mPlayBar.setProgress(mPlayIndex);
		} else {
			mSpeedLayout.setVisibility(View.GONE);
			mPlayIndex = 0;
			mPlayBar.setProgress(mPlayIndex);
			//mCurSpeed.setVisibility(View.GONE);
			mIsPlay = false;
			mPlayImg.setImageResource(R.drawable.selector_btn_pause_bg);
			mPlayText.setText("播放");
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	private void update(int alarmnum) {
		if (!mTrackList.isEmpty()) {
			float mileage = 0;
			for (MtqTrackHistory track : mTrackList) {
				mileage += track.mileage;
			}
			DecimalFormat df = new DecimalFormat("0.0");
			mMileage.setText(df.format(mileage) + "");
			mMaxSpeed.setText(getMaxSpeed() + "");
			mAlarmNum.setText(alarmnum + "");
		}
	}

	private int getMaxSpeed() {
		int max_speed = 0;
		for (MtqTrackHistory track : mTrackList) {
			if (track.pos_data != null && !track.pos_data.isEmpty()) {
				for (MtqPosData pos : track.pos_data) {
					if (pos.speed > max_speed) {
						max_speed = pos.speed;
					}
				}
			}
		}
		return max_speed;
	}

	/** 轨迹点的坐标 */
	private ArrayList<LatLng> mTrackPointList;
	private MapPolyLine mTrackline;// 线条View

	private void showTrack() {
		if (!mPosDataList.isEmpty()) {
			CldLog.e(TAG, "pos_data_len: " + mPosDataList.size());
			mTrackPointList.clear();
			Iterator<MtqPosData> iter = mPosDataList.iterator();
			while (iter.hasNext()) {
				MtqPosData tmp = iter.next();
				// CldLog.e(TAG, "  ++++++++++++++  ");
				// CldLog.i(TAG, "  x: " + tmp.x);
				// CldLog.i(TAG, "  y: " + tmp.y);
				// CldLog.i(TAG, "  direction: " + tmp.direction);
				LatLng point = new LatLng(tmp.y, tmp.x);
				mTrackPointList.add(point);
			}
			/**
			 * 清除所有覆盖物
			 */
			MapViewAPI.getInstance().removeAllOverlay(mMapView.getMap());
			/**
			 * 存在覆盖物的时候,先清除
			 */
			if (mTrackline != null) {
				mMapView.getMap().removeOverlay(mTrackline);
			}

			List<OverlayOptions> options = new ArrayList<OverlayOptions>();
			OverlayOptions selectOption = null;

			int len = mTrackPointList.size();
			for (int i = 0; i < len; i++) {
				LatLng point = mTrackPointList.get(i);
				int picId = 0;
				if (i == 0) {
					//CldLog.e(TAG, "起 i: " + i);
					//CldLog.e(TAG, "起 x: " + point.longitude);
					//CldLog.e(TAG, "起 y: " + point.latitude);
					View view = LayoutInflater.from(this).inflate(
							R.layout.layout_track_point_poi, null);
					TextView txtNum = (TextView) view
							.findViewById(R.id.track_point_poi_number);
					ImageView img = (ImageView) view
							.findViewById(R.id.track_point_poi_img);

					picId = R.drawable.icon_water_select;
					txtNum.setText("起");
					img.setImageResource(picId);

					selectOption = new MarkerOptions().position(point).layout(
							view);
					options.add(selectOption);
				} else if (i == len - 1) {
					//CldLog.e(TAG, "终 i: " + i);
					//CldLog.e(TAG, "终 x: " + point.longitude);
					//CldLog.e(TAG, "终 y: " + point.latitude);
					View view = LayoutInflater.from(this).inflate(
							R.layout.layout_track_point_poi, null);
					TextView txtNum = (TextView) view
							.findViewById(R.id.track_point_poi_number);
					ImageView img = (ImageView) view
							.findViewById(R.id.track_point_poi_img);

					picId = R.drawable.icon_water_red;
					txtNum.setText("终");
					img.setImageResource(picId);

					// 选中放在最上面画，不被其他的盖住
					selectOption = new MarkerOptions().position(point).layout(
							view);
					options.add(selectOption);
				}
			}
			MapViewAPI.getInstance().addOverlay(mMapView.getMap(), options);

			// 添加覆盖物
			mTrackline = (MapPolyLine) mMapView.getMap().addOverlay(
					new PolyLineOptions().color(Color.RED)// 设置线的颜色
							.points(mTrackPointList)// 设置经过的点
							.width(10)// 设置线的宽度
					/* .dottedLine(true) */);

			mMapView.getMap().zoomToSpan(mTrackPointList);// 缩放到合适比例，将传入的点坐标都显示出来
			// MapViewAPI.getInstance().setZoomLevel(mMapView.getMap(), 3);
		}
	}

	private List<Overlay> mCurOverlay = new ArrayList<Overlay>();

	private void playTrack(int index) {
		if (!mCurOverlay.isEmpty()) {
			MapViewAPI.getInstance().removeOverlay(mMapView.getMap(),
					mCurOverlay);
		}

		List<OverlayOptions> options = new ArrayList<OverlayOptions>();
		OverlayOptions selectOption = null;
		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_track_point, null);
		ImageView img = (ImageView) view.findViewById(R.id.track_point_img);
		int picId = R.drawable.icon_track_move;
		img.setImageResource(picId);
		TextView speed = (TextView)
		view.findViewById(R.id.track_point_speed);
		speed.setText(mPosDataList.get(index).speed + "km/h");

		// 选中放在最上面画，不被其他的盖住
		LatLng latLng = getLatLng(index);
		selectOption = new MarkerOptions().position(latLng).layout(view);

		// 选中放在最上面画，不被其他的盖住
		options.add(selectOption);
		mCurOverlay.clear();
		mCurOverlay = MapViewAPI.getInstance().addOverlay(mMapView.getMap(),
				options);
		//mCurSpeed.setText(mPosDataList.get(index).speed + "km/h");
		//mCurSpeed.setVisibility(View.VISIBLE);
		mHandler.sendEmptyMessageDelayed(PLAY, 1000);
	}

	private LatLng getLatLng(int index) {
		if (!mPosDataList.isEmpty()) {
			LatLng latLng = new LatLng();
			latLng.longitude = mPosDataList.get(index).x;
			latLng.latitude = mPosDataList.get(index).y;
			return latLng;
		}
		return null;
	}
	
	private void stopPlay() {
		mHandler.removeMessages(PLAY);
		mIsPlay = false;
		mPlayImg.setImageResource(R.drawable.selector_btn_pause_bg);
		mPlayText.setText("播放");
		mPlayIndex = 0;
		mPlayBar.setProgress(mPlayIndex);
		//mCurSpeed.setVisibility(View.GONE);
		if (!mCurOverlay.isEmpty()) {
			MapViewAPI.getInstance().removeOverlay(mMapView.getMap(),
					mCurOverlay);
		}
	}

	final int TIMEOUT = 1;
	final int PLAY = 2;
	final int CLEAR = 3;

	int mPlayIndex = 0;
	boolean timeOut = false;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("DefaultLocale")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIMEOUT: {
				if (ProgressDialog.isShowProgress()) {
					ProgressDialog.cancelProgress();
				}
				timeOut = true;
				Toast.makeText(TrackActivity.this, "加载失败", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			case PLAY: {
				if (!mPosDataList.isEmpty()) {
					if (mPlayIndex < mPosDataList.size() - 1) {
						mPlayIndex += mSpeedRate;
						if (mPlayIndex >= mPosDataList.size() - 1) {
							mPlayIndex = mPosDataList.size() - 1;
						}
						playTrack(mPlayIndex);
						mPlayBar.setProgress(mPlayIndex);
					} else {
						mIsPlay = false;
						mPlayImg.setImageResource(R.drawable.selector_btn_pause_bg);
						mPlayText.setText("播放");
						mPlayIndex = 0;
						mPlayBar.setProgress(mPlayIndex);
						//mCurSpeed.setVisibility(View.GONE);
						/**
						 * 恢复初始状态
						 */
						if (!mCurOverlay.isEmpty()) {
							MapViewAPI.getInstance().removeOverlay(
									mMapView.getMap(), mCurOverlay);
						}
					}
				}
				break;
			}
			case CLEAR: {

				break;
			}
			default:
				break;
			}
		}
	};
}
