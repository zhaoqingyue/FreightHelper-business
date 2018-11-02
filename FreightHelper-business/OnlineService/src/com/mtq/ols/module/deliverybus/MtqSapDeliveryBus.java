package com.mtq.ols.module.deliverybus;

import java.util.HashMap;
import java.util.Map;

import com.mtq.ols.bll.CldBllUtil;
import com.mtq.ols.module.delivery.tool.CldKDeviceAPI;
import com.mtq.ols.module.deliverybus.parse.MtqBaseParse;
import com.mtq.ols.module.deliverybus.parse.MtqSapReturn;
import com.mtq.ols.tools.CldSapParser;

public class MtqSapDeliveryBus {

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
	 *            密码（MD5加密后）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn login(long duid, int apptype, String version,
			String username, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("duid", duid);
		map.put("apptype", apptype);
		map.put("version", version);
		map.put("username", username);
		map.put("password", password);

		MtqSapReturn errRes = MtqBaseParse.getNoSignPostParms(map, getSvr()
				+ "v1/user_login.php");
		return errRes;
	}

	/**
	 * 用户退出
	 * 
	 * @param timestamp
	 *            请求时间（UTC时间）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn logout() {
		Map<String, Object> map = getPubMap();
		map.put("timestamp", CldKDeviceAPI.getSvrTime());

		MtqSapReturn errRes = post(map, "v1/user_logout.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getCarStateList(int group_id, int carstatus,
			String keywords, int pageindex, int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("group_id", group_id);
		map.put("carstatus", carstatus);
		CldSapParser.putStringToMap(map, "keywords", keywords);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_carstate_list.php");
		return errRes;
	}

	/**
	 * 车辆状态统计
	 * 
	 * @param group_id
	 *            车队ID（0为全部）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getCarStateCount(int group_id) {
		Map<String, Object> map = getPubMap();
		map.put("group_id", group_id);

		MtqSapReturn errRes = post(map, "v1/get_carstate_count.php");
		return errRes;
	}

	/**
	 * 车辆实时状态数据
	 * 
	 * @param carid
	 *            车辆ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getCarStateDetail(int carid) {
		Map<String, Object> map = getPubMap();
		map.put("carid", carid);

		MtqSapReturn errRes = post(map, "v1/get_carstate_detail.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getTaskStore(int carid, int pageindex,
			int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("carid", carid);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_taskstore_today.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getTaskNavi(int carid, int pageindex,
			int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("carid", carid);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_tasknavi_today.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn setCarSend(int time, int smstype, int carid,
			int driverid, long x, long y, String addr, String poiname, String text,
			int voiceduration, String voicedata) {
		Map<String, Object> map = getPubMap();
		map.put("time", time);
		map.put("smstype", smstype);
		map.put("carid", carid);
		map.put("driverid", driverid);
		String outMd5Source = "";
		if (smstype == 1) {
			map.put("x", x);
			map.put("y", y);
			CldSapParser.putStringToMap(map, "addr", addr);
			CldSapParser.putStringToMap(map, "poiname", poiname);
			CldSapParser.putStringToMap(map, "text", text);
			outMd5Source = CldSapParser.formatSource(map);
		} else if (smstype == 2) {
			map.put("voiceduration", voiceduration);
			outMd5Source = CldSapParser.formatSource(map);
			CldSapParser.putStringToMap(map, "voicedata", voicedata);
		}

		MtqSapReturn errRes = post(map, outMd5Source, "v1/set_carsend.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getTrackHistory(int carid, long starttime,
			long endtime) {
		Map<String, Object> map = getPubMap();
		map.put("carid", carid);
		map.put("starttime", starttime);
		map.put("endtime", endtime);

		MtqSapReturn errRes = post(map, "v1/get_track_history.php");
		return errRes;
	}

	/**
	 * 获取最新警情消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识（用于增量取数据，为空则不增量更新）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getAlarmMsg(String incrindex, int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("incrindex", incrindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_message_alarm.php");
		return errRes;
	}

	/**
	 * 拉取历史警情消息（含详情）
	 * 
	 * @param starttime
	 *            开始时间（UTC时间）
	 * @param endtime
	 *            结束时间（UTC时间）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * @param order
	 *            排序字段(按报警时间0-倒序(默认)，1-顺序)
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-28
	 */
	public static MtqSapReturn getHistoryAlarmMsg(long starttime, long endtime,
			int pageindex, int pagesize, int order) {
		Map<String, Object> map = getPubMap();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);
		map.put("order", order);

