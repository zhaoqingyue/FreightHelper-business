/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BottomBarTab.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.customview
 * @Description: 自定义导航栏tab
 * @author: zhaoqy
 * @date: 2017年6月15日 下午6:35:59
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class BottomBarTab extends FrameLayout {
	private ImageView mIcon;
	private TextView mTvTitle;
	private int mTabPosition = -1;

	private TextView mTvUnreadCount;

	public BottomBarTab(Context context, int icon, CharSequence title) {
		this(context, null, icon, title);
	}

	public BottomBarTab(Context context, AttributeSet attrs, int icon,
			CharSequence title) {
		this(context, attrs, 0, icon, title);
	}

	public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr,
			int icon, CharSequence title) {
		super(context, attrs, defStyleAttr);
		init(context, icon, title);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, int icon, CharSequence title) {

		LinearLayout lLContainer = new LinearLayout(context);
		lLContainer.setOrientation(LinearLayout.VERTICAL);
		lLContainer.setGravity(Gravity.CENTER);
		LayoutParams paramsContainer = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsContainer.gravity = Gravity.CENTER;
		lLContainer.setLayoutParams(paramsContainer);

		int fontsize = (int) (getResources().getDisplayMetrics().heightPixels * 0.09 * 0.21);
		int iconsize = (int) (getResources().getDisplayMetrics().heightPixels * 0.09 * 0.53);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				iconsize, iconsize);
		mIcon = new ImageView(context);
		mIcon.setImageResource(icon);
		mIcon.setLayoutParams(params);
		lLContainer.addView(mIcon);

		mTvTitle = new TextView(context);
		mTvTitle.setText(title);
		LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
		mTvTitle.setTextColor(getResources().getColor(R.color.tab_text_nor_color));
		mTvTitle.setLayoutParams(paramsTv);
		lLContainer.addView(mTvTitle);
		addView(lLContainer);

		int min = dip2px(context, 18);
		int padding = dip2px(context, 4);
		mTvUnreadCount = new TextView(context);
		mTvUnreadCount.setBackgroundResource(R.drawable.msg_bubble_bg);
		mTvUnreadCount.setMinWidth(min);
		mTvUnreadCount.setTextColor(Color.WHITE);
		float textSize = (float) (fontsize*0.8);
		mTvUnreadCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		mTvUnreadCount.setPadding(padding, 0, padding, 0);
		mTvUnreadCount.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams tvUnReadParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, min);
		tvUnReadParams.gravity = Gravity.CENTER;
		tvUnReadParams.leftMargin = dip2px(context, 19);
		tvUnReadParams.bottomMargin = dip2px(context, 20);
		mTvUnreadCount.setLayoutParams(tvUnReadParams);
		mTvUnreadCount.setVisibility(GONE);

		addView(mTvUnreadCount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			mTvTitle.setTextColor(getResources().getColor(
					R.color.tab_text_select_color));
		} else {
			mTvTitle.setTextColor(getResources().getColor(
					R.color.tab_text_nor_color));
		}
	}

	public void setTabPosition(int position) {
		mTabPosition = position;
		if (position == 0) {
			setSelected(true);
		}
	}

	public int getTabPosition() {
		return mTabPosition;
	}

	/**
	 * 设置未读数量
	 */
	public void setUnreadCount(int num) {
		if (num <= 0) {
			mTvUnreadCount.setText(String.valueOf(0));
			mTvUnreadCount.setVisibility(GONE);
		} else {
			mTvUnreadCount.setVisibility(VISIBLE);
			if (num > 99) {
				mTvUnreadCount.setText("99+");
			} else {
				mTvUnreadCount.setText(String.valueOf(num));
			}
		}
	}

	/**
	 * 获取当前未读数量
	 */
	public int getUnreadCount() {
		int count = 0;
		if (TextUtils.isEmpty(mTvUnreadCount.getText())) {
			return count;
		}
		if (mTvUnreadCount.getText().toString().equals("99+")) {
			return 99;
		}
		try {
			count = Integer.valueOf(mTvUnreadCount.getText().toString());
		} catch (Exception ignored) {
		}
		return count;
	}

	private int dip2px(Context context, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
