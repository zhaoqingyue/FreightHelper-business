/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AlarmFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.msg
 * @Description: 警情消息
 * @author: zhaoqy
 * @date: 2017年6月16日 下午11:41:37
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.fragment.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.db.MsgAlarmTable;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.msg.AlarmMsgActivity;
import com.mtq.bus.freighthelper.ui.adapter.MsgAlarmAdapter;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh.OnRefreshListener;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseFragment;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.MsgUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqAlarmMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqHistoryAlarmMsgListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AlarmFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener {

	public static final String TAG = "AlarmFragment";
	public static final int REQUEST_ALARM_MSG = 1;
	public static final int PAGESIZE = 200;

	public static final int UPDATE_LOADING = 0;
	public static final int UPDATE_NET_ABNORMAL = 1;
	public static final int UPDATE_SUCCESS = 2;
	public static final int UPDATE_FAILED = 3;
	public static final int UPDATE_EMPTY = 4;

	private TextView mEmpty;
	private LinearLayout mFailed;
	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mSelectLayout;
	private ListViewForRefresh mListView;

	private TextView mCar;
	private TextView mDevice;
	private TextView mBehavior;
	private MsgAlarmAdapter mAdapter;
	private List<MtqMsgAlarm> mTotalMsgs;
	private List<MtqMsgAlarm> mMsgAlarms;
	public String mIncrindex = "";
	private int mCurPosition = -1;
	private IUpdateMsgCount mListener;
	private long mStartTime;
	private long mEndTime;
	private boolean mIsLoading = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_msg_alarm;
	}

	@Override
	protected void initViews(View view) {
		mEmpty = (TextView) view.findViewById(R.id.msg_alarm_default);
		mFailed = (LinearLayout) view.findViewById(R.id.msg_alarm_failed);
		mNetAbnormal = (LinearLayout) view
				.findViewById(R.id.msg_alarm_net_abnormal);
		mLoading = (LinearLayout) view.findViewById(R.id.msg_alarm_laoding);
		mSelectLayout = (LinearLayout) view.findViewById(R.id.msg_alarm_select_layout);
		mCar = (TextView) view.findViewById(R.id.msg_alarm_car);
		mDevice = (TextView) view.findViewById(R.id.msg_alarm_device);
		mBehavior = (TextView) view.findViewById(R.id.msg_alarm_behavior);
		mListView = (ListViewForRefresh) view
				.findViewById(R.id.msg_alarm_listview);
	}

	@Override
	protected void setListener(View view) {
		view.findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		view.findViewById(R.id.failed_refresh).setOnClickListener(this);
		mCar.setOnClickListener(this);
		mDevice.setOnClickListener(this);
		mBehavior.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
	}

	@Override
	protected void initData() {
		mTotalMsgs = new ArrayList<MtqMsgAlarm>();
		mMsgAlarms = new ArrayList<MtqMsgAlarm>();
		// mMsgAlarms = TestUtils.getMsgAlarm();
		mAdapter = new MsgAlarmAdapter(getActivity(), mMsgAlarms);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mEmpty);
		mIsLoading = true;
	}

	@Override
	protected void loadData() {
		if (!DeliveryBusAPI.getInstance().hasAlarmMsgPermission()) {
			mEmpty.setText("当前账号无警情消息权限");
			update(UPDATE_EMPTY);
			return;
		}

		if (CldPhoneNet.isNetConnected()) {
			/**
			 * 防止该时间段内没有消息，将开始时间设置成10天前
			 */
			mStartTime = TimeUtils.getStartTimeForStamp(10);
			mEndTime = TimeUtils.getEndTimeForStamp();
			getHistoryAlarmMsg();
		} else {
			update(UPDATE_NET_ABNORMAL);
		}
	}

	@Override
	protected void updateUI() {

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
		case R.id.msg_alarm_car: {
			if (mTotalMsgs != null && !mTotalMsgs.isEmpty()) {
				boolean selected = mCar.isSelected();
				mCar.setSelected(!selected);
				clickMsgType();
			}
			break;
		}
		case R.id.msg_alarm_device: {
			if (mTotalMsgs != null && !mTotalMsgs.isEmpty()) {
				boolean selected = mDevice.isSelected();
				mDevice.setSelected(!selected);
				clickMsgType();
			}
			break;
		}
		case R.id.msg_alarm_behavior: {
			if (mTotalMsgs != null && !mTotalMsgs.isEmpty()) {
				boolean selected = mBehavior.isSelected();
				mBehavior.setSelected(!selected);
				clickMsgType();
			}
			break;
		}
		default:
			break;
		}
	}

	private void clickMsgType() {
		String str = getResources().getString(R.string.tip_refreshing);
		ProgressDialog.showProgress(getActivity(), str, null);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				updateMsgType();
			}
		}, 500);
	}

	protected void updateMsgType() {
		if (mTotalMsgs != null && !mTotalMsgs.isEmpty()) {
			List<MtqMsgAlarm> temp = getTempMsgs(mTotalMsgs);
			mMsgAlarms.clear();
			mMsgAlarms.addAll(temp);
			mAdapter.notifyDataSetChanged();
		}
		if (ProgressDialog.isShowProgress()) {
			ProgressDialog.cancelProgress();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int headerCount = mListView.getHeaderViewsCount();
		int newPosition = position - headerCount;
		mCurPosition = newPosition;
		Bundle bundle = new Bundle();
		MtqMsgAlarm msgAlarm = mMsgAlarms.get(mCurPosition);
		Intent intent = new Intent(getActivity(), AlarmMsgActivity.class);
		bundle.putSerializable(ConstantUtils.MTQ_MSG_ALARM, msgAlarm);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUEST_ALARM_MSG);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == REQUEST_ALARM_MSG) {
				mMsgAlarms.get(mCurPosition).readstatus = 1;
				mAdapter.updateView(mListView, mCurPosition);
				MsgAlarmTable.getInstance()
						.update(mMsgAlarms.get(mCurPosition));
				/**
				 * 更新未读数量
				 */
				if (mListener != null) {
					mListener.onUpdateMsgCount();
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		if (!DeliveryBusAPI.getInstance().hasAlarmMsgPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无警情消息权限");
			update(UPDATE_EMPTY);
			return;
		}
		
		if (!CldPhoneNet.isNetConnected()) {
			mListView.onRefreshFinish();
			/**
			 * 如果mMsgAlarms为空，则直接显示"网络连接失败"
			 * modify 2017-08-03
			 */
			if (mMsgAlarms.isEmpty()) {
				update(UPDATE_NET_ABNORMAL);
			} else {
				Toast.makeText(getActivity(), R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			if (!TextUtils.isEmpty(mIncrindex)) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						pullLatestAlarmMsg();
					}
				}, 500);
			} else {
				/**
				 * mIncrindex 为空，且mMsgAlarms为空, 则重新获取警情系统消息
				 * modify 2017-08-03
				 */
				if (mMsgAlarms.isEmpty()) {
					update(UPDATE_LOADING);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							getHistoryAlarmMsg();
						}
					}, 500);
				} else {
					mListView.onRefreshFinish();
					Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	@Override
	public void onLoadMoring() {
		if (!DeliveryBusAPI.getInstance().hasAlarmMsgPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无警情消息权限");
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
					int len = mMsgAlarms.size();
					/**
					 * 结束时间：与最后一条消息的时间 间隔100ms(自己定的)
					 * 
					 * 开始时间：结束时间往前推7天
					 */
					long endtime = mMsgAlarms.get(len - 1).locattime - 100;
					long starttime = endtime - TimeUtils.SECONDS_IN_DAY * 7;
					mStartTime = starttime;
					mEndTime = endtime;
					getNextHistoryPage();
				}
			}, 500);
		}
	}

	/**
	 * 下拉最新的系统消息 每次下拉10条
	 */
	private void pullLatestAlarmMsg() {
		DeliveryBusAPI.getInstance().getAlarmMsg(mIncrindex, 10,
				new IMtqAlarmMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgAlarm> data,
							String incrindex) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							if (!TextUtils.isEmpty(incrindex)
									&& !incrindex.equals("0")) {
								mIncrindex = incrindex;
							}
							if (data != null && !data.isEmpty()) {
								/**
								 * 增量刷新，返回的数据是根据时间的升序排列的，终端把它改成降序展示 add by
								 * zhaoqy 2017-07-17
								 */
								Collections.reverse(data);

								List<MtqMsgAlarm> handledMsg = getHandledMsg(data);
								mTotalMsgs.addAll(0, handledMsg);
								List<MtqMsgAlarm> temp = getTempMsgs(handledMsg);
								if (!temp.isEmpty()) {
									mMsgAlarms.addAll(0, temp);
									mAdapter.notifyDataSetChanged();
								}
								MsgAlarmTable.getInstance().insert(handledMsg);
								/**
								 * 更新未读数量
								 */
								if (mListener != null) {
									mListener.onUpdateMsgCount();
								}
							}
							mListView.onRefreshFinish();
							Toast.makeText(getActivity(), "刷新成功",
									Toast.LENGTH_SHORT).show();
						} else {
							mListView.onRefreshFinish();
							Toast.makeText(getActivity(), "刷新失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * 获取历史警情消息
	 */
	private void getHistoryAlarmMsg() {
		DeliveryBusAPI.getInstance().getHistoryAlarmMsg(mStartTime, mEndTime,
				1, PAGESIZE, 0, new IMtqHistoryAlarmMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgAlarm> data,
							String incrindex) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							if (!TextUtils.isEmpty(incrindex)
									&& !incrindex.equals("0")) {
								mIncrindex = incrindex;
							}
							if (data != null && !data.isEmpty()) {
								mListView.onRefreshFinish();
								List<MtqMsgAlarm> handledMsg = getHandledMsg(data);
								mTotalMsgs.addAll(handledMsg);
								List<MtqMsgAlarm> temp = getTempMsgs(handledMsg);
								if (!temp.isEmpty()) {
									mMsgAlarms.addAll(temp);
									mAdapter.notifyDataSetChanged();
								}

								update(UPDATE_SUCCESS);
								MsgAlarmTable.getInstance().insert(handledMsg);
								/**
								 * 更新未读数量
								 */
								if (mListener != null) {
									mListener.onUpdateMsgCount();
								}
							} else {
								mListView.onRefreshFinish();
								update(UPDATE_EMPTY);
							}
						} else {
							mListView.onRefreshFinish();
							update(UPDATE_FAILED);
						}
					}
				});
	}

	/**
	 * 获取下一页历史系统消息
	 */
	private void getNextHistoryPage() {
		DeliveryBusAPI.getInstance().getHistoryAlarmMsg(mStartTime, mEndTime,
				1, PAGESIZE, 0, new IMtqHistoryAlarmMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgAlarm> data,
							String incrindex) {
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
								mListView.onRefreshFinish();
								List<MtqMsgAlarm> handledMsg = getHandledMsg(data);
								mTotalMsgs.addAll(handledMsg);
								List<MtqMsgAlarm> temp = getTempMsgs(handledMsg);
								if (!temp.isEmpty()) {
									Toast.makeText(getActivity(), "加载成功",
											Toast.LENGTH_SHORT).show();
									mMsgAlarms.addAll(temp);
									mAdapter.notifyDataSetChanged();
								} else {
									Toast.makeText(getActivity(), "没有更多了",
											Toast.LENGTH_SHORT).show();
								}
								MsgAlarmTable.getInstance().insert(handledMsg);
								/**
								 * 更新未读数量
								 */
								if (mListener != null) {
									mListener.onUpdateMsgCount();
								}
							} else {
								mListView.onRefreshFinish();
								Toast.makeText(getActivity(), "没有更多了",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							mListView.onRefreshFinish();
							Toast.makeText(getActivity(), "加载失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_BATCH_SET_ALARM_MSG_READ: {
			String id = "";
			int len = mMsgAlarms.size();
			/**
			 * add 2017-08-18
			 */
			if (len > 0) {
				List<MtqMsgAlarm> temp = new ArrayList<MtqMsgAlarm>();
				for (int i = 0; i < len; i++) {
					if (mMsgAlarms.get(i).readstatus == 0) {
						temp.add(mMsgAlarms.get(i));
					}
				}
				
				if (!temp.isEmpty()) {
					int temp_len = temp.size();
					for (int i = 0; i < temp_len; i++) {
						id += temp.get(i).id;
						if (i < temp_len - 1) {
							id += ";";
						}
					}
				}
			}
			
			boolean debug = false;
			if (debug) {
				for (int i = 0; i < len; i++) {
					if (mMsgAlarms.get(i).readstatus == 0) {
						id += mMsgAlarms.get(i).id;
						/**
						 * 这里判断i < len - 1 是不妥的
						 */
						if (i < len - 1) {
							id += ";";
						}
					}
				}
			}
			
			if (!TextUtils.isEmpty(id)) {
				DeliveryBusAPI.getInstance().batchSetMsgAlarmRead(id);
			}
			break;
		}
		case MsgId.MSGID_BATCH_SET_ALARM_MSG_READ_SUCCESS: {
			int len = mMsgAlarms.size();
			for (int i = 0; i < len; i++) {
				if (mMsgAlarms.get(i).readstatus == 0) {
					mMsgAlarms.get(i).readstatus = 1;
				}
			}
			mAdapter.notifyDataSetChanged();
			MsgAlarmTable.getInstance().update(mMsgAlarms);
			/**
			 * 更新未读数量
			 */
			if (mListener != null) {
				mListener.onUpdateMsgCount();
			}
			break;
		}
		case MsgId.MSGID_BATCH_SET_ALARM_MSG_READ_FAILED: {
			break;
		}
		default:
			break;
		}
	}

	protected void update(int type) {
		switch (type) {
		case UPDATE_LOADING: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.VISIBLE);
			mSelectLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			break;
		}
		case UPDATE_NET_ABNORMAL: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mSelectLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_SUCCESS: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mSelectLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mIsLoading = false;
			break;
		}
		case UPDATE_FAILED: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.VISIBLE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mSelectLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_EMPTY: {
			mEmpty.setVisibility(View.VISIBLE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mSelectLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mIsLoading = false;
			break;
		}
		default:
			break;
		}
	}

	protected List<MtqMsgAlarm> getHandledMsg(List<MtqMsgAlarm> data) {
		List<MtqMsgAlarm> temp = new ArrayList<MtqMsgAlarm>();
		int len = data.size();
		for (int i = 0; i < len; i++) {
			MtqMsgAlarm item = data.get(i);
			if (MsgUtils.isHandledMsg(item.alarmid)) {
				int alarmType = MsgUtils.getAlarmClassify(item.alarmid);
				item.alarmType = alarmType;
				temp.add(item);
			}
		}
		return temp;
	}

	private List<MtqMsgAlarm> getTempMsgs(List<MtqMsgAlarm> tempMsgs) {
		List<MtqMsgAlarm> temp = new ArrayList<MtqMsgAlarm>();
		/**
		 * 全选
		 */
		if (mCar.isSelected() && mDevice.isSelected() && mBehavior.isSelected()) {
			temp.addAll(tempMsgs);
			return temp;
		}
		/**
		 * 全未选
		 */
		if (!mCar.isSelected() && !mDevice.isSelected()
				&& !mBehavior.isSelected()) {
			temp.addAll(tempMsgs);
			return temp;
		}

		int len = tempMsgs.size();
		for (int i = 0; i < len; i++) {
			MtqMsgAlarm msg = tempMsgs.get(i);
			int alarmType = msg.alarmType;
			/**
			 * 车况
			 */
			if (mCar.isSelected() && alarmType == MsgUtils.ALARM_TYPE_CAR) {
				temp.add(msg);
			}
			/**
			 * 设备
			 */
			if (mDevice.isSelected() && alarmType == MsgUtils.ALARM_TYPE_DEVICE) {
				temp.add(msg);
			}
			/**
			 * 驾驶行为
			 */
			if (mBehavior.isSelected()
					&& alarmType == MsgUtils.ALARM_TYPE_BEHAVIOR) {
				temp.add(msg);
			}
		}
		return temp;
	}

	public void pullLatestMsg() {
		/**
		 * 无警情消息权限，则不处理
		 */
		if (!DeliveryBusAPI.getInstance().hasAlarmMsgPermission()) {
			return;
		}

		if (CldPhoneNet.isNetConnected()) {
			if (TextUtils.isEmpty(mIncrindex)) {
				mListView.onRefreshFinish();
			} else {
				pullLatestAlarmMsg();
			}
		}
	}

	public void setUpdateMsgCountListener(IUpdateMsgCount listener) {
		mListener = listener;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
}
