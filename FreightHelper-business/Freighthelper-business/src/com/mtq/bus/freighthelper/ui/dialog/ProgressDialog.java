/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: ProgressDialog.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.dialog
 * @Description: 进度对话框
 * @author: zhaoqy
 * @date: 2017年6月26日 下午3:45:16
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class ProgressDialog extends Dialog implements OnClickListener {

	private static ProgressDialog mDialog = null;
	private static ProgressDialogListener mListener;

	public interface ProgressDialogListener {
		public void onCancel();
	}

	public ProgressDialog(Context context) {
		super(context);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建进度条
	 */
	private static ProgressDialog createDialog(Context context,
			final String msg, final ProgressDialogListener listener) {
		ProgressDialog.mListener = listener;
		if (context != null) {
			final Activity act = (Activity) context;
			if (null != act) {
				act.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mDialog == null) {
							mDialog = new ProgressDialog(act,
									R.style.dialog_style);
							mDialog.setContentView(R.layout.dialog_progress);
							mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
							mDialog.setCanceledOnTouchOutside(false);

							if (!TextUtils.isEmpty(msg)) {
								TextView tv_msg = (TextView) mDialog
										.findViewById(R.id.progress_content);
								if (null != tv_msg) {
									tv_msg.setText(msg);
								}
							}

							// 没有设置取消监听，不显示x按钮
							if (listener == null) {
								ImageView cancel = (ImageView) mDialog
										.findViewById(R.id.progress_cancel);
								cancel.setVisibility(View.GONE);
							}
							mDialog.show();
						}
					}
				});
			}
		}
		return mDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		try {
			if (mDialog == null)
				return;

			ImageView btn_cancel = (ImageView) mDialog
					.findViewById(R.id.progress_cancel);
			btn_cancel.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示等待进度条弹出框
	 */
	public static ProgressDialog showProcess(Context context) {
		createDialog(context, null, null);
		return mDialog;
	}

	/**
	 * 显示等待进度条弹出框
	 */
	public static ProgressDialog showProgress(Context context, String message) {
		createDialog(context, message, null);
		return mDialog;
	}

	/**
	 * 显示等待进度条弹出框
	 */
	public static ProgressDialog showProgress(Context context,
			ProgressDialogListener listener) {
		createDialog(context, null, listener);
		return mDialog;
	}

	/**
	 * 显示等待进度条弹出框
	 */
	public static ProgressDialog showProgress(Context context, String message,
			ProgressDialogListener listener) {
		createDialog(context, message, listener);
		return mDialog;
	}

	/**
	 * 等待进度条弹出框是否显示
	 */
	public static boolean isShowProgress() {
		return (null != mDialog && mDialog.isShowing());
	}

	/**
	 * 取消等待进度条弹出框
	 */
	public static void cancelProgress() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.cancel();
			mDialog = null;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.progress_cancel: {
			if (mListener != null) {
				mListener.onCancel();
			}
			cancelProgress();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancelProgress();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancelProgress();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
