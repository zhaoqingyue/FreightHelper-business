/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DeliveryBusAPI.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.api.deliverybus
 * @Description: 货运宝-企业端相关API
 * @author: zhaoqy
 * @date: 2017年6月15日 下午4:31:02
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.api.deliverybus;

import java.util.List;

import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.application.BFApplication;
import com.mtq.bus.freighthelper.application.BFContext;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.bean.eventbus.LoginEvent;
import com.mtq.bus.freighthelper.db.MsgAlarmTable;
import com.mtq.bus.freighthelper.db.MsgSysTable;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.utils.AppInfo;
import com.mtq.bus.freighthelper.utils.Config;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.api.CldKAccountAPI;
import com.mtq.ols.api.CldOlsBase.IInitListener;
import com.mtq.ols.dal.CldDalKAccount;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqAlarmMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarDataDetailListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarDataListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarSendListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateCountListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateDetailListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarStateListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDeviceDTypeListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDriverDataDetailListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDriverDataListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqFeedbackListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqHistoryAlarmMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqHistorySysMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqInviteDriverListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqLoginListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqLogoutListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqMsgAlarmReadListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqMsgSysReadListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqOrderCountListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqSysMsgListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskCountListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskNaviListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskStoreListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTrackHistoryListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqUploadAttachPicListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqGroup;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqLogin;

import de.greenrobot.event.EventBus;

public class DeliveryBusAPI {

	protected static final String TAG = "DeliveryBusAPI";

	private static DeliveryBusAPI instance;

	private DeliveryBusAPI() {

	}

