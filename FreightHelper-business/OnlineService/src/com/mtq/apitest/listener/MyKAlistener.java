/*
 * @Title MyKAlistener.java
 * @Copyright Copyright 2010-2014 Careland Software Co,.Ltd All Rights Reserved.
 * @Description 
 * @author Zhouls
 * @date 2015-1-6 9:03:59
 * @version 1.0
 */
package com.mtq.apitest.listener;

import com.cld.log.CldLog;
import com.mtq.ols.api.CldKAccountAPI;
import com.mtq.ols.api.CldKAccountAPI.ICldKAccountListener;


/**
 * The Class MyKAlistener.
 * 
 * @Description 帐户系统回调
 * @author Zhouls
 * @date 2014-11-4 下午12:07:41
 */
public class MyKAlistener implements ICldKAccountListener {

	/**
	 * On is reg user.
	 * 
	 * @param errCode
	 *            the err code
	 * @param kuid
	 *            the kuid
	 * @param loginName
	 *            the login name
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onIsRegUser(int,
	 *      long, java.lang.String)
	 */
	@Override
	public void onIsRegUser(int errCode, long kuid, String loginName) {
		// TODO Auto-generated method stub

	}

	/**
	 * On register.
	 * 
	 * @param errCode
	 *            the err code
	 * @param kuid
	 *            the kuid
	 * @param loginName
	 *            the login name
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onRegister(int,
	 *      long, java.lang.String)
	 */
	@Override
	public void onRegister(int errCode, long kuid, String loginName) {
		// TODO Auto-generated method stub

	}

	/**
	 * On reg by sms.
	 * 
	 * @param errCode
	 *            the err code
	 * @param kuid
	 *            the kuid
	 * @param loginName
	 *            the login name
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onRegBySms(int,
	 *      long, java.lang.String)
	 */
	@Override
	public void onRegBySms(int errCode, long kuid, String loginName) {
		// TODO Auto-generated method stub

	}

	/**
	 * On retrieve pwd.
	 * 
	 * @param errCode
	 *            the err code
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onRetrievePwd(int)
	 */
	@Override
	public void onRetrievePwd(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * On revise pwd.
	 * 
	 * @param errCode
	 *            the err code
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onRevisePwd(int)
	 */
	@Override
	public void onRevisePwd(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * On update user info.
	 * 
	 * @param errCode
	 *            the err code
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onUpdate
	 *      UserInfo(int)
	 */
	@Override
	public void onUpdateUserInfo(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * On login out result.
	 * 
	 * @param errCode
	 *            the err code
	 * @see com.cld.navicm.api.CldKAccountAPI.CldKAccountListener#onLoginOutResult(int)
	 */
	@Override
	public void onLoginOutResult(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * On get verify code.
	 * 
	 * @param errCode
	 *            the err code
	 * @param bussinessid
	 *            the bussinessid
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onGetVerifyCode(int,
	 *      int)
	 */
	@Override
	public void onGetVerifyCode(int errCode, int bussinessid) {
		// TODO Auto-generated method stub

	}

	/**
	 * On auto login result.
	 * 
	 * @param loginState
	 *            the login state
	 * @param errCode
	 *            the err code
	 * @param errCode_getUserInfo
	 *            the err code_get user info
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onAutoLoginResult(int,
	 *      int, int)
	 */
	@Override
	public void onAutoLoginResult(int loginState, int errCode) {

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onInValidSession()
	 */
	@Override
	public void onInValidSession(int bussiness) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onGetQRcodeResult(int)
	 */
	@Override
	public void onGetQRcodeResult(int errCode) {
		// TODO Auto-generated method stub
		CldLog.d("QR", CldKAccountAPI.getInstance().getQRcodeValue());
	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onBindMobile(int)
	 */
	@Override
	public void onBindMobile(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onUnbindMobile(int)
	 */
	@Override
	public void onUnbindMobile(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onUpdateMobile(int)
	 */
	@Override
	public void onUpdateMobile(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onRevisePwdByFastLogin(int)
	 */
	@Override
	public void onRevisePwdByFastLogin(int errCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onGetQRLoginStatusResult(int,
	 *      int)
	 */
	@Override
	public void onGetQRLoginStatusResult(int errCode) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.CldKAccountListener#onGetUserInfoResult(int)
	 */
	@Override
	public void onGetUserInfoResult(int errCode) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mtq.ols.api.CldKAccountAPI.ICldKAccountListener#onThirdLoginResult(int)
	 */
	@Override
	public void onThirdLoginResult(int errCode) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mtq.ols.api.CldKAccountAPI.ICldKAccountListener#onLoginResult(int, boolean)
	 */
	@Override
	public void onLoginResult(int errCode, boolean isFastLogin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckMobileVeriCode(int errCode, int bussinessid) {
		// TODO Auto-generated method stub
		
	}

}
