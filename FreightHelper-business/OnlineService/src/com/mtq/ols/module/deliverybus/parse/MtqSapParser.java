package com.mtq.ols.module.deliverybus.parse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;

import android.text.TextUtils;

import com.cld.gson.Gson;
import com.cld.log.CldLog;
import com.mtq.ols.module.delivery.tool.CldSapParser;
import com.mtq.ols.tools.err.CldOlsErrManager.CldOlsErrCode;

public class MtqSapParser {
	
	/**
	 * 解析泛型
	 * 
	 * @param t
	 * @param cls
	 * @param errRes
	 * @return void
	 * @author Zhouls
	 * @date 2015-8-11 上午9:06:39
	 */
	public static <T> void parseObject(T t, Class<T> cls, MtqSapReturn errRes) {
		if (null != t) {
			if (CldSapParser.hasKey(cls, "errcode")) {
				/**
				 * 如果t 有errcode属性
				 */
				errRes.errCode = (Integer) CldSapParser.getFieldValueByName(
						"errcode", t);
				errRes.errMsg = (String) CldSapParser.getFieldValueByName(
						"errmsg", t);
			} else {
				if (CldSapParser.hasKey(cls.getSuperclass(), "errcode")) {
					/**
					 * 
					 * 如果父类 有errcode属性
					 */
					errRes.errCode = (Integer) CldSapParser
							.getFieldValueByName("errcode", t);
					errRes.errMsg = (String) CldSapParser.getFieldValueByName(
							"errmsg", t);
				} else {
					errRes.errCode = 0;
				}
			}
		} else {
			errRes.errCode = CldOlsErrCode.PARSE_ERR;
			errRes.errMsg = "解析异常";
		}
	}

