package com.mtq.bus.freighthelper.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class CarStateCard extends LinearLayout {

	private ImageView mIcon;
	private TextView mTitle;
	private TextView mContent;
	private int imgid;
	private String title;
	private String content;
	private int content_color;

	public CarStateCard(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public CarStateCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		handleTypedArray(context, attrs);
		initView(context);
	}

	private void handleTypedArray(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CarStateCard);
		imgid = typedArray.getResourceId(R.styleable.CarStateCard_csc_icon, -1);
		title = typedArray.getString(R.styleable.CarStateCard_csc_title);
		content = typedArray.getString(R.styleable.CarStateCard_csc_content);

		@SuppressWarnings("deprecation")
		int def_color = getResources().getColor(R.color.black);
		content_color = typedArray.getColor(
				R.styleable.CarStateCard_csc_content_color, def_color);
		typedArray.recycle();
	}

	private void initView(Context context) {
		inflate(context, R.layout.layout_carstate_card, this);
		/**
		 * 设置图标
		 */
		if (imgid > 0) {
			mIcon = (ImageView) findViewById(R.id.carstate_card_icon);
			mIcon.setImageResource(imgid);
			mIcon.setVisibility(View.VISIBLE);
		}
		/**
		 * 设置标题
		 */
		mTitle = (TextView) findViewById(R.id.carstate_card_title);
		mTitle.setText(title);
		/**
		 * 设置内容
		 */
		mContent = (TextView) findViewById(R.id.carstate_card_content);
		mContent.setText(content);
		mContent.setTextColor(content_color);
	}

	public void setContent(String content) {
		mContent.setText(content);
	}
	
	public void setContentColor(int color) {
		mContent.setTextColor(color);
	}
}
