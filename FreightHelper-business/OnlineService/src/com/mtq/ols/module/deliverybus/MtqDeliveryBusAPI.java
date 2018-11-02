/**
 * 货运宝-企业端API
 * 
 * @author zhaoqy
 * @date 2017-06-15
 */

package com.mtq.ols.module.deliverybus;

import java.util.List;

import android.text.TextUtils;

import com.mtq.ols.api.CldKAccountAPI;
import com.mtq.ols.api.CldOlsBase.IInitListener;
import com.mtq.ols.dal.CldDalKAccount;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCar;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarData;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarDataDetail;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarStateCount;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDeviceDType;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriverDetail;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqGroup;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqLogin;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqOrderCount;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskCount;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskNavi;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskStore;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTrackHistory;
import com.mtq.ols.module.deliverybus.parse.MtqSapReturn;
import com.mtq.ols.sap.CldSapUtil;

public class MtqDeliveryBusAPI {

	private static MtqDeliveryBusAPI instance;

	private MtqDeliveryBusAPI() {

	}

	public static MtqDeliveryBusAPI getInstance() {
		if (null == instance) {
			instance = new MtqDeliveryBusAPI();
		}
		return instance;
	}

	public static interface IMtqLoginListener {
		/**
		 * 用户登录回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, String errmsg);
	}

	/**
	 * 用户登录
	 * 
	 * @param duid
	 *            登录设备ID
	 * @param apptype
	 *            APP应用类型
	 * @param version
	 *            APP版本号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void login(long duid, int apptype, String version, String username,
			String password, IMtqLoginListener listener) {
		String loginPwd;
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			if (password.length() == 32) {
				loginPwd = password;
			} else {
				loginPwd = CldSapUtil.MD5(password);
			}
			
			MtqBllDeliveryBus.getInstance().login(duid, apptype, version,
					username, loginPwd, listener);
		} else {
			MtqSapReturn errRes = MtqBllDeliveryBus.errDeal();
			if (listener != null) {
				listener.onResult(errRes.errCode, null);
			}
		}
	}

	/**
	 * 自动登录
	 * 
	 * @param listener
	 * 
	 * @author zhaoqy
	 * @date 2017-06-16
	 */
	public void autoLogin(IMtqLoginListener listener) {
		long duid = MtqDalDeliveryBus.getInstance().getDuid();
		int apptype = MtqDalDeliveryBus.getInstance().getAppType();
		String version = MtqDalDeliveryBus.getInstance().getVersion();
		String username = MtqDalDeliveryBus.getInstance().getUserName();
		String password = MtqDalDeliveryBus.getInstance().getPassword();
		login(duid, apptype, version, username, password, listener);
	}

	/**
	 * 获取登录状态
	 */
	public boolean getLoginStatus() {
		return MtqDalDeliveryBus.getInstance().getLoginStatus();
	}

	/**
	 * 获取用户名
	 */
	public String getUserName() {
		return MtqDalDeliveryBus.getInstance().getUserName();
	}

	/**
	 * 获取密码
	 */
	public String getPassword() {
		return MtqDalDeliveryBus.getInstance().getPassword();
	}

	/**
	 * 获取登录信息
	 */
	public MtqLogin getMtqLogin() {
		return MtqDalDeliveryBus.getInstance().getMtqLogin();
	}

	/**
	 * 获取权限
	 */
	public List<String> getActions() {
		return MtqDalDeliveryBus.getInstance().getActions();
	}

	/**
	 * 获取车队
	 */
	public List<MtqGroup> getGroups() {
		return MtqDalDeliveryBus.getInstance().getGroups();
	}

	public void uninit() {
		MtqDalDeliveryBus.getInstance().uninit();
	}

	public static interface IMtqLogoutListener {
		/**
		 * 用户退出回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode);
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
	public void logout(IMtqLogoutListener listener) {
		MtqBllDeliveryBus.getInstance().logout(listener);
	}

	public static interface IMtqCarStateListener {

		/**
		 * 车辆状态列表回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqCarState> data, int total);
	}

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
	 * @date 2017-06-10
	 */
	public void getCarStateList(int group_id, int carstatus, String keywords,
			int pageindex, int pagesize, IMtqCarStateListener listener) {
		MtqBllDeliveryBus.getInstance().getCarStateList(group_id, carstatus,
				keywords, pageindex, pagesize, listener);
	}

	public static interface IMtqCarStateCountListener {

