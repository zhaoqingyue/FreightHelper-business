package com.mtq.ols.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cld.log.CldLog;
import com.mtq.ols.bll.CldBllUtil;
import com.mtq.ols.module.delivery.tool.CldSapParser;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfo;
import com.mtq.ols.sap.parse.CldKBaseParse;
import com.mtq.ols.tools.CldSapReturn;

public class CldSapKAppCenter {

	/** 公共输入参数. */
	private final static int APIVER = 1;
	/** The RSCHARSET. */
	private final static int RSCHARSET = 1;
	/** The RSFORMAT. */
	private final static int RSFORMAT = 1;
	/** The UMSAVER. */
	private final static int UMSAVER = 2;
	/** The ENCRYPT. */
	private final static int ENCRYPT = 0;
	/** 渠道号 */
	public static int CID;
	/** 程序版本号 */
	public static String PROVER;

	/** 操作系统编码 运营平台定义 */
	public static final int system_code = 1;
	/** 设备型号编码 运营平台定义 */
	public static final int device_code = 1;
	/** 产品型号编码 运营平台定义 */
	public static final int product_code = 2;

	public static String kgo_key = "";

	public static void setKgoKey(String key) {
		kgo_key = key;
		CldLog.i("ols", "kgo_key= " + kgo_key);
	}

	public static String getKgoKey() {
		return kgo_key;
	}

