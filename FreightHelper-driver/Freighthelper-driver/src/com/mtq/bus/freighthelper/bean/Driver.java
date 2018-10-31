package com.mtq.bus.freighthelper.bean;

import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;

public class Driver extends MtqDriver {
	
	public String first;
	public String letter;
	public boolean selected;
	
	public Driver() {
		driverid = 0;
		driver_name = "";
		phone = "";
		first = "";
		letter = "";
		selected = false;
	}
}
