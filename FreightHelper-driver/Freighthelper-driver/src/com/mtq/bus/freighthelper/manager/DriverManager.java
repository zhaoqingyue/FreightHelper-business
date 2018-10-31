package com.mtq.bus.freighthelper.manager;

import java.util.List;

import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.db.DriverTable;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDriverDataListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;

public class DriverManager {

	public static final int PAGESIZE = 200;

	private static DriverManager mManager = null;

	public static DriverManager getInstance() {
		if (mManager == null) {
			synchronized (DriverManager.class) {
				if (mManager == null) {
					mManager = new DriverManager();
				}
			}
		}
		return mManager;
	}

	public void init() {
		/**
		 * 取司机列表信息，主要用于发送调度消息时，使用默认的司机，但是没有driverid字段
		 */
		DeliveryBusAPI.getInstance().getDriverDataList(0, null, 1, PAGESIZE,
				new IMtqDriverDataListener() {

					@Override
					public void onResult(int errCode, List<MtqDriver> data,
							int total) {
						if (errCode == 0) {
							/**
							 * 插入到数据库
							 */
							DriverTable.getInstance().insert(data);
						}
					}
				});
	}
}
