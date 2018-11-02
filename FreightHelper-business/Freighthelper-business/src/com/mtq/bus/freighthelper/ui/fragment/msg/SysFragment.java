/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: SysFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.msg
 * @Description: 系统消息
 * @author: zhaoqy
 * @date: 2017年6月19日 下午10:29:03
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
import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.db.MsgSysTable;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.msg.SysMsgActivity;
import com.mtq.bus.freighthelper.ui.adapter.MsgSysAdapter;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh;
import com.mtq.bus.freighthelper.ui.customview.ListViewForRefresh.OnRefreshListener;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseFragment;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqHistorySysMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqSysMsgListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class SysFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener {

	public static final String TAG = "SysFragment";
	public static final int REQUEST_SYS_MSG = 1;
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
	private ListViewForRefresh mListView;

	private MsgSysAdapter mAdapter;
	private List<MtqMsgSys> mMsgSyss;
	private String mIncrindex = "";
	private int mCurPosition = -1;
	private IUpdateMsgCount mListener;
	private long mStartTime;
	private long mEndTime;
	private boolean mIsLoading = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_msg_sys;
	}

	@Override
	protected void initViews(View view) {
		mEmpty = (TextView) view.findViewById(R.id.msg_sys_default);
		mFailed = (LinearLayout) view.findViewById(R.id.msg_sys_failed);
		mNetAbnormal = (LinearLayout) view
				.findViewById(R.id.msg_sys_net_abnormal);
		mLoading = (LinearLayout) view.findViewById(R.id.msg_sys_laoding);
		mListView = (ListViewForRefresh) view
				.findViewById(R.id.msg_sys_listview);
	}

	@Override
	protected void setListener(View view) {
		view.findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		view.findViewById(R.id.failed_refresh).setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
	}

	@Override
	protected void initData() {
		mMsgSyss = new ArrayList<MtqMsgSys>();
		// mMsgSyss = TestUtils.getMsgSys();
		mAdapter = new MsgSysAdapter(getActivity(), mMsgSyss);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mEmpty);
		mIsLoading = true;
	}

	@Override
	protected void loadData() {
		if (!DeliveryBusAPI.getInstance().hasSysMsgPermission()) {
			mEmpty.setText("当前账号无系统消息权限");
			update(UPDATE_EMPTY);
			return;
		}

		if (CldPhoneNet.isNetConnected()) {
			/**
			 * 防止该时间段内没有消息，将开始时间设置成10天前
			 */
			mStartTime = TimeUtils.getStartTimeForStamp(10);
			mEndTime = TimeUtils.getEndTimeForStamp();
			getHistorySysMsg();
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
	public void onClick(View v) {
		switch (v.getId()) {
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int headerCount = mListView.getHeaderViewsCount();
		int newPosition = position - headerCount;
		mCurPosition = newPosition;
		Bundle bundle = new Bundle();
		MtqMsgSys msgSys = mMsgSyss.get(mCurPosition);
		Intent intent = new Intent(getActivity(), SysMsgActivity.class);
		bundle.putSerializable(ConstantUtils.MTQ_MSG_SYS, msgSys);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUEST_SYS_MSG);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == REQUEST_SYS_MSG) {
				mMsgSyss.get(mCurPosition).readstatus = 1;
				mAdapter.updateView(mListView, mCurPosition);
				MsgSysTable.getInstance().update(mMsgSyss.get(mCurPosition));
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
		if (!DeliveryBusAPI.getInstance().hasSysMsgPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无系统消息权限");
			update(UPDATE_EMPTY);
			return;
		}
		
		if (!CldPhoneNet.isNetConnected()) {
			mListView.onRefreshFinish();
			/**
			 * 如果mMsgSyss为空，则直接显示"网络连接失败"
			 * modify 2017-08-03
			 */
			if (mMsgSyss.isEmpty()) {
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
						pullLatestSysMsg();
					}
				}, 500);
			} else {
				/**
				 * mIncrindex 为空，且mMsgSyss为空, 则重新获取历史系统消息
				 * modify 2017-08-03
				 */
				if (mMsgSyss.isEmpty()) {
					update(UPDATE_LOADING);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							getHistorySysMsg();
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
		if (!DeliveryBusAPI.getInstance().hasSysMsgPermission()) {
			mListView.onRefreshFinish();
			mEmpty.setText("当前账号无系统消息权限");
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
					int len = mMsgSyss.size();
					/**
					 * 与最后一条消息的时间 间隔100ms(自己定的)
					 * 
					 * 开始时间：结束时间往前推7天
					 */
					long endtime = mMsgSyss.get(len - 1).time - 100;
					long starttime = endtime - TimeUtils.SECONDS_IN_DAY * 7;
					mStartTime = starttime;
					mEndTime = endtime;
					getNextHistoryPage();
				}
			}, 500);
		}
	}

	/**
	 * 下拉最新的系统消息
	 */
	private void pullLatestSysMsg() {
		DeliveryBusAPI.getInstance().getSysMsg(mIncrindex, 10,
				new IMtqSysMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgSys> data,
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

								mListView.onRefreshFinish();
								mMsgSyss.addAll(0, data);
								mAdapter.notifyDataSetChanged();
								MsgSysTable.getInstance().insert(data);
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
	 * 获取历史系统消息
	 */
	private void getHistorySysMsg() {
		DeliveryBusAPI.getInstance().getHistorySysMsg(mStartTime, mEndTime, 1,
				PAGESIZE, 0, new IMtqHistorySysMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgSys> data,
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
							CldLog.e(TAG, "size: " + data.size());
							if (data != null && !data.isEmpty()) {
								mListView.onRefreshFinish();
								mMsgSyss.addAll(0, data);
								mAdapter.notifyDataSetChanged();

								update(UPDATE_SUCCESS);
								MsgSysTable.getInstance().insert(data);
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
		DeliveryBusAPI.getInstance().getHistorySysMsg(mStartTime, mEndTime, 1,
				PAGESIZE, 0, new IMtqHistorySysMsgListener() {

					@Override
					public void onResult(int errCode, List<MtqMsgSys> data,
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
								Toast.makeText(getActivity(), "加载成功",
										Toast.LENGTH_SHORT).show();
								mMsgSyss.addAll(data);
								mAdapter.notifyDataSetChanged();
								MsgSysTable.getInstance().insert(data);
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
		case MsgId.MSGID_BATCH_SET_SYS_MSG_READ: {
			String serialid = "";
			int len = mMsgSyss.size();
			/**
			 * add 2017-08-18
			 */
			if (len > 0) {
				List<MtqMsgSys> temp = new ArrayList<MtqMsgSys>();
				for (int i = 0; i < len; i++) {
					if (mMsgSyss.get(i).readstatus == 0) {
						temp.add(mMsgSyss.get(i));
					}
				}
				
				if (!temp.isEmpty()) {
					int temp_len = temp.size();
					for (int i = 0; i < temp_len; i++) {
						serialid += temp.get(i).serialid;
						if (i < temp_len - 1) {
							serialid += ";";
						}
					}
				}
			}
			
			boolean debug = false; 
			if (debug) {
				for (int i = 0; i < len; i++) {
					if (mMsgSyss.get(i).readstatus == 0) {
						serialid += mMsgSyss.get(i).serialid;
						/**
						 * 这里判断i < len - 1 是不妥的
						 */
						if (i < len - 1) {
							serialid += ";";
						}
					}
				}
			}
			CldLog.e(TAG, "serialid: " + serialid);
			if (!TextUtils.isEmpty(serialid)) {
				DeliveryBusAPI.getInstance().batchSetMsgSysRead(serialid);
			}
			break;
		}
		case MsgId.MSGID_BATCH_SET_SYS_MSG_READ_SUCCESS: {
			int len = mMsgSyss.size();
			for (int i = 0; i < len; i++) {
				if (mMsgSyss.get(i).readstatus == 0) {
					mMsgSyss.get(i).readstatus = 1;
				}
			}
			mAdapter.notifyDataSetChanged();
			MsgSysTable.getInstance().update(mMsgSyss);
			/**
			 * 更新未读数量
			 */
			if (mListener != null) {
				mListener.onUpdateMsgCount();
			}
			break;
		}
		case MsgId.MSGID_BATCH_SET_SYS_MSG_READ_FAILED: {
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
			mListView.setVisibility(View.GONE);
			break;
		}
		case UPDATE_NET_ABNORMAL: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_SUCCESS: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mIsLoading = false;
			break;
		}
		case UPDATE_FAILED: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.VISIBLE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		case UPDATE_EMPTY: {
			mEmpty.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mIsLoading = false;
			break;
		}
		default:
			break;
		}
	}

	public void pullLatestMsg() {
		/**
		 * 无系统消息权限, 则不处理
		 */
		if (!DeliveryBusAPI.getInstance().hasSysMsgPermission()) {
			return;
		}

		if (CldPhoneNet.isNetConnected()) {
			if (TextUtils.isEmpty(mIncrindex)) {
				mListView.onRefreshFinish();
			} else {
				pullLatestSysMsg();
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
