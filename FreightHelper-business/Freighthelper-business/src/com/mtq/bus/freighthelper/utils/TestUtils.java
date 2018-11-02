package com.mtq.bus.freighthelper.utils;

import java.util.ArrayList;
import java.util.List;

import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarData;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskNavi;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskStore;

public class TestUtils {
	
	
	
	public static boolean debug = false;
	
	public static List<MtqCarData> getMyCar() {
		List<MtqCarData> myCars = new ArrayList<MtqCarData>();
		
		MtqCarData myCar0 = new MtqCarData();
		myCar0.carlicense = "粤B88888";
		myCar0.sourceid = 1;
		myCar0.driver = "赵庆月";
		myCar0.phone = "13421812821";
		myCars.add(myCar0);
		
		MtqCarData myCar1 = new MtqCarData();
		myCar1.carlicense = "粤B66666";
		myCar1.sourceid = 2;
		myCar1.driver = "赵列家";
		myCar1.phone = "13545168475";
		myCars.add(myCar1);
		
		return myCars;
	}
	
	public static List<MtqDriver> getMyDriver() {
		List<MtqDriver> myDrivers = new ArrayList<MtqDriver>();
		
		MtqDriver myDriver0 = new MtqDriver();
		myDriver0.driver_name = "赵庆月";
		myDriver0.invitestatus = 1;
		myDriver0.phone = "13421812821";
		myDrivers.add(myDriver0);
		
		MtqDriver myDriver1 = new MtqDriver();
		myDriver1.driver_name = "赵列家";
		myDriver1.invitestatus = 2;
		myDriver1.phone = "13545168475";
		myDrivers.add(myDriver1);
		
		return myDrivers;
	}
	
	public static List<MtqCarState> getCarState() {
		List<MtqCarState> carStates = new ArrayList<MtqCarState>();
		
		MtqCarState carState0 = new MtqCarState();
		carState0.carlicense = "粤B88888";
		carState0.carstatus = 1;
		carState0.mileage = 210;
		carStates.add(carState0);
		
		MtqCarState carState1 = new MtqCarState();
		carState1.carlicense = "粤B66666";
		carState1.carstatus = 2;
		carState1.mileage = 999;
		carStates.add(carState1);
		
		return carStates;
	}
	
	public static List<MtqDriver> getDriver() {
		List<MtqDriver> drivers = new ArrayList<MtqDriver>();
		
		MtqDriver driver0 = new MtqDriver();
		driver0.driver_name = "赵庆月";
		driver0.phone = "13421812821";
		drivers.add(driver0);
		
		MtqDriver driver1 = new MtqDriver();
		driver1.driver_name = "蔡东来";
		driver1.phone = "13545168475";
		drivers.add(driver1);
		
		MtqDriver driver2 = new MtqDriver();
		driver2.driver_name = "周巧兰";
		driver2.phone = "13545168475";
		drivers.add(driver2);
		
		MtqDriver driver3 = new MtqDriver();
		driver3.driver_name = "赵烈贵";
		driver3.phone = "13545168475";
		drivers.add(driver3);
		
		MtqDriver driver4 = new MtqDriver();
		driver4.driver_name = "陈梦云";
		driver4.phone = "13545168475";
		drivers.add(driver4);
		
		MtqDriver driver5 = new MtqDriver();
		driver5.driver_name = "罗佳伟";
		driver5.phone = "13545168475";
		drivers.add(driver5);
		
		MtqDriver driver6 = new MtqDriver();
		driver6.driver_name = "李连杰";
		driver6.phone = "13545168475";
		drivers.add(driver6);
		
		MtqDriver driver7 = new MtqDriver();
		driver7.driver_name = "爱丽丝";
		driver7.phone = "13545168475";
		drivers.add(driver7);
		
		MtqDriver driver8 = new MtqDriver();
		driver8.driver_name = "包青天";
		driver8.phone = "13545168475";
		drivers.add(driver8);
		
		MtqDriver driver9 = new MtqDriver();
		driver9.driver_name = "董老大";
		driver9.phone = "13545168475";
		drivers.add(driver9);
		
		MtqDriver driver10 = new MtqDriver();
		driver10.driver_name = "依依";
		driver10.phone = "13545168475";
		drivers.add(driver10);
		
		MtqDriver driver11 = new MtqDriver();
		driver11.driver_name = "方世玉";
		driver11.phone = "13545168475";
		drivers.add(driver11);
		
		MtqDriver driver12 = new MtqDriver();
		driver12.driver_name = "高山";
		driver12.phone = "13545168475";
		drivers.add(driver12);
		
		return drivers;
	}
	
