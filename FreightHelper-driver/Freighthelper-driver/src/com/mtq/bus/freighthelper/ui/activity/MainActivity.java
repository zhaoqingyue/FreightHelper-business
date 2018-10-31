/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MainActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity
 * @Description: 主页面
 * @author: zhaoqy
 * @date: 2017年7月5日 下午3:10:36
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity;

import me.yokeyword.fragmentation.SupportFragment;
import android.os.Bundle;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.bean.eventbus.TabSelectedEvent;
import com.mtq.bus.freighthelper.db.MsgAlarmTable;
import com.mtq.bus.freighthelper.db.MsgSysTable;
import com.mtq.bus.freighthelper.ui.activity.base.BaseMainActivity;
import com.mtq.bus.freighthelper.ui.customview.BottomBar;
import com.mtq.bus.freighthelper.ui.customview.BottomBarTab;
import com.mtq.bus.freighthelper.ui.fragment.main.CarFragment;
import com.mtq.bus.freighthelper.ui.fragment.main.MeFragment;
import com.mtq.bus.freighthelper.ui.fragment.main.MsgFragment;
import com.mtq.bus.freighthelper.ui.fragment.main.MsgFragment.IMsgCount;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends BaseMainActivity implements IMsgCount {

	private static final String TAG = "MainActivity";
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;

	private SupportFragment[] mFragments = new SupportFragment[3];
	private BottomBar mBottomBar;
	private BottomBarTab mBarMsg;
	
	@Override
	protected int getLayoutResID() {
		return R.layout.activity_main;
	}

	@Override
	protected void initFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			mFragments[FIRST] = MsgFragment.newInstance();
			mFragments[SECOND] = CarFragment.newInstance();
			mFragments[THIRD] = MeFragment.newInstance();
			loadMultipleRootFragment(R.id.id_main_layout, FIRST,
					mFragments[FIRST], mFragments[SECOND], mFragments[THIRD]);
		} else {
			mFragments[FIRST] = findFragment(MsgFragment.class);
			mFragments[SECOND] = findFragment(CarFragment.class);
			mFragments[THIRD] = findFragment(MeFragment.class);
		}
	}

	@Override
	protected void initViews() {
		mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
		mBarMsg = new BottomBarTab(this, R.drawable.main_bottom_msg,
				getString(R.string.main_bottom_msg));
		BottomBarTab car = new BottomBarTab(this, R.drawable.main_bottom_car,
				getString(R.string.main_bottom_car));
		BottomBarTab me = new BottomBarTab(this, R.drawable.main_bottom_me,
				getString(R.string.main_bottom_me));
		mBottomBar.addItem(mBarMsg).addItem(car).addItem(me);
		mBottomBar
				.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
					
					@Override
					public void onTabSelected(int position, int prePosition) {
						showHideFragment(mFragments[position],
								mFragments[prePosition]);
					}

					@Override
					public void onTabUnselected(int position) {

					}

					@Override
					public void onTabReselected(int position) {
						SupportFragment currentFragment = mFragments[position];
						int count = currentFragment.getChildFragmentManager()
								.getBackStackEntryCount();
						if (count == 1) {
							EventBus.getDefault().post(
									new TabSelectedEvent(position));
						}
					}
				});
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void initData() {
		/**
		 * 将车辆监控设置为APP首页
		 * modify 2017-08-15
		 */
		mBottomBar.setCurrentItem(SECOND);
	}

	@Override
	protected void loadData() {
		
	}
	
	@Override
	protected void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			((MsgFragment) mFragments[FIRST]).onConnectivityChange();
			((CarFragment) mFragments[SECOND]).onConnectivityChange();
		}
	}

	@Override
	protected void updateUI() {
		int alarm_count = MsgAlarmTable.getInstance().queryUnReadCount();
		int sys_count = MsgSysTable.getInstance().queryUnReadCount();
		CldLog.e(TAG, "alarm_count: " + alarm_count + ", sys_count: "
				+ sys_count);
		mBarMsg.setUnreadCount(alarm_count + sys_count);
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	@Override
	public void onMsgCount() {
		updateUI();
	}
}
