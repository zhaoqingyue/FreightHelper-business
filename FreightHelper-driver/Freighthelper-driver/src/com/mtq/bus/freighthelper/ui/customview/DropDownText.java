/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DropDownText.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.customview
 * @Description: 自定义可下拉TextView
 * @author: zhaoqy
 * @date: 2017年6月5日 下午2:55:07
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class DropDownText extends LinearLayout implements OnClickListener {

	private TextView mTitle;
	private ImageView mArrow;
	private String title;
	private boolean dropable;
	private int bg_color;
	private int title_color;
	private boolean expend = false;

	private IDdtListener mListener;

	public interface IDdtListener {
		public void onDdtClick(View view);
	}

	public void setOnDdtListener(IDdtListener listener) {
		mListener = listener;
	}

	public DropDownText(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public DropDownText(Context context, AttributeSet attrs) {
		super(context, attrs);
		handleTypedArray(context, attrs);
		initView(context);
	}

	@SuppressWarnings("deprecation")
	private void handleTypedArray(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.DropDownText);
		title = typedArray.getString(R.styleable.DropDownText_ddt_title);
		dropable = typedArray.getBoolean(R.styleable.DropDownText_ddt_dropable,
				true);
		int def_color = getResources().getColor(R.color.windows_color);
		bg_color = typedArray.getColor(R.styleable.DropDownText_ddt_bg_color,
				def_color);
		def_color = getResources().getColor(R.color.text_normal_color);
		title_color = typedArray.getColor(
				R.styleable.DropDownText_ddt_title_color, def_color);
		typedArray.recycle();
	}

	private void initView(Context context) {
		inflate(context, R.layout.layout_dropdown_text, this);
		setBackgroundColor(bg_color);
		/**
		 * 设置标题
		 */
		mTitle = (TextView) findViewById(R.id.drop_down_title);
		mTitle.setText(title);
		mTitle.setTextColor(title_color);
		/**
		 * 显示右箭头
		 */
		if (dropable) {
			mArrow = (ImageView) findViewById(R.id.drop_down_arrow);
			mArrow.setVisibility(View.VISIBLE);
			mArrow.setOnClickListener(this);
		}
	}

	public boolean isExpend() {
		return expend;
	}
	
	public void setExpend(boolean expend) {
		this.expend = expend;
		if (expend) {
			mArrow.setImageResource(R.drawable.icon_drop_up);
		} else {
			mArrow.setImageResource(R.drawable.icon_drop_down);
		}
	}
	
	public void setTitle(String title) {
		mTitle.setText(title);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.drop_down_arrow: {
			if (mListener != null) {
				expend = !expend;
				if (expend) {
					mArrow.setImageResource(R.drawable.icon_drop_up);
				} else {
					mArrow.setImageResource(R.drawable.icon_drop_down);
				}
				mListener.onDdtClick(this);
			}
			break;
		}
		default:
			break;
		}
	}
}
