package com.mtq.ols.sap.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cld.gson.JsonArray;
import com.cld.gson.JsonObject;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfo;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfoNew;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqLogoList;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqLogoTips;
import com.mtq.ols.sap.parse.CldKBaseParse.ProtBase;
import com.mtq.ols.tools.CldSapParser;

/**
 * Kgo平台解析类
 * 
 * @author zhaoqy
 * @date 2017-5-25
 */
public class CldKAppCenterParse {

	public static MtqAppInfo parseAppInfo(String strData) {
		MtqAppInfo mtqAppInfo = null;
		JsonArray array = CldSapParser.fromJsonArray(strData, "data");
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				mtqAppInfo = new MtqAppInfo();
				if (array.get(i) != null) {
					if (array.get(i).isJsonObject()) {
						JsonObject json = (JsonObject) array.get(i);
						ProtAppInfo protAppinfo = CldSapParser.fromJson(
								json.toString(), ProtAppInfo.class);
						if (protAppinfo != null) {
							protAppinfo.protParse(mtqAppInfo);
						}
					}
				}
			}
		}
		return mtqAppInfo;
	}

	public static MtqAppInfoNew parseAppInfoForNew(String strData) {
		MtqAppInfoNew mtqAppInfo = null;
		JsonObject tmpobj = CldSapParser.fromJsonObject(strData, "upgrade",
				"upgrade_version");
		if (tmpobj != null) {
			mtqAppInfo = CldSapParser.fromJson(tmpobj.toString(),
					MtqAppInfoNew.class);
		}
		return mtqAppInfo;
	}

	public static void parseAppInfos(String strData,
			List<MtqAppInfo> lstOfResult) {
		JsonArray array = CldSapParser.fromJsonArray(strData, "data");
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				MtqAppInfo appinfo = new MtqAppInfo();
				if (array.get(i) != null) {
					if (array.get(i).isJsonObject()) {
						JsonObject json = (JsonObject) array.get(i);
						ProtAppInfo protAppinfo = CldSapParser.fromJson(
								json.toString(), ProtAppInfo.class);
						if (protAppinfo != null) {
							protAppinfo.protParse(appinfo);
							lstOfResult.add(appinfo);
						}
					}
				}
			}
		}
	}

	/**
	 * App信息解析
	 * 
	 * @author zhaoqy
	 * @date 2017-2-9
	 */
	public static class ProtAppInfo extends ProtBase {

		public String app_icon;
		public String app_name;
		public String ver_name;
		public String app_url;
		public String upgrade_desc;
		public int pack_size;
		public int ver_code;
		public String pack_name;
		public int quiesce;
		public int down_times;
		public int validate;
		public String app_desc;

		public void protParse(MtqAppInfo mtqApp) {
			mtqApp.app_icon = this.app_icon;
			mtqApp.app_name = this.app_name;
			mtqApp.ver_name = this.ver_name;
			mtqApp.app_url = this.app_url;
			mtqApp.upgrade_desc = this.upgrade_desc;
			mtqApp.pack_size = this.pack_size;
			mtqApp.ver_code = this.ver_code;
			mtqApp.pack_name = this.pack_name;
			mtqApp.quiesce = this.quiesce;
			mtqApp.down_times = this.down_times;
			mtqApp.validate = this.validate;
			mtqApp.app_desc = this.app_desc;
		}
	}

	public static class ProtAppStatus extends ProtBase {
		private ArrayList<Packname> packnames;

		public ArrayList<Packname> getPacknames() {
			return packnames;
		}

		public void setPacknames(ArrayList<Packname> packnames) {
			this.packnames = packnames;
		}

		class Packname {
			String packname;

			public String getPackname() {
				return packname;
			}

			public void setPackname(String packname) {
				this.packname = packname;
			}
		}
	}

	/**
	 * 开机logo和tips解析
	 * 
	 * @author zhaoqy
	 * @date 2017-5-25
	 */
	public static class ProtLogoTips extends ProtBase {

		public String rooturl;

		public List<ProtLogoList> logolist;

		public static class ProtLogoList {
			/** logo版本号 */
			public long logo_prover;
			/** tips版本号 */
			public long tips_prover;
			/** logo过期时间 */
			public long logo_timeout;
			/** tps过期时间 */
			public long tips_timeout;
			/** Logo图片地址 */
			public String logo_url;
			/** Tips图片地址列表 */
			public String[] tips_url;
			/** 停留时间(秒) */
			public int stop_time;
			/** 闪屏模式: 1-自动消失; 2-手动跳动 */
			public int pic_mode;
			/** 闪屏按钮: 1-圆形; 2-方形; 3-无按钮 */
			public int pic_button;

			public void protParse(MtqLogoList logo) {
				logo.logo_prover = this.logo_prover;
				logo.tips_prover = this.tips_prover;
				logo.logo_timeout = this.logo_timeout;
				logo.tips_timeout = this.tips_timeout;
				logo.logo_url = this.logo_url;
				logo.tips_url = this.tips_url;
				logo.stop_time = this.stop_time;
				logo.pic_mode = this.pic_mode;
				logo.pic_button = this.pic_button;
			}
		}

		public void protParse(MtqLogoTips logoTips) {
			logoTips.rooturl = this.rooturl;
			if (logolist != null && !logolist.isEmpty()) {
				if (logoTips.logolist != null) {
					logoTips.logolist = new ArrayList<MtqLogoList>();
				}
				for (int i = 0; i < logolist.size(); i++) {
					MtqLogoList logo = new MtqLogoList();
					ProtLogoList item = logolist.get(i);
					item.protParse(logo);
					logoTips.logolist.add(logo);
				}
			}
		}
	}

}
