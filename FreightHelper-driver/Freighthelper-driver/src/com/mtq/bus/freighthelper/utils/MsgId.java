package com.mtq.bus.freighthelper.utils;

public class MsgId {
	public static final int DEFAULT = 1024;
	/**
	 * 登录
	 */
	public static final int MSGID_LOGIN_SUCCESS = DEFAULT + 1;
	public static final int MSGID_LOGIN_FAILED = DEFAULT + 2;
	/**
	 * 登出
	 */
	public static final int MSGID_LOGOUT_SUCCESS = DEFAULT + 3;
	public static final int MSGID_LOGOUT_FAILED = DEFAULT + 4;
	
	/**
	 * 车辆状态列表
	 */
	public static final int MSGID_GET_CARSTATE_LIST_SUCCESS = DEFAULT + 5;
	public static final int MSGID_GET_CARSTATE_LIST_EMPTY = DEFAULT + 6;
	public static final int MSGID_GET_CARSTATE_LIST_FAILED = DEFAULT + 7;
	/**
	 * 车辆状态统计
	 */
	public static final int MSGID_GET_CARSTATE_COUNT_SUCCESS = DEFAULT + 8;
	public static final int MSGID_GET_CARSTATE_COUNT_FAILED = DEFAULT + 9;
	/**
	 * 车辆实时状态数据
	 */
	public static final int MSGID_GET_CARSTATE_DETAIL_SUCCESS = DEFAULT + 10;
	public static final int MSGID_GET_CARSTATE_DETAIL_FAILED = DEFAULT + 11;
	/**
	 * 车辆当日运单数据
	 */
	public static final int MSGID_GET_TASK_STORE_SUCCESS = DEFAULT + 12;
	public static final int MSGID_GET_TASK_STORE_EMPTY = DEFAULT + 13;
	public static final int MSGID_GET_TASK_STORE_FAILED = DEFAULT + 14;
	/**
	 * 车辆当日行程数据
	 */
	public static final int MSGID_GET_TASK_NAVI_SUCCESS = DEFAULT + 15;
	public static final int MSGID_GET_TASK_NAVI_EMPTY = DEFAULT + 16;
	public static final int MSGID_GET_TASK_NAVI_FAILED = DEFAULT + 17;
	/**
	 * 车辆调度
	 */
	public static final int MSGID_SET_CARSEND_SUCCESS = DEFAULT + 18;
	public static final int MSGID_SET_CARSEND_FAILED = DEFAULT + 19;
	/**
	 * 历史轨迹
	 */
	public static final int MSGID_GET_TRACK_HISTORY_SUCCESS = DEFAULT + 20;
	public static final int MSGID_GET_TRACK_HISTORY_EMPTY = DEFAULT + 21;
	public static final int MSGID_GET_TRACK_HISTORY_FAILED = DEFAULT + 22;
	
	/**
	 * 警情消息
	 */
	public static final int MSGID_GET_MSG_ALARM_SUCCESS = DEFAULT + 23;
	public static final int MSGID_GET_MSG_ALARM_EMPTY = DEFAULT + 24;
	public static final int MSGID_GET_MSG_ALARM_FAILED = DEFAULT + 25;
	/**
	 * 更改警情消息已读状态
	 */
	public static final int MSGID_SET_MSG_ALARM_READ_SUCCESS = DEFAULT + 26;
	public static final int MSGID_SET_MSG_ALARM_READ_FAILED = DEFAULT + 27;
	
	public static final int MSGID_BATCH_SET_ALARM_MSG_READ_SUCCESS = DEFAULT + 101;
	public static final int MSGID_BATCH_SET_ALARM_MSG_READ_FAILED = DEFAULT + 102;
	
	/**
	 * 下拉最新的系统消息
	 */
	public static final int MSGID_PULL_SYS_MSG_SUCCESS = DEFAULT + 201;
	public static final int MSGID_PULL_SYS_MSG_EMPTY = DEFAULT + 202;
	public static final int MSGID_PULL_SYS_MSG_FAILED = DEFAULT + 203;
	
