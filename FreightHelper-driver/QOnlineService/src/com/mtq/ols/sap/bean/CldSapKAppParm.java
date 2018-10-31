package com.mtq.ols.sap.bean;

import java.util.List;

import com.mtq.ols.sap.parse.CldKMessageParse.ProtRecMsg.ProtMsgType;


public class CldSapKAppParm {
	
	public static class MtqAppInfo {
		/** 应用图标URL */
		public String app_icon; 
		/** 应用名称 */
		public String app_name; 
		/** 应用版本名称 */
		public String ver_name; 
		/** apk下载地址 */
		public String app_url; 
		/** 更新描述 */
		public String upgrade_desc; 
		/** 安装包大小（单位：字节） */
		public int pack_size; 
		/** 应用版本编码 */
		public int ver_code; 
		/** 应用包名 */
		public String pack_name; 
		/** 是否静默安装 0：否; 1：是  */
		public int quiesce; 
		/** 下载次数 */
		public int down_times; 
		/** 是否需要验证 0：否; 1：是  */
		public int validate; 
		/** 应用描述 */
		public String app_desc; 
	}
	
	public static class MtqAppInfoNew {
		
		public int version;
		public int publishtime;
		public int filesize;
		//强制升级标识， 1：是；0：否
		public int upgradeflag;
		public int expiredtime;
		
		//下载标识，1：下载；2：删除
		public int downloadtype;
		//压缩标识，1：压缩；0：非压缩
		public int zipflag;
		public String filepath;
		public String mark;
	}
	
	public static class MtqLogoTips {
		/** 根路径 */
		public String rooturl; 
		
		public List<MtqLogoList> logolist;
	}
	
	public static class MtqLogoList {
		
		/** logo版本号  */
		public long logo_prover; 
		/** tips版本号  */
		public long tips_prover; 
		/** logo过期时间  */
		public long logo_timeout; 
		/** tps过期时间  */
		public long tips_timeout; 
		/** Logo图片地址  */
		public String logo_url; 
		/** Tips图片地址列表  */
		public String[] tips_url; 
		/** 停留时间(秒) */
		public int stop_time; 
		/** 闪屏模式: 1-自动消失; 2-手动跳动  */
		public int pic_mode; 
		/** 闪屏按钮: 1-圆形; 2-方形; 3-无按钮  */
		public int pic_button; 
	}
}