		/**
		 * 车辆状态统计回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqCarStateCount data);
	}

	/**
	 * 车辆状态统计
	 * 
	 * @param group_id
	 *            车队ID（0为全部）
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void getCarStateCount(int group_id,
			IMtqCarStateCountListener listener) {
		MtqBllDeliveryBus.getInstance().getCarStateCount(group_id, listener);

	}

	public static interface IMtqCarStateDetailListener {
		/**
		 * 车辆实时状态数据回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqCar mtqCar, MtqState mtqState);
	}

	/**
	 * 车辆实时状态数据
	 * 
	 * @param carid
	 *            车辆ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void getCarStateDetail(int carid, IMtqCarStateDetailListener listener) {
		MtqBllDeliveryBus.getInstance().getCarStateDetail(carid, listener);
	}

	public static interface IMtqTaskStoreListener {

		/**
		 * 车辆当日运单数据回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqTaskStore> data, int total);
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
	 * @date 2017-06-10
	 */
	public void getTaskStore(int carid, int pageindex, int pagesize,
			IMtqTaskStoreListener listener) {
		MtqBllDeliveryBus.getInstance().getTaskStore(carid, pageindex,
				pagesize, listener);
	}

	public static interface IMtqTaskNaviListener {

		/**
		 * 车辆当日行程数据回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqTaskNavi> data, int total);
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
	 * @date 2017-06-10
	 */
	public void getTaskNavi(int carid, int pageindex, int pagesize,
			IMtqTaskNaviListener listener) {
		MtqBllDeliveryBus.getInstance().getTaskNavi(carid, pageindex, pagesize,
				listener);
	}

	public static interface IMtqCarSendListener {

		/**
		 * 车辆调度回调
		 * 
		 * @param errCode
		 * @param systime
		 */
		public void onResult(int errCode);
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
	 * @date 2017-06-10
	 */
	public void setCarSend(int time, int smstype, int carid, int driverid,
			long x, long y, String addr, String poiname, String text, 
			int voiceduration, String voicedata, 
			IMtqCarSendListener listener) {
		MtqBllDeliveryBus.getInstance().setCarSend(time, smstype, carid,
				driverid, x, y, addr, poiname, text, voiceduration, 
				voicedata, listener);
	}

	public static interface IMtqTrackHistoryListener {

		/**
		 * 历史轨迹回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqTrackHistory> data,
				int alarmnum);
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
	 * @date 2017-06-10
	 */
	public void getTrackHistory(int carid, long starttime, long endtime,
			IMtqTrackHistoryListener listener) {
		MtqBllDeliveryBus.getInstance().getTrackHistory(carid, starttime,
				endtime, listener);
	}

	public static interface IMtqAlarmMsgListener {

		/**
		 * 警情消息回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqMsgAlarm> data,
				String incrindex);
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
	 * @date 2017-06-10
	 */
	public void getAlarmMsg(String incrindex, int pagesize,
			IMtqAlarmMsgListener listener) {
		MtqBllDeliveryBus.getInstance().getAlarmMsg(incrindex, pagesize,
				listener);
	}

	public static interface IMtqHistoryAlarmMsgListener {

		/**
		 * 历史警情消息回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqMsgAlarm> data,
				String incrindex);
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
	 * @date 2017-06-10
	 */
	public void getHistoryAlarmMsg(long starttime, long endtime, int pageindex,
			int pagesize, int order, IMtqHistoryAlarmMsgListener listener) {
		MtqBllDeliveryBus.getInstance().getHistoryAlarmMsg(starttime, endtime,
				pageindex, pagesize, order, listener);
	}

	public static interface IMtqMsgAlarmReadListener {

		/**
		 * 更改警情消息已读状态回调
		 * 
		 * @param errCode
		 * @param systime
		 */
		public void onResult(int errCode);
	}

	/**
	 * 更改警情消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void setMsgAlarmRead(String id, IMtqMsgAlarmReadListener listener) {
		MtqBllDeliveryBus.getInstance().setMsgAlarmRead(id, listener);
	}

	public static interface IMtqSysMsgListener {

		/**
		 * 系统消息回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqMsgSys> data, String incrindex);
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
	 * @date 2017-06-10
	 */
	public void getSysMsg(String incrindex, int pagesize,
			IMtqSysMsgListener listener) {
		MtqBllDeliveryBus.getInstance()
				.getSysMsg(incrindex, pagesize, listener);
	}

	public static interface IMtqHistorySysMsgListener {

		/**
		 * 系统消息回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqMsgSys> data, String incrindex);
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
	 * @date 2017-06-10
	 */
	public void getHistorySysMsg(long starttime, long endtime, int pageindex,
			int pagesize, int order, IMtqHistorySysMsgListener listener) {
		MtqBllDeliveryBus.getInstance().getHistorySysMsg(starttime, endtime,
				pageindex, pagesize, order, listener);
	}

