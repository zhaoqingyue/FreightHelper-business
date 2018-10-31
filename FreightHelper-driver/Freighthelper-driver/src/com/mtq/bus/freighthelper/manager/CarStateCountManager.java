/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: CarStateCountManager.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.manager
 * @Description: 车辆状态统计 定时轮询(目前定义 60s)
 * @author: zhaoqy
 * @date: 2017年6月26日 下午12:24:09
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.manager;

import java.util.Timer;
import java.util.TimerTask;

import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateCountListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarStateCount;

import de.greenrobot.event.EventBus;

public class CarStateCountManager {

	private static CarStateCountManager mManager = null;
	private CarStateCountPoll mPoll;
	private static int mGroupId = 0;

	public static CarStateCountManager getInstance() {
		if (mManager == null) {
			synchronized (CarStateCountManager.class) {
				if (mManager == null) {
					mManager = new CarStateCountManager();
				}
			}
		}
		return mManager;
	}

	public CarStateCountManager() {
		mPoll = new CarStateCountPoll();
	}

	private boolean inited = false;

	public void init() {
		/**
		 * 无车辆监控权限，则不需要初始化
		 */
		if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) 
			return;
		
		if (!inited) {
			inited = true;
			mPoll.startGetMsg();
		}
	}
	
	public void reInit() {
		/**
		 * 无车辆监控权限，则不需要初始化
		 */
		if (!DeliveryBusAPI.getInstance().hasMonitorPermission()) 
			return;
		
		mPoll.startGetMsg();
	}

	public void unInit() {
		mPoll.stopGetMsg();
	}
	
	public void setGroupId(int groupId) {
		mGroupId = groupId;
		/**
		 * 切换车队后，重新计时
		 * add by zhaoqy 2017-08-01
		 */
		mPoll.startGetMsg();
	}

	private static MtqCarStateCount mCarStateCount;

	public MtqCarStateCount getMtqCarStateCount() {
		return mCarStateCount;
	}

	public static class CarStateCountPoll {

		private static final int INTERVAL = 60 * 1000;
		private Timer mTimer;
		private TimerTask mTask;

		/**
		 * 车辆状态统计
		 */
		public void getCarStateCount() {
			DeliveryBusAPI.getInstance().getCarStateCount(mGroupId,
					new IMtqCarStateCountListener() {

						@Override
						public void onResult(int errCode, MtqCarStateCount data) {
							/**
							 * 帐号被挤出
							 */
							if (errCode == 1008) {
								HandleErrManager.getInstance().handleErrCode(
										errCode);
								return;
							}
							
							if (errCode == 0 && data != null) {
								mCarStateCount = data;

								BaseEvent event = new BaseEvent();
								event.msgId = MsgId.MSGID_GET_CARSTATE_COUNT_SUCCESS;
								event.errCode = errCode;
								EventBus.getDefault()
										.post(new BaseEvent(event));
							} else {
								BaseEvent event = new BaseEvent();
								event.msgId = MsgId.MSGID_GET_CARSTATE_COUNT_FAILED;
								event.errCode = errCode;
								EventBus.getDefault()
										.post(new BaseEvent(event));
							}
						}
					});
		}

		public void startGetMsg() {
			stopGetMsg();
			if (mTask == null) {
				mTask = new MsgTimerTask();
			}

			if (mTimer == null) {
				mTimer = new Timer();
				mTimer.schedule(mTask, 1000, INTERVAL);
			}
		}

		public void stopGetMsg() {
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}

			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		private class MsgTimerTask extends TimerTask {

			@Override
			public void run() {
				getCarStateCount();
			}
		};
	}
}
