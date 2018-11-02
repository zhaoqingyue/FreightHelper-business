/*
 * @Title CldSapKDeliveryParse.java
 * @Copyright Copyright 2010-2015 Careland Software Co,.Ltd All Rights Reserved.
 * @author Zhouls
 * @date 2015-12-9 下午12:56:12
 * @version 1.0
 */
package com.mtq.ols.module.delivery;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.text.TextUtils;

import com.cld.gson.Gson;
import com.cld.log.CldLog;
import com.cld.utils.CldAlg;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.AuthInfoList;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldCorpRouteInfo;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliCorpLimit;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliCorpWarning;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliGroup;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliSearchStoreResult;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliStore;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliStoreAuditInfo;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldDeliStorePos;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldElectfence;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.CldMonitorAuth;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.MtqCar;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.MtqCarRoute;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.MtqDeviceCar;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.MtqTask;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.MtqTrack;
import com.mtq.ols.module.delivery.CldSapKDeliveryParam.Navi;
import com.mtq.ols.module.delivery.bean.MtqDeliReceiveData;
import com.mtq.ols.module.delivery.bean.MtqDeliTask;
import com.mtq.ols.module.delivery.bean.MtqDeliTaskDetail;
import com.mtq.ols.module.delivery.tool.CldKBaseParse.ProtBase;
import com.mtq.ols.module.delivery.tool.CldPubFuction.CldDataUtils;
import com.mtq.ols.module.delivery.tool.CldShapeCoords;

/**
 * 解析层
 * 
 * @author Zhouls
 * @date 2015-12-9 下午12:56:12
 */
public class CldSapKDeliveryParse {

	/**
	 * 
	 * 新增鉴权
	 * @author Zhouls
	 * @date 2016-4-22 上午11:35:56
	 */
	public static class ProtMonitorAuth extends ProtBase {
		public ProtData data;

		public static class ProtData {
			public String coupon;
			public long add_time;
		}

		public void protParase(CldMonitorAuth bean) {
			if (null != bean && null != data) {
				bean.id = (data.coupon);
				bean.authTime = (data.add_time);
			}
		}
	}
	
	/**
	 * @annotation :企业权限信息
	 * @author : yuyh
	 * @date :2016-9-22上午10:48:27
	 * @parama :
	 * @return :
	 **/
	public static class ProtAuthInfoLst extends ProtBase {

		public List<ProtDatas> data;

		public static class ProtDatas {
			public String corpid;
			public String corpname;
			public int isadd;
			public int ismod;
			public int isread;
		}