	/**
	 * 解析
	 * 
	 * @param jsonString
	 * @param cls
	 * @param errRes
	 * @return
	 * @return T
	 * @author Zhouls
	 * @date 2015-4-3 下午12:00:41
	 */
	public static <T> T parseJson(String jsonString, Class<T> cls,
			MtqSapReturn errRes) {
		T t = null;
		errRes.jsonReturn = jsonString;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
			if (null != t) {
				if (hasKey(cls, "errcode")) {
					/**
					 * 如果t 有errcode属性
					 */
					errRes.errCode = (Integer) getFieldValueByName("errcode", t);
					errRes.errMsg = (String) getFieldValueByName("errmsg", t);
				} else {
					if (hasKey(cls.getSuperclass(), "errcode")) {
						/**
						 * 如果父类 有errcode属性
						 */
						errRes.errCode = (Integer) getFieldValueByName(
								"errcode", t);
						errRes.errMsg = (String) getFieldValueByName("errmsg",
								t);

					} else {
						errRes.errCode = 0;
					}
				}
			} else {
				errRes.errCode = -105;
				errRes.errMsg = "解析异常";
			}
		} catch (Exception e) {
			errRes.errCode = -105;
			errRes.errMsg = "解析异常";
		}
		return t;
	}

	/**
	 * 将map反序列化为json字符串
	 * 
	 * @param map
	 *            参数表
	 * @return
	 * @return String
	 * @author Zhouls
	 * @date 2015-3-18 下午3:56:16
	 */
	public static <T> String mapToJson(Map<String, T> map) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(map);
		return jsonStr;
	}

	/**
	 * 获取协议层md5Source串
	 * 
	 * @param o
	 * @return
	 * @return String
	 * @author Zhouls
	 * @date 2015-3-18 上午9:14:17
	 */
	public static String formatSource(Object o) {
		if (null != o) {
			/**
			 * 获取对象中的属性名数组（已按ascii码增序排好顺序）
			 */
			String[] parms = getFiledName(o);
			if (null != parms) {
				String md5Source = "";
				for (int i = 0; i < parms.length; i++) {
					String parm = (String) getFieldValueByName(parms[i], o);
					if (i != 0) {
						if (!TextUtils.isEmpty(parm)) {
							md5Source = md5Source + "&" + parms[i] + "=" + parm;
						}
					} else {
						if (!TextUtils.isEmpty(parm)) {
							md5Source = md5Source + parms[i] + "=" + parm;
						}
					}
				}
				return md5Source;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取协议层md5Source串
	 * 
	 * @param map
	 * @return
	 * @return String
	 * @author Zhouls
	 * @date 2015-3-18 上午9:54:09
	 */
	@SuppressWarnings("rawtypes")
	public static String formatSource(Map<String, Object> map) {
		if (null != map) {
			int size = map.size();
			String[] parms = new String[size];
			Iterator<?> iter = map.entrySet().iterator();
			int i = 0;
			String md5Source = "";
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				parms[i] = (String) entry.getKey();
				i++;
			}
			BubbleSort.sort(parms);
			for (i = 0; i < parms.length; i++) {
				if (i != 0) {
					if (!TextUtils.isEmpty(parms[i])) {
						md5Source = md5Source + "&" + parms[i] + "="
								+ map.get(parms[i]);
					}
				} else {
					if (!TextUtils.isEmpty(parms[i])) {
						md5Source = md5Source + parms[i] + "="
								+ map.get(parms[i]);
					}
				}
			}
			return md5Source;
		} else {
			return "";
		}
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 * @param fieldName
	 *            属性名
	 * @param o
	 *            对象
	 * @return
	 * @return Object
	 * @author Zhouls
	 * @date 2015-3-18 上午9:15:04
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		CldLog.e("MtqSapParser", "fieldName: " + fieldName);
		CldLog.e("MtqSapParser", "Object: " + o.toString());
		
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			CldLog.e("MtqSapParser", "firstLetter: " + firstLetter);
			String getter = "get" + firstLetter + fieldName.substring(1);
			CldLog.e("MtqSapParser", "getter: " + getter);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			CldLog.e("MtqSapParser", "value: " + value);
			return value;
		} catch (Exception e) {
			CldLog.e("MtqSapParser", "Exception: " + e.toString());
			return null;
		}
	}

	/**
	 * 获取属性名数组
	 * 
	 * @param o
	 * @return
	 * @return String[]
	 * @author Zhouls
	 * @date 2015-3-18 上午9:15:30
	 */
	public static String[] getFiledName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * 类是否有Name 属性
	 * 
	 * @param o
	 * @param name
	 * @return
	 * @return boolean
	 * @author Zhouls
	 * @date 2015-3-19 上午10:51:51
	 */
	@SuppressWarnings("rawtypes")
	public static boolean hasKey(Class o, String name) {
		Field[] fields = o.getDeclaredFields();
		if (!TextUtils.isEmpty(name)) {
			for (int i = 0; i < fields.length; i++) {
				if (name.equals(fields[i].getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Md5加密
	 * 
	 * @param sourceStr
	 *            加密源串
	 * @return
	 * @return String
	 * @author Zhouls
	 * @date 2015-3-18 上午8:57:35
	 */
	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	/**
	 * 
	 * 冒泡排序
	 * 
	 * @author Zhouls
	 * @date 2015-3-18 上午8:55:13
	 */
	public static class BubbleSort {

		/**
		 * 冒泡排序按Ascii码大小增序排序（循环数组大小次，每次将最大的放到最后）
		 * 
		 * @param array
		 *            排序数组
		 * @return void
		 * @author Zhouls
		 * @date 2015-3-18 上午8:55:24
		 */
		public static void sort(String[] array) {
			if (null != array) {
				for (int i = 1; i < array.length; i++) {
					for (int j = 0; j < array.length - i; j++) {
						if (compare(array[j + 1], array[j])) {
							String temp = array[j];
							array[j] = array[j + 1];
							array[j + 1] = temp;
						}
					}
				}
			}
		}

		/**
		 * 2个字符串比较Ascii 码大小
		 * 
		 * @param strA
		 * @param strB
		 * @return 若A<B 返回true 否则返回false
		 * @return boolean
		 * @author Zhouls
		 * @date 2015-3-18 上午8:55:45
		 */
		public static boolean compare(String strA, String strB) {
			char[] a = strA.toCharArray();
			char[] b = strB.toCharArray();
			int cycNum = a.length < b.length ? a.length : b.length;
			for (int i = 0; i < cycNum; i++) {
				if (a[i] == b[i]) {
					continue;
				} else {
					if (a[i] < b[i]) {
						return true;
					} else {
						return false;
					}
				}
			}
			if (a.length != b.length) {
				/**
				 * 一个是另一个的前缀
				 */
				return false;
			} else {
				/**
				 * 2个字符串完全相等
				 */
				return true;
			}
		}
	}

}
