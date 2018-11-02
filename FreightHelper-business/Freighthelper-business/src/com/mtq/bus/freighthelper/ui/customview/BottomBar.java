/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BottomBar.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.customview
 * @Description: 自定义导航栏
 * @author: zhaoqy
 * @date: 2017年6月15日 下午6:37:23
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.customview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BottomBar extends LinearLayout {

	private List<BottomBarTab> mTabs = new ArrayList<BottomBarTab>();
	private LinearLayout mTabLayout;
	private LayoutParams mTabParams;
	private int mCurrentPosition = 0;
	private OnTabSelectedListener mListener;

	public BottomBar(Context context) {
		this(context, null);
	}

	public BottomBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setOrientation(VERTICAL);

		mTabLayout = new LinearLayout(context);
		mTabLayout.setBackgroundColor(Color.WHITE);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		addView(mTabLayout, new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
		mTabParams.weight = 1;
	}

	public BottomBar addItem(final BottomBarTab tab) {
		tab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener == null)
					return;

				int pos = tab.getTabPosition();
				if (mCurrentPosition == pos) {
					mListener.onTabReselected(pos);
				} else {
					mListener.onTabSelected(pos, mCurrentPosition);
					tab.setSelected(true);
					mListener.onTabUnselected(mCurrentPosition);
					mTabs.get(mCurrentPosition).setSelected(false);
					mCurrentPosition = pos;
				}
			}
		});
		tab.setTabPosition(mTabLayout.getChildCount());
		tab.setLayoutParams(mTabParams);
		mTabLayout.addView(tab);
		mTabs.add(tab);
		return this;
	}

	public void setOnTabSelectedListener(
			OnTabSelectedListener onTabSelectedListener) {
		mListener = onTabSelectedListener;
	}

	public void setCurrentItem(final int position) {
		mTabLayout.post(new Runnable() {
			
			@Override
			public void run() {
				mTabLayout.getChildAt(position).performClick();
			}
		});
	}

	public int getCurrentItemPosition() {
		return mCurrentPosition;
	}

	/**
	 * 获取 Tab
	 */
	public BottomBarTab getItem(int index) {
		if (mTabs.size() < index)
			return null;
		return mTabs.get(index);
	}

	public interface OnTabSelectedListener {
		void onTabSelected(int position, int prePosition);

		void onTabUnselected(int position);

		void onTabReselected(int position);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, mCurrentPosition);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		if (mCurrentPosition != ss.position) {
			mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
			mTabLayout.getChildAt(ss.position).setSelected(true);
		}
		mCurrentPosition = ss.position;
	}

	static class SavedState extends BaseSavedState {
		private int position;

		public SavedState(Parcel source) {
			super(source);
			position = source.readInt();
		}

		public SavedState(Parcelable superState, int position) {
			super(superState);
			this.position = position;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(position);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
