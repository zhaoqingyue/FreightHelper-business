/*
 * @Title CldMonitorAuth.java
 * @Copyright Copyright 2010-2015 Careland Software Co,.Ltd All Rights Reserved.
 * @author Zhouls
 * @date 2015-12-9 上午10:03:00
 * @version 1.0
 */
package com.mtq.ols.module.delivery;

import java.io.Serializable;
import java.util.List;

import com.cld.db.annotation.Column;
import com.cld.db.annotation.Id;
import com.cld.db.annotation.NoAutoIncrement;
import com.mtq.ols.module.delivery.tool.CldShapeCoords;

/**
 * 授权信息
 * 
 * @author Zhouls
 * @date 2015-12-9 上午10:03:00
 */
public class CldSapKDeliveryParam {

	/**
	 * 
	 * 授权列表
	 * 
	 * @author Zhouls
	 * @date 2015-12-9 下午12:57:11
	 */
	public static class CldMonitorAuth {
		/** 授权标识 */
		public String id;
		/** 授权手机号 */
		public String mobile;
		/** 备注 */
		public String mark;
		/** 过期时间 */
		public long timeOut;
		/** 授权时间 */
		public long authTime;
	}

	/**
	 * 
	 * 设置超时时间枚举
	 * 
	 * @author Zhouls
	 * @date 2015-12-9 上午11:24:16
	 */
	public static enum CldAuthTimeOut {
		oneday, threeday, sevenday, permanent;
		public static long valueOf(CldAuthTimeOut timeout) {
			switch (timeout) {
			case oneday:
				return 1 * 24 * 60 * 60;
			case threeday:
				return 3 * 24 * 60 * 60;
			case sevenday:
				return 7 * 24 * 60 * 60;
			case permanent:
				// 永久20年
				return 20 * 365 * 24 * 60 * 60;
			}
			return 20 * 365 * 24 * 60 * 60;
		}
	}

	/**
	 * 车队信息
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午12:15:42
	 */
	public static class CldDeliGroup {
		/** 企业ID */
		public String corpId;
		/** 公司名称 */
		public String corpName;
		/** 车队ID */
		public String groupId;
		/** 车队名称 */
		public String groupName;
		/** 联系人 */
		public String contact;
		/** 联系电话 */
		public String mobile;
	}

	/**
	 * 
	 * 收款信息上报
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午2:58:44
	 */
	public static class CldDeliReceiptParm {
		/** 企业ID（配送任务所属企业ID） */
		public String corpid;
		/** 配送任务ID */
		public String taskid;
		/** 门店ID */
		public String storeid;
		/** 配送单号 */
		public String waybill;
		/** 收款方式 */
		public String pay_method;
		/** 实收金额 */
		public float real_amount;
		/** 退货原因 */
		public String return_desc;
		/** 退货金额 */
		public float return_amount;
		/** 收款备注、说明 */
		public String pay_remark;
		/** 电子回单 */
		public byte[][] uploadPng;
		/** 客户单号**/
		public String cust_orderid ;
		/** 回单附加信息 **/
		public String e_receipts_0_ext;

		public CldDeliReceiptParm() {
			pay_method = null;
			real_amount = -1;
			return_desc = null;
			return_amount = -1;
			pay_remark = null;
			uploadPng = null;
			cust_orderid = null;
			e_receipts_0_ext = null;
		}
	}

	/**
	 * 
	 * 企业门店
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午4:55:25
	 */
	public static class CldDeliStore {
		/** 企业ID */
		public String corpId;
		/** 门店ID */
		public String storeId;
		/** 门店编码 */
		public String storeCode;
		/** 门店名称 */
		public String storeName;
		/** 门店地址 */
		public String storeAddr;
		/** 联系人 */
		public String linkMan;
		/** 联系人电话 */
		public String linkPhone;
		/** 门店说明 */
		public String remark;
		/** 位置信息 */
		public CldDeliStorePos pos;
		/** 审核信息 */
		public CldDeliStoreAuditInfo auditInfo;
	}

	/**
	 * 
	 * 企业门店位置信息
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午4:55:39
	 */
	public static class CldDeliStorePos {
		/** 门店位置x */
		public int x;
		/** 门店位置y */
		public int y;
		/** 区域编码 */
		public int regionCode;
		/** 区域名称 */
		public String regionName;
		/** 位置k码 */
		public String kCode;
	}