	/**
	 * 获取历史系统消息
	 */
	public static final int MSGID_GET_HISTORY_SYS_MSG_SUCCESS = DEFAULT + 28;
	public static final int MSGID_GET_HISTORY_SYS_MSG_EMPTY = DEFAULT + 29;
	public static final int MSGID_GET_HISTORY_SYS_MSG_FAILED = DEFAULT + 30;
	
	/**
	 * 获取下一页历史系统消息
	 */
	//public static final int MSGID_GET_MSG_SYS_SUCCESS = DEFAULT + 28;
	//public static final int MSGID_GET_MSG_SYS_EMPTY = DEFAULT + 29;
	//public static final int MSGID_GET_MSG_SYS_FAILED = DEFAULT + 30;
	
	
	
	public static final int MSGID_UPDATE_UNREAD = DEFAULT + 108;
	
	/**
	 * 更改系统消息已读状态
	 */
	public static final int MSGID_SET_MSG_SYS_READ_SUCCESS = DEFAULT + 31;
	public static final int MSGID_SET_MSG_SYS_READ_FAILED = DEFAULT + 32;
	/**
	 * 批量更改系统消息已读状态
	 */
	public static final int MSGID_BATCH_SET_SYS_MSG_READ_SUCCESS = DEFAULT + 103;
	public static final int MSGID_BATCH_SET_SYS_MSG_READ_FAILED = DEFAULT + 104;
	
	public static final int MSGID_BATCH_SET_ALARM_MSG_READ = DEFAULT + 105;
	public static final int MSGID_BATCH_SET_SYS_MSG_READ = DEFAULT + 106;
	
	/**
	 * 车辆列表
	 */
	public static final int MSGID_GET_MYCAR_LIST_SUCCESS = DEFAULT + 33;
	public static final int MSGID_GET_MYCAR_LIST_EMPTY = DEFAULT + 34;
	public static final int MSGID_GET_MYCAR_LIST_FAILED = DEFAULT + 35;
	/**
	 * 车辆详情
	 */
	public static final int MSGID_GET_MYCAR_DETAIL_SUCCESS = DEFAULT + 36;
	public static final int MSGID_GET_MYCAR_DETAIL_FAILED = DEFAULT + 37;
	/**
	 * 司机列表
	 */
	public static final int MSGID_GET_MYDRIVER_LIST_SUCCESS = DEFAULT + 38;
	public static final int MSGID_GET_MYDRIVER_LIST_EMPTY = DEFAULT + 39;
	public static final int MSGID_GET_MYDRIVER_LIST_FAILED = DEFAULT + 40;
	/**
	 * 司机详情
	 */
	public static final int MSGID_GET_MYDRIVER_DETAIL_SUCCESS = DEFAULT + 41;
	public static final int MSGID_GET_MYDRIVER_DETAIL_FAILED = DEFAULT + 42;
	/**
	 * 邀请加入车队
	 */
	public static final int MSGID_INVITE_DRIVER_SUCCESS = DEFAULT + 43;
	public static final int MSGID_INVITE_DRIVER_FAILED = DEFAULT + 44;
	
	/**
	 *  运单统计看板 
	 */
	public static final int MSGID_GET_ORDER_COUNT_SUCCESS = DEFAULT + 45;
	public static final int MSGID_GET_ORDER_COUNT_FAILED = DEFAULT + 46;
	/**
	 *  运单统计看板 
	 */
	public static final int MSGID_GET_TASK_COUNT_SUCCESS = DEFAULT + 47;
	public static final int MSGID_GET_TASK_COUNT_FAILED = DEFAULT + 48;
	
	/**
	 *  吐槽反馈
	 */
	public static final int MSGID_SET_FEEDBACK_SUCCESS = DEFAULT + 49;
	public static final int MSGID_SET_FEEDBACK_FAILED = DEFAULT + 50;
	/**
	 *  上传附件照片文件
	 */
	public static final int MSGID_UPLOAD_ATTACH_SUCCESS = DEFAULT + 51;
	public static final int MSGID_UPLOAD_ATTACH_FAILED = DEFAULT + 52;
	/**
	 *  获取硬件设备类型
	 */
	public static final int MSGID_GET_DTYPE_SUCCESS = DEFAULT + 53;
	public static final int MSGID_GET_DTYPE_FAILED = DEFAULT + 54;
	
	
	
}