		public void protParase(List<AuthInfoList> lstOfBean) {
			if (null != lstOfBean && null != data && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					if (null == data.get(i)) {
						continue;
					}
					AuthInfoList bean = new AuthInfoList();
					bean.corpid = (data.get(i).corpid);
					bean.corpname = (data.get(i).corpname);
					bean.isadd = (data.get(i).isadd);
					bean.ismod = (data.get(i).ismod);
					bean.isread = (data.get(i).isread);
					lstOfBean.add(bean);
				}
			}
		}
	}

	/**
	 * 
	 * 获取鉴权列表
	 * @author Zhouls
	 * @date 2016-4-22 上午11:36:09
	 */
	public static class ProtMonitorAuthLst extends ProtBase {

		public List<ProtDatas> datas;

		public static class ProtDatas {
			public String coupon;
			public String mobile;
			public String remark;
			public long expiry_time;
			public long add_time;
		}

		public void protParase(List<CldMonitorAuth> lstOfBean) {
			if (null != lstOfBean && null != datas && datas.size() > 0) {
				for (int i = 0; i < datas.size(); i++) {
					if (null == datas.get(i)) {
						continue;
					}
					CldMonitorAuth bean = new CldMonitorAuth();
					bean.id = (datas.get(i).coupon);
					bean.mobile = (datas.get(i).mobile);
					bean.mark = (datas.get(i).remark);
					bean.timeOut = (datas.get(i).expiry_time);
					bean.authTime = (datas.get(i).add_time);
					lstOfBean.add(bean);
				}
			}
		}
	}

	/**
	 * 
	 * 登录鉴权解析
	 * @author Zhouls
	 * @date 2016-4-22 上午11:41:15
	 */
	public static class ProtLoginAuth extends ProtBase {
		public long systime;
		public String syskey;
		public ProtData data;

		public static class ProtData {
			public int state;
			public String expiry_time;
			public int lockcorpid;
			public List<ProtDataGroup> groups;

			public void protParse(List<CldDeliGroup> lstOfMyGroups) {
				if (null != groups && groups.size() > 0
						&& null != lstOfMyGroups) {
					for (int i = 0; i < groups.size(); i++) {
						if (null != groups.get(i)
								&& null != groups.get(i).protParse()) {
							lstOfMyGroups.add(groups.get(i).protParse());
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * 车队信息
	 * @author Zhouls
	 * @date 2016-4-27 下午2:34:20
	 */
	public static class ProtDataGroup {
		public String corpid;
		public String corpname;
		public String group_id;
		public String group_name;
		public String contact;
		public String mobile;

		public CldDeliGroup protParse() {
			CldDeliGroup temp = new CldDeliGroup();
			temp.corpId = corpid;
			temp.corpName = corpname;
			temp.groupId = group_id;
			temp.groupName = group_name;
			temp.contact = contact;
			temp.mobile = mobile;
			return temp;
		}
	}

	/**
	 * 
	 * 无位置门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:40:38
	 */
	public static class ProtBaseStore {
		public String corpid;
		public String storeid;
		public String storecode;
		public String storename;
		public String storeaddr;
		public String linkman;
		public String linkphone;
		public String remark;
	}

	/**
	 * 
	 * 已审核门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:40:49
	 */
	public static class ProtStore extends ProtBaseStore {
		public int x;
		public int y;
		public int regioncode;
		public String regioncity;
		public String kcodestore;
	}

	/**
	 * 
	 * 待审核门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:41:03
	 */
	public static class ProtCheckStore extends ProtStore {
		public int approvestatus;
		public String approvestatustext;
	}

	public static class ProtNpStore extends ProtBaseStore {
		public int approvestatus;
		public String approvestatustext;
	}
	
	public static class ProtOrder extends ProtBase {
		public String cut_orderid;
		public String send_name;
		public String send_phone;
		public String send_addr;
		public String receive_name;
		public String receive_phone;
		public String receive_addr;
	}
	
	public static class ProtNavi extends ProtBase {
		public String serialid;
		public String starttime;
		public String endtime;
		public String mileage;
		public String traveltime;
		public List<ProtOrder> orderList;
	}
	
	/**
	 * 司机驾驶车辆信息
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class ProtCar extends ProtBase {
		public MtqCar car;
	}
	
	/**
	 * 行程日期
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class ProtTask extends ProtBase {
		public int total;
		public List<MtqTask> tasks;
	}
	
	/**
	 * 单天行程
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class ProtCarRoute extends ProtBase {
		public List<MtqCarRoute> cars;
	}
	
	/**
	 * 行程详情
	 * @author zhaoqy
	 * @date 2017-4-18
	 */
	public static class ProtTaskDetail extends ProtBase {
		
		public Navi navi;
		public List<MtqTrack> tracks;
	}
	
	public static class ProtTrack extends ProtBase {
		public String carduid;
		public String start_time;
		public String end_time;
		public String mileage;
		public List<ProtPos> posList;
	}
	
	public static class ProtPos extends ProtBase {
		public String x;
		public String y;
		public String speed;
		public String direction;
		public String time;
		public String pos_type;
		public String park_time;
	}
	
	public static class ProtDeviceCar extends ProtBase {
		public MtqDeviceCar data;
	}
	
	public static class ProtUpdateDeviceCar extends ProtBase {
		public String systime;
	}
	
	/**
	 * 
	 * 搜索周边门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:42:51
	 */
	public static class ProtSearchNearStores extends ProtBase {
		public int page;
		public int pagecount;
		public int record;
		public List<ProtStore> data;

		public CldDeliSearchStoreResult protParse() {
			CldDeliSearchStoreResult result = new CldDeliSearchStoreResult();
			result.page = page;
			result.pagecount = pagecount;
			result.record = record;
			if (null != data && data.size() > 0) {
				List<CldDeliStore> lstOfStore = new ArrayList<CldDeliStore>();
				for (int i = 0; i < data.size(); i++) {
					ProtStore tempStore = data.get(i);
					if (null == tempStore) {
						continue;
					}
					CldDeliStore temp = new CldDeliStore();
					temp.corpId = tempStore.corpid;
					temp.storeId = tempStore.storeid;
					temp.storeCode = tempStore.storecode;
					temp.storeName = tempStore.storename;
					temp.storeAddr = tempStore.storeaddr;
					temp.linkMan = tempStore.linkman;
					temp.linkPhone = tempStore.linkphone;
					temp.remark = tempStore.remark;
					CldDeliStorePos pos = new CldDeliStorePos();
					pos.x = tempStore.x;
					pos.y = tempStore.y;
					pos.regionCode = tempStore.regioncode;
					pos.regionName = tempStore.regioncity;
					pos.kCode = tempStore.kcodestore;
					temp.pos = pos;
					lstOfStore.add(temp);
				}
				result.lstOfStores = lstOfStore;
			}
			return result;
		}
	}

	/**
	 * 
	 * 搜索门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:44:52
	 */
	public static class ProtSearchStores extends ProtBase {
		public int page;
		public int pagecount;
		public int record;
		public List<ProtCheckStore> data;

		public CldDeliSearchStoreResult protParse() {
			CldDeliSearchStoreResult result = new CldDeliSearchStoreResult();
			result.page = page;
			result.pagecount = pagecount;
			result.record = record;
			if (null != data && data.size() > 0) {
				List<CldDeliStore> lstOfStore = new ArrayList<CldDeliStore>();
				for (int i = 0; i < data.size(); i++) {
					ProtCheckStore tempStore = data.get(i);
					if (null == tempStore) {
						continue;
					}
					CldDeliStore temp = new CldDeliStore();
					temp.corpId = tempStore.corpid;
					temp.storeId = tempStore.storeid;
					temp.storeCode = tempStore.storecode;
					temp.storeName = tempStore.storename;
					temp.storeAddr = tempStore.storeaddr;
					temp.linkMan = tempStore.linkman;
					temp.linkPhone = tempStore.linkphone;
					temp.remark = tempStore.remark;
					CldDeliStorePos pos = new CldDeliStorePos();
					pos.x = tempStore.x;
					pos.y = tempStore.y;
					pos.regionCode = tempStore.regioncode;
					pos.regionName = tempStore.regioncity;
					pos.kCode = tempStore.kcodestore;
					temp.pos = pos;
					CldDeliStoreAuditInfo auditInfo = new CldDeliStoreAuditInfo();
					auditInfo.auditStatus = tempStore.approvestatus;
					auditInfo.auditStatusText = tempStore.approvestatustext;
					temp.auditInfo = auditInfo;
					lstOfStore.add(temp);
				}
				result.lstOfStores = lstOfStore;
			}
			return result;
		}
	}

	/**
	 * 
	 * 搜索无位置门店
	 * @author Zhouls
	 * @date 2016-4-22 下午4:45:04
	 */
	public static class ProtSearchNpStores extends ProtBase {
		public int page;
		public int pagecount;
		public int record;
		public List<ProtNpStore> data;

		public CldDeliSearchStoreResult protParse() {
			CldDeliSearchStoreResult result = new CldDeliSearchStoreResult();
			result.page = page;
			result.pagecount = pagecount;
			result.record = record;
			if (null != data && data.size() > 0) {
				List<CldDeliStore> lstOfStore = new ArrayList<CldDeliStore>();
				for (int i = 0; i < data.size(); i++) {
					ProtNpStore tempStore = data.get(i);
					if (null == tempStore) {
						continue;
					}
					CldDeliStore temp = new CldDeliStore();
					temp.corpId = tempStore.corpid;
					temp.storeId = tempStore.storeid;
					temp.storeCode = tempStore.storecode;
					temp.storeName = tempStore.storename;
					temp.storeAddr = tempStore.storeaddr;
					temp.linkMan = tempStore.linkman;
					temp.linkPhone = tempStore.linkphone;
					temp.remark = tempStore.remark;
					CldDeliStoreAuditInfo auditInfo = new CldDeliStoreAuditInfo();
					auditInfo.auditStatus = tempStore.approvestatus;
					auditInfo.auditStatusText = tempStore.approvestatustext;
					temp.auditInfo = auditInfo;
					lstOfStore.add(temp);
				}
				result.lstOfStores = lstOfStore;
			}
			return result;
		}
	}

	/**
	 * 
	 * 加入车队
	 * @author Zhouls
	 * @date 2016-4-27 下午2:36:14
	 */
	public static class ProtJoinGroup extends ProtBase {
		public List<ProtDataGroup> groups;

		public void protParse(List<CldDeliGroup> lstOfMyGroups) {
			if (null != groups && groups.size() > 0 && null != lstOfMyGroups) {
				for (int i = 0; i < groups.size(); i++) {
					if (null != groups.get(i)
							&& null != groups.get(i).protParse()) {
						lstOfMyGroups.add(groups.get(i).protParse());
					}
				}
			}
		}
	}

	/**
	 * 
	 * 获取未读任务条数
	 * @author Zhouls
	 * @date 2016-5-24 上午11:18:33
	 */
	public static class ProtGetUnReadTaskCount extends ProtBase {
		public int count;
	}

	/**
	 * 
	 * 获取未完成任务条数
	 * @author Zhouls
	 * @date 2016-5-24 上午11:18:45
	 */
	public static class ProtGetUnFinishTaskCount extends ProtBase {
		public List<ProtUnFinishData> counts;

		public static class ProtUnFinishData {
			public int status;
			public int count;
		}
	}

	/**
	 * 获取授权门店企业列表 
	 *
	 * @author Zhouls
	 * @date 2016-5-24 上午11:18:58
	 */
	public static class ProtGetAuthStoreList extends ProtBase {
		public List<ProtDataGroup> data;

		public void protParse(List<CldDeliGroup> listOfAuthGroups) {
			if (null != listOfAuthGroups && null != data && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					if (null != data.get(i)) {
						listOfAuthGroups.add(data.get(i).protParse());
					}
				}
			}
		}
	}

	/**
	 * 
	 * 获取运货单列表
	 * @author Zhouls
	 * @date 2016-4-27 下午4:32:34
	 */
	public static class ProtGetTaskList extends ProtBase {
		public List<MtqDeliTask> tasks;
	}

	/**
	 * 
	 * 获取运货单历史列表
	 * @author Zhouls
	 * @date 2016-4-27 下午4:34:11
	 */
	public static class ProtGetTaskHistoryList extends ProtBase {
		public int page;
		public int pagecount;
		public int total;
		public List<MtqDeliTask> tasks;
	}

	public static class ProtGetTaskInfo extends ProtBase {
		public MtqDeliTaskDetail taskinfo;
	}

	public static class ProtUpdateTask extends ProtBase {
		public String corpid;
		public String taskid;
		public int status;
		
	}

	public static class ProtUpdateTaskStore extends ProtBase {
		public String corpid;
		public String taskid;
		public int status;
		public String storeid;
		public String waybill;
		public String ewaybill;
		public MtqDeliReceiveData data;
	}

	public static class ProtGetCorpLimitData extends ProtBase {
		public int truckcount;
		public int warningcount;
		public int truckversion;
		public int warningversion;

		public List<ProtCorpLimit> truck;
		public List<ProtCorpWarning> warning;

		public void protParse(List<CldDeliCorpLimit> lstOfLimit,
				List<CldDeliCorpWarning> lstOfWarning) {
			if (null != truck && truck.size() > 0 && null != lstOfLimit) {
				for (int i = 0; i < truck.size(); i++) {
					if (null != truck.get(i)) {
						lstOfLimit.add(truck.get(i).protParse());
					}
				}
			}
			if (null != warning && warning.size() > 0 && null != lstOfWarning) {
				for (int i = 0; i < warning.size(); i++) {
					if (null != warning.get(i)) {
						lstOfWarning.add(warning.get(i).protParse());
					}
				}
			}
		}
	}

	public static class ProtCorpWarning extends ProtBase {
		public int x;
		public int y;
		public int cell;
		public int uid;
		public String r;
		public String m;
		public String corpid;

		public CldDeliCorpWarning protParse() {
			CldDeliCorpWarning temp = new CldDeliCorpWarning();
			temp.x = x;
			temp.y = y;
			temp.cellid = cell;
			temp.uid = uid;
			temp.roadName = r;
			temp.voiceContent = m;
			temp.corpid = corpid;
			return temp;
		}
	}

	public static class ProtCorpLimit extends ProtBase {
		public int x;
		public int y;
		public int cell;
		public int uid;
		public String r;
		public String m;
		public int limittype;
		public int prohibittype;
		public float limitweight;
		public float limitlong;
		public float limitwidth;
		public float limitheight;
		public String corpid;

		public CldDeliCorpLimit protParse() {
			CldDeliCorpLimit temp = new CldDeliCorpLimit();
			temp.x = x;
			temp.y = y;
			temp.cellid = cell;
			temp.uid = uid;
			temp.roadName = r;
			temp.voiceContent = m;
			temp.limitType = limittype;
			temp.prohibitType = prohibittype;
			temp.limitWeight = limitweight;
			temp.limitLong = limitlong;
			temp.limitWidth = limitwidth;
			temp.limitHeight = limitheight;
			temp.corpid = corpid;
			return temp;
		}
	}

	/**
	 * 
	 * Mst数据
	 * @author Zhouls
	 * @date 2016-4-29 下午6:01:17
	 */
	public static class ProtCorpMST extends ProtBase {
		public String corpid;
		public int existroute;
		public String debug;
		public CldCorpRouteInfo routeinfo;
	}

	/**
	 * 
	 * Mst业务数据头
	 * @author Zhouls
	 * @date 2016-4-29 下午6:00:52
	 */
	public static class ProtMstHeader {
		public byte[] flag = new byte[4]; // 预留4位标识符
		public long length; // MST业务数据包长度
		public long rns_length; // 六部线路规划数据包长度
		public long crc32;// MST业务数据包+六部规划数据包一起的效验值
		public static final int lenOfClass = 16;// 字节大小

		public void setData(byte[] in_byteData, int in_offset) {
			try {
				byte[] flag = new byte[4];
				System.arraycopy(in_byteData, in_offset, flag, 0, 4);

				byte[] byteArray = new byte[4];
				System.arraycopy(in_byteData, in_offset + 4, byteArray, 0, 4);
				length = CldDataUtils.bytes2Long(byteArray);

				System.arraycopy(in_byteData, in_offset + 8, byteArray, 0, 4);
				rns_length = CldDataUtils.bytes2Long(byteArray);

				System.arraycopy(in_byteData, in_offset + 12, byteArray, 0, 4);
				crc32 = CldDataUtils.bytes2Long(byteArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Mst业务数据
	 * @author Zhouls
	 * @date 2016-4-29 下午6:00:36
	 */
	public static class ProtMstBussData {
		public ProtCorpMST routeInfo;
		public byte[] rnsData;
	}

	/**
	 * 解析数据
	 * @param data
	 * @return
	 * @return ProtMstBussData
	 * @author Zhouls
	 * @date 2016-4-29 下午6:00:26
	 */
	public static ProtMstBussData covertCorpRouteData(byte[] data) {
		// 长度不够协议头 直接抛弃
		if (null == data || data.length <= 16) {
			return null;
		}
		// 解析头内容
		ProtMstHeader header = new ProtMstHeader();
		header.setData(data, 0);
		if (header.length <= 0 || header.rns_length <= 0
				|| header.length + header.rns_length != data.length - 16) {
			return null;
		}
		long crcCheck = header.crc32;
		byte[] checkArray = new byte[data.length - 16];
		System.arraycopy(data, 16, checkArray, 0, data.length - 16);
		long crc32 = CldAlg.getCrcValue(checkArray);
		if (crcCheck != crc32) {
			return null;
		}
		ProtMstBussData bussData = new ProtMstBussData();
		byte[] mstArray = new byte[(int) header.length];
		System.arraycopy(data, 16, mstArray, 0, (int) header.length);
		String mstJson = new String(mstArray);
		Gson gson = new Gson();
		try {
			bussData.routeInfo = gson.fromJson(mstJson, ProtCorpMST.class);
		} catch (Exception e) {
			e.printStackTrace();
			CldLog.e("ols", "parse routeInfo failed!use jsonObject");
			try {
				JSONObject mstJsonObeject = new JSONObject(mstJson);
				if (null != mstJsonObeject) {
					ProtCorpMST mstInfo = new ProtCorpMST();
					if (mstJsonObeject.has("errcode")) {
						mstInfo.errcode = mstJsonObeject.getInt("errcode");
					}
					if (mstJsonObeject.has("errmsg")) {
						mstInfo.errmsg = mstJsonObeject.getString("errmsg");
					}
					if (mstJsonObeject.has("corpid")) {
						mstInfo.corpid = mstJsonObeject.getString("corpid");
					}
					if (mstJsonObeject.has("isroute")) {
						mstInfo.existroute = mstJsonObeject.getInt("isroute");
					}
					if (mstJsonObeject.has("debug")) {
						mstInfo.debug = mstJsonObeject.getString("debug");
					}
					if (mstJsonObeject.has("routeinfo")) {
						String innerjson = mstJsonObeject
								.getString("routeinfo");
						if (!TextUtils.isEmpty(innerjson)
								&& innerjson.startsWith("{")) {
							mstInfo.routeinfo = gson.fromJson(mstJson,
									CldCorpRouteInfo.class);
						} else {
							mstInfo.routeinfo = null;
						}
					}
					bussData.routeInfo = mstInfo;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CldLog.e("ols", "use jsonObject failed!");
			}
		}
		byte[] rnsArray = new byte[(int) header.rns_length];
		System.arraycopy(data, 16 + (int) header.length, rnsArray, 0,
				(int) header.rns_length);
		bussData.rnsData = rnsArray;
		return bussData;
	}

	/**
	 * 
	 * 获取电子围栏
	 * @author Zhouls
	 * @date 2016-5-24 下午12:00:34
	 */
	public static class ProtGetElectfence extends ProtBase {
		public String corpid;
		public int count;
		public List<ProtElectfence> data;

		public void protParse(List<CldElectfence> lstOfEF) {
			if (null == lstOfEF || null == data) {
				return;
			}
			for (int i = 0; i < data.size(); i++) {
				ProtElectfence tempData = data.get(i);
				if (null == tempData || TextUtils.isEmpty(tempData.shape)) {
					continue;
				}
				CldElectfence temp = new CldElectfence();
				temp.id = tempData.ruleid;
				temp.alarmType = tempData.alarm;
				temp.count = tempData.count;
				temp.etime = tempData.etime;
				temp.stime = tempData.btime;
				temp.limitSpeed = tempData.speed;
				String[] tempShapes = tempData.shape.split(";");
				if (null == tempShapes || tempShapes.length != tempData.count) {
					continue;
				}
				List<CldShapeCoords> lstOfShape = new ArrayList<CldShapeCoords>();
				for (int j = 0; j < tempShapes.length; j++) {
					String tempS = tempShapes[j];
					if (TextUtils.isEmpty(tempS)) {
						continue;
					}
					String[] xy = tempS.split(",");
					if (null == xy || xy.length < 2) {
						continue;
					}
					try {
						int x = Integer.parseInt(xy[0]);
						int y = Integer.parseInt(xy[1]);
						lstOfShape.add(new CldShapeCoords(x, y));
					} catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				if (lstOfShape.size() <= 0) {
					continue;
				}
				temp.lstOfShapeCoords = lstOfShape;
				lstOfEF.add(temp);
			}
		}

		/**
		 * 
		 * 电子围栏
		 * @author Zhouls
		 * @date 2016-5-24 上午11:36:45
		 */
		public static class ProtElectfence {
			public String ruleid;
			public long btime;
			public long etime;
			public int speed;
			public int alarm;
			public int shptype;
			public int count;
			public String shape;
		}
	}
}