	public static List<MtqMsgAlarm> getMsgAlarm() {
		List<MtqMsgAlarm> msgAlarms = new ArrayList<MtqMsgAlarm>();
		
		MtqMsgAlarm msgAlarm0 = new MtqMsgAlarm();
		msgAlarm0.carlicense = "粤B88888";
		msgAlarm0.alarmid = 200;
		msgAlarm0.locattime = 1497930999;
		msgAlarm0.readstatus = 1;
		msgAlarms.add(msgAlarm0);
		
		MtqMsgAlarm msgAlarm1 = new MtqMsgAlarm();
		msgAlarm1.carlicense = "粤B66666";
		msgAlarm1.alarmid = 201;
		msgAlarm1.locattime = 1497872079;
		msgAlarm1.readstatus = 1;
		msgAlarms.add(msgAlarm1);
		
		return msgAlarms;
	}
	
	public static List<MtqMsgSys> getMsgSys() {
		List<MtqMsgSys> msgSyss = new ArrayList<MtqMsgSys>();
		
		MtqMsgSys msgSys0 = new MtqMsgSys();
		msgSys0.title = "春节快乐";
		msgSys0.time = 1497940637;
		msgSys0.content = "春节来临之际，为保证大家的使用体验睡觉觉三十vdiakskj";
		msgSys0.readstatus = 1;
		msgSyss.add(msgSys0);
		
		MtqMsgSys msgSys1 = new MtqMsgSys();
		msgSys1.title = "赵庆月";
		msgSys1.time = 1497940637;
		msgSys1.content = "回萨达U胡时候爱暑促奥数慈湖ASUC胡";
		msgSys1.readstatus = 1;
		msgSyss.add(msgSys1);
		
		return msgSyss;
	}
	
	public static List<MtqTaskStore> getTaskStore() {
		List<MtqTaskStore> taskStores = new ArrayList<MtqTaskStore>();
		
		MtqTaskStore taskStore0 = new MtqTaskStore();
		taskStore0.cut_orderid = "YD355155445903";
		taskStore0.orderstatus = 0;
		taskStores.add(taskStore0);
		
		MtqTaskStore taskStore1 = new MtqTaskStore();
		taskStore1.cut_orderid = "YD355155445903";
		taskStore1.orderstatus = 1;
		taskStores.add(taskStore1);
		
		MtqTaskStore taskStore2 = new MtqTaskStore();
		taskStore2.cut_orderid = "YD355155445903";
		taskStore2.orderstatus = 20;
		taskStores.add(taskStore2);
		
		return taskStores;
	}
	
	public static List<MtqTaskNavi> getTaskNavi() {
		List<MtqTaskNavi> taskNavis = new ArrayList<MtqTaskNavi>();
		
		MtqTaskNavi taskNavi0 = new MtqTaskNavi();
		taskNavi0.mileage = 200;
		taskNavi0.traveltime = 120;
		taskNavis.add(taskNavi0);
		
		MtqTaskNavi taskNavi1 = new MtqTaskNavi();
		taskNavi1.mileage = 300;
		taskNavi1.traveltime = 220;
		taskNavis.add(taskNavi1);
		
		return taskNavis;
	}

}
