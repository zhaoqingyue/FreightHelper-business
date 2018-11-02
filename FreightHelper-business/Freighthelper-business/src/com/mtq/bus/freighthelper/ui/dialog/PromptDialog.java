package com.mtq.bus.freighthelper.ui.dialog;

import com.mtq.bus.freighthelper.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PromptDialog extends Dialog implements OnClickListener {

	private Context mContext;
	private TextView mTitle;
	private TextView mContent;
	private Button mCancel;
	private Button mSure;
	private String title;
	private String content;
	private String cancel;
	private String sure;
	private int resid = 0;

	private IPromptListener mListener;

	public interface IPromptListener {

		public void OnCancel();

		public void OnSure();
	}

	public PromptDialog(Context context) {
		super(context);
		mContext = context;
	}

	public PromptDialog(Context context, String title) {
		super(context, R.style.dialog_style);
		mContext = context;
		this.title = title;
	}

	public PromptDialog(Context context, String title, String content,
			String cancel, String sure, IPromptListener listener) {
		super(context, R.style.dialog_style);
		mContext = context;
		this.title = title;
		this.content = content;
		this.cancel = cancel;
		this.sure = sure;
		mListener = listener;
	}
	
	public PromptDialog(Context context, String title, String content,
			String cancel, String sure, int resid, IPromptListener listener) {
		super(context, R.style.dialog_style);
		mContext = context;
		this.title = title;
		this.content = content;
		this.cancel = cancel;
		this.sure = sure;
		this.resid = resid;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 点击Dialog以外的区域，Dialog不关闭
		setCanceledOnTouchOutside(false);
		// 设置成系统级别的Dialog，即全局性质的Dialog，在任何界面下都可以弹出来
		// getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		getWindow().setContentView(R.layout.dialog_prompt);

		initViews();
		setListener();
		setViews();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.prompt_title);
		mContent = (TextView) findViewById(R.id.prompt_content);
		mCancel = (Button) findViewById(R.id.prompt_cancel);
		mSure = (Button) findViewById(R.id.prompt_sure);
	}

	private void setListener() {
		mSure.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}

	private void setViews() {
		mTitle.setText(title);
		
		/**
		 * 没有效果
		 */
		if (resid > 0) {
			@SuppressWarnings("deprecation")
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.icon_driver_img_selected);
			drawable.setBounds(drawable.getMinimumWidth(),
					drawable.getMinimumHeight(), 0, 0);
			mTitle.setCompoundDrawables(drawable, null, null, null);
		}
		
		mContent.setText(content);
		if (TextUtils.isEmpty(content)) {
			mContent.setVisibility(View.GONE);
		}
		
		mSure.setText(sure);
		mCancel.setText(cancel);
		if (TextUtils.isEmpty(cancel)) {
			mCancel.setVisibility(View.GONE);
			findViewById(R.id.prompt_line).setVisibility(View.GONE);;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prompt_cancel: {
			dismiss();
			if (mListener != null) {
				mListener.OnCancel();
			}
			break;
		}
		case R.id.prompt_sure: {
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
