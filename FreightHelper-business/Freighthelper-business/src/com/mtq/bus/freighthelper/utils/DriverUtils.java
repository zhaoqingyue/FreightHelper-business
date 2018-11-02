package com.mtq.bus.freighthelper.utils;

import com.mtq.bus.freighthelper.R;

public class DriverUtils {

	/**
	 * @Title: getInviteStatus
	 * @Description: 获取邀请状态
	 * @param status
	 *            邀请状态（1-未邀请，2-已读邀，3-同意，4-已拒绝，5-已退出)
	 * @return: String
	 */
	public static String getInviteStatus(int invitestatus) {
		String status = "";
		switch (invitestatus) {
		case 0: {
			status = "全部";
			break;
		}
		case 1: {
			status = "未邀请";
			break;
		}
		case 2: {
			status = "已邀请";
			break;
		}
		case 3: {
			status = "同意";
			break;
		}
		case 4: {
			status = "已拒绝";
			break;
		}
		case 5: {
			status = "已退出";
			break;
		}
		default:
			break;
		}
		return status;
	}

	/**
	 * @Title: getStatusColor
	 * @Description: 获取状态颜色
	 * @param status
	 * @return: String
	 */
	public static int getStatusColor(int status) {
		int colorid = 0;
		switch (status) {
		case 1: {
			colorid = R.color.orange;
			break;
		}
		case 2: {
			colorid = R.color.main_color;
			break;
		}
		case 3: {
			colorid = R.color.text_highlight_color;
			break;
		}
		case 4: {
			colorid = R.color.text_normal_color;
			break;
		}
		case 5: {
			colorid = R.color.text_normal_color;
			break;
		}
		default:
			break;
		}
		return colorid;
	}

}
