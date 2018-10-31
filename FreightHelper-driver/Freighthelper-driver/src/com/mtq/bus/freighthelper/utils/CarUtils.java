package com.mtq.bus.freighthelper.utils;

import com.mtq.bus.freighthelper.R;

public class CarUtils {

	/**
	 * @Title: getCarStatus
	 * @Description: 获取车辆状态
	 * @param status
	 *            车辆状态 (1-空闲，2-已派车，3-作业中，20-为维修保养)
	 * @return: String
	 */
	public static String getCarStatus(int status) {
		String carstatus = "";
		switch (status) {
		case 1: {
			carstatus = "空闲";
			break;
		}
		case 2: {
			carstatus = "已派车";
			break;
		}
		case 3: {
			carstatus = "作业中";
			break;
		}
		case 20: {
			carstatus = "维修保养";
			break;
		}
		default:
			break;
		}
		return carstatus;
	}

	/**
	 * @Title: getStatusColor
	 * @Description: 获取车辆状态颜色
	 * @param status
	 * @return: String
	 */
	public static int getStatusColor(int status) {
		int colorid = 0;
		switch (status) {
		case 1: {
			colorid = R.color.text_highlight_color;
			break;
		}
		case 2: {
			colorid = R.color.main_color;
			break;
		}
		case 3: {
			colorid = R.color.main_color;
			break;
		}
		case 20: {
			colorid = R.color.orange;
			break;
		}
		default:
			break;
		}
		return colorid;
	}

	/**
	 * @Title: getOrderStatus
	 * @Description: 获取订单状态
	 * @param status
	 *            订单状态(0待提交、1已提交，20已计划，21已发布，30待提货（作业中），
	 *            31提货中（作业中），40送货中（作业中），50已完成，51已取消)
	 * @return: String
	 */
	public static String getOrderStatus(int status) {
		String orderstatus = "";
		switch (status) {
		case 0: {
			orderstatus = "待提交";
			break;
		}
		case 1: {
			orderstatus = "已提交";
			break;
		}
		case 20: {
			orderstatus = "已计划";
			break;
		}
		case 21: {
			orderstatus = "已发布";
			break;
		}
		case 30: {
			orderstatus = "待提货";
			break;
		}
		case 31: {
			orderstatus = "提货中";
			break;
		}
		case 40: {
			orderstatus = "送货中";
			break;
		}
		case 50: {
			orderstatus = "已完成";
			break;
		}
		case 51: {
			orderstatus = "已取消";
			break;
		}
		default:
			break;
		}
		return orderstatus;
	}
	
	/**
	 * @Title: getSource
	 * @Description: 获取车辆来源
	 * @param sourceid
	 * @return: String
	 */
	public static String getSource(int sourceid) {
		String source = "";
		switch (sourceid) {
		case 1: {
			source = "自有车辆";
			break;
		}
		case 2: {
			source = "社会车辆";
			break;
		}	
		default:
			break;
		}
		return source;
	}
	
	/**
	 * @Title: getCarType
	 * @Description: 获取车型分类
	 * @param type 车型分类: 1-微型 2-轻型 3-中型 4-重型
	 * @return: String
	 */
	public static String getCarType(int type) {
		String cartype = "";
		switch (type) {
		case 1: {
			cartype = "微型";
			break;
		}
		case 2: {
			cartype = "轻型";
			break;
		}
		case 3: {
			cartype = "中型";
			break;
		}
		case 4: {
			cartype = "重型";
			break;
		}
		default:
			break;
		}
		return cartype;
	}
}
