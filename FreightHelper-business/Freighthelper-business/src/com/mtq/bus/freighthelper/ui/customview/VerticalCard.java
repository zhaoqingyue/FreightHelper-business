/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: VerticalCard.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.customview
 * @Description: 自定义垂直型卡片
 * @author: zhaoqy
 * @date: 2017年6月5日 下午2:54:11
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.DensityUtils;

public class VerticalCard extends LinearLayout implements OnClickListener {

	private ImageView mIcon;
	private TextView mTitle;
	private TextView mContent;
	private ImageView mArrow;
	private View mLine;
	private int imgid;
	private String title;
	private String content;
	private int content_color;
	private boolean clickable;
	private boolean alignRight;
	private boolean hascontent;
	private int imgarrowid;
	private boolean dropable;
	private boolean hasarrow;
	private boolean hasline;
	private boolean ismobile;
	private boolean withstar;
	private float paddingLeft;
	
	private boolean expend = false;
	private IVCListener mListener;

	public interface IVCListener {
		public void onVcClick(View view);
	}

	public void setOnVCListener(IVCListener listener) {
		mListener = listener;
	}

	public VerticalCard(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public VerticalCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		handleTypedArray(context, attrs);
		initView(context);
	}

	private void handleTypedArray(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.VerticalCard);
		imgid = typedArray.getResourceId(R.styleable.VerticalCard_vc_icon, -1);
		title = typedArray.getString(R.styleable.VerticalCard_vc_title);
		content = typedArray.getString(R.styleable.VerticalCard_vc_content);
		clickable = typedArray.getBoolean(
				R.styleable.VerticalCard_vc_clickable, false);
		@SuppressWarnings("deprecation")
		int def_color = getResources().getColor(R.color.text_normal_color);
		content_color = typedArray.getColor(
				R.styleable.VerticalCard_vc_content_color, def_color);
		alignRight = typedArray.getBoolean(
				R.styleable.VerticalCard_vc_alignRight, true);
		hascontent = typedArray.getBoolean(
				R.styleable.VerticalCard_vc_hascontent, false);
		hasarrow = typedArray.getBoolean(R.styleable.VerticalCard_vc_hasarrow,
				true);
		imgarrowid = typedArray.getResourceId(
				R.styleable.VerticalCard_vc_arrowicon, -1);
		dropable = typedArray.getBoolean(R.styleable.VerticalCard_vc_dropable,
				false);
		hasline = typedArray.getBoolean(R.styleable.VerticalCard_vc_hasline,
				true);
		ismobile = typedArray.getBoolean(R.styleable.VerticalCard_vc_ismobile,
				false);
		withstar = typedArray.getBoolean(R.styleable.VerticalCard_vc_withstar,
				false);
		paddingLeft = typedArray.getFloat(
				R.styleable.VerticalCard_vc_paddingLeft, 0);

		typedArray.recycle();
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		inflate(context, R.layout.layout_vertical_card, this);
		/**
		 * 设置图标
		 */
		if (imgid > 0) {
			mIcon = (ImageView) findViewById(R.id.vertical_card_icon);
			mIcon.setImageResource(imgid);
			mIcon.setVisibility(View.VISIBLE);
		}
		/**
		 * 设置标题
		 */
		mTitle = (TextView) findViewById(R.id.vertical_card_title);
		mTitle.setText(title);
		/**
		 * 设置内容
		 */
		if (hascontent) {
			mContent = (TextView) findViewById(R.id.vertical_card_content);
			mContent.setText(content);
			mContent.setTextColor(content_color);
			mContent.setVisibility(View.VISIBLE);
			if (!alignRight) {
				LinearLayout layout = (LinearLayout) findViewById(R.id.vertical_card_content_layout); 
				layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				layout.setPadding(0, 0, 0, 0);
				
				/**
				 * content左对齐，则paddingLeft为0
				 */
				mContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				mContent.setPadding(0, 0, 0, 0);
			} else {
				/**
				 * content右对齐，则paddingLeft为3%w
				 */
				int paddingLeft = (int) (DensityUtils.getWidth(context) * 0.03);
				mContent.setPadding(paddingLeft, 0, 0, 0);
			}

			if (ismobile) {
				int mobile_color = getResources()
						.getColor(R.color.mobile_color);
				mContent.setTextColor(mobile_color);
				mContent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				mContent.setOnClickListener(this);
			}

			if (clickable) {
				View layout = findViewById(R.id.vertical_card_content_layout); 
				layout.setOnClickListener(this);
			}
		}
		if (withstar) {
			findViewById(R.id.vertical_card_star).setVisibility(View.VISIBLE);
		}

		/**
		 * 显示右箭头
		 */
		if (hasarrow) {
			mArrow = (ImageView) findViewById(R.id.vertical_card_arrow);
			mArrow.setVisibility(View.VISIBLE);
			if (imgarrowid > 0) {
				mArrow.setImageResource(imgarrowid);
			}
			
			if (dropable) {
				mArrow.setOnClickListener(this);
			}
		}
		/**
		 * 显示下划线
		 */
		if (hasline) {
			mLine = findViewById(R.id.vertical_card_line);
			mLine.setVisibility(View.VISIBLE);
			if (paddingLeft > 0) {
				int paddingLeft = (int) (DensityUtils.getWidth(context) * this.paddingLeft);
				mLine.setPadding(paddingLeft, 0, 0, 0);
			}
		}
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	public void setContent(String content) {
		mContent.setText(content);
	}
	
	public void setContentColor(int color) {
		mContent.setTextColor(color);
	}
	
	public String getContent() {
		return mContent.getText().toString();
	}

	public boolean isExpend() {
		return expend;
	}
	
	public void closeExpend() {
		expend = false;
		mArrow.setImageResource(R.drawable.icon_drop_down);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.vertical_card_content: 
		case R.id.vertical_card_content_layout:{
			if (mListener != null) {
				mListener.onVcClick(this);
			}
			break;
		}
		case R.id.vertical_card_arrow: {
			if (mListener != null) {
				expend = !expend;
				if (expend) {
					mArrow.setImageResource(R.drawable.icon_drop_up);
				} else {
					mArrow.setImageResource(R.drawable.icon_drop_down);
				}
				mListener.onVcClick(this);
			}
			break;
		}
		default:
			break;
		}
	}
}
