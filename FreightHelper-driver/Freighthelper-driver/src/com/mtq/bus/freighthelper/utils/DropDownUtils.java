package com.mtq.bus.freighthelper.utils;

import java.util.ArrayList;
import java.util.List;

import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqGroup;

public class DropDownUtils {
	
	public static List<DropDown> getBusDrop() {
		List<DropDown> dropDowns = new ArrayList<DropDown>();
		
		DropDown drop0 = new DropDown();
		drop0.id = 0;
		drop0.name = "今天";
		dropDowns.add(drop0);
		
		DropDown drop1 = new DropDown();
		drop1.id = 1;
		drop1.name = "前7天";
		dropDowns.add(drop1);
		
		DropDown drop2 = new DropDown();
		drop2.id = 2;
		drop2.name = "自定义";
		dropDowns.add(drop2);
		
		return dropDowns;
	}
	
	public static List<DropDown> getMyCarDrop() {
		List<DropDown> dropDowns = new ArrayList<DropDown>();
		
		DropDown drop0 = new DropDown();
		drop0.id = 0;
		drop0.name = "全部车队";
		dropDowns.add(drop0);
		
		List<MtqGroup> groups = DeliveryBusAPI.getInstance().getGroups();
		if (groups != null && !groups.isEmpty()) {
			int len = groups.size();
			for (int i = 0; i < len; i++) {
				MtqGroup group = groups.get(i);
				DropDown drop = new DropDown();
				drop.id = group.group_id;
				drop.name = group.group_name;
				dropDowns.add(drop);
			}
		}
		return dropDowns;
	}
	
	public static List<DropDown> getMyDriverDrop() {
		List<DropDown> dropDowns = new ArrayList<DropDown>();
		
		DropDown drop0 = new DropDown();
		drop0.id = 0;
		drop0.name = "全部";
		dropDowns.add(drop0);
		
		DropDown drop1 = new DropDown();
		drop1.id = 1;
		drop1.name = "未邀请";
		dropDowns.add(drop1);
		
		DropDown drop2 = new DropDown();
		drop2.id = 2;
		drop2.name = "已邀请";
		dropDowns.add(drop2);
		
		DropDown drop3 = new DropDown();
		drop3.id = 3;
		drop3.name = "同意";
		dropDowns.add(drop3);
		
		DropDown drop4 = new DropDown();
		drop4.id = 4;
		drop4.name = "已拒绝";
		dropDowns.add(drop4);
		
		DropDown drop5 = new DropDown();
		drop5.id = 5;
		drop5.name = "已退出";
		dropDowns.add(drop5);
		
		return dropDowns;
	}
	
	public static List<DropDown> getFdTypeDrop() {
		List<DropDown> dropDowns = new ArrayList<DropDown>();
		
		DropDown drop0 = new DropDown();
		drop0.id = 1;
		drop0.name = "平台类";
		dropDowns.add(drop0);
		
		DropDown drop1 = new DropDown();
		drop1.id = 2;
		drop1.name = "硬件类";
		dropDowns.add(drop1);
		
		return dropDowns;
	}
	
	public static List<DropDown> getDeviceTypeDrop() {
		List<DropDown> dropDowns = new ArrayList<DropDown>();
		
		DropDown drop0 = new DropDown();
		drop0.id = 2;
		drop0.name = "北斗双模一体机";
		dropDowns.add(drop0);
		
		DropDown drop1 = new DropDown();
		drop1.id = 3;
		drop1.name = "凯立德KPND";
		dropDowns.add(drop1);
		
		DropDown drop2 = new DropDown();
		drop2.id = 4;
		drop2.name = "TD-BOX";
		dropDowns.add(drop2);
		
		DropDown drop3 = new DropDown();
		drop3.id = 5;
		drop3.name = "TD-PND";
		dropDowns.add(drop3);
		
		return dropDowns;
	}

}
