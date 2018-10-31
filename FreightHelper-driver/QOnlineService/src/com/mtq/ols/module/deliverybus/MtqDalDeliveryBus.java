/**
 * 货运企业端-数据层
 * 
 * @author zhaoqy
 * @date 2017-06-16
 */

package com.mtq.ols.module.deliverybus;

import java.util.ArrayList;
import java.util.List;

import com.cld.setting.CldSetting;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqGroup;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqLogin;


public class MtqDalDeliveryBus {
	private boolean loginStatus; // 登录状态
	private MtqLogin mMtqLogin;
	private List<String> actions;
	private List<MtqGroup> groups;
	
	private static MtqDalDeliveryBus instance;

	public static MtqDalDeliveryBus getInstance() {
		if (null == instance) {
			instance = new MtqDalDeliveryBus();
		}
		return instance;
	}
	
	public MtqDalDeliveryBus() {
		loginStatus = false;
		mMtqLogin = new MtqLogin();
		actions = new ArrayList<String>();
		groups = new ArrayList<MtqGroup>();
	}
	
	public void uninit() {
		loginStatus = false;
		mMtqLogin = new MtqLogin();
		actions = new ArrayList<String>();
		groups = new ArrayList<MtqGroup>();
		CldSetting.put("bus_password", "");
	}
	
	public String getSessKey() {
		return CldSetting.getString("bus_sesskey");
	}

	public void setSessKey(String sesskey) {
		CldSetting.put("bus_sesskey", sesskey);
	}
	
	public int getAdminID() {
		return CldSetting.getInt("bus_admin_id");
	}

	public void setAdminID(int admin_id) {
		CldSetting.put("bus_admin_id", admin_id);
	}
	
	public long getDuid() {
		return CldSetting.getLong("bus_duid");
	}

	public void setDuid(long duid) {
		CldSetting.put("bus_duid", duid);
	}
	
	public int getAppType() {
		return CldSetting.getInt("bus_apptype");
	}

	public void setAppType(int apptype) {
		CldSetting.put("bus_apptype", apptype);
	}
	
	public String getVersion() {
		return CldSetting.getString("bus_version");
	}

	public void setVersion(String version) {
		CldSetting.put("bus_version", version);
	}
	
	public String getToken() {
		return CldSetting.getString("bus_token");
	}

	public void setToken(String token) {
		CldSetting.put("bus_token", token);
	}
	
	public String getUserName() {
		return CldSetting.getString("bus_username");
	}

	public void setUserName(String username) {
		CldSetting.put("bus_username", username);
	}
	
	public String getPassword() {
		return CldSetting.getString("bus_password");
	}

	public void setPassword(String password) {
		CldSetting.put("bus_password", password);
	}
	
	public boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	public MtqLogin getMtqLogin() {
		return mMtqLogin;
	}
	
	public void setMtqLogin(MtqLogin mtqLogin) {
		this.mMtqLogin = mtqLogin;
		if (mtqLogin != null) {
			setSessKey(mtqLogin.sesskey);
			setAdminID(mtqLogin.admin_id);
			setToken(mtqLogin.token);
		} else {
			setSessKey("");
			setAdminID(0);
			setToken("");
		}
	}
	
	public List<String> getActions() {
		return actions;
	}
	
	public void setActions(List<String> actions) {
		this.actions = actions;
	}
	
	public List<MtqGroup> getGroups() {
		return groups;
	}
	
	public void setGroups(List<MtqGroup> groups) {
		this.groups = groups;
	}
}
