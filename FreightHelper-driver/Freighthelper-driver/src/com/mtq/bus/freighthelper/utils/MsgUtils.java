package com.mtq.bus.freighthelper.utils;

import android.text.TextUtils;

import com.cld.gson.Gson;
import com.cld.gson.JsonParseException;
import com.cld.gson.JsonParser;
import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_103;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_109;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_115;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_116;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_207;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_208;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_209;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_210;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_211;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_212;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_213;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_214;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_215;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_217;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_218;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_219;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_220;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_221;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_222;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_223;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_225;
import com.mtq.bus.freighthelper.bean.alarm.Alarm_233;

public class MsgUtils {

	public static final int ALARM_TYPE_CAR = 1;
	public static final int ALARM_TYPE_DEVICE = 2;
	public static final int ALARM_TYPE_BEHAVIOR = 3;

	public static boolean isHandledMsg(int alarmid) {
		if (alarmid == 101 || alarmid == 102 || alarmid == 103
				|| alarmid == 104 || alarmid == 105 || alarmid == 106
				|| alarmid == 107 || alarmid == 108 || alarmid == 109
				|| alarmid == 110 || alarmid == 111 || alarmid == 112
				|| alarmid == 113 || alarmid == 114 || alarmid == 115
				|| alarmid == 116 || alarmid == 118 || alarmid == 119
				|| alarmid == 120 || alarmid == 121 || alarmid == 122
				|| alarmid == 123 || alarmid == 124 || alarmid == 200
				|| alarmid == 201 || alarmid == 207 || alarmid == 208
				|| alarmid == 209 || alarmid == 210 || alarmid == 211
				|| alarmid == 212 || alarmid == 213 || alarmid == 214
				|| alarmid == 215 || alarmid == 216 || alarmid == 217
				|| alarmid == 218 || alarmid == 219 || alarmid == 220
				|| alarmid == 221 || alarmid == 222 || alarmid == 223
				|| alarmid == 224 || alarmid == 225 || alarmid == 230
				|| alarmid == 233 || alarmid == 300) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: getAlarmClassify
	 * @Description: 通过alarmid获取报警类别
	 * @param alarmid
	 * @return: String
	 */
	public static int getAlarmClassify(int alarmid) {
		int alarmType = 0;
		switch (alarmid) {
		case 101: {
			/**
			 * 设备紧急报警
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 102: {
			/**
			 * 车辆超速报警
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 103: {
			/**
			 * 疲劳驾驶报警
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 104: {
			/**
			 * 设备预警
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 105: {
			/**
			 * 设备定位模块故障
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 106: {
			/**
			 * 设备定位天线未接或被剪断
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 107: {
			/**
			 * 设备定位天线短路
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 108: {
			/**
			 * 设备电源电压低
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 109: {
			/**
			 * 设备断电报警
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 110: {
			/**
			 * 设备显示器故障
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 111: {
			/**
			 * 设备语音播报故障
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 112: {
			/**
			 * 摄像头故障
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 113: {
			/**
			 * 当天累计驾驶超时
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 114: {
			/**
			 * 超时停车
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 115: {
			/**
			 * 进出设定区域
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 116: {
			/**
			 * 进出设定路线
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 117: {
			/**
			 * 路段行驶时间不足/过长
			 */
			break;
		}
		case 118: {
			/**
			 * 偏离设定路线报警
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 119: {
			/**
			 * 车辆仪表盘车速表故障
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 120: {
			/**
			 * 车辆油量异常
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 121: {
			/**
			 * 车辆被盗
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 122: {
			/**
			 * 车辆非法点火
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 123: {
			/**
			 * 车辆非法位移
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 124: {
			/**
			 * 碰撞侧翻报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 200: {
			/**
			 * 车辆点火
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 201: {
			/**
			 * 车辆熄火
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 202: {
			/**
			 * 水平校准进行中
			 */
			break;
		}
		case 203: {
			/**
			 * 水平校准完成
			 */
			break;
		}
		case 204: {
			/**
			 * 方向校准进行中
			 */
			break;
		}
		case 205: {
			/**
			 * 方向校准完成
			 */
			break;
		}
		case 206: {
			/**
			 * 安装位置移动
			 */
			break;
		}
		case 207: {
			/**
			 * 急刹车
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 208: {
			/**
			 * 急加速
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 209: {
			/**
			 * 急变道
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 210: {
			/**
			 * 弯道加速
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 211: {
			/**
			 * 碰撞
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 212: {
			/**
			 * 频繁变道
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 213: {
			/**
			 * 烂路高速行驶
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 214: {
			/**
			 * 急转弯
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 215: {
			/**
			 * 翻车
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 216: {
			/**
			 * 异常震动
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 217: {
			/**
			 * 车门异常
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 218: {
			/**
			 * 胎压和手刹异常
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 219: {
			/**
			 * 水温报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 220: {
			/**
			 * 转速报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 221: {
			/**
			 * 电瓶电压报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 222: {
			/**
			 * 车辆故障报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 223: {
			/**
			 * 怠速报警
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 224: {
			/**
			 * 拖吊报警
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 225: {
			/**
			 * 终端异常
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 226: {
			/**
			 * 副机工作状态
			 */
			break;
		}
		case 227: {
			/**
			 * 终端MCU重启
			 */
			break;
		}
		case 228: {
			/**
			 * 车辆启动行驶
			 */
			break;
		}
		case 229: {
			/**
			 * 加油事件
			 */
			break;
		}
		case 230: {
			/**
			 * 车辆漏油
			 */
			alarmType = ALARM_TYPE_CAR;
			break;
		}
		case 231: {
			/**
			 * 发电开始事件
			 */
			break;
		}
		case 232: {
			/**
			 * 发电结束事件
			 */
			break;
		}
		case 233: {
			/**
			 * 超速报警
			 */
			alarmType = ALARM_TYPE_BEHAVIOR;
			break;
		}
		case 300: {
			/**
			 * SOS求助
			 */
			alarmType = ALARM_TYPE_DEVICE;
			break;
		}
		case 400: {
			/**
			 * 人车分离
			 */
			break;
		}
		case 401: {
			/**
			 * 未用推荐线路
			 */
			break;
		}
		case 402: {
			/**
			 * SIM卡状态提醒
			 */
			break;
		}
		default:
			break;
		}
		return alarmType;
	}

	/**
	 * @Title: getAlarmType
	 * @Description: 通过alarmid获取报警类型
	 * @param alarmid
	 * @return: String
	 */
	public static String getAlarmType(int alarmid) {
		String alarmType = "";
		switch (alarmid) {
		case 101: {
			alarmType = "设备紧急报警";
			break;
		}
		case 102: {
			alarmType = "车辆超速报警";
			break;
		}
		case 103: {
			alarmType = "疲劳驾驶报警";
			break;
		}
		case 104: {
			alarmType = "设备预警";
			break;
		}
		case 105: {
			alarmType = "设备定位模块故障";
			break;
		}
		case 106: {
			alarmType = "设备定位天线未接或被剪断";
			break;
		}
		case 107: {
			alarmType = "设备定位天线短路";
			break;
		}
		case 108: {
			alarmType = "设备电源电压低";
			break;
		}
		case 109: {
			alarmType = "设备断电报警";
			break;
		}
		case 110: {
			alarmType = "设备显示器故障";
			break;
		}
		case 111: {
			alarmType = "设备语音播报故障";
			break;
		}
		case 112: {
			alarmType = "摄像头故障";
			break;
		}
		case 113: {
			alarmType = "当天累计驾驶超时";
			break;
		}
		case 114: {
			alarmType = "超时停车";
			break;
		}
		case 115: {
			alarmType = "进出设定区域";
			break;
		}
		case 116: {
			alarmType = "进出设定路线";
			break;
		}
		case 117: {
			alarmType = "路段行驶时间不足/过长";
			break;
		}
		case 118: {
			alarmType = "偏离设定路线报警";
			break;
		}
		case 119: {
			alarmType = "车辆仪表盘车速表故障";
			break;
		}
		case 120: {
			alarmType = "车辆油量异常";
			break;
		}
		case 121: {
			alarmType = "车辆被盗";
			break;
		}
		case 122: {
			alarmType = "车辆非法点火";
			break;
		}
		case 123: {
			alarmType = "车辆非法位移";
			break;
		}
		case 124: {
			alarmType = "碰撞侧翻报警";
			break;
		}
		case 200: {
			alarmType = "车辆点火";
			break;
		}
		case 201: {
			alarmType = "车辆熄火";
			break;
		}
		case 202: {
			alarmType = "水平校准进行中";
			break;
		}
		case 203: {
			alarmType = "水平校准完成";
			break;
		}
		case 204: {
			alarmType = "方向校准进行中";
			break;
		}
		case 205: {
			alarmType = "方向校准完成";
			break;
		}
		case 206: {
			alarmType = "安装位置移动";
			break;
		}
		case 207: {
			alarmType = "急刹车";
			break;
		}
		case 208: {
			alarmType = "急加速";
			break;
		}
		case 209: {
			alarmType = "急变道";
			break;
		}
		case 210: {
			alarmType = "弯道加速";
			break;
		}
		case 211: {
			alarmType = "碰撞";
			break;
		}
		case 212: {
			alarmType = "频繁变道";
			break;
		}
		case 213: {
			alarmType = "烂路高速行驶";
			break;
		}
		case 214: {
			alarmType = "急转弯";
			break;
		}
		case 215: {
			alarmType = "翻车";
			break;
		}
		case 216: {
			alarmType = "异常震动";
			break;
		}
		case 217: {
			alarmType = "车门异常";
			break;
		}
		case 218: {
			alarmType = "胎压和手刹异常";
			break;
		}
		case 219: {
			alarmType = "水温报警";
			break;
		}
		case 220: {
			alarmType = "转速报警";
			break;
		}
		case 221: {
			alarmType = "电瓶电压报警";
			break;
		}
		case 222: {
			alarmType = "车辆故障报警";
			break;
		}
		case 223: {
			alarmType = "怠速报警";
			break;
		}
		case 224: {
			alarmType = "拖吊报警";
			break;
		}
		case 225: {
			alarmType = "终端异常";
			break;
		}
		case 226: {
			alarmType = "副机工作状态";
			break;
		}
		case 227: {
			alarmType = "终端MCU重启";
			break;
		}
		case 228: {
			alarmType = "车辆启动行驶";
			break;
		}
		case 229: {
			alarmType = "加油";
			break;
		}
		case 230: {
			alarmType = "车辆漏油";
			break;
		}
		case 231: {
			alarmType = "发电开始事件";
			break;
		}
		case 232: {
			alarmType = "发电结束事件";
			break;
		}
		case 233: {
			alarmType = "超速报警";
			break;
		}
		case 300: {
			alarmType = "SOS求助";
			break;
		}
		case 400: {
			alarmType = "人车分离";
			break;
		}
		case 401: {
			alarmType = "未用推荐线路";
			break;
		}
		case 402: {
			alarmType = "SIM卡状态提醒";
			break;
		}
		default:
			break;
		}
		return alarmType;
	}

	public static boolean isGoodJson(String json) {
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			CldLog.e("MsgUtils", "bad json: " + json);
			return false;
		}
	}

