package com.mtq.ols.module.deliverybus;

import java.util.List;

import com.mtq.ols.module.delivery.tool.CldKBaseParse.ProtBase;
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

public class MtqSapDeliveryBusParse {
	
	
	/*public static class ProtBase extends ProtBase{
		*//** 系统时间（UTC时间） *//*
		public int systime;
	}*/

	/**
	 * 登录信息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtLogin extends ProtBase {
		public List<String> actions;
		public MtqLogin login;
		public List<MtqGroup> groups;
	}

	/**
	 * 车辆状态
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtCarStateList extends ProtBase {
		public int pageindex;
		public int total;
		public List<MtqCarState> data;
	}

	/**
	 * 车辆状态统计
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtCarStateCount extends ProtBase {
		public MtqCarStateCount data;
	}

	/**
	 * 车辆实时状态数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtCarState extends ProtBase {
		public MtqCar car;
		public MtqState state;
	}

	/**
	 * 车辆当日运单数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtTaskStore extends ProtBase {
		public int pageindex;
		public int total;
		public List<MtqTaskStore> data;
	}

	/**
	 * 车辆当日运单数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtTaskNavi extends ProtBase {
		public int pageindex;
		public int total;
		public List<MtqTaskNavi> data;
	}

	/**
	 * 历史轨迹
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtTrackHistory extends ProtBase {
		public int alarmnum;
		public List<MtqTrackHistory> data;
	}

	/**
	 * 警情消息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtMsgAlarm extends ProtBase {
		public int pageindex;
		public String incrindex;
		public int total;
		public List<MtqMsgAlarm> data;
	}

	/**
	 * 系统消息
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtMsgSys extends ProtBase {
		public int pageindex;
		public String incrindex;
		public int total;
		public List<MtqMsgSys> data;
	}

	/**
	 * 车辆数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtCarData extends ProtBase {
		public int pageindex;
		public int total;
		public List<MtqCarData> data;
	}

	/**
	 * 车辆数据详情
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtCarDataDetail extends ProtBase {
		public int pageindex;
		public int total;
		public MtqCarDataDetail car;
		public List<MtqGroup> groups;
	}

	/**
	 * 司机数据
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtDriverData extends ProtBase {
		public int pageindex;
		public int total;
		public List<MtqDriver> data;
	}

	/**
	 * 司机数据详情
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtDriverDetail extends ProtBase {
		public MtqDriverDetail data;
	}
	
	/**
	 * 邀请司机加入车队
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtInviteDriver extends ProtBase {
		public int invitestatus;
	}

	/**
	 * 运单统计看板
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtOrderCount extends ProtBase {
		public MtqOrderCount data;
	}

	/**
	 * 运输计划统计看板
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtTaskCount extends ProtBase {
		public MtqTaskCount data;
	}
	
	/**
	 * 上传附件照片文件
	 * 
	 * @author zhaoqy
	 * @date 2017-06-09
	 */
	public static class ProtUploadAttachPic extends ProtBase {
		public String mediaid;
	}

	/**
	 * 硬件设备类型
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static class ProtDeviceDType extends ProtBase {
		public List<MtqDeviceDType> data;
	}
}
