/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: LicenseAgreementActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me.about
 * @Description: 软件使用许可协议
 * @author: zhaoqy
 * @date: 2017年6月2日 下午3:52:44
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me.about;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.utils.FileUtils;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LicenseAgreementActivity extends BaseActivity implements OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mProtocol;
	
	@Override
	protected int getLayoutResID() {
		return R.layout.activity_legal_notice;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mProtocol = (TextView) findViewById(R.id.agreement_text);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.about_license_agreement);
		mBack.setVisibility(View.VISIBLE);
	}

	@Override
	protected void loadData() {
		String text = FileUtils.getFromAssets(this, "about/protocol.txt");
		mProtocol.setText(text);
	}

	@Override
	protected void updateUI() {

	}
	
	@Override
	protected void onConnectivityChange() {
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}
}
