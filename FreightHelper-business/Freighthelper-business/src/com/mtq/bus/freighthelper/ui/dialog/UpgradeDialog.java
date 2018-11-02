/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: UpdateDialog.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.dialog
 * @Description: 升级对话框
 * @author: zhaoqy
 * @date: 2017年7月18日 下午7:04:06
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class UpgradeDialog extends Dialog implements OnClickListener {

	private TextView mTitle;
	private TextView mContent;
	private Button mCancel;
	private Button mSure;
	private String title;
	private String content;
	private String cancel;
	private String sure;

	private IUpdateListener mListener;

	public interface IUpdateListener {

		public void OnCancel();

		public void OnSure();
	}

	public UpgradeDialog(Context context) {
		super(context);
	}

	public UpgradeDialog(Context context, String title, String content,
			String cancel, String sure, IUpdateListener listener) {
		super(context, R.style.dialog_style);
		this.title = title;
		this.content = content;
		this.cancel = cancel;
		this.sure = sure;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		getWindow().setContentView(R.layout.dialog_upgrade);

		initViews();
		setListener();
		setViews();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.update_title);
		mContent = (TextView) findViewById(R.id.update_content);
		mCancel = (Button) findViewById(R.id.update_cancel);
		mSure = (Button) findViewById(R.id.update_sure);
	}

	private void setListener() {
		mSure.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}

	private void setViews() {
		mTitle.setText(title);
		mContent.setText(content);
		if (TextUtils.isEmpty(content)) {
			mContent.setVisibility(View.GONE);
		}
		
		mSure.setText(sure);
		mCancel.setText(cancel);
		if (TextUtils.isEmpty(cancel)) {
			mCancel.setVisibility(View.GONE);
			findViewById(R.id.update_line).setVisibility(View.GONE);;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_cancel: {
			dismiss();
			if (mListener != null) {
				mListener.OnCancel();
			}
			break;
		}
		case R.id.update_sure: {
			dismiss();
			if (mListener != null) {
				mListener.OnSure();
			}
			break;
		}
		default:
			break;
		}
	}
}
