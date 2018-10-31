/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DownloadDialog.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.dialog
 * @Description: 下载apk
 * @author: zhaoqy
 * @date: 2017年7月18日 下午7:06:59
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;

public class DownloadDialog extends Dialog {
	private TextView mPercent;
	private ProgressBar mProgress;
	
	public DownloadDialog(Context context) {
		super(context, R.style.dialog_style);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		getWindow().setContentView(R.layout.dialog_download);

		initViews();
	}

	private void initViews() {
		mPercent = (TextView) findViewById(R.id.update_percent);
		mProgress = (ProgressBar) findViewById(R.id.update_progressbar);
	}
	
	public void setProgressBar(int size) {
		mPercent.setText("(" + size + "%)");
		mProgress.setProgress(size);
		mProgress.setMax(100);
	}
}