	public static DeliveryBusAPI getInstance() {
		if (null == instance) {
			instance = new DeliveryBusAPI();
		}
		return instance;
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void login(final String username, final String password) {
		long duid = CldDalKAccount.getInstance().getDuid();
		if (duid == 0) {
			/**
			 * add by zhaoqy 2017-7-21
			 * 解决bug：应用清除数据后，先断网启动app，然后联网登录，此时ols库没有初始化，导致duid为0，且配置域名为空，
			 * 此时要重新初始化ols库
			 */
			BFApplication.initOlsLib(new IInitListener() {

				@Override
				public void onUpdateConfig() {

				}

				@Override
				public void onInitDuid() {
					long temp = CldKAccountAPI.getInstance().getDuid();
					startLogin(temp, username, password);
				}
			});
		} else {
			startLogin(duid, username, password);
		}
	}

	public void startLogin(long duid, String username, String password) {
		String version = AppInfo.getVerName(BFContext.getContext());
		MtqDeliveryBusAPI.getInstance().login(duid, Config.apptype, version,
				username, password, new IMtqLoginListener() {

					@Override
					public void onResult(int errCode, String errMsg) {
						if (errCode == 0) {
							/**
							 * 登录成功后， 删除缓存消息
							 */
							MsgAlarmTable.getInstance().delete();
							MsgSysTable.getInstance().delete();

							LoginEvent event = new LoginEvent();
							event.msgId = MsgId.MSGID_LOGIN_SUCCESS;
							event.errCode = errCode;
							event.errMsg = errMsg;
							EventBus.getDefault().post(event);
						} else {
							LoginEvent event = new LoginEvent();
							event.msgId = MsgId.MSGID_LOGIN_FAILED;
							event.errCode = errCode;
							event.errMsg = errMsg;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 自动登录
	 * 
	 * @param listener
	 * 
	 * @author zhaoqy
	 * @date 2017-06-16
	 */
	public void autoLogin() {
		MtqDeliveryBusAPI.getInstance().autoLogin(new IMtqLoginListener() {

			@Override
			public void onResult(int errCode, String errMsg) {
				if (errCode == 0) {
					/**
					 * 登录成功后， 删除缓存消息
					 */
					MsgAlarmTable.getInstance().delete();
					MsgSysTable.getInstance().delete();

					LoginEvent event = new LoginEvent();
					event.msgId = MsgId.MSGID_LOGIN_SUCCESS;
					event.errCode = errCode;
					event.errMsg = errMsg;
					EventBus.getDefault().post(event);
				} else {
					LoginEvent event = new LoginEvent();
					event.msgId = MsgId.MSGID_LOGIN_FAILED;
					event.errCode = errCode;
					event.errMsg = errMsg;
					EventBus.getDefault().post(event);
				}
			}
		});
	}

	/**
	 * 获取登录状态
	 */
	public boolean getLoginStatus() {
		return MtqDeliveryBusAPI.getInstance().getLoginStatus();
	}

	/**
	 * 获取用户名
	 */
	public String getUserName() {
		return MtqDeliveryBusAPI.getInstance().getUserName();
	}

	/**
	 * 获取密码
	 */
	public String getPassword() {
		return MtqDeliveryBusAPI.getInstance().getPassword();
	}

	/**
	 * 获取登录信息
	 */
	public MtqLogin getMtqLogin() {
		return MtqDeliveryBusAPI.getInstance().getMtqLogin();
	}

	/**
	 * 获取权限
	 */
	public List<String> getActions() {
		return MtqDeliveryBusAPI.getInstance().getActions();
	}

	/**
	 * 获取车队
	 */
	public List<MtqGroup> getGroups() {
		return MtqDeliveryBusAPI.getInstance().getGroups();
	}

	/**
	 * 反初始化
	 */
	public void uninit() {
		MtqDeliveryBusAPI.getInstance().uninit();
	}

	/**
	 * 用户退出
	 * 
	 * @param timestamp
	 *            请求时间（UTC时间）
	 * @param listener
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void logout() {
		MtqDeliveryBusAPI.getInstance().logout(new IMtqLogoutListener() {

			@Override
			public void onResult(int errCode) {
				if (errCode == 0) {
					BaseEvent event = new BaseEvent();
					event.msgId = MsgId.MSGID_LOGOUT_SUCCESS;
					event.errCode = errCode;
					EventBus.getDefault().post(event);
				} else {
					BaseEvent event = new BaseEvent();
					event.errCode = errCode;
					event.msgId = MsgId.MSGID_LOGOUT_FAILED;
					EventBus.getDefault().post(event);
				}
			}
		});
	}

	public static final int PAGESIZE = 200;

	/**
	 * 车辆状态列表
	 * 
	 * @param group_id
	 *            车队ID（0为全部）
	 * @param carstatus
	 *            车辆状态 (0为全部，1空闲，2已派车，3作业中，20为维修保养)
	 * @param keywords
	 *            模糊检索关键字（车牌号码）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getCarStateList(int group_id, int pageindex,
			IMtqCarStateListener listener) {
		MtqDeliveryBusAPI.getInstance().getCarStateList(group_id, 0, null,
				pageindex, PAGESIZE, listener);
	}

	/**
	 * 车辆状态统计
	 * 
	 * @param group_id
	 *            车队ID（0为全部）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getCarStateCount(int group_id,
			IMtqCarStateCountListener listener) {
		MtqDeliveryBusAPI.getInstance().getCarStateCount(group_id, listener);

	}

	/**
	 * 车辆实时状态数据
	 * 
	 * @param carid
	 *            车辆ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getCarStateDetail(int carid, IMtqCarStateDetailListener listener) {
		MtqDeliveryBusAPI.getInstance().getCarStateDetail(carid, listener);
	}

	/**
	 * 车辆当日运单数据
	 * 
	 * @param carid
	 *            车辆ID
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getTaskStore(int carid, int pageindex, int pagesize,
			IMtqTaskStoreListener listener) {
		MtqDeliveryBusAPI.getInstance().getTaskStore(carid, pageindex,
				pagesize, listener);
	}

	/**
	 * 车辆当日行程数据
	 * 
	 * @param carid
	 *            车辆ID
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getTaskNavi(int carid, int pageindex, int pagesize,
			IMtqTaskNaviListener listener) {
		MtqDeliveryBusAPI.getInstance().getTaskNavi(carid, pageindex, pagesize,
				listener);
	}

	/**
	 * 车辆调度
	 * 
	 * @param time
	 *            调度时间（UTC时间）
	 * @param smstype
	 *            消息类型（1为文本类，2为语音类）
	 * @param carid
	 *            被调度车辆ID
	 * @param driverid
	 *            被调度司机ID
	 * @param x
	 *            调往位置坐标X（文本类必填）
	 * @param y
	 *            调往位置坐标Y（文本类必填）
	 * @param addr
	 *            调往位置地址（文本类必填）
	 * @param text
	 *            文本内容（文本类必填）
	 * @param voicedata
	 *            语音内容（语音类必填）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void setCarSend(int time, int smstype, int carid, int driverid,
			long x, long y, String addr, String poiname, String text, 
			int voiceduration, String voicedata, 
			IMtqCarSendListener listener) {
		MtqDeliveryBusAPI.getInstance().setCarSend(time, smstype, carid,
				driverid, x, y, addr, poiname, text, voiceduration, 
				voicedata, listener);
	}

	/**
	 * 历史轨迹
	 * 
	 * @param carid
	 *            车辆ID
	 * @param starttime
	 *            开始时间（UTC时间）
	 * @param endtime
	 *            结束时间（大于开始时间，小于或等于当前时间，时间跨度不超过10天）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getTrackHistory(int carid, long starttime, long endtime,
			IMtqTrackHistoryListener listener) {
		MtqDeliveryBusAPI.getInstance().getTrackHistory(carid, starttime,
				endtime, listener);
	}

	/**
	 * 警情消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getAlarmMsg(String incrindex, int pagesize,
			IMtqAlarmMsgListener listener) {
		MtqDeliveryBusAPI.getInstance().getAlarmMsg(incrindex, pagesize,
				listener);
	}

	/**
	 * 拉取历史警情消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getHistoryAlarmMsg(long starttime, long endtime, int pageindex,
			int pagesize, int order, IMtqHistoryAlarmMsgListener listener) {
		CldLog.i(TAG, "endtime: " + TimeUtils.stampToYmdHm(endtime));
		CldLog.i(TAG, "starttime: " + TimeUtils.stampToYmdHm(starttime));
		MtqDeliveryBusAPI.getInstance().getHistoryAlarmMsg(starttime, endtime,
				pageindex, pagesize, order, listener);
	}

	/**
	 * 更改警情消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void setMsgAlarmRead(String id) {
		MtqDeliveryBusAPI.getInstance().setMsgAlarmRead(id,
				new IMtqMsgAlarmReadListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_MSG_ALARM_READ_SUCCESS;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						} else {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_MSG_ALARM_READ_FAILED;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 批量更改警情消息已读状态
	 * 
	 * @param id
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-29
	 */
	public void batchSetMsgAlarmRead(String id) {
		MtqDeliveryBusAPI.getInstance().setMsgAlarmRead(id,
				new IMtqMsgAlarmReadListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_BATCH_SET_ALARM_MSG_READ_SUCCESS;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						} else {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_BATCH_SET_ALARM_MSG_READ_FAILED;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 系统消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getSysMsg(String incrindex, int pagesize,
			IMtqSysMsgListener listener) {
		MtqDeliveryBusAPI.getInstance()
				.getSysMsg(incrindex, pagesize, listener);
	}

	/**
	 * 拉取历史系统消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getHistorySysMsg(long starttime, long endtime, int pageindex,
			int pagesize, int order, IMtqHistorySysMsgListener listener) {
		CldLog.i(TAG, "endtime: " + TimeUtils.stampToYmdHm(endtime));
		CldLog.i(TAG, "starttime: " + TimeUtils.stampToYmdHm(starttime));
		MtqDeliveryBusAPI.getInstance().getHistorySysMsg(starttime, endtime,
				pageindex, pagesize, order, listener);
	}

	/**
	 * 更改系统消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void setMsgSysRead(String serialid) {
		MtqDeliveryBusAPI.getInstance().setMsgSysRead(serialid,
				new IMtqMsgSysReadListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_MSG_SYS_READ_SUCCESS;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						} else {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_MSG_SYS_READ_FAILED;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 批量更改系统消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-29
	 */
	public void batchSetMsgSysRead(String serialid) {
		MtqDeliveryBusAPI.getInstance().setMsgSysRead(serialid,
				new IMtqMsgSysReadListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_BATCH_SET_SYS_MSG_READ_SUCCESS;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						} else {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_BATCH_SET_SYS_MSG_READ_FAILED;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 车辆数据列表
	 * 
	 * @param group_id
	 *            车队ID（0为全部）
	 * @param keywords
	 *            模糊检索关键字（车牌号码）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getCarDataList(int group_id, String keywords, int pageindex,
			int pagesize, IMtqCarDataListener listener) {
		MtqDeliveryBusAPI.getInstance().getCarDataList(group_id, keywords,
				pageindex, pagesize, listener);
	}

	/**
	 * 车辆数据详情
	 * 
	 * @param carid
	 *            车队ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getCarDataDetail(int carid, IMtqCarDataDetailListener listener) {
		MtqDeliveryBusAPI.getInstance().getCarDataDetail(carid, listener);
	}

	/**
	 * 司机数据列表
	 * 
	 * @param invitestatus
	 *            邀请状态（0为全部，1未读邀请消息，2已读邀请消息，3同意加入车队，4拒绝加入车队，5已退出车队)
	 * @param keywords
	 *            模糊检索关键字（司机姓名）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getDriverDataList(int invitestatus, String keywords,
			int pageindex, int pagesize, IMtqDriverDataListener listener) {
		MtqDeliveryBusAPI.getInstance().getDriverDataList(invitestatus,
				keywords, pageindex, pagesize, listener);
	}

	/**
	 * 司机数据详情
	 * 
	 * @param driverid
	 *            司机ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getDriverDataDetail(int driverid,
			IMtqDriverDataDetailListener listener) {
		MtqDeliveryBusAPI.getInstance().getDriverDataDetail(driverid, listener);
	}

	/**
	 * 邀请司机加入车队
	 * 
	 * @param driverid
	 *            司机ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void inviteDriver(int driverid, IMtqInviteDriverListener listener) {
		MtqDeliveryBusAPI.getInstance().inviteDriver(driverid, listener);
	}

	/**
	 * 运单统计看板
	 * 
	 * @param startdate
	 *            查询开始日期
	 * @param enddate
	 *            查询结束日期（大于或等于开始日期，小于或等于当前日期，日期跨度不超过31天）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getOrderCount(String startdate, String enddate,
			IMtqOrderCountListener listener) {
		CldLog.e(TAG, "startdate: " + startdate);
		CldLog.e(TAG, "enddate: " + enddate);
		MtqDeliveryBusAPI.getInstance().getOrderCount(startdate, enddate,
				listener);
	}

	/**
	 * 运输计划统计看板
	 * 
	 * @param startdate
	 *            查询开始日期
	 * @param enddate
	 *            查询结束日期（大于或等于开始日期，小于或等于当前日期，日期跨度不超过31天）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getTaskCount(String startdate, String enddate,
			IMtqTaskCountListener listener) {
		CldLog.e(TAG, "startdate: " + startdate);
		CldLog.e(TAG, "enddate: " + enddate);
		MtqDeliveryBusAPI.getInstance().getTaskCount(startdate, enddate,
				listener);
	}

	/**
	 * 吐槽反馈
	 * 
	 * @param fdtype
	 *            反馈类型（1为平台类，2为硬件类）
	 * @param dtype
	 *            设备类型（2为北斗双模一体机，3为凯立德KPND，4为TD-BOX，5为TD-PND）
	 * @param title
	 *            标题
	 * @param content
	 *            反馈内容
	 * @param contact
	 *            联系方式
	 * @param pics
	 *            图片文件信息（媒体标识ID，多个以小写“;”分号分隔。图片文件需通过“上传附件照片文件”接口上传）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void setFeedback(int fdtype, int dtype, String title,
			String content, String contact, String pics) {
		MtqDeliveryBusAPI.getInstance().setFeedback(fdtype, dtype, title,
				content, contact, pics, new IMtqFeedbackListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_FEEDBACK_SUCCESS;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						} else {
							BaseEvent event = new BaseEvent();
							event.msgId = MsgId.MSGID_SET_FEEDBACK_FAILED;
							event.errCode = errCode;
							EventBus.getDefault().post(event);
						}
					}
				});
	}

	/**
	 * 上传附件照片文件
	 * 
	 * @param time
	 *            拍照时间（UTC时间）
	 * @param x
	 *            拍照时凯立德坐标X（定位失败为0）
	 * @param y
	 *            拍照时凯立德坐标Y（定位失败为0）
	 * @param data
	 *            图片内容（base64）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void uploadAttachPic(int x, int y, String data,
			IMtqUploadAttachPicListener listener) {
		MtqDeliveryBusAPI.getInstance().uploadAttachPic(x, y, data, listener);
	}

	/**
	 * 硬件设备类型
	 * 
	 * @param timestamp
	 *            请求时间（UTC时间）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public void getDeviceDType(IMtqDeviceDTypeListener listener) {
		MtqDeliveryBusAPI.getInstance().getDeviceDType(listener);
	}

	/**
	 * 是否有警情消息权限
	 */
	public boolean hasAlarmMsgPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_alarmmsg")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有系统消息权限
	 */
	public boolean hasSysMsgPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_sysmsg")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有车辆监控权限
	 */
	public boolean hasMonitorPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_monitor")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有车辆调度权限
	 */
	public boolean hasSchedulePermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_carsend")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有轨迹回放权限
	 */
	public boolean hasTrackPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_track")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有查看车辆权限
	 */
	public boolean hasCarReadPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_carread")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有查看司机权限
	 */
	public boolean hasDriverReadPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_driverread")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有邀请司机权限
	 */
	public boolean hasDriverInvitePermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_driverinvite")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否有业务看板权限
	 */
	public boolean hasBusPermission() {
		List<String> permissions = getActions();
		if (permissions != null && !permissions.isEmpty()) {
			for (String permission : permissions) {
				if (permission.equalsIgnoreCase("app_busikanban")) {
					return true;
				}
			}
		}
		return false;
	}
}
