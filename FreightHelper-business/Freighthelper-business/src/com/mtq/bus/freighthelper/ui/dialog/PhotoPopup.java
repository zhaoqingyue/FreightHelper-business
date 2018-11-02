/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: PhotoPopup.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.dialog
 * @Description: 图片选择PopupWindow
 * @author: zhaoqy
 * @date: 2017年6月21日 下午7:31:16
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class PhotoPopup extends PopupWindow implements OnClickListener {

	private OnPopupListener mListener;
	private View mPopView;

	public PhotoPopup(Context context, OnPopupListener listener) {
		super(context);
		mListener = listener;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView = inflater.inflate(R.layout.dialog_select_photo, null);
		setContentView(mPopView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		TextView choose_photo = (TextView) mPopView
				.findViewById(R.id.setphoto_choose_photo);
		TextView take_photo = (TextView) mPopView
				.findViewById(R.id.setphoto_take_photo);
		TextView cancel = (TextView) mPopView.findViewById(R.id.setphoto_cancel);
		choose_photo.setOnClickListener(this);
		take_photo.setOnClickListener(this);
		cancel.setOnClickListener(this);

		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 点击外面的控件也可以使得PopUpWindow dimiss
		setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);// 半透明颜色
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setphoto_choose_photo: {
			dismiss();
			mListener.onChoosePhoto();
			break;
		}
		case R.id.setphoto_take_photo: {
			dismiss();
			mListener.onTakePhoto();
			break;
		}
		case R.id.setphoto_cancel: {
			dismiss();
			break;
		}
		default:
			break;
		}
	}

	public interface OnPopupListener {
		/**
		 * @Description: 拍照
		 */
		public void onTakePhoto();

		/**
		 * @Description: 从相册中获取
		 */
		public void onChoosePhoto();
	}
}