	/**
	 * 
	 * 企业门店审核信息
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午4:55:53
	 */
	public static class CldDeliStoreAuditInfo {
		/** 审核状态 */
		public int auditStatus;
		/** 审核状态说明 */
		public String auditStatusText;
	}

	/**
	 * 
	 * 搜索门店结果
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午5:06:15
	 */
	public static class CldDeliSearchStoreResult {
		/** 当前页 */
		public int page;
		/** 总页数 */
		public int pagecount;
		/** 总记录数 */
		public int record;
		/** 门店列表 */
		public List<CldDeliStore> lstOfStores;
	}

	/**
	 * 
	 * 标注门店
	 * 
	 * @author Zhouls
	 * @date 2016-4-22 下午6:06:49
	 */
	public static class CldDeliUploadStoreParm {
		/** 企业ID（门店所属的企业ID） */
		public String corpid;
		/** 上报类型 1-新增 2-纠错 */
		public int settype;
		/** 门店ID【上报类型为新增时为0】 */
		public String storeid;
		/** 门店名称 */
		public String storename;
		/** 门店地址 */
		public String address;
		/** 联系人 */
		public String linkman;
		/** 联系电话 */
		public String phone;
		/** 门店位置K码 */
		public String storekcode;
		/** 补充备注 */
		public String remark;
		/** 照片 */
		public byte[] uploadPng;
		/**类型（3-客户地址，1-配送中心，0-门店）**/
		public int iscenter;
	}

	/**
	 * 
	 * 运货单
	 * 
	 * @author Zhouls
	 * @date 2016-4-27 下午4:32:21
	 */
//	public static class CldDeliTask {
//		/** 配送任务ID */
//		public String taskid;
//		/** 送货日期 */
//		public long taskdate;
//		/** 发送时间 */
//		public long sendtime;
//		/** 运货状态【0待运货1运货中2已完成3暂停状态4中止状态 】 */
//		public int status;
//		/** 所属企业ID */
//		public String corpid;
//		/** 企业名称 */
//		public String corpname;
//		/** 运货点总数 */
//		public int store_count;
//		/** 已完成数量 */
//		public int finish_count;
//		/** 已读未读状态0未读 1 已读 */
//		public int readstatus;
//		/** 最近一次更新时间 */
//		public long dttime;
//	}
	

	/**
	 * @annotation :运货点明细
	 * @author : yuyh
	 * @date :2016-9-20上午9:04:19
	 * @parama :
	 * @return :
	 **/
	public static class CldDeliTaskOrders implements Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3527820282165481608L;
		/** 客户单号**/
		public String cust_orderid;
		/** 要求送达时间(起)**/
		public String reqtime;
		/** 要求送达时间(止)**/
		public String reqtime_e;
		/** 发货人**/
		public String send_name;
		/** 发货人电话**/
		public String send_phone;
		/** 发货人地址 **/
		public String send_addr;
		/** 发货人K码 **/
		public String send_kcode;
		/** 收货人**/
		public String receive_name;
		/** 收货人电话**/
		public String receive_phone;
		/** 收货人地址**/
		public String receive_addr;
		/** 收货人K码**/
		public String receive_kcode;
		/** 托运须知**/
		public String note;
		/** 已上传照片数 **/
		public String photo_nums;
		/** 已上传回单数 **/
		public String receipt_nums;
		/** 货物**/
		public List<Goods> goods;
	}
	
	/**
	 * @annotation :货物
	 * @author : yuyh
	 * @date :2016-9-20上午9:14:46
	 * @parama :
	 * @return :
	 **/
	public static class Goods implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6272346263251331927L;
		/**货物名称**/
		public String name;
		/**货物数量**/
		public String amount;
		/**单位**/
		public String unit;
		/**包裹？ **/
		public String pack;
		/**重量**/
		public String weight;
		/**体积**/
		public String volume;
	}
	
	/**
	 * @annotation :后台的车辆信息
	 * @author : yuyh
	 * @date :2016-10-28下午5:23:14
	 * @parama :
	 * @return :
	 **/
	public static class UMS_Carinfo implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -217515439900500190L;
		/** 车高级别 */
		public String tht;
		/** 车宽级别 */
		public String twh;
		/** 车重级别*/
		public String twt;
		/** 车型级别 */ 
		public String tvt;
		/** 车牌类型*/
		public String tlnt;
	}

	/**
	 * 
	 * 运货点
	 * 
	 * @author Zhouls
	 * @date 2016-4-27 下午4:59:12
	 */
	public static class CldDeliTaskStore implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8158680453468331686L;
		/** 配送单号 */
		public String waybill;
		/** 门店ID */
		public String storeid;
		/** 位置X(凯立德坐标) */
		public long storex;
		/** 位置Y(凯立德坐标) */
		public long storey;
		/** 运货点名称 */
		public String storename;
		/** 运货点地址 */
		public String storeaddr;
		/** 收货人 */
		public String linkman;
		/** 收货人电话 */
		public String linkphone;
		/** 送货顺序 */
		public int storesort;
		/** 完成时间（只有已完成才有值 */
		public long finishtime;
		/** 送货说明 */
		public String storemark;
		/** 送货状态（0-等待送货，1-正在配送中，2-已完成配送，3-暂停送货 ） */
		public int storestatus;
		/** 任务类型（1送货/3提货/4回程） */
		public int optype;
		/** 停车位置X(凯立德坐标) */
		public long stopx;
		/** 停车位置Y(凯立德坐标) */
		public long stopy;
		/** 是否需要收款（0-不需要，1-需要） */
		public int pay_mode;
		/** 收款方式（已选择的收款方式，没有则为空） */
		public String pay_method;
		/** 实收金额（已收到的金额，没有则为0） */
		public float real_amount;
		/** 应收金额（没有则为0） */
		public float total_amount;
		/** 退货原因（已选择的退货原因，没有则为空） */
		public String return_desc;
		/** 最近一次更新时间 */
		public long dttime;
		/** 客户单号**/
		public String cust_orderid;
		/** 是否需要回单 **/
		public String is_receipt;
		/** 运单助手链接 **/
		public String assist_url;

		/** 客户端维护 **/
		/** 本地送货状态（0-等待送货，1-正在配送中，2-已完成配送，3-暂停送货 ） */
		public int local_storestatus;
		/** 所属的运货单ID **/
		public String taskID;
		/** 收款补充说明 **/
		public String payRemark;
		/** 企业ID**/
		public String corpId;
		/** 下发时间 **/
		public String sendTime;
		/** 生成回单时间 **/
		public String receiptTime;
		/**orders信息**/
		public CldDeliTaskOrders orders;
	}

	/**
	 * 
	 * 运货单详情
	 * 
	 * @author Zhouls
	 * @date 2016-4-28 下午4:26:37
	 */