	public static String getAlarmDesc(int alarmid, String jsonStr) {
		CldLog.e("MsgUtils", "alarmid: " + alarmid);
		if (!isGoodJson(jsonStr))
			return "";		
		
		CldLog.e("MsgUtils", "json: " + jsonStr);
		Gson gson = new Gson();
		String alarmDesc = "";
		switch (alarmid) {
		case 101: {
			/**
			 * 设备紧急报警
			 */
			alarmDesc = "设备紧急报警，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 102: {
			/**
			 * 车辆超速报警
			 */
			alarmDesc = "车辆超速行驶";
			break;
		}
		case 103: {
			/**
			 * 疲劳驾驶报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_103 alarm_103 = null;
			alarm_103 = gson.fromJson(jsonStr, Alarm_103.class);
			float hour = alarm_103.duration / 3600;
			alarmDesc = "车辆持续驾驶" + hour + "小时";
			break;
		}
		case 104: {
			/**
			 * 设备预警
			 */
			alarmDesc = "设备预警，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 105: {
			/**
			 * 设备定位模块故障
			 */
			alarmDesc = "设备定位模块发生故障，可联系设备产商维修更换";
			break;
		}
		case 106: {
			/**
			 * 设备定位天线未接或被剪断
			 */
			alarmDesc = "设备定位天线未接或被剪断，请及时安排检查";
			break;
		}
		case 107: {
			/**
			 * 设备定位天线短路
			 */
			alarmDesc = "设备定位天线短路，可联系设备产商维修更换";
			break;
		}
		case 108: {
			/**
			 * 设备电源电压低
			 */
			alarmDesc = "设备电源电压低，可联系设备产商维修更换";
			break;
		}
		case 109: {
			/**
			 * 设备断电报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_109 alarm_109 = null;
			alarm_109 = gson.fromJson(jsonStr, Alarm_109.class);
			if (alarm_109.status == 0) {
				alarmDesc = "设备断电，将影响定位追踪，请及时检查";
			} else if (alarm_109.status == 1) {
				alarmDesc = "设备恢复供电";
			}
			break;
		}
		case 110: {
			/**
			 * 设备显示器故障
			 */
			alarmDesc = "设备显示器出现故障";
			break;
		}
		case 111: {
			/**
			 * 设备语音播报故障
			 */
			alarmDesc = "设备语音播报出现故障";
			break;
		}
		case 112: {
			/**
			 * 摄像头故障
			 */
			alarmDesc = "摄像头出现故障，将影响行车记录，请及时安排检查";
			break;
		}
		case 113: {
			/**
			 * 当天累计驾驶超时
			 */
			alarmDesc = "车辆当天累计驾驶超时";
			break;
		}
		case 114: {
			/**
			 * 超时停车
			 */
			alarmDesc = "停车超时";
			break;
		}
		case 115: {
			/**
			 * 进出区域
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_115 alarm_115 = null;
			alarm_115 = gson.fromJson(jsonStr, Alarm_115.class);
			float hour = alarm_115.duration / 3600;
			switch (alarm_115.raildire) {
			case 0: {
				alarmDesc = "车辆进入设定区域/路线";
				break;
			}
			case 1: {
				alarmDesc = "车辆离开设定区域/路线";
				break;
			}
			case 2: {
				alarmDesc = "车辆偏离设定路线";
				break;
			}
			case 3: {
				alarmDesc = "车辆进入设定区域/路线" + hour + "小时";
				break;
			}
			case 4: {
				alarmDesc = "车辆离开设定区域/路线" + hour + "小时";
				break;
			}
			default:
				break;
			}
			break;
		}
		case 116: {
			/**
			 * 进出路线
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_116 alarm_116 = null;
			alarm_116 = gson.fromJson(jsonStr, Alarm_116.class);
			float hour = alarm_116.duration / 3600;
			switch (alarm_116.raildire) {
			case 0: {
				alarmDesc = "车辆进入设定区域/路线";
				break;
			}
			case 1: {
				alarmDesc = "车辆离开设定区域/路线";
				break;
			}
			case 2: {
				alarmDesc = "车辆偏离设定路线";
				break;
			}
			case 3: {
				alarmDesc = "车辆进入设定区域/路线" + hour + "小时";
				break;
			}
			case 4: {
				alarmDesc = "车辆离开设定区域/路线" + hour + "小时";
				break;
			}
			default:
				break;
			}
			break;
		}
		case 117: {
			/**
			 * 路段行驶时间不足/过长
			 */
			alarmDesc = "";
			break;
		}
		case 118: {
			/**
			 * 路线偏离报警
			 */
			alarmDesc = "";
			break;
		}
		case 119: {
			/**
			 * 盘车速表故障
			 */
			alarmDesc = "车辆仪表盘车速表故障，请及时安排检查";
			break;
		}
		case 120: {
			/**
			 * 车辆油量异常
			 */
			alarmDesc = "车辆油量异常，可能被盗或漏油，请及时安排检查";
			break;
		}
		case 121: {
			/**
			 * 车辆被盗
			 */
			alarmDesc = "车辆可能被盗，请及时安排检查确保车辆安全";
			break;
		}
		case 122: {
			/**
			 * 车辆非法点火
			 */
			alarmDesc = "车辆异常点火，请及时安排检查确保车辆安全";
			break;
		}
		case 123: {
			/**
			 * 车辆非法位移
			 */
			alarmDesc = "车辆发生异常移动，请及时安排检查确保车辆安全";
			break;
		}
		case 124: {
			/**
			 * 碰撞侧翻报警
			 */
			alarmDesc = "车辆可能发生侧翻，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 200: {
			/**
			 * 车辆点火
			 */
			alarmDesc = "车辆点火";
			break;
		}
		case 201: {
			/**
			 * 车辆熄火
			 */
			alarmDesc = "车辆熄火";
			break;
		}
		case 202: {
			/**
			 * 水平校准进行中
			 */
			alarmDesc = "";
			break;
		}
		case 203: {
			/**
			 * 水平校准完成
			 */
			alarmDesc = "";
			break;
		}
		case 204: {
			/**
			 * 方向校准进行中
			 */
			alarmDesc = "";
			break;
		}
		case 205: {
			/**
			 * 方向校准完成
			 */
			alarmDesc = "";
			break;
		}
		case 206: {
			/**
			 * 安装位置移动
			 */
			alarmDesc = "";
			break;
		}
		case 207: {
			/**
			 * 急刹车
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_207 alarm_207 = null;
			alarm_207 = gson.fromJson(jsonStr, Alarm_207.class);
			float speedbefore = alarm_207.speedbefore;
			float speedafter = alarm_207.speedafter;
			alarmDesc = "车辆发生急刹车，刹车前车速" + speedbefore + "Km/h，刹车后车速"
					+ speedafter + "Km/h";
			break;
		}
		case 208: {
			/**
			 * 急加速
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_208 alarm_208 = null;
			alarm_208 = gson.fromJson(jsonStr, Alarm_208.class);
			float speedbefore = alarm_208.speedbefore;
			float speedafter = alarm_208.speedafter;
			alarmDesc = "车辆发生急加速，加速前车速" + speedbefore + "Km/h，加速后车速"
					+ speedafter + "Km/h";
			break;
		}
		case 209: {
			/**
			 * 急变道
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_209 alarm_209 = null;
			alarm_209 = gson.fromJson(jsonStr, Alarm_209.class);
			String direction = "";
			if (alarm_209.direction == 0) {
				direction = "左";
			} else if (alarm_209.direction == 1) {
				direction = "右";
			}
			float speed = alarm_209.speed;
			alarmDesc = "车辆往" + direction + "快速变道，变道时车速" + speed + "Km/h";
			break;
		}
		case 210: {
			/**
			 * 弯道加速
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_210 alarm_210 = null;
			alarm_210 = gson.fromJson(jsonStr, Alarm_210.class);
			String direction = "";
			if (alarm_210.direction == 0) {
				direction = "左";
			} else if (alarm_210.direction == 1) {
				direction = "右";
			}
			float speedbefore = alarm_210.speedbefore;
			float speedafter = alarm_210.speedafter;
			alarmDesc = "车辆加速" + direction + "转弯，转弯前车速" + speedbefore
					+ "Km/h，转弯后车速" + speedafter + "Km/h";
			break;
		}
		case 211: {
			/**
			 * 碰撞
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_211 alarm_211 = null;
			alarm_211 = gson.fromJson(jsonStr, Alarm_211.class);
			float speedcollision = alarm_211.speedcollision;
			alarmDesc = "车辆可能发生碰撞，碰撞时车速" + speedcollision
					+ "Km/h，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 212: {
			/**
			 * 频繁变道
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_212 alarm_212 = null;
			alarm_212 = gson.fromJson(jsonStr, Alarm_212.class);
			int num = alarm_212.num;
			alarmDesc = "车辆连续变道" + num + "次";
			break;
		}
		case 213: {
			/**
			 * 烂路高速行驶
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_213 alarm_213 = null;
			alarm_213 = gson.fromJson(jsonStr, Alarm_213.class);
			float speed = alarm_213.speed;
			alarmDesc = "车辆烂路高速行驶，行驶车速" + speed + "Km/h";
			break;
		}
		case 214: {
			/**
			 * 急转弯
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_214 alarm_214 = null;
			alarm_214 = gson.fromJson(jsonStr, Alarm_214.class);
			String direction = "";
			if (alarm_214.direction == 0) {
				direction = "左";
			} else if (alarm_214.direction == 1) {
				direction = "右";
			}
			float speed = alarm_214.speed;
			alarmDesc = "车辆发生" + direction + "侧急转弯，转弯时车速" + speed + "Km/h";
			break;
		}
		case 215: {
			/**
			 * 翻车
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_215 alarm_215 = null;
			alarm_215 = gson.fromJson(jsonStr, Alarm_215.class);
			float speedturnedover = alarm_215.speedturnedover;
			alarmDesc = "车辆可能发生翻车，翻车时车速" + speedturnedover
					+ "Km/h，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 216: {
			/**
			 * 异常震动
			 */
			alarmDesc = "车辆发生异常振动，请及时安排检查，确保车辆安全";
			break;
		}
		case 217: {
			/**
			 * 车门异常
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_217 alarm_217 = null;
			alarm_217 = gson.fromJson(jsonStr, Alarm_217.class);
			String alarm = "";
			String status = alarm_217.status;
			if (status.length() > 1) {
				String[] sArray = status.split(",");
				int len = sArray.length;
				for (int i = 0; i < len; i++) {
					if (sArray[i].equals("0")) {
						alarm += "左前门";
					} else if (sArray[i].equals("1")) {
						alarm += "右前门";
					} else if (sArray[i].equals("2")) {
						alarm += "左后门";
					} else if (sArray[i].equals("3")) {
						alarm += "右后门";
					} else if (sArray[i].equals("4")) {
						alarm += "尾箱";
					}
					if (i < len - 1 && i > 0) {
						alarm += ",";
					}
				}
			} else if (status.length() == 1) {
				if (status.equals("0")) {
					alarm = "左前门";
				} else if (status.equals("1")) {
					alarm = "右前门";
				} else if (status.equals("2")) {
					alarm = "左后门";
				} else if (status.equals("3")) {
					alarm = "右后门";
				} else if (status.equals("4")) {
					alarm = "尾箱";
				}
			}
			alarmDesc = "车辆行驶中" + alarm + "未关闭，请及时提醒驾驶员";
			break;
		}
		case 218: {
			/**
			 * 胎压和手刹异常
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_218 alarm_218 = null;
			alarm_218 = gson.fromJson(jsonStr, Alarm_218.class);
			String alarm = "";
			String status = "";
			status = alarm_218.status;
			if (status.length() > 1) {
				String[] sArray = status.split(",");
				int len = sArray.length;
				for (int i = 0; i < len; i++) {
					if (sArray[i].equals("0")) {
						alarm += "左前轮胎压异常";
					} else if (sArray[i].equals("1")) {
						alarm += "右前轮胎压异常";
					} else if (sArray[i].equals("2")) {
						alarm += "左后轮胎压异常";
					} else if (sArray[i].equals("3")) {
						alarm += "右后轮胎压异常";
					} else if (sArray[i].equals("4")) {
						alarm += "手刹未放";
					}
					if (i < len - 1) {
						alarm += ",";
					}
				}
				alarmDesc = "车辆行驶中" + alarm + "，请及时提醒驾驶员";
			} else if (status.length() == 1) {
				if (status.equals("0")) {
					alarm = "左前轮胎压异常";
				} else if (status.equals("1")) {
					alarmDesc = "右前轮胎压异常";
				} else if (status.equals("2")) {
					alarmDesc = "左后轮胎压异常";
				} else if (status.equals("3")) {
					alarmDesc = "右后轮胎压异常";
				} else if (status.equals("4")) {
					alarmDesc = "手刹未放";
				}
			}
			alarmDesc = "车辆行驶中" + alarm + "，请及时提醒驾驶员";
			break;
		}
		case 219: {
			/**
			 * 水温报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_219 alarm_219 = null;
			alarm_219 = gson.fromJson(jsonStr, Alarm_219.class);
			String status = "";
			switch (alarm_219.status) {
			case 0: {
				status = "过低";
				break;
			}
			case 1: {
				status = "报警取消";
				break;
			}
			case 255: {
				status = "过高";
				break;
			}
			default:
				break;
			}
			int temperature = alarm_219.temperature;
			alarmDesc = "车辆冷却液温度" + status + "，水温为" + temperature + "℃";
			break;
		}
		case 220: {
			/**
			 * 转速报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_220 alarm_220 = null;
			alarm_220 = gson.fromJson(jsonStr, Alarm_220.class);
			int speed = alarm_220.speed;
			alarmDesc = "车辆转速达" + speed + "转";
			break;
		}
		case 221: {
			/**
			 * 电瓶电压报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_221 alarm_221 = null;
			alarm_221 = gson.fromJson(jsonStr, Alarm_221.class);
			String status = "";
			switch (alarm_221.status) {
			case 0: {
				status = "过低";
				break;
			}
			case 1: {
				status = "报警取消";
				break;
			}
			case 255: {
				status = "过高";
				break;
			}
			default:
				break;
			}
			alarmDesc = "车辆电频电压" + status + "，请注意";
			break;
		}
		case 222: {
			/**
			 * 车辆故障报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_222 alarm_222 = null;
			alarm_222 = gson.fromJson(jsonStr, Alarm_222.class);
			String alarm = "";
			int status = alarm_222.status;
			if (status == 0) {
				alarm = "发动机";
			} else if (status == 1) {
				alarm = "变速箱";
			} else if (status == 2) {
				alarm = "刹车";
			} else if (status == 3) {
				alarm = "气囊";
			} else if (status == 4) {
				alarm = "仪表板";
			} else if (status == 5) {
				alarm = "车身控制";
			} else if (status == 6) {
				alarm = "空调异常";
			}
			alarmDesc = "车辆" + alarm + "存在故障，请及时安排检查";
			break;
		}
		case 223: {
			/**
			 * 怠速报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_223 alarm_223 = null;
			alarm_223 = gson.fromJson(jsonStr, Alarm_223.class);
			int idleduration = alarm_223.idleduration / 60;
			alarmDesc = "车辆持续怠速" + idleduration + "分钟";
			break;
		}
		case 224: {
			/**
			 * 拖吊报警
			 */
			alarmDesc = "车辆可能被拖吊，请及时安排检查，确保车辆安全";
			break;
		}
		case 225: {
			/**
			 * 终端异常
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";
			Alarm_225 alarm_225 = null;
			alarm_225 = gson.fromJson(jsonStr, Alarm_225.class);
			switch (alarm_225.status) {
			case 0: {
				alarmDesc = "设备定位天线异常，将影响车辆追踪，请及时安排检查";
				break;
			}
			case 1: {
				alarmDesc = "设备定位天线回复正常";
				break;
			}
			case 2: {
				alarmDesc = "设备工作异常，将影响车辆追踪，请及时安排检查";
				break;
			}
			case 3: {
				alarmDesc = "设备恢复正常工作";
				break;
			}
			default:
				break;
			}
			break;
		}
		case 226: {
			/**
			 * 副机工作状态
			 */
			alarmDesc = "";
			break;
		}
		case 227: {
			/**
			 * 终端MCU重启
			 */
			alarmDesc = "";
			break;
		}
		case 228: {
			/**
			 * 车辆启动行驶
			 */
			alarmDesc = "";
			break;
		}
		case 229: {
			/**
			 * 加油事件
			 */
			alarmDesc = "";
			break;
		}
		case 230: {
			/**
			 * 漏油事件
			 */
			alarmDesc = "车辆可能漏油，请及时安排检查";
			break;
		}
		case 231: {
			/**
			 * 发电开始事件
			 */
			alarmDesc = "";
			break;
		}
		case 232: {
			/**
			 * 发电结束事件
			 */
			alarmDesc = "";
			break;
		}
		case 233: {
			/**
			 * OBD超速报警
			 */
			if (TextUtils.isEmpty(jsonStr))
				return "";

			Alarm_233 alarm_233 = null;
			alarm_233 = gson.fromJson(jsonStr, Alarm_233.class);
			int duration = alarm_233.duration / 60;
			float distance = alarm_233.distance / 1000;
			float speed = alarm_233.speed;
			alarmDesc = "车辆超速行驶" + duration + "分钟，超速行驶距离" + distance
					+ "千米，当前车速" + speed + "Km/h";
			break;
		}
		case 300: {
			/**
			 * SOS求助
			 */
			alarmDesc = "车辆发生紧急求助，请及时联系驾驶员，确保人员和车辆安全";
			break;
		}
		case 400: {
			/**
			 * 人车分离
			 */
			alarmDesc = "";
			break;
		}
		case 401: {
			/**
			 * 未用推荐线路
			 */
			alarmDesc = "";
			break;
		}
		case 402: {
			/**
			 * SIM卡状态提醒
			 */
			alarmDesc = "";
			break;
		}
		default:
			break;
		}
		return alarmDesc;
	}

}