	/**
	 * 初始化密钥
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn initKeyCode() {
		String key = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		//map.put("cid", CID);
		//map.put("prover", PROVER);
		// map.put("encrypt", ENCRYPT);

		if (CldBllUtil.getInstance().isTestVersion()) {
			//key = "373275EB226022907CCA40BD2AE481D8";
			key = "BBA9B3328AD5D4DBEF5C756D3DBF335D";
			// key = "07CCA260229D340BD28AE48175EB2732";
		} else {
			//key = "373275EB226022907CCA40BD2AE481D8";
			key = "BBA9B3328AD5D4DBEF5C756D3DBF335D";
		}

		/*CldSapReturn errRes = CldKBaseParse
				.getGetParms(map, getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_code.php", key);*/
		CldSapReturn errRes = CldKBaseParse
				.getGetParms(map, getOperationPlatformHeadUrlForNew()
						+ "cm_pub/php/pub_get_code.php", key); 
		return errRes;
	}

	/**
	 * 应用自升级
	 * 
	 * @param width
	 *            分辨率宽
	 * @param height
	 *            分辨率高
	 * @param dpi
	 *            dpi
	 * @param systemVer
	 *            android 系统版本
	 * @param launcherVer
	 *            launcher版本号
	 * @param duid
	 *            duid
	 * @param kuid
	 *            kuid
	 * @param regionId
	 *            区域id
	 * @param customCode
	 *            客户编号
	 * @param planCode
	 *            方案商编号
	 * @param appParms
	 *            已安装app
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getUpgrade(int width, int height, int dpi,
			String systemVer, String launcherVer, long duid, long kuid,
			int regionId, int customCode, int planCode, String packname,
			int vercode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);
		map.put("system_code", system_code);
		map.put("device_code", device_code);
		map.put("product_code", product_code);
		map.put("width", width);
		map.put("height", height);
		map.put("dpi", dpi);
		map.put("system_ver", systemVer);
		map.put("page", 0);
		map.put("size", 0);
		map.put("launcher_ver", launcherVer);
		map.put("duid", duid);
		map.put("kuid", kuid);
		map.put("area_code", regionId);
		map.put("custom_code", customCode);
		map.put("plan_code", planCode);

		// appInfoList不加入计算sign
		String mymd5 = CldSapParser.formatSource(map);
		List<Map<String, Object>> applist = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapApp = null;
		mapApp = new HashMap<String, Object>();
		mapApp.put("packname", packname);
		mapApp.put("vercode", vercode);
		applist.add(mapApp);
		map.put("install_app", applist);

		// CldLog.i("ols", "key= " + kgo_key);
		CldSapReturn errRes = CldKBaseParse.getPostParms(map,
				getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_app_upgrade.php", kgo_key, mymd5);

		return errRes;
	}

	/**
	 * 检查已安装的app是否有升级
	 * 
	 * @param width
	 *            分辨率宽
	 * @param height
	 *            分辨率高
	 * @param dpi
	 *            dpi
	 * @param systemVer
	 *            android 系统版本
	 * @param page
	 *            页码
	 * @param size
	 *            每页记录数
	 * @param launcherVer
	 *            launcher版本号
	 * @param duid
	 *            duid
	 * @param kuid
	 *            kuid
	 * @param regionId
	 *            区域id
	 * @param customCode
	 *            客户编号
	 * @param planCode
	 *            方案商编号
	 * @param appParms
	 *            已安装app
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getAppsUpgrade(int width, int height, int dpi,
			String systemVer, int page, int size, String launcherVer,
			long duid, long kuid, int regionId, int customCode, int planCode,
			List<MtqAppInfo> appinfos) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);
		map.put("system_code", system_code);
		map.put("device_code", device_code);
		map.put("product_code", product_code);
		map.put("width", width);
		map.put("height", height);
		map.put("dpi", dpi);
		map.put("system_ver", systemVer);
		map.put("page", page);
		map.put("size", size);
		map.put("launcher_ver", launcherVer);
		map.put("duid", duid);
		map.put("kuid", kuid);
		map.put("area_code", regionId);
		map.put("custom_code", customCode);
		map.put("plan_code", planCode);

		// appInfoList不加入计算sign
		String mymd5 = CldSapParser.formatSource(map);
		List<Map<String, Object>> applist = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapApp = null;
		for (MtqAppInfo item : appinfos) {
			mapApp = new HashMap<String, Object>();
			mapApp.put("packname", item.pack_name);
			mapApp.put("vercode", "" + item.ver_code);
			applist.add(mapApp);
		}
		map.put("install_app", applist);

		CldSapReturn errRes = CldKBaseParse.getPostParms(map,
				getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_app_upgrade.php", kgo_key, mymd5);
		return errRes;
	}
	
	/**
	 * 应用自升级
	 * 
	 * @param width
	 *            分辨率宽
	 * @param height
	 *            分辨率高
	 * @param vercode
	 *            版本号
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getUpgradeForNew(int width, int height, int vercode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		
		map.put("apptype", getApptype());
		map.put("vercode", vercode);
		map.put("wrate", width);
		map.put("hrate", height);

		String mymd5 = CldSapParser.formatSource(map);
		CldSapReturn errRes = CldKBaseParse.getGetParms(map,
				getOperationPlatformHeadUrlForNew()
						+ "cm_pub/php/upgrade_check_version.php", mymd5,
				kgo_key);

		return errRes;
	}

	/**
	 * 获取运营平台推荐的应用列表(并排除终端上已安装的应用)
	 * 
	 * @param systemCode
	 *            操作系统编码(运营平台定义)
	 * @param deviceCode
	 *            设备型号编码(运营平台定义)
	 * @param productCode
	 *            产品型号编码(运营平台定义)
	 * @param width
	 *            分辨率宽
	 * @param height
	 *            分辨率高
	 * @param page
	 *            页码
	 * @param size
	 *            每页记录数
	 * @param launcherVer
	 *            launcher版本号
	 * @param appParms
	 *            已安装app
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getRecdApp(int width, int height, int page,
			int size, String launcherVer, List<MtqAppInfo> appinfos) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);
		map.put("system_code", system_code);
		map.put("device_code", device_code);
		map.put("product_code", product_code);
		map.put("width", width);
		map.put("height", height);
		map.put("page", page);
		map.put("size", size);
		map.put("launcher_ver", launcherVer);

		// appInfoList不加入计算sign
		String mymd5 = CldSapParser.formatSource(map);
		List<Map<String, Object>> applist = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapApp = null;
		for (MtqAppInfo item : appinfos) {
			mapApp = new HashMap<String, Object>();
			mapApp.put("packname", item.pack_name);
			applist.add(mapApp);
		}
		map.put("install_app", applist);

		CldSapReturn errRes = CldKBaseParse.getPostParms(map,
				getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_recommend_app.php", kgo_key, mymd5);
		return errRes;
	}

	/**
	 * 获取应用状态信息(返回已下架app的包名)
	 * 
	 * @param appParms
	 *            已安装app
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getAppStatus(List<MtqAppInfo> appinfos) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);

		// appInfoList不加入计算sign
		String mymd5 = CldSapParser.formatSource(map);
		List<Map<String, Object>> applist = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapApp = null;
		for (MtqAppInfo item : appinfos) {
			mapApp = new HashMap<String, Object>();
			mapApp.put("packname", item.pack_name);
			applist.add(mapApp);
		}
		map.put("install_app", applist);

		CldSapReturn errRes = CldKBaseParse.getPostParms(map,
				getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_app_status.php", kgo_key, mymd5);
		return errRes;
	}

	/**
	 * 更新应用下载次数
	 * 
	 * @param appParm
	 *            已安装app
	 * 
	 * @return CldSapReturn
	 */
	public static CldSapReturn getUpdateAppDowntimes(MtqAppInfo appinfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);
		map.put("packname", appinfo.pack_name);
		map.put("vercode", appinfo.ver_code);
		CldSapReturn errRes = CldKBaseParse.getGetParms(map,
				getOperationPlatformHeadUrl()
						+ "kgo/api/kgo_get_update_app_down_times.php", kgo_key);
		return errRes;
	}

	/**
	 * 获取车型列表
	 * 
	 * @return CldSapReturn
	 * @author zhaoqy
	 * @date 2017-2-10
	 */
	public static CldSapReturn getCarList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("umsaver", UMSAVER);
		map.put("rscharset", RSCHARSET);
		map.put("rsformat", RSFORMAT);
		map.put("apiver", APIVER);
		map.put("cid", CID);
		map.put("prover", PROVER);
		map.put("encrypt", ENCRYPT);
		map.put("useid", 2);

		CldSapReturn errRes = CldKBaseParse.getGetParms(map,
				getOperationPlatformHeadUrl() + "kgo/api/kgo_get_car_list.php",
				kgo_key);
		return errRes;
	}

	/**
	 * 获取闪屏、tips下载地址
	 * 
	 * @param apptype
	 * @param prover
	 *            launcher版本号
	 * @param width
	 * @param height
	 * @return CldSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-5-25
	 */
	public static CldSapReturn getSplash(int apptype, String prover, int width,
			int height) {
		String key = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("apptype", apptype);
		map.put("prover", prover);
		map.put("length", width);
		map.put("width", height);

		if (CldBllUtil.getInstance().isTestVersion()) {
			key = "F4A41A19D58AE8F7B7306FD30CB3F3FA";
		} else {
			key = "BB2CC2F71F01DF39A156E4F4FE56FEE5";
		}

		CldSapReturn errRes = CldKBaseParse
				.getGetParms(map, getOperationPlatformHeadUrl()
						+ "kgo/api/get_logo_tips_url.php", key);
		return errRes;
	}

	private static String getOperationPlatformHeadUrl() {
		if (CldBllUtil.getInstance().isTestVersion()) {
			return "http://tmctest.careland.com.cn/";
		} else {
			return "http://stat.careland.com.cn/";
		}
	}

	private static String getOperationPlatformHeadUrlForNew() {
		if (CldBllUtil.getInstance().isTestVersion()) {
			return "http://test.careland.com.cn/kz/";
		} else {
			return "http://pj.careland.com.cn/";
		}
	}

	public static int getApptype() {
		return CldBllUtil.getInstance().getApptype();
	}
}