//	public static class CldDeliTaskDetail implements Serializable{
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -2706103986984298866L;
//		/** 配送任务ID */
//		public String taskid;
//		/** 送货日期 */
//		public String taskdate;
//		/** 送货状态 */
//		public int status;
//		/** 运货单最近一次更新时间 **/
//		public String ddtime;
//		/** 送货人员 */
//		public String sender;
//		/** 行程距离（米） */
//		public int distance;
//		/** 企业ID */
//		public String corpid;
//		/** 物流企业名称 */
//		public String corpname;
//		/** 是否返程（1为是，0为否，‘返程’即送完最后配送点后返回到配送中心） */
//		public int isback;
//		/** 当前页码 */
//		public int page;
//		/** 当前页条数 */
//		public int pagecount;
//		/** 已完成运货点数量 */
//		public int finishcount;
//		/** 运货点总数 */
//		public int total;
//		/** 车牌号 */
//		public String carlicense; 
//		/** 运货点列表 */
//		public List<CldDeliTaskStore> store;
//		/** 运货点明细*/
//		public List<CldDeliTaskOrders> orders;
//		/** 车辆信息*/
//		public UMS_Carinfo ums_carinfo;
//
//		/** 客户端维护 **/
//		/** 保存在本地的送货状态 */
//		public int local_status;
//
//		public long downUtcTime;
//
//		/** 配送中心 **/
//		public CldDeliveryTaskCentre taskCentre;
//
//	}

	/**
	 * 获取企业限行数据
	 * 
	 * @author Zhouls
	 * @date 2016-5-13 上午10:29:58
	 */
	public static class CldDeliCorpLimitParm {
		/** 获取方式【0-所有(默认)，1-只返回限行数据,2-只返回警示数据】 */
		public int req;
		/** 货车参数，车高级别 */
		public int tht;
		/** 货车参数，车宽级别 */
		public int twh;
		/** 货车参数，车重级别 */
		public int twt;
		/** 货车参数，车轴重级别 */
		public int tad;
		/** 货车参数，引擎对车型的枚举值 */
		public int tvt;

		public CldDeliCorpLimitParm() {
			req = 0;
			tht = -1;
			twh = -1;
			twt = -1;
			tad = -1;
			tvt = -1;
		}
	}

	/**
	 * 
	 * 路径上的企业限行参数
	 * 
	 * @author Zhouls
	 * @date 2016-5-14 下午4:31:18
	 */
	public static class CldDeliCorpLimitRouteParm extends CldDeliCorpLimitParm {
		/** 所在运货的企业ID */
		public String curTaskCorpId;
		/** 指定uid路段上的限行数据【多个uid用”|”分隔】(version>=3新增) */
		public String uid;
	}

	/**
	 * 
	 * 图面企业限行数据
	 * 
	 * @author Zhouls
	 * @date 2016-5-14 下午4:31:33
	 */
	public static class CldDeliCorpLimitMapParm extends CldDeliCorpLimitParm {
		/** 刷图面图幅框子 */
		public int minX;
		public int minY;
		public int maxX;
		public int maxY;

		public CldDeliCorpLimitMapParm() {
			minX = -1;
			minY = -1;
			maxX = -1;
			maxY = -1;
		}
	}

	/**
	 * 
	 * 限行数据
	 * 
	 * @author Zhouls
	 * @date 2016-4-28 下午4:48:27
	 */
	public static class CldDeliCorpLimit extends CldDeliCorpWarning {
		/** 类型（1为限行，2为禁行） */
		public int limitType;
		/** 禁行车型（0为不禁，1为所有，2为轻型及以上，3为中重型，4为重型） */
		public int prohibitType;
		/** 限重（吨，0为不限） */
		public float limitWeight;
		/** 限长（米，0为不限） */
		public float limitLong;
		/** 限宽（米，0为不限） */
		public float limitWidth;
		/** 限高（米，0为不限） */
		public float limitHeight;
		/**企业名称**/
		public String corpName ;

		public CldDeliCorpLimit() {
			limitWeight = -1;
			limitLong = -1;
			limitWidth = -1;
			limitHeight = -1;
		}
	}

	/**
	 * 
	 * 警示数据
	 * 
	 * @author Zhouls
	 * @date 2016-4-28 下午4:48:45
	 */
	public static class CldDeliCorpWarning {
		/** X坐标 */
		public int x;
		/** Y坐标 */
		public int y;
		/** 道路cell */
		public int cellid;
		/** 道路UID */
		public int uid;
		/** 道路名称 */
		public String roadName;
		/** 语音播报内容 */
		public String voiceContent;
		/** 所属企业ID */
		public String corpid;
		/**企业名称**/
		public String corpName;
	}

	/**
	 * 
	 * 企业线路详情
	 * 
	 * @author Zhouls
	 * @date 2016-4-29 下午4:20:36
	 */
	public static class CldCorpRouteInfo {
		/** 推荐线路ID */
		public int routeid;
		/** 线路标题 */
		public String title;
		/** 使用推荐线路导航ID */
		public int naviid;
		/** 所属企业ID */
		public String corpid;
		/** 限制速度(公里/小时)，0不限速 */
		public int limitspeed;
		/** 偏航范围(米)，0不限制 */
		public int yawrange;
		/** 线路备注说明 */
		public String remark;
	}

	/**
	 * 
	 * 缓存用户运货单阅读状态数据
	 * 
	 * @author Zhouls
	 * @date 2016-5-7 下午5:28:15
	 */
	public static class CldDeliTaskDB {
		@Id
		@NoAutoIncrement
		@Column(column = "id")
		public long id;// 企业Id+运货单Id唯一主键
		@Column(column = "kuid")
		public long kuid;// 运货单所属用户id
		@Column(column = "status")
		public int status;// 运货单是否已读 0未读 1已读
	}

	/**
	 * 
	 * 获取企业线路结果
	 * 
	 * @author Zhouls
	 * @date 2016-5-11 上午10:03:22
	 */
	public static class CldDeliCorpRoutePlanResult {
		/** 接口返回错误 */
		public int errCode;
		/** 接口返回错误描述 */
		public String errMsg;
		/** 企业ID */
		public String corpId;
		/** 是否有企业线路（1为有，0为无） */
		public int isRoute;
		/** 企业线路相关信息 */
		public CldCorpRouteInfo routeInfo;
		/** 路线规划结果 */
		public byte[] rpData;

		public CldDeliCorpRoutePlanResult() {
			errCode = -1;
			errMsg = "";
			routeInfo = null;
			rpData = null;
			isRoute = -1;
			corpId = "";
		}
	}

	/**
	 * 
	 * 电子围栏
	 * 
	 * @author Zhouls
	 * @date 2016-5-24 上午11:43:01
	 */
	public static class CldElectfence {
		/** id */
		public String id;
		/** 起始时间 */
		public long stime;
		/** 结束时间 */
		public long etime;
		/** 最大限速 */
		public int limitSpeed;
		/** 触发类型（1为在内,2为在外,3为从内到外,4为从外到内,5为进出） */
		public int alarmType;
		/** 形状点数量 */
		public int count;
		/** 形状点集合 */
		public List<CldShapeCoords> lstOfShapeCoords;
	}

	/**
	 * 
	 * 上报电子围栏参数
	 * 
	 * @author Zhouls
	 * @date 2016-5-24 下午2:23:45
	 */
	public static class CldUploadEFParm {
		/** 围栏规则所在企业id */
		public String corpid;
		/** 报警x */
		public int x;
		/** 报警y */
		public int y;
		/** 触发的围栏信息 */
		public List<CldDeliElectFenceUpload> lstOfLauchEF;
	}

	/**
	 * 
	 * 补报电子围栏参数
	 * 
	 * @author Zhouls
	 * @date 2016-5-30 上午9:21:19
	 */
	public static class CldReUploadEFParm {
		/** 触发的围栏信息 */
		public List<CldDeliElectFenceReUpload> lstOfLauchEF;
	}

	/**
	 * 
	 * 上报围栏对象
	 * 
	 * @author Zhouls
	 * @date 2016-5-30 上午9:09:37
	 */
	public static class CldDeliElectFenceUpload {
		/** 报警规则ID */
		public String rid;
		/** 报警类型【1为在内2为在外3为从内到外4为从外到内5为进出6超速】 */
		public int am;
		/** 上报类型【1-初始状态，2变化状态,3补报】 */
		public int at;
		/** 当前状态【1在内，2在外】 */
		public int st;
	}

	/**
	 * 
	 * 补报电子围栏对象
	 * 
	 * @author Zhouls
	 * @date 2016-5-30 上午9:15:47
	 */
	public static class CldDeliElectFenceReUpload extends
			CldDeliElectFenceUpload {
		/** 　企业ID（报警规则所属企业ID） */
		public String corpid;
		/** 　报警位置坐标X */
		public int x;
		/** 　报警位置坐标Y */
		public int y;
		/** 　报警时间【UTC时间】 */
		public long time;
	}
	
	public static class AuthInfoList
	{
		/**企业ID */
		public String corpid;
		/**企业名称*/
		public String corpname;
		/**是否有新增权限*/
		public int isadd;
		/**是否有修改权限*/
		public int ismod;
		/**是否有读取权限*/
		public int isread;
	}

	/**
	 * 配送中心
	 * 
	 * @author buxc
	 * @date 2015年8月19日 下午3:41:14
	 */
	public class CldDeliveryTaskCentre implements Serializable {

		private static final long serialVersionUID = 1L;
		private String storeId;
		private long storeX;
		private long storeY;
		private String storeName;
		private String storeAddr;
		private long stopX;
		private long stopY;

		public CldDeliveryTaskCentre(String sID, long sX, long sY,
				String sName, String sAddr, long stopX, long stopY) {
			storeId = sID;
			this.stopX = sX;
			this.stopY = sY;
			storeName = sName;
			storeAddr = sAddr;
			this.stopX = stopX;
			this.stopY = stopY;
		}

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}

		public long getStoreX() {
			return storeX;
		}

		public void setStoreX(long storeX) {
			this.storeX = storeX;
		}

		public long getStoreY() {
			return storeY;
		}

		public void setStoreY(long storeY) {
			this.storeY = storeY;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public String getStoreAddr() {
			return storeAddr;
		}

		public void setStoreAddr(String storeAddr) {
			this.storeAddr = storeAddr;
		}

		public long getStopX() {
			return stopX;
		}

		public void setStopX(long stopX) {
			this.stopX = stopX;
		}

		public long getStopY() {
			return stopY;
		}

		public void setStopY(long stopY) {
			this.stopY = stopY;
		}
	}
	
	/**
	 * 司机驾驶车辆信息
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class MtqCar {
		/** 车牌号码  */
		public String carlicense;
		/** 车辆设备ID */
		public String carduid;
		/** 货车类型  */
		public String carmodel;
		/** 车辆品牌  */
		public String brand;
		/** 车辆型号 */
		public String vehicletype;
		/** 设备类型名称  */
		public String devicename;
		/** 设备序列号  */
		public String mcuid;
		/** 设备SIM过期时间 */
		public String sim_endtime;
		
		public MtqNavi navi;
	}
	
	/**
	 * 行程日期
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class MtqTask {
		/** 配送任务ID */
		public String taskid;
		/** 物流公司ID */
		public String corpid;
		/** 开始送货时间 */
		public String starttime;
		/** 送货完成时间 */
		public String finishtime;
	}
	
	/**
	 * 单天行程
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class MtqCarRoute {
		/** 车牌号码  */
		public String carlicense;
		/** 车辆设备ID */
		public String carduid;
		/** 配送任务ID */
		public String taskid;
		/** 物流公司ID */
		public String corpid;
		/** 行程个数  */
		public String navicount;
		
		public List<MtqNavi> navis;
	}
	
	public static class MtqNavi {
		/** 行程记录ID  */
		public String serialid;
		/** 点火时间 */
		public String starttime;
		/** 熄火时间  */
		public String endtime;
		/** 总里程  */
		public String mileage;
		/** 总时长  */
		public String traveltime;
		
		public List<MtqOrder> orders;
	}
	
	@SuppressWarnings("serial")
	public static class MtqOrder implements Serializable {
		/** 客户单号  */
		public String cut_orderid;
		/** 发货人 */
		public String send_name;
		/** 发货人电话  */
		public String send_phone;
		/** 发货人地址  */
		public String send_addr;
		/** 收货人 */
		public String receive_name;
		/** 收货人电话  */
		public String receive_phone;
		/** 收货人地址  */
		public String receive_addr;
	}
	
	/**
	 * 行程详情
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class MtqTaskDetail {
		public Navi navi;
		
		public List<MtqTrack> tracks;
	}

	public static class Navi {
		/** 车辆设备ID */
		public String carduid;
		/** 点火时间  */
		public String starttime;
		/** 熄火时间  */
		public String endtime;
		/** 累计喷油量（升）  */
		public String fuelcon;
		/** 累计怠速耗油量（升）  */
		public String idlefuelcon;
		/** 总里程，单位：千米  */
		public String mileage;
		/** 行程最高车速（公里/时） */
		public String topspeed;
		/** 累计行驶时间（秒） */
		public String traveltime;
		/** 累计热车时间（秒） */
		public String warmedtime;
		/** 累计怠速时间（秒） */
		public String idletime;
		/** 行程内舒适度得分（分） */
		public String comfortscore;
	}
	
	public static class MtqTrack {
		/** 车辆设备ID  */
		public String carduid;
		/** 轨迹开始时间（UTC时间 */
		public String start_time;
		/** 轨迹结束时间（UTC时间） */
		public String end_time;
		/** 总里程，单位：千米  */
		public String mileage;
		
		public List<MtqPosData> pos_data;
	}
	
	public static class MtqPosData {
		/** X坐标  */
		public String x;
		/** Y坐标 */
		public String y;
		/** 即时速度（KM/H） */
		public String speed;
		/** 方向（正北）  */
		public String direction;
		/** 上报UTC时间戳 */
		public String time;
		/** 点类型  */
		public String pos_type;
		/** 停车时长  */
		public String park_time;
	}
	
	/**
	 * 获取设备的车辆信息
	 * 
	 * @author zhaoqy
	 * @date 2017-5-31
	 */
	public static class MtqDeviceCar {
		
		/** 车辆归属（1为个人车辆，2为企业车辆）*/
		public int owner;
		/** 车辆设备ID（为0时则无绑定设备，个人车辆为当前登录设备ID）*/
		public String carduid;
		/** 设备类型（1为JTT808，2为OBD007，3为TPND，0为其它）*/
		public int devicetype;
		/** 设备类型名称（与平台端的设备管理中设备类型一致）*/
		public String devicename;
		/** 车牌号  */
		public String carlicense;
		/** 品牌  */
		public String brand;
		/** 车辆类型  */
		public String carmodel;
		/** 车辆型号（0-未知; 1-微型; 2-轻型; 3-中型; 4-重型）  */
		public int cartype;
		/** 长（米）  */
		public String carlong;
		/** 宽（米）  */
		public String carwidth;
		/** 高（米）  */
		public String carheight;
		/** 轴数（轴）  */
		public int caraxle;
		/** 载重（吨）  */
		public String carweight;
		/** 车架号（后6位）  */
		public String carvin;
		/** 发动机号（后6位）  */
		public String carengine;
	}
}
