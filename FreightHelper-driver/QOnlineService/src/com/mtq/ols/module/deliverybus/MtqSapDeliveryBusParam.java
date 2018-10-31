package com.mtq.ols.module.deliverybus;

import java.io.Serializable;
import java.util.List;

public class MtqSapDeliveryBusParam {

	/**
	 * 登录信息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqLogin {
		/** 登录标识 */
		public String sesskey;
		/** 管理员用户ID */
		public int admin_id;
		/** 管理员用户名 */
		public String user_name;
		/** 用户姓名 */
		public String user_alias;
		/** 所属组织机构ID */
		public int org_id;
		/** 所属组织机构名称 */
		public String org_name;
		/** 接入密钥 */
		public String token;
	}

	/**
	 * 车队信息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqGroup {
		/** 车队ID */
		public int group_id;
		/** 车队名称 */
		public String group_name;
	}

	/**
	 * 车辆状态
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqCarState implements Serializable {
	
		private static final long serialVersionUID = -351130022385804944L;
		/** 车辆ID */
		public int carid;
		/** 车牌号码 */
		public String carlicense;
		/** 车辆状态(1空闲，2已派车，3作业中，20为维修保养) */
		public int carstatus;
		/** 最近定位坐标X */
		public int x;
		/** 最近定位坐标Y */
		public int y;
		/** 最近定位时间 */
		public int gpstime;
		/** 今日里程（米） */
		public int mileage;
	}

	/**
	 * 车辆状态统计
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqCarStateCount {
		/** 在线车辆数 */
		public int onlines;
		/** 今日总里程（公里） */
		public float mileage;
	}

	/**
	 * 车辆实时数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqCar {
		/** 车牌号码 */
		public String carlicense;
		/** 车辆状态(1空闲，2已派车，3作业中，20为维修保养) */
		public int carstatus;
		/** 今日里程（米） */
		public int mileage;
		/** 最近定位坐标X */
		public int x;
		/** 最近定位坐标Y */
		public int y;
		/** 最近定位时间 */
		public int gpstime;
		/** 主司机ID */
		public int mdriverid;
		/** 主司机姓名 */
		public String mdriver;
		/** 主司机手机号 */
		public String mphone;
		/** 副司机ID */
		public int sdriverid;
		/** 副司机姓名 */
		public String sdriver;
		/** 副司机手机号 */
		public String sphone;
	}

	/**
	 * 车辆状态数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqState {
		/** 车况更新时间（【行驶基础信息】） */
		public int time;
		/** 油箱剩余燃油（%，无效为-512，【行驶基础信息】） */
		public int surplusoil;
		/** 电瓶电压（伏，无效为-512，【行驶基础信息】） */
		public float batteryvoltage;
		/** 发动机转速（RPM，无效为-512，【行驶基础信息】） */
		public int enginespeed;
		/** 车辆速度（公里/时，无效为-512，【行驶基础信息】） */
		public int carspeed;
		/** 发动机冷却液温度（摄氏度，无效为-512，【行驶基础信息】） */
		public int enginecoolcent;
		/** 车辆累计行驶总里程（公里，【行驶基础信息】） */
		public int mileage;
		/** 瞬时油耗（L/100KM，无效为-512，【行驶扩展信息】） */
		public float instantfuel;
		/** 温度报警上限（车辆绑定设备的设置参数） */
		public int maxtempalarm;
		/** 温度报警下限（车辆绑定设备的设置参数） */
		public int mintempalarm;
		/** 传感器1温度（度，无效为-512，【温度传感信息】） */
		public int temperature1;
		/** 传感器2温度（度，无效为-512，【温度传感信息】） */
		public int temperature2;
		/** 传感器3温度（度，无效为-512，【温度传感信息】） */
		public int temperature3;
	}

	/**
	 * 车辆当日运单数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqTaskStore {
		/** 客户单号 */
		public String cut_orderid;
		/**
		 * 订单状态(0待提交、1已提交，20已计划，21已发布，30待提货（作业中），
		 * 31提货中（作业中），40送货中（作业中），50已完成，51已取消)
		 */
		public int orderstatus;
		/** 异常状态（0无异常，1有异常<不可继续完成>，2 有异常<可继续完成，不可重审核与删除>） */
		public int abnormal_status;
		/** 发货人地区 */
		public String send_regionname;
		/** 发货人地址 */
		public String send_address;
		/** 收货人地区 */
		public String receive_regionname;
		/** 收货人地址 */
		public String receive_address;
		/** 要求提货/送达时间 */
		public int receive_date;
		/** 货物总重量（吨） */
		public float goods_weight;
		/** 货物总体积（方） */
		public float goods_volume;
		/** 司机姓名 */
		public String driver_name;
		/** 司机手机号 */
		public String driver_phone;
	}

	/**
	 * 车辆当日行程数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqTaskNavi {
		/** 点火时间 */
		public int starttime;
		/** 熄火时间 */
		public int endtime;
		/** 本次行程总里程 */
		public float mileage;
		/** 本次行程总时长 */
		public int traveltime;
	}

	/**
	 * 历史轨迹
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqTrackHistory {
		/** 设备ID */
		public long duid;
		/** 点火时间 */
		public int starttime;
		/** 熄火时间 */
		public int endtime;
		/** 本次行程总里程，单位：千米 */
		public float mileage;
		/** 数据点 */
		public List<MtqPosData> pos_data;
	}

	/**
	 * 数据点
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqPosData {
		/** X坐标 */
		public int x;
		/** Y坐标 */
		public int y;
		/** 即时速度（km/h） */
		public int speed;
		/** 方向（正北） */
		public int direction;
		/** 上报UTC时间 */
		public int time;
		/** 点类型:-1：贴路补充点;0：轨迹点;1：起点 ;2：终点;3：停车点 */
		public int pos_type;
		/** 停车时长（分钟） */
		public int park_time;
	}

	/**
	 * 警情消息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqMsgAlarm implements Serializable {

		private static final long serialVersionUID = -644825787180782175L;
		/** 记录ID */
		public String id;
		/** 车辆ID */
		public int carid;
		/** 车牌号码 */
		public String carlicense;
		/** 经度（凯立德千分之一秒坐标） */
		public int x;
		/** 纬度（凯立德千分之一秒坐标） */
		public int y;
		/** GPS速度（公里/时） */
		public int speed;
		/** GPS方向（度） */
		public int direction;
		/** 报警ID（ */
		public int alarmid;
		/** 报警事件明细 */
		public String eventjson;
		/** 报警时间（UTC时间） */
		public int locattime;
		/** 司机姓名（当前司机） */
		public String driver_name;
		/** 司机手机号（当前司机） */
		public String driver_phone;
		/** 主司机姓名 */
		public String mdriver;
		/** 主司机手机号 */
		public String mphone;
		/** 副司机姓名 */
		public String sdriver;
		/** 副司机手机号 */
		public String sphone;
		/** 阅读状态（0未读1已读） */
		public int readstatus;
		
		public int alarmType;  
	}

	/**
	 * 系统消息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqMsgSys implements Serializable {
		
		private static final long serialVersionUID = -4067706869512346337L;
		/** 记录ID */
		public int serialid;
		/** 消息时间（UTC时间） */
		public int time;
		/** 标题 */
		public String title;
		/** 内容 */
		public String content;
		/** 发布者 */
		public String publisher;
		/** 阅读状态（0未读1已读） */
		public int readstatus;
	}

	/**
	 * 车辆数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqCarData {
		/** 车辆ID */
		public int carid;
		/** 车牌号码 */
		public String carlicense;
		/** 车辆来源ID（1-自有车辆 2-社会车辆） */
		public int sourceid;
		/** 当前司机姓名 */
		public String driver;
		/** 当前司机手机号码 */
		public String phone;
	}

	/**
	 * 车辆数据详情
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqCarDataDetail {
		/** 车牌号码 */
		public String carlicense;
		/** 车辆来源ID（1-自有车辆 2-社会车辆） */
		public int sourceid;
		/** 主司机姓名 */
		public String mdriver;
		/** 主司机手机号 */
		public String mphone;
		/** 副司机姓名 */
		public String sdriver;
		/** 副司机手机号 */
		public String sphone;
		/** 车辆类型（行驶证上的车辆类型） */
		public String carmodel;
		/** 车型分类（1-微型 2-轻型 3-中型 4-重型） */
		public int cartype;
		/** 设备类型（0为无绑定设备，2为北斗双模一体机，3为凯立德KPND，4为TD-BOX，5为TD-PND） */
		public int dtype;
		/** 设备类型名称 */
		public String dtypename;
		/** 设备型号 */
		public String model;
		/** 终端设备号 */
		public String idsn;
		/** SIM卡号 */
		public String idphone;
	}

	/**
	 * 司机数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqDriver {
		/** 司机ID */
		public int driverid;
		/** 司机姓名 */
		public String driver_name;
		/** 司机手机号码 */
		public String phone;
		/** 邀请状态（1未读邀请消息，2已读邀请消息，3同意加入车队，4拒绝加入车队，5已退出车队) */
		public int invitestatus;
	}

	/**
	 * 司机数据详情
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqDriverDetail {
		/** 司机姓名 */
		public String driver_name;
		/** 司机手机号码 */
		public String phone;
		/** 邀请状态（1未读邀请消息，2已读邀请消息，3同意加入车队，4拒绝加入车队，5已退出车队) */
		public int invitestatus;
		/** 司机身份证号码 */
		public String id_number;
		/** 紧急联系人1 */
		public String emergency_man1;
		/** 紧急联系人电话1 */
		public String emergency_phone1;
		/** 紧急联系人2 */
		public String emergency_man2;
		/** 紧急联系人电话1 */
		public String emergency_phone2;
	}

	/**
	 * 运单统计看板
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqOrderCount {
		/** 运单总数 */
		public int total;
		/** 待调度 */
		public int notsend;
		/** 待运输 */
		public int notbegin;
		/** 运输中 */
		public int transport;
		/** 已签收 */
		public int finished;
		/** 异常单 */
		public int abnormal;
	}

	/**
	 * 运输计划统计看板
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class MtqTaskCount {
		/** 运单总数 */
		public int total;
		/** 正常发车 */
		public int normal;
		/** 晚点发车 */
		public int late;
		/** 等待发车 */
		public int wait;
		/** 预计延误 */
		public int estidelay;
	}

	/**
	 * 硬件设备类型
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static class MtqDeviceDType {
		/** 设备类型（2为北斗双模一体机，3为凯立德KPND，4为TD-BOX，5为TD-PND） */
		public int dtype;
		/** 设备名称 */
		public String dname;
	}
}
