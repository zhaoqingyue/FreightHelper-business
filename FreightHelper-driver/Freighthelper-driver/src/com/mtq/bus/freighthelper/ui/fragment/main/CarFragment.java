/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: CarFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.main
 * @Description: 车辆监控
 * @author: zhaoqy
 * @date: 2017年6月16日 下午9:55:55
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.fragment.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.CarStateCountManager;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.car.CarDetailActivity;
import com.mtq.bus.freighthelper.ui.adapter.CarStateAdapter;
import com.mtq.bus.freighthelper.ui.adapter.DropDownAdapter;
import com.mtq.bus.freighthelper.ui.customview.DropDownText;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh.OnRefreshListener;
import com.mtq.bus.freighthelper.ui.customview.ShadowDrawable;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseMainFragment;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.mtq.bus.freighthelper.utils.DropDownUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarStateCount;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class CarFragment extends BaseMainFragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener {

	public static final int PAGESIZE = 200;
	public static final String TAG = "CarFragment";

	public static final int UPDATE_LOADING = 0;
	public static final int UPDATE_NET_ABNORMAL = 1;
	public static final int UPDATE_SUCCESS = 2;
	public static final int UPDATE_FAILED = 3;
	public static final int UPDATE_EMPTY = 4;

	private View mTitleLayout;
	private TextView mTitle;
	private ImageView mDropImg;

	private TextView mOnlines;
	private TextView mOnlinesUnit;
	private TextView mMileage;
	private TextView mMileageUnit;
	private TextView mWorking;
	private TextView mHasSend;
	private TextView mIdle;
	private TextView mMaintenance;

	private TextView mEmpty;
	private LinearLayout mFailed;
	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;

	private LinearLayout mListLayout;
	private LinearLayout mSelectLayout;
	private DropDownText mCount;
	private ListViewForRefresh mListView;

	private CarStateAdapter mAdapter;
	private List<MtqCarState> mCarStates;
	private List<MtqCarState> mTotalCarStates;
	private MtqCarStateCount mCarStateCount;
	private int mCurPage = 0;
	private int mTotal = 0;

	private PopupWindow mPop;
	private DropDownAdapter mPullAdapter;
	private List<DropDown> mDropList;
	private int mShadowWidth = 0;
	private int xoff = 0;
	private boolean mHasPull = false;
	private int mCurGroupId = 0;
	private boolean mIsLoading = false;

	public static CarFragment newInstance() {
		Bundle args = new Bundle();
		CarFragment fragment = new CarFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_car, container,
				false);
		initViews(view);
		setListener(view);
		initData();
		loadData();
		return view;
	}

	private void initViews(View view) {
		mTitleLayout = view.findViewById(R.id.car_title_layout);
		mTitle = (TextView) view.findViewById(R.id.car_title);
		mDropImg = (ImageView) view.findViewById(R.id.car_title_img);

		mOnlines = (TextView) view.findViewById(R.id.car_onlines);
		mOnlinesUnit = (TextView) view.findViewById(R.id.car_onlines_unit);
		mMileage = (TextView) view.findViewById(R.id.car_mileage);
		mMileageUnit = (TextView) view.findViewById(R.id.car_mileage_unit);
		mWorking = (TextView) view.findViewById(R.id.car_type_work);
		mHasSend = (TextView) view.findViewById(R.id.car_type_has_sent);
		mIdle = (TextView) view.findViewById(R.id.car_type_idle);
		mMaintenance = (TextView) view.findViewById(R.id.car_type_maintenance);

		mEmpty = (TextView) view.findViewById(R.id.car_empty);
		mFailed = (LinearLayout) view.findViewById(R.id.car_failed);
		mNetAbnormal = (LinearLayout) view.findViewById(R.id.car_net_abnormal);
		mLoading = (LinearLayout) view.findViewById(R.id.car_laoding);
		mListLayout = (LinearLayout) view.findViewById(R.id.car_list_layout);
		mSelectLayout = (LinearLayout) view.findViewById(R.id.car_select_layout);
		mCount = (DropDownText) view.findViewById(R.id.car_count);
		mListView = (ListViewForRefresh) view.findViewById(R.id.car_listview);
	}

	private void setListener(View view) {
		mDropImg.setOnClickListener(this);
		view.findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		view.findViewById(R.id.failed_refresh).setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mWorking.setOnClickListener(this);
		mHasSend.setOnClickListener(this);
		mIdle.setOnClickListener(this);
		mMaintenance.setOnClickListener(this);
	}

	private void initData() {
		mTitle.setText(R.string.car_all_group);
		mTotalCarStates = new ArrayList<MtqCarState>();
		mCarStates = new ArrayList<MtqCarState>();
		mAdapter = new CarStateAdapter(getActivity(), mCarStates);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mEmpty);
		mIsLoading = true;

		mDropList = DropDownUtils.getMyCarDrop();
		mPullAdapter = new DropDownAdapter(getActivity(), mDropList);
		mShadowWidth = DensityUtils.getDedaultSize(getActivity());
		xoff = (int) (DensityUtils.getWidth(getActivity()) * 0.60) / 2;

		/**
		 * 开始轮询车辆状态统计
		 */
		CarStateCountManager.getInstance().init();
	}

	private void loadData() {
		if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) {
			mEmpty.setText("当前账号无车辆监控权限");
			update(UPDATE_EMPTY);
			return;
		}

		if (CldPhoneNet.isNetConnected()) {
			mCurPage = 1;
			getCarStateList(mCurGroupId, mCurPage);
		} else {
			update(UPDATE_NET_ABNORMAL);
		}
	}

	@Override
	public void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			if (mLoading != null && mIsLoading) {
				mNetAbnormal.setVisibility(View.VISIBLE);
				mLoading.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.car_title_img: {
			if (!mHasPull) {
				mHasPull = true;
				mDropImg.setImageResource(R.drawable.icon_pull_up);
				pull();
			}
			break;
		}
		case R.id.car_type_work: {
			if (mTotalCarStates != null && !mTotalCarStates.isEmpty()) {
				boolean selected = mWorking.isSelected();
				mWorking.setSelected(!selected);
				clickCarType();
			}
			break;
		}
		case R.id.car_type_has_sent: {
			if (mTotalCarStates != null && !mTotalCarStates.isEmpty()) {
				boolean selected = mHasSend.isSelected();
				mHasSend.setSelected(!selected);
				clickCarType();
			}
			break;
		}
		case R.id.car_type_idle: {
			if (mTotalCarStates != null && !mTotalCarStates.isEmpty()) {
				boolean selected = mIdle.isSelected();
				mIdle.setSelected(!selected);
				clickCarType();
			}
			break;
		}
		case R.id.car_type_maintenance: {
			if (mTotalCarStates != null && !mTotalCarStates.isEmpty()) {
				boolean selected = mMaintenance.isSelected();
				mMaintenance.setSelected(!selected);
				clickCarType();
			}
			break;
		}
		case R.id.net_abnormal_refresh:
		case R.id.failed_refresh: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(getActivity(), R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				update(UPDATE_LOADING);
				loadData();
			}
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void pull() {
		if (mPop == null) {
			ListView listView = new ListView(getActivity());
			listView.setCacheColorHint(0x00000000);
			listView.setBackgroundColor(getResources().getColor(R.color.white));
			Drawable drawable = getResources().getDrawable(
					R.drawable.divider_bg);
			listView.setDivider(drawable);
			listView.setDividerHeight(1);
			listView.setAdapter(mPullAdapter);
			ShadowDrawable.Builder.on(listView)
					.bgColor(getResources().getColor(R.color.white))
					.shadowColor(Color.parseColor("#000000"))
					.shadowRange(mShadowWidth).offsetBottom(mShadowWidth)
					.offsetTop(mShadowWidth).offsetLeft(mShadowWidth)
					.offsetRight(mShadowWidth).create();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mPop.dismiss();
					mHasPull = false;
					mDropImg.setImageResource(R.drawable.icon_pull_down);
					filterByGroupId(mDropList.get(position).id);
					mTitle.setText(mDropList.get(position).name);
				}
			});
			int width = (int) (DensityUtils.getWidth(getActivity()) * 0.40)
					+ mShadowWidth * 2;
			/**
			 * 车队个数大于4，则最多显示4个
			 */
			int height = 0;
			if (mDropList.size() >= 4) {
				height = (int) (DensityUtils.getHeight(getActivity()) * 0.08)
						* 4 + mShadowWidth * 2;
			} else {
				height = (int) (DensityUtils.getHeight(getActivity()) * 0.08)
						+ mShadowWidth * 2;
			}
			mPop = new PopupWindow(listView, width, height, true);
			mPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			mPop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mPop.dismiss();
					mHasPull = false;
					mDropImg.setImageResource(R.drawable.icon_pull_down);
				}
			});
		}
		mPop.showAsDropDown(mTitleLayout, xoff - mShadowWidth, -mShadowWidth);
	}

	private void clickCarType() {
		String str = getResources().getString(R.string.tip_refreshing);
		ProgressDialog.showProgress(getActivity(), str, null);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				updateCarType();
			}
		}, 500);
	}

	private void updateCarType() {
		if (mTotalCarStates != null && !mTotalCarStates.isEmpty()) {
			List<MtqCarState> temp = getSortedStates(mTotalCarStates);
			mCarStates.clear();
			mCarStates.addAll(temp);
			mAdapter.notifyDataSetChanged();
			updateStateList();
		}
		if (ProgressDialog.isShowProgress()) {
			ProgressDialog.cancelProgress();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mCarStates != null && !mCarStates.isEmpty()) {
			int headerCount = mListView.getHeaderViewsCount();
			int newPosition = position - headerCount;
			MtqCarState carState = mCarStates.get(newPosition);
			Bundle bundle = new Bundle();
			Intent intent = new Intent(getActivity(), CarDetailActivity.class);
			bundle.putSerializable(ConstantUtils.MTQ_CAR_STATE, carState);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onRefresh() {
		if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无车辆监控权限");
			update(UPDATE_EMPTY);
			return;
		}
		
		if (!CldPhoneNet.isNetConnected()) {
			mListView.onRefreshFinish();
			/**
			 * 如果mTotalCarStates为空，则直接显示"网络连接失败"
			 * modify 2017-08-03
			 */
			if (mTotalCarStates.isEmpty()) {
				update(UPDATE_NET_ABNORMAL);
			} else {
				Toast.makeText(getActivity(), R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			/**
			 * 如果mTotalCarStates为空, 则重新获取车辆状态
			 * modify 2017-08-03
			 */
			if (mTotalCarStates.isEmpty()) {
				update(UPDATE_LOADING);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						getCarStateList(mCurGroupId, mCurPage);
					}
				}, 500);
				return;
			}
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					int size = mTotalCarStates.size();
					if (size < mTotal) {
						mCurPage++;
						getCarStateList(mCurGroupId, mCurPage);
					} else {
						/**
						 * 当前个数等于返回个数
						 */
						if (mCurPage == 1) {
							onRefreshMore(mCurGroupId, mCurPage);
						} else if (mCurPage > 1) {
							mCurPage++;
							onRefreshMore(mCurGroupId, mCurPage);
						}
					}
				}
			}, 500);
		}
	}

	@Override
	public void onLoadMoring() {
		if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无车辆监控权限");
			update(UPDATE_EMPTY);
			return;
		}
		
		if (!CldPhoneNet.isNetConnected()) {
			mListView.onRefreshFinish();
			Toast.makeText(getActivity(), R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mListView.onRefreshFinish();
					Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT)
							.show();
				}
			}, 500);
		}
	}

	/**
	 * 车辆状态列表
	 */
	private void getCarStateList(int group_id, int pageindex) {
		/**
		 * 重新刷新统计
		 */
		CarStateCountManager.getInstance().reInit();
		
		DeliveryBusAPI.getInstance().getCarStateList(group_id, pageindex,
				new IMtqCarStateListener() {

					@Override
					public void onResult(int errCode, List<MtqCarState> data,
							int total) {
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
							mTotal = total;
							mListView.onRefreshFinish();
							if (data != null && !data.isEmpty()) {
								if (mCurPage > 1) {
									Toast.makeText(getActivity(), "刷新成功",
											Toast.LENGTH_SHORT).show();
									mListView.onRefreshFinish();
								} else {
									mTotalCarStates.clear();
									mCarStates.clear();
								}
								mTotalCarStates.addAll(0, data);
								List<MtqCarState> temp = getSortedStates(data);
								mCarStates.addAll(0, temp);
								mAdapter.notifyDataSetChanged();
								updateStateList();
							} else {
								if (mCurPage > 1) {
									Toast.makeText(getActivity(), "刷新成功",
											Toast.LENGTH_SHORT).show();
									mCurPage--;
									mListView.onRefreshFinish();
								} else {
									mTotalCarStates.clear();
									mCarStates.clear();
									mAdapter.notifyDataSetChanged();
									update(UPDATE_EMPTY);
								}
							}
						} else {
							mListView.onRefreshFinish();
							if (mCurPage > 1) {
								Toast.makeText(getActivity(), "刷新失败",
										Toast.LENGTH_SHORT).show();
								mCurPage--;
								mListView.onRefreshFinish();
							} else {
								mTotalCarStates.clear();
								mCarStates.clear();
								mAdapter.notifyDataSetChanged();
								update(UPDATE_FAILED);
							}
						}
					}
				});
	}

	protected void onRefreshMore(int group_id, int pageindex) {
		/**
		 * 重新刷新统计
		 */
		CarStateCountManager.getInstance().reInit();
		
		DeliveryBusAPI.getInstance().getCarStateList(group_id, pageindex,
				new IMtqCarStateListener() {

					@Override
					public void onResult(int errCode, List<MtqCarState> data,
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
							mTotal = total;
							Toast.makeText(getActivity(), "刷新成功",
									Toast.LENGTH_SHORT).show();
							mListView.onRefreshFinish();
							if (data != null && !data.isEmpty()) {
								if (mCurPage == 1) {
									mTotalCarStates.clear();
									mCarStates.clear();
								}
								mTotalCarStates.addAll(0, data);
								List<MtqCarState> temp = getSortedStates(data);
								mCarStates.addAll(0, temp);
								mAdapter.notifyDataSetChanged();
								updateStateList();
							} else {
								if (mCurPage > 1) {
									mCurPage--;
								}
							}
						} else {
							Toast.makeText(getActivity(), "刷新失败",
									Toast.LENGTH_SHORT).show();
							mListView.onRefreshFinish();
							if (mCurPage > 1) {
								mCurPage--;
							}
						}
					}
				});
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_GET_CARSTATE_COUNT_SUCCESS: {
			updateStateCount();
			break;
		}
		case MsgId.MSGID_GET_CARSTATE_COUNT_FAILED: {
			break;
		}
		default:
			break;
		}
	}

	private void updateStateCount() {
		mCarStateCount = CarStateCountManager.getInstance()
				.getMtqCarStateCount();
		if (mCarStateCount != null) {
			if (mCarStateCount.onlines > 0) {
				mOnlines.setText(mCarStateCount.onlines + "");
				mOnlinesUnit.setVisibility(View.VISIBLE);
			} else {
				mOnlines.setText("-");
				mOnlinesUnit.setVisibility(View.INVISIBLE);
			}

			if (mCarStateCount.mileage > 10000) {
				/**
				 * 大于1万公里
				 */
				String mileage = "";
				DecimalFormat df = new DecimalFormat("0.00");
				mileage = df.format(mCarStateCount.mileage / 10000);
				mMileage.setText(mileage);
				mMileageUnit.setText("万公里");
				mMileageUnit.setVisibility(View.VISIBLE);
			} else if (mCarStateCount.mileage > 0) {
				mMileage.setText(Math.round(mCarStateCount.mileage) + "");
				mMileageUnit.setText("公里");
				mMileageUnit.setVisibility(View.VISIBLE);
			} else {
				mMileage.setText("-");
				mMileageUnit.setVisibility(View.INVISIBLE);
			}
		} else {
			mOnlines.setText("-");
			mOnlinesUnit.setVisibility(View.INVISIBLE);
			mMileage.setText("-");
			mMileageUnit.setVisibility(View.INVISIBLE);
		}
	}

	private void updateStateList() {
		String countHint = getResources().getString(R.string.car_list_count);
		String count = String.format(countHint, mCarStates.size());
		mCount.setTitle(count);
		update(UPDATE_SUCCESS);
	}

	protected void update(int type) {
		switch (type) {
		case UPDATE_LOADING: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.VISIBLE);
			mListLayout.setVisibility(View.GONE);
			break;
		}
		case UPDATE_NET_ABNORMAL: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mListLayout.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_SUCCESS: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mListLayout.setVisibility(View.VISIBLE);
			mSelectLayout.setVisibility(View.VISIBLE);
			mCount.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mIsLoading = false;
			break;
		}
		case UPDATE_FAILED: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.VISIBLE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mListLayout.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_EMPTY: {
			mEmpty.setVisibility(View.VISIBLE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mListLayout.setVisibility(View.VISIBLE);
			mSelectLayout.setVisibility(View.GONE);
			mCount.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mIsLoading = false;
			break;
		}
		default:
			break;
		}
	}

	private List<MtqCarState> getSortedStates(List<MtqCarState> carStates) {
		List<MtqCarState> temp = getSelectedStates(carStates);
		/**
		 * 排序
		 */
		if (!temp.isEmpty()) {
			List<MtqCarState> onlines = new ArrayList<MtqCarState>();
			List<MtqCarState> offlines = new ArrayList<MtqCarState>();
			for (MtqCarState car : temp) {
				if (TimeUtils.isOnLine(car.gpstime)) {
					onlines.add(car);
				} else {
					offlines.add(car);
				}
			}

			if (!onlines.isEmpty()) {
				Collections.sort(onlines, new MileageComparator());
			}

			if (!offlines.isEmpty()) {
				Collections.sort(offlines, new MileageComparator());
			}

			temp.clear();
			temp.addAll(onlines);
			temp.addAll(offlines);
		}
		return temp;
	}
	
	private List<MtqCarState> getSelectedStates(List<MtqCarState> carStates) {
		List<MtqCarState> temp = new ArrayList<MtqCarState>();
		/**
		 * 全选
		 */
		if (mWorking.isSelected() && mHasSend.isSelected()
				&& mIdle.isSelected() && mMaintenance.isSelected()) {
			temp.addAll(carStates);
			return temp;
		}
		
		/**
		 * 全未选
		 */
		if (!mWorking.isSelected() && !mHasSend.isSelected()
				&& !mIdle.isSelected() && !mMaintenance.isSelected()) {
			temp.addAll(carStates);
			return temp;
		}
		
		int len = carStates.size();
		for (int i = 0; i < len; i++) {
			MtqCarState carState = carStates.get(i);
			int carstatus = carState.carstatus;
			/**
			 * 作业中
			 */
			if (mWorking.isSelected() && carstatus == 3) {
				temp.add(carState);
			}
			/**
			 * 已派车
			 */
			if (mHasSend.isSelected() && carstatus == 2) {
				temp.add(carState);
			}
			/**
			 * 空闲
			 */
			if (mIdle.isSelected() && carstatus == 1) {
				temp.add(carState);
			}
			/**
			 * 维修保养
			 */
			if (mMaintenance.isSelected() && carstatus == 20) {
				temp.add(carState);
			}
		}
		return temp;
	}

	/**
	 * 按里程降序排序
	 */
	private class MileageComparator implements Comparator<Object> {

		@SuppressLint("UseValueOf")
		@Override
		public int compare(Object object1, Object object2) {
			MtqCarState p1 = (MtqCarState) object1;
			MtqCarState p2 = (MtqCarState) object2;
			return new Integer(p2.mileage).compareTo(new Integer(p1.mileage));
		}
	}

	protected void filterByGroupId(int groupid) {
		if (groupid != mCurGroupId) {
			mCurGroupId = groupid;
			CarStateCountManager.getInstance().setGroupId(mCurGroupId);
			mCurPage = 1;
			/**
			 * 无车辆监控权限, 则不处理
			 */
			if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) {
				return;
			}
			String str = getResources().getString(R.string.tip_loading2);
			ProgressDialog.showProgress(getActivity(), str, null);

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					getCarStateList(mCurGroupId, mCurPage);
				}
			}, 500);
		}
	}
}