		MtqSapReturn errRes = post(map, "v1/get_message_alarm_history.php");
		return errRes;
	}

	/**
	 * 更改警情消息已读状态
	 * 
	 * @param id
	 *            记录ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn setMsgAlarmRead(String id) {
		Map<String, Object> map = getPubMap();
		map.put("id", id);

		MtqSapReturn errRes = post(map, "v1/set_message_alarm_read.php");
		return errRes;
	}

	/**
	 * 系统消息（含详情）
	 * 
	 * @param incrindex
	 *            增量标识（用于增量取数据，为空则不增量更新）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getSysMsg(String incrindex, int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("incrindex", incrindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_message_sys.php");
		return errRes;
	}

	/**
	 * 拉取历史系统消息（含详情）
	 * 
	 * @param starttime
	 *            开始时间（UTC时间）
	 * @param endtime
	 *            结束时间（UTC时间）
	 * @param pageindex
	 *            页码（从1开始）
	 * @param pagesize
	 *            每页条数（默认每页10条，最大200条）
	 * @param order
	 *            排序字段(按报警时间0-倒序(默认)，1-顺序)
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-28
	 */
	public static MtqSapReturn getHistorySysMsg(long starttime, long endtime,
			int pageindex, int pagesize, int order) {
		Map<String, Object> map = getPubMap();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);
		map.put("order", order);

		MtqSapReturn errRes = post(map, "v1/get_message_sys_history.php");
		return errRes;
	}

	/**
	 * 更改系统消息已读状态
	 * 
	 * @param serialid
	 *            记录ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn setMsgSysRead(String serialid) {
		Map<String, Object> map = getPubMap();
		map.put("serialid", serialid);

		MtqSapReturn errRes = post(map, "v1/set_message_sys_read.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getCarDataList(int group_id, String keywords,
			int pageindex, int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("group_id", group_id);
		CldSapParser.putStringToMap(map, "keywords", keywords);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_cardata_list.php");
		return errRes;
	}

	/**
	 * 车辆数据详情
	 * 
	 * @param carid
	 *            车队ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getCarDataDetail(int carid) {
		Map<String, Object> map = getPubMap();
		map.put("carid", carid);

		MtqSapReturn errRes = post(map, "v1/get_cardata_detail.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getDriverDataList(int invitestatus,
			String keywords, int pageindex, int pagesize) {
		Map<String, Object> map = getPubMap();
		map.put("invitestatus", invitestatus);
		CldSapParser.putStringToMap(map, "keywords", keywords);
		map.put("pageindex", pageindex);
		map.put("pagesize", pagesize);

		MtqSapReturn errRes = post(map, "v1/get_driverdata_list.php");
		return errRes;
	}

	/**
	 * 司机数据详情
	 * 
	 * @param driverid
	 *            司机ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getDriverDataDetail(int driverid) {
		Map<String, Object> map = getPubMap();
		map.put("driverid", driverid);

		MtqSapReturn errRes = post(map, "v1/get_driverdata_detail.php");
		return errRes;
	}

	/**
	 * 邀请司机加入车队
	 * 
	 * @param driverid
	 *            司机ID
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn inviteDriver(int driverid) {
		Map<String, Object> map = getPubMap();
		map.put("driverid", driverid);

		MtqSapReturn errRes = post(map, "v1/invite_driver.php");
		return errRes;
	}

	/**
	 * 运单统计看板
	 * 
	 * @param startdate
	 *            查询开始日期
	 * @param enddate
	 *            查询结束日期（大于或等于开始日期，小于或等于当前日期，日期跨度不超过31天）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getOrderCount(String startdate, String enddate) {
		Map<String, Object> map = getPubMap();
		map.put("startdate", startdate);
		map.put("enddate", enddate);

		MtqSapReturn errRes = post(map, "v1/get_order_count.php");
		return errRes;
	}

	/**
	 * 运输计划统计看板
	 * 
	 * @param startdate
	 *            查询开始日期
	 * @param enddate
	 *            查询结束日期（大于或等于开始日期，小于或等于当前日期，日期跨度不超过31天）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getTaskCount(String startdate, String enddate) {
		Map<String, Object> map = getPubMap();
		map.put("startdate", startdate);
		map.put("enddate", enddate);

		MtqSapReturn errRes = post(map, "v1/get_task_count.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn setFeedback(int fdtype, int dtype, String title,
			String content, String contact, String pics) {
		Map<String, Object> map = getPubMap();
		map.put("fdtype", fdtype);
		map.put("dtype", dtype);
		map.put("title", title);
		map.put("content", content);
		CldSapParser.putStringToMap(map, "contact", contact);
		CldSapParser.putStringToMap(map, "pics", pics);

		MtqSapReturn errRes = post(map, "v1/set_feedback.php");
		return errRes;
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
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn uploadAttachPic(int x, int y, String data) {
		Map<String, Object> map = getPubMap();
		map.put("time", CldKDeviceAPI.getSvrTime());
		map.put("x", x);
		map.put("y", y);
		String outMd5Source = CldSapParser.formatSource(map);
		map.put("data", data);

		MtqSapReturn errRes = post(map, outMd5Source,
				"v1/upload_attach_pic.php");
		return errRes;
	}

	/**
	 * 硬件设备类型
	 * 
	 * @param timestamp
	 *            请求时间（UTC时间）
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static MtqSapReturn getDeviceDType() {
		Map<String, Object> map = getPubMap();
		map.put("timestamp", CldKDeviceAPI.getSvrTime());

		MtqSapReturn errRes = post(map, "v1/get_device_dtype.php");
		return errRes;
	}

	/**
	 * 获取公共参数map
	 * 
	 * @return Map<String,Object>
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static Map<String, Object> getPubMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sesskey", MtqDalDeliveryBus.getInstance().getSessKey());
		map.put("admin_id", MtqDalDeliveryBus.getInstance().getAdminID());

		map.put("duid", MtqDalDeliveryBus.getInstance().getDuid());
		map.put("apptype", MtqDalDeliveryBus.getInstance().getAppType());
		map.put("version", MtqDalDeliveryBus.getInstance().getVersion());
		return map;
	}

	/**
	 * post拼接方式
	 * 
	 * @param outMd5Source
	 *            外部决定参与签名的参数
	 * @param map
	 * @param url
	 * @return MtqSapReturn
	 */
	private static MtqSapReturn post(Map<String, Object> map,
			String outMd5Source, String url) {
		return MtqBaseParse.getCustPostParms(map, outMd5Source, getSvr() + url,
				MtqDalDeliveryBus.getInstance().getToken());
	}

	/**
	 * post拼接方式
	 * 
	 * @param map
	 * @param url
	 * @return MtqSapReturn
	 */
	private static MtqSapReturn post(Map<String, Object> map, String url) {
		return MtqBaseParse.getCustPostParms(map, getSvr() + url,
				MtqDalDeliveryBus.getInstance().getToken());
	}

	/**
	 * 获取货运宝企业版接口地址
	 * 
	 * @param 测试地址
	 *            （内网）：http://192.168.200.213/ktms/hybapp/
	 * @param 测试地址
	 *            （外网）：http://test.matiquan.cn:50301/ktms/hybapp/
	 * @param 正式地址
	 *            ：https://hy.careland.com.cn/hybapp/
	 * @return
	 */
	public static String getSvr() {
		String addr = "";
		if (CldBllUtil.getInstance().isTestVersion()) {
			addr = "http://test.matiquan.cn:50301/ktms/hybapp/";
			//addr = "http://192.168.200.213/ktms/hybapp/";
		} else {
			addr = "https://hy.careland.com.cn/hybapp/";
			       
		}
		return addr;
	}
}
