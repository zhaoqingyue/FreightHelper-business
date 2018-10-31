package com.mtq.bus.freighthelper.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class BoxCard extends LinearLayout {

	private TextView mTitle;
	private TextView mNum;
	private String title;
	private String num;
	private int bgid;

	public BoxCard(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public BoxCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		handleTypedArray(context, attrs);
		initView(context);
	}

	private void handleTypedArray(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.BoxCard);
		title = typedArray.getString(R.styleable.BoxCard_bc_title);
		num = typedArray.getString(R.styleable.BoxCard_bc_num);
		bgid = typedArray.getResourceId(R.styleable.BoxCard_bc_bg, -1);
		
		typedArray.recycle();
	}

	private void initView(Context context) {
		inflate(context, R.layout.layout_box_card, this);
		/**
		 * 设置标题
		 */
		mTitle = (TextView) findViewById(R.id.box_card_title);
		mTitle.setText(title);
		
		mNum = (TextView) findViewById(R.id.box_card_num);
		mNum.setText(num);
		if (bgid > 0) {
			mNum.setBackgroundResource(bgid);
		}
	}
	
	public void setNum(int num) {
		mNum.setText(num + "");
	}
}
