/**
 * 协议层返回结果类
 * 
 * @author zhaoqy
 * @date 2017-06-15
 */

package com.mtq.ols.module.deliverybus.parse;

import java.util.Map;

public class MtqSapReturn {

	/** 错误码 */
	public int errCode;
	/** 错误信息 */
	public String errMsg;
	/** 返回Json */
	public String jsonReturn;
	/** 请求URL */
	public String url;
	/** PostJson串 */
	public String jsonPost;
	/** Post二进制数组 */
	public byte[] bytePost;
	/** Post标准map */
	public Map<String, String> mapPost;

	public MtqSapReturn() {
		errCode = -1;
		errMsg = "";
		jsonReturn = "";
		url = "";
		jsonPost = "";
	}
}
