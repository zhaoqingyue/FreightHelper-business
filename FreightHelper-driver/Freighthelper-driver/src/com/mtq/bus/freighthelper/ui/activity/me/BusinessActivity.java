/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BusinessActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me
 * @Description: 业务看板页面
 * @author: zhaoqy
 * @date: 2017年7月4日 上午9:12:16
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me;

import java.util.ArrayList;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseFragmentActivity;
import com.mtq.bus.freighthelper.ui.adapter.FragmentAdapter;
import com.mtq.bus.freighthelper.ui.customview.NoScrollViewPager;
import com.mtq.bus.freighthelper.ui.fragment.bus.TaskFragment;
import com.mtq.bus.freighthelper.ui.fragment.bus.WaybillFragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class BusinessActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView mWaybill;
	private TextView mTask;
	private NoScrollViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private WaybillFragment mWaybillFragment;
	private TaskFragment mTaskFragment;
	
	@Override
	protected int getLayoutResID() {
		return R.layout.activity_business;
	}

	@Override
	protected void initViews() {
		mWaybill = (TextView) findViewById(R.id.business_waybill);
		mTask = (TextView) findViewById(R.id.business_task);
		mViewPager = (NoScrollViewPager) findViewById(R.id.business_viewpager);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.title_left_img).setOnClickListener(this);
		mWaybill.setOnClickListener(this);
		mTask.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		initViewPage();
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	protected void updateUI() {
		
	}
	
	@Override
	protected void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			if (mWaybillFragment != null) {
				mWaybillFragment.onConnectivityChange();
			}
			
			if (mTaskFragment != null) {
				mTaskFragment.onConnectivityChange();
			}
		}
	}
	
	private void initViewPage() {
		mFragments = new ArrayList<Fragment>();
		mWaybillFragment = new WaybillFragment();
		mFragments.add(mWaybillFragment);
		mTaskFragment = new TaskFragment();
		mFragments.add(mTaskFragment);
		
		mViewPager.setNoScroll(true);
		mViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), mFragments));
		mViewPager.setCurrentItem(0);
		mWaybill.setSelected(true);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.business_waybill: {
			if (mViewPager.getCurrentItem() != 0) {
				mViewPager.setCurrentItem(0);
				mWaybill.setSelected(true);
				mTask.setSelected(false);
				mWaybillFragment.autoRefreshOrder();
			}
			break;
		}
		case R.id.business_task: {
			if (mViewPager.getCurrentItem() != 1) {
				mViewPager.setCurrentItem(1);
				mWaybill.setSelected(false);
				mTask.setSelected(true);
				mTaskFragment.autoRefreshTask();
			}
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
