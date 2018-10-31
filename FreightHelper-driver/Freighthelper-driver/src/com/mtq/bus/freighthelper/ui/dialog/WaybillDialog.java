/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: WaybillDialog.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.dialog
 * @Description: 运单详情dialog
 * @author: zhaoqy
 * @date: 2017年6月17日 上午11:36:26
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.CarUtils;

public class WaybillDialog extends Dialog implements OnClickListener {

	private Context mContext;
	private TextView mOrderid;
	private TextView mSendRegionName;
	private TextView mSendAddr;
	private TextView mReceiveRegionName;
	private TextView mReceiveAddr;
	private TextView mOrderstatus;
	private TextView mReceiveDate;
	private TextView mWeightVolume;
	private TextView mDriver;
	private String orderid;
	private String send_regionname;
	private String send_addr;
	private String receive_regionname;
	private String receive_addr;
	private int orderstatus;
	private String receive_date;
	private float weight;
	private float volume;
	private String driver;
	private String phone;

	public WaybillDialog(Context context) {
		super(context, R.style.dialog_style);
	}

	public WaybillDialog(Context context, String orderid,
			String send_regionname, String send_addr,
			String receive_regionname, String receive_addr, int orderstatus,
			String receive_date, float weight, float volume, String driver, String phone) {
		super(context, R.style.dialog_style);
		mContext = context;
		this.orderid = orderid;
		this.send_regionname = send_regionname;
		this.send_addr = send_addr;
		this.receive_regionname = receive_regionname;
		this.receive_addr = receive_addr;
		this.orderstatus = orderstatus;
		this.receive_date = receive_date;
		this.weight = weight;
		this.volume = volume;
		this.driver = driver;
		this.phone = phone;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		getWindow().setContentView(R.layout.dialog_waybill);

		initViews();
		setListener();
		setViews();
	}

	private void initViews() {
		mOrderid = (TextView) findViewById(R.id.waybill_orderid);
		mSendRegionName = (TextView) findViewById(R.id.waybill_send_regionname);
		mSendAddr = (TextView) findViewById(R.id.waybill_send_addr);
		mReceiveRegionName = (TextView) findViewById(R.id.waybill_receive_regionname);
		mReceiveAddr = (TextView) findViewById(R.id.waybill_receive_addr);
		mOrderstatus = (TextView) findViewById(R.id.waybill_orderstatus);
		mReceiveDate = (TextView) findViewById(R.id.waybill_receive_date);
		mWeightVolume = (TextView) findViewById(R.id.waybill_weight_volume);
		mDriver = (TextView) findViewById(R.id.waybill_driver);
	}

	private void setListener() {
		findViewById(R.id.waybill_cancel).setOnClickListener(this);
		findViewById(R.id.waybill_driver_layout).setOnClickListener(this);
	}

	private void setViews() {
		mOrderid.setText(orderid);
		mSendRegionName.setText(send_regionname);
		mSendAddr.setText(send_addr);
		mReceiveRegionName.setText(receive_regionname);
		mReceiveAddr.setText(receive_addr);
	
		mOrderstatus.setText(CarUtils.getOrderStatus(orderstatus));
		mReceiveDate.setText(receive_date);
		mWeightVolume.setText(weight + "吨" + "  " + volume + "方");
		mDriver.setText(driver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.waybill_cancel: {
			dismiss();
			break;
		}
		case R.id.waybill_driver_layout: {
			if (!TextUtils.isEmpty(phone)) {
				CallUtils.makeCall((Activity) mContext, phone);
			}
			break;
		}
		default:
			break;
		}
	}
}
