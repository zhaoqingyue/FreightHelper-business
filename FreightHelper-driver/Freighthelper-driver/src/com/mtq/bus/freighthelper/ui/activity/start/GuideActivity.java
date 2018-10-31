/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: GuideActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.start
 * @Description: 引导页
 * @author: zhaoqy
 * @date: 2017年6月16日 下午10:39:08
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.start;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.LoginEvent;
import com.mtq.bus.freighthelper.ui.activity.MainActivity;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.GuidePagerAdapter;
import com.mtq.bus.freighthelper.utils.MsgId;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class GuideActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewPager;
	private GuidePagerAdapter mAdapter;
	private List<View> mViews;
	private TextView mJump;
	private Button mEnter;
	private ImageView[] dots;
	private int mCurIndex;
	
	private static final int[] pics = { /*R.layout.layout_guid_view0,*/
		R.layout.layout_guid_view1, R.layout.layout_guid_view2,
		R.layout.layout_guid_view3 };

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_guide;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initViews() {
		mViews = new ArrayList<View>();
		for (int i = 0; i < pics.length; i++) {
			View view = LayoutInflater.from(this).inflate(pics[i], null);
			if (i == pics.length - 1) {
				mEnter = (Button) view.findViewById(R.id.guide_enter);
				mEnter.setTag("enter");
				mEnter.setOnClickListener(this);
			}
			mViews.add(view);
		}
		mViewPager = (ViewPager) findViewById(R.id.guide_pager);
		mAdapter = new GuidePagerAdapter(mViews);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mJump = (TextView) findViewById(R.id.guide_jump);
		
		initDots();
	}

	@Override
	protected void setListener() {
		mEnter.setOnClickListener(this);
		mJump.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.guide_dot);
		dots = new ImageView[pics.length];
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(false);
			dots[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			dots[i].setTag(i);
		}
		mCurIndex = 0;
		dots[mCurIndex].setEnabled(true);
	}

	@Override
	protected void loadData() {

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
		case R.id.guide_enter: 
		case R.id.guide_jump: {
			jump();
			break;
		}
		default:
			break;
		}
	}

	private void jump() {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		/**
		 * 是否登录
		 */
		boolean isLogin = DeliveryBusAPI.getInstance().getLoginStatus();
		if (isLogin) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		} else if (!DeliveryBusAPI.getInstance().getUserName().isEmpty()
				&& !DeliveryBusAPI.getInstance().getPassword().isEmpty()) {
			/**
			 * 自动登录
			 */
			DeliveryBusAPI.getInstance().autoLogin();
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 设置当前指示点
	 */
	private void setCurDot(int position) {
		if (position < 0 || position > pics.length || mCurIndex == position) {
			return;
		}
		dots[position].setEnabled(true);
		dots[mCurIndex].setEnabled(false);
		mCurIndex = position;
	}

	private class PageChangeListener implements OnPageChangeListener {
		
		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int position) {
			
		}

		// 当前页面被滑动时调用
		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
			// arg0 :当前页面，及你点击滑动的页面
			// arg1:当前页面偏移的百分比
			// arg2:当前页面偏移的像素位置
		}

		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int position) {
			// 设置底部小点选中状态
			setCurDot(position);
			switch (position) {
			case 0:
				mJump.setVisibility(View.VISIBLE);
				break;
			case 1:
				mJump.setVisibility(View.VISIBLE);
				break;
			case 2:
				mJump.setVisibility(View.GONE);
				break;
			/*case 3:
				mJump.setVisibility(View.GONE);
				break;*/
			default:
				break;
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(LoginEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_LOGIN_SUCCESS: {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case MsgId.MSGID_LOGIN_FAILED: {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
	}
}
