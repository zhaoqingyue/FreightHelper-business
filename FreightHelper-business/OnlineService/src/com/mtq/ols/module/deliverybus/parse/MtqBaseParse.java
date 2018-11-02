package com.mtq.ols.module.deliverybus.parse;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.cld.log.CldLog;
import com.mtq.ols.module.delivery.tool.CldPubFuction.MD5Util;
import com.mtq.ols.sap.CldSapUtil;

public class MtqBaseParse {

	public static final String TAG = "deliverybus";
	
	/**
	 * 无密钥需要加密的、不需要签名的Post拼接方式
	 * 
	 * @param map
	 * @param urlHead
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getNoSignPostParms(Map<String, Object> map, String urlHead) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			/**
			 * map不为空
			 */
			errRes.url = urlHead;
			String strPostMap = "";
			Map<String, String> dataMap = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (!TextUtils.isEmpty(entry.getKey())
						&& null != entry.getValue()
						&& !TextUtils.isEmpty(entry.getValue().toString())) {
					CldLog.d("olscheckmap", entry.getKey() + "="
							+ entry.getValue().toString());
					dataMap.put(entry.getKey(), entry.getValue().toString());
					strPostMap = strPostMap + "&" + entry.getKey() + "="
							+ entry.getValue().toString();
				}
			}
			CldLog.i(TAG, "strPostMap: " + strPostMap);
			errRes.mapPost = dataMap;
		}
		return errRes;
	}

	/**
	 * postStringArray
	 * 
	 * @param map
	 * @param urlHead
	 * @param key
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getCustPostParms(Map<String, Object> map,
			String urlHead, String key) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			CldLog.i(TAG, "key: " + key);
			/**
			 * map不为空
			 */
			String md5Source = MtqSapParser.formatSource(map);
			if (!TextUtils.isEmpty(key)) {
				/**
				 * 密钥不为空，需要sign 加密验证
				 */
				md5Source += key;
				String sign = MD5Util.MD5(md5Source);
				map.put("sign", sign);
				CldLog.i(TAG, "sign: " + sign);
			}
			CldLog.i(TAG, "md5Source: " + md5Source);
			
			errRes.url = urlHead;
			String strPost = MtqSapParser.mapToJson(map);
			CldLog.i(TAG, "strPost: " + strPost);
			errRes.jsonPost = strPost;
			
			String strPostMap = "";
			Map<String, String> dataMap = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (!TextUtils.isEmpty(entry.getKey())
						&& null != entry.getValue()
						&& !TextUtils.isEmpty(entry.getValue().toString())) {
					CldLog.d("olscheckmap", entry.getKey() + "="
							+ entry.getValue().toString());
					dataMap.put(entry.getKey(), entry.getValue().toString());
					strPostMap = strPostMap + "&" + entry.getKey() + "="
							+ entry.getValue().toString();
				}
			}
			CldLog.i(TAG, "strPostMap: " + strPostMap);
			errRes.mapPost = dataMap;
		}
		return errRes;
	}

	/**
	 * postStringArray
	 * 
	 * @param map
	 * @param outMd5Source
	 * @param urlHead
	 * @param key
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getCustPostParms(Map<String, Object> map,
			String outMd5Source, String urlHead, String key) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			CldLog.i(TAG, "key: " + key);
			/**
			 * map不为空
			 */
			String md5Source = (!TextUtils.isEmpty(outMd5Source)) ? outMd5Source
					: MtqSapParser.formatSource(map);
			if (!TextUtils.isEmpty(key)) {
				/**
				 * 密钥不为空，需要sign 加密验证
				 */
				md5Source += key;
				String sign = MD5Util.MD5(md5Source);
				map.put("sign", sign);
			}
			CldLog.i(TAG, "md5Source: " + md5Source);
			errRes.url = urlHead;
			String strPostMap = "";
			Map<String, String> dataMap = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (!TextUtils.isEmpty(entry.getKey())
						&& null != entry.getValue()
						&& !TextUtils.isEmpty(entry.getValue().toString())) {
					CldLog.d("olscheckmap", entry.getKey() + "="
							+ entry.getValue().toString());
					dataMap.put(entry.getKey(), entry.getValue().toString());
					strPostMap = strPostMap + "&" + entry.getKey() + "="
							+ entry.getValue().toString();
				}
			}
			CldLog.i(TAG, "strPostMap: " + strPostMap);
			errRes.mapPost = dataMap;
		}
		return errRes;
	}
	
	
	
	
	/**
	 * 无密钥需要加密的Post拼接方式
	 * 
	 * @param map
	 * @param urlHead
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getPostParms(Map<String, Object> map,
			String urlHead) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			/**
			 * map不为空
			 */
			String md5Source = MtqSapParser.formatSource(map);
			CldLog.i(TAG, "md5Source: " + md5Source);
			String sign = MD5Util.MD5(md5Source);
			CldLog.i(TAG, "sign: " + sign);
			map.put("sign", sign);
			String strPost = MtqSapParser.mapToJson(map);
			CldLog.i(TAG, "strPost: " + strPost);
			errRes.url = urlHead;
			errRes.jsonPost = strPost;
		}
		return errRes;
	}

	/**
	 * 有密钥需要加密的Post拼接方式
	 * 
	 * @param map
	 * @param urlHead
	 * @param key
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getPostParms(Map<String, Object> map,
			String urlHead, String key) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			CldLog.i(TAG, "key: " + key);
			/**
			 * map不为空
			 */
			String urlSource = MtqSapParser.formatSource(map);
			String md5Source = urlSource;
			CldLog.i(TAG, "md5Source: " + md5Source);
			if (!TextUtils.isEmpty(key)) {
				md5Source += key;
			}
			String sign = CldSapUtil.MD5(md5Source);
			CldLog.i(TAG, "sign: " + sign);
			map.put("sign", sign);
			String strPost = MtqSapParser.mapToJson(map);
			CldLog.i(TAG, "strPost: " + strPost);
			errRes.url = urlHead;
			errRes.jsonPost = strPost;
		}
		return errRes;
	}

	/**
	 * 有密钥需要加密的Post拼接方式
	 * 
	 * @param map
	 * @param outMd5Source
	 *            外部决定参与签名的参数
	 * @param urlHead
	 * @param key
	 * @return MtqSapReturn
	 * 
	 * @author zhaoqy
	 * @date 2017-06-15
	 */
	public static MtqSapReturn getPostParms(Map<String, Object> map,
			String outMd5Source, String urlHead, String key) {
		MtqSapReturn errRes = new MtqSapReturn();
		if (map != null) {
			CldLog.i(TAG, "urlHead: " + urlHead);
			CldLog.i(TAG, "key: " + key);
			/**
			 * map不为空
			 */
			String urlSource = (!TextUtils.isEmpty(outMd5Source)) ? outMd5Source
					: MtqSapParser.formatSource(map);
			String md5Source = urlSource;
			CldLog.i(TAG, "md5Source: " + md5Source);
			if (!TextUtils.isEmpty(key)) {
				md5Source += key;
			}
			String sign = CldSapUtil.MD5(md5Source);
			CldLog.i(TAG, "sign: " + sign);
			map.put("sign", sign);
			String strPost = MtqSapParser.mapToJson(map);
			CldLog.i(TAG, "strPost: " + strPost);
			errRes.url = urlHead;
			errRes.jsonPost = strPost;
		}
		return errRes;
	}
}
