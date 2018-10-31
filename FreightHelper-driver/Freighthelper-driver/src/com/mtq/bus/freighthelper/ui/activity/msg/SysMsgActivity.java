/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: SysMsgActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.msg
 * @Description: 系统消息详情
 * @author: zhaoqy
 * @date: 2017年6月19日 下午10:34:06
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.utils.ConstantUtils;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class SysMsgActivity extends BaseActivity implements OnClickListener {

	protected static final String TAG = "SysMsgActivity";
	private ImageView mBack;
	private TextView mTitle;
	private TextView mMsgTitle;
	private TextView mMsgContent;
	private TextView mMsgTime;
	private MtqMsgSys mMsgSys;
	private boolean hasSetRead = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_msg_sys;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mMsgTitle = (TextView) findViewById(R.id.msg_sys_detail_title);
		mMsgContent = (TextView) findViewById(R.id.msg_sys_detail_content);
		mMsgTime = (TextView) findViewById(R.id.msg_sys_detail_time);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.msg_sys_detail);
		mBack.setVisibility(View.VISIBLE);
		mMsgSys = (MtqMsgSys) getIntent().getSerializableExtra(
				ConstantUtils.MTQ_MSG_SYS);
	}

	@Override
	protected void loadData() {
		updateMsg();
		if (mMsgSys != null && mMsgSys.readstatus == 0) {
			/**
			 * 已读状态为0，则更新已读状态
			 */
			int serialid = mMsgSys.serialid;
			CldLog.e(TAG, "serialid: " + serialid);
			DeliveryBusAPI.getInstance().setMsgSysRead(serialid + "");
		}
	}

	private void updateMsg() {
		if (mMsgSys != null) {
			mMsgTitle.setText(mMsgSys.title);
			mMsgContent.setText(mMsgSys.content);
			mMsgTime.setText(TimeUtils.stampToYmd(mMsgSys.time));
		}
	}

	@Override
	protected void updateUI() {

	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			back();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		/**
		 * 注释super.onBackPressed();
		 * 否则只走onBackPressed
		 */
		// super.onBackPressed();
		back();
	}

	private void back() {
		if (hasSetRead) {
			Intent intent = getIntent();
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_SET_MSG_SYS_READ_SUCCESS: {
			hasSetRead = true;
			break;
		}
		case MsgId.MSGID_SET_MSG_SYS_READ_FAILED: {
			hasSetRead = true;
			break;
		}
		default:
			break;
		}
	}
}
