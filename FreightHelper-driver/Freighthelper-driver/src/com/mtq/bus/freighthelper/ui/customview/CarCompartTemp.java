package com.mtq.bus.freighthelper.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class CarCompartTemp extends LinearLayout {

	private TextView mTitle;
	private TextView mContent;
	private SpringProgressView mSpring;
	private String title;
	private String content;

	public CarCompartTemp(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public CarCompartTemp(Context context, AttributeSet attrs) {
		super(context, attrs);
		handleTypedArray(context, attrs);
		initView(context);
	}

	private void handleTypedArray(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CarCompartTemp);
		title = typedArray.getString(R.styleable.CarCompartTemp_cct_title);
		content = typedArray.getString(R.styleable.CarCompartTemp_cct_content);
		typedArray.recycle();
	}

	private void initView(Context context) {
		inflate(context, R.layout.layout_carcompart_temp, this);
		/**
		 * 设置标题
		 */
		mTitle = (TextView) findViewById(R.id.carcompart_temperature_title);
		mTitle.setText(title);
		/**
		 * 设置内容
		 */
		mContent = (TextView) findViewById(R.id.carcompart_temperature_content);
		mContent.setText(content);
		
		mSpring = (SpringProgressView) findViewById(R.id.carcompart_temperature_sp);
	}

	public void setContent(String content) {
		mContent.setText(content);
	}
	
	/***
	 * 设置最大的进度值
	 */
	public void setMaxCount(float maxCount) {
		mSpring.setMaxCount(maxCount);
	}
	
	/***
	 * 设置当前的进度值
	 */
	public void setCurCount(float currentCount) {
		mSpring.setCurrentCount(currentCount);
	}
}
