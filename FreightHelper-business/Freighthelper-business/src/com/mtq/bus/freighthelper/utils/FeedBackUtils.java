package com.mtq.bus.freighthelper.utils;

public class FeedBackUtils {

	/**
	 * @Title: getFdType
	 * @Description: 获取反馈类型
	 * @param type
	 *            1-平台类，2-硬件类
	 * @return: String
	 */
	public static String getFdType(int type) {
		String typeStr = "";
		switch (type) {
		case 1: {
			typeStr = "平台类";
			break;
		}
		case 2: {
			typeStr = "硬件类";
			break;
		}
		default:
			break;
		}
		return typeStr;
	}
	
	/**
	 * @Title: getDeviceType
	 * @Description: 获取设备类型
	 * @param type 2-北斗双模一体机，3-凯立德KPND，4-TD-BOX，5-TD-PND
	 * @return: String
	 */
	public static String getDeviceType(int type) {
		String devicetype = "";
		switch (type) {
		case 2: {
			devicetype = "北斗双模一体机";
			break;
		}
		case 3: {
			devicetype = "凯立德KPND";
			break;
		}
		case 4: {
			devicetype = "TD-BOX";
			break;
		}
		case 5: {
			devicetype = "TD-PND";
			break;
		}
		default:
			break;
		}
		return devicetype;
	}

}