	public static interface IMtqMsgSysReadListener {

		/**
		 * 更改系统消息已读状态回调
		 * 
		 * @param errCode
		 * @param systime
		 */
		public void onResult(int errCode);
	}

	/**
	 * 更改系统消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void setMsgSysRead(String serialid, IMtqMsgSysReadListener listener) {
		MtqBllDeliveryBus.getInstance().setMsgSysRead(serialid, listener);
	}

	public static interface IMtqCarDataListener {

		/**
		 * 车辆数据列表回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqCarData> data, int total);
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
	 * @date 2017-06-10
	 */
	public void getCarDataList(int group_id, String keywords, int pageindex,
			int pagesize, IMtqCarDataListener listener) {
		MtqBllDeliveryBus.getInstance().getCarDataList(group_id, keywords,
				pageindex, pagesize, listener);
	}

	public static interface IMtqCarDataDetailListener {

		/**
		 * 车辆数据详情回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqCarDataDetail car,
				List<MtqGroup> groups);
	}

	/**
	 * 车辆数据详情
	 * 
	 * @param carid
	 *            车队ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void getCarDataDetail(int carid, IMtqCarDataDetailListener listener) {
		MtqBllDeliveryBus.getInstance().getCarDataDetail(carid, listener);
	}

	public static interface IMtqDriverDataListener {

		/**
		 * 司机数据列表回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqDriver> data, int total);
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
	 * @date 2017-06-10
	 */
	public void getDriverDataList(int invitestatus, String keywords,
			int pageindex, int pagesize, IMtqDriverDataListener listener) {
		MtqBllDeliveryBus.getInstance().getDriverDataList(invitestatus,
				keywords, pageindex, pagesize, listener);
	}

	public static interface IMtqDriverDataDetailListener {

		/**
		 * 司机数据详情回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqDriverDetail data);
	}

	/**
	 * 司机数据详情
	 * 
	 * @param driverid
	 *            司机ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void getDriverDataDetail(int driverid,
			IMtqDriverDataDetailListener listener) {
		MtqBllDeliveryBus.getInstance().getDriverDataDetail(driverid, listener);
	}

	public static interface IMtqInviteDriverListener {

		/**
		 * 邀请司机加入车队回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, int invitestatus);
	}

	/**
	 * 邀请司机加入车队
	 * 
	 * @param driverid
	 *            司机ID
	 * 
	 * @author zhaoqy
	 * @date 2017-06-10
	 */
	public void inviteDriver(int driverid, IMtqInviteDriverListener listener) {
		MtqBllDeliveryBus.getInstance().inviteDriver(driverid, listener);
	}

	public static interface IMtqOrderCountListener {

		/**
		 * 运单统计看板回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqOrderCount data);
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
	 * @date 2017-06-10
	 */
	public void getOrderCount(String startdate, String enddate,
			IMtqOrderCountListener listener) {
		MtqBllDeliveryBus.getInstance().getOrderCount(startdate, enddate,
				listener);
	}

	public static interface IMtqTaskCountListener {

		/**
		 * 运输计划统计看板回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, MtqTaskCount data);
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
	 * @date 2017-06-10
	 */
	public void getTaskCount(String startdate, String enddate,
			IMtqTaskCountListener listener) {
		MtqBllDeliveryBus.getInstance().getTaskCount(startdate, enddate,
				listener);
	}

	public static interface IMtqFeedbackListener {

		/**
		 * 吐槽反馈回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode);
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
	 * @date 2017-06-10
	 */
	public void setFeedback(int fdtype, int dtype, String title,
			String content, String contact, String pics,
			IMtqFeedbackListener listener) {
		MtqBllDeliveryBus.getInstance().setFeedback(fdtype, dtype, title,
				content, contact, pics, listener);
	}

	public static interface IMtqUploadAttachPicListener {

		/**
		 * 上传附件照片文件回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, String mediaid);
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
	 * @date 2017-06-10
	 */
	public void uploadAttachPic(int x, int y, String data,
			IMtqUploadAttachPicListener listener) {
		MtqBllDeliveryBus.getInstance().uploadAttachPic(x, y, data, listener);
	}

	public static interface IMtqDeviceDTypeListener {

		/**
		 * 硬件设备类型回调
		 * 
		 * @param errCode
		 * @param result
		 */
		public void onResult(int errCode, List<MtqDeviceDType> data);
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
		MtqBllDeliveryBus.getInstance().getDeviceDType(listener);
	}
}
