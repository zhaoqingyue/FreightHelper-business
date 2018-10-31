/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: ScheduleActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.car
 * @Description: 调度消息
 * @author: zhaoqy
 * @date: 2017年6月13日 下午8:25:04
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.car;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cld.log.CldLog;
import com.cld.mapapi.model.LatLng;
import com.cld.mapapi.util.CoordinateConverter;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.audio.MtqRecorderUPlayer;
import com.mtq.bus.freighthelper.audio.MtqScheduleAudioFile;
import com.mtq.bus.freighthelper.bean.AddressBean;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;
import com.mtq.bus.freighthelper.ui.dialog.SendVoiceDialog;
import com.mtq.bus.freighthelper.ui.dialog.SendVoiceDialog.OnSendVoiceDialogListner;
import com.mtq.bus.freighthelper.utils.DisplayUtil;
import com.mtq.bus.freighthelper.utils.MtqStringUtils;
import com.mtq.bus.freighthelper.utils.TextUtil;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqCarSendListener;
import com.zhy.android.percent.support.PercentLinearLayout;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ScheduleActivity extends BaseActivity implements OnClickListener,
		OnSendVoiceDialogListner, TextWatcher {

	private static final String TAG = "ScheduleActivity";
	private static final int VOICE_REQUEST_CODE = 0;
	private static final int MAX_TEXT_NUM = 100;
	private static final int REQUEST_FOR_DRIVER = 1;
	private static final int REQUEST_FOR_LOCATION = 2;

	private ImageView mBack;
	private TextView mTitle;

	private VerticalCard mLicense;
	private VerticalCard mDriver;
	private TextView mModeText;
	private TextView mModeVoice;
	private TextView mPoi;
	private EditText mEdit;
	private TextView mCount;
	private View mSend;

	private SendVoiceDialog sendVoiceDialog;
	private String encodeBase64File = "";

	private PercentLinearLayout ll_scedule_voice_play_img;
	private ImageView schedule_send_voice_bg;
	private TextView schedule_sendvoice_text_time;

	private String carlicense;
	private String mdriver;

	private int smstype = 1;
	private int carid = 0;
	private int driverid = 0;
	private long x = 0;
	private long y = 0;
	private String addr = null;
	private String poiname = null;
	private String text = null;
	private int voiceduration;
	private String voicedata;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_schedule;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mLicense = (VerticalCard) findViewById(R.id.schedule_license);
		mDriver = (VerticalCard) findViewById(R.id.schedule_driver);
		mModeText = (TextView) findViewById(R.id.schedule_mode_text);
		mModeVoice = (TextView) findViewById(R.id.schedule_mode_voice);
		mPoi = (TextView) findViewById(R.id.schedule_map_poi);
		mEdit = (EditText) findViewById(R.id.schedule_msg_edit);
		mCount = (TextView) findViewById(R.id.schedule_msg_count);
		mSend = findViewById(R.id.schedule_send);
		mSend.setAlpha(0.5f);
		// 发送语音的布局
		ll_scedule_voice_play_img = (PercentLinearLayout) findViewById(R.id.ll_scedule_voice_play_img);
		schedule_send_voice_bg = (ImageView) findViewById(R.id.schedule_send_voice_bg);
		schedule_sendvoice_text_time = (TextView) findViewById(R.id.schedule_sendvoice_text_time);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mDriver.setOnClickListener(this);
		mModeText.setOnClickListener(this);
		mModeVoice.setOnClickListener(this);
		findViewById(R.id.schedule_map_select_point).setOnClickListener(this);
		mSend.setOnClickListener(this);

		sendVoiceDialog = new SendVoiceDialog(this);
		sendVoiceDialog.setOnSendVoiceDialogListner(this);
		mEdit.addTextChangedListener(this);
		schedule_send_voice_bg.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// 设置不可以被点击
		mSend.setClickable(false);
		mTitle.setText(R.string.car_schedule);
		mBack.setVisibility(View.VISIBLE);
		mModeText.setSelected(true);
		if (getIntent() != null) {
			carid = getIntent().getIntExtra("carid", 0);
			carlicense = getIntent().getStringExtra("carlicense");
			mdriver = getIntent().getStringExtra("mdriver");
			driverid = getIntent().getIntExtra("mdriverid", 0);
			CldLog.e(TAG, "车辆ID: " + carid + ", carlicense: " + carlicense);
			CldLog.e(TAG, "mdriver: " + mdriver + ", driverid: " + driverid);
		}
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void updateUI() {
		mLicense.setContent(carlicense);
		if (TextUtils.isEmpty(mdriver)) {
			mdriver = "请选择司机";
		}
		mDriver.setContent(mdriver);
		mEdit.setVisibility(View.VISIBLE);
		mCount.setVisibility(View.VISIBLE);
		mCount.setText("0/100");
	}

	@Override
	protected void onConnectivityChange() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.schedule_driver: {
			Intent intent = new Intent(this, DriverListActivity.class);
			startActivityForResult(intent, REQUEST_FOR_DRIVER);
			break;
		}
		case R.id.schedule_mode_text: {
			mModeText.setSelected(true);
			if (mEdit.getText().toString().trim().isEmpty()) {
				mSend.setAlpha(0.5f);
				mSend.setClickable(false);
			}
			mModeVoice.setSelected(false);
			smstype = 1;
			mEdit.setVisibility(View.VISIBLE);
			mCount.setVisibility(View.VISIBLE);
			ll_scedule_voice_play_img.setVisibility(View.GONE);
			break;
		}
		case R.id.schedule_mode_voice: {
			mModeText.setSelected(false);
			mModeVoice.setSelected(true);
			smstype = 2;
			mEdit.setVisibility(View.GONE);
			mCount.setVisibility(View.GONE);

			requestSendVoicePermision();
			// sendVoiceDialog.show();
			break;
		}
		case R.id.schedule_map_select_point: {
			Intent intent = new Intent(this, MapSelectPointActivity.class);
			startActivityForResult(intent, REQUEST_FOR_LOCATION);
			break;
		}
		case R.id.schedule_send: {
			startSendScheduleNetWork();
			break;
		}
		case R.id.schedule_send_voice_bg: {
			MtqRecorderUPlayer.getInstance().startPlay(this,
					MtqScheduleAudioFile.getWavFilePath());
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_FOR_DRIVER: {
				if (data != null) {
					Bundle bundle = data.getExtras();
					driverid = bundle.getInt("driverid");
					CldLog.e(TAG, "司机ID: " + driverid);
					String drivername = bundle.getString("driver_name");
					mDriver.setContent(drivername);
				}
				break;
			}
			case REQUEST_FOR_LOCATION: {
				if (data != null) {
					AddressBean parcelable = data
							.getParcelableExtra("addressinfo");
					addr = parcelable.address;
					poiname = data.getStringExtra("poiname");
					/**
					 * 经度、纬度转化成凯立德坐标
					 */
					LatLng latLng = new LatLng();
					latLng.latitude = parcelable.y;
					latLng.longitude = parcelable.x;
					LatLng ll = CoordinateConverter.LatLngConvertCld(latLng);
					x = (long) ll.longitude;
					y = (long) ll.latitude;
					mPoi.setText(addr);
				}
				break;
			}
			default:
				break;
			}
		}
	}

	@SuppressLint("InlinedApi")
	private void requestSendVoicePermision() {
		if ((ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
				&& (ContextCompat.checkSelfPermission(this,
						Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
				&& (ContextCompat.checkSelfPermission(this,
						Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
			sendVoiceDialog.show();
			// 判断是否开启语音权限
		} else {
			ActivityCompat.requestPermissions(this, new String[] {
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.READ_EXTERNAL_STORAGE },
					VOICE_REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == VOICE_REQUEST_CODE) {
			if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
					&& (grantResults[1] == PackageManager.PERMISSION_GRANTED)
					&& (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
				sendVoiceDialog.show();
			} else {
				Toast.makeText(this, "已拒绝权限！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	/**
	 * 文字调度/语音调度的网络请求
	 */
	private void startSendScheduleNetWork() {
		String str = getResources().getString(R.string.send_scedule_msg);
		ProgressDialog.showProgress(this, str, null);
		if (driverid == 0) {
			Toast.makeText(this, "请选择调度司机!", Toast.LENGTH_SHORT).show();
			if (ProgressDialog.isShowProgress()) {
				ProgressDialog.cancelProgress();
			}
			return;
		}
		int time = (int) (System.currentTimeMillis() / 1000);
		// 判断是文字调度还是语音调度1,文字. 2:语音
		if (smstype == 1) {
			// 文本内容
			text = mEdit.getText().toString().trim();
			voiceduration = 0;
			voicedata = null;
		} else if (smstype == 2) {
			// 语音调度
			voicedata = encodeBase64File;
		}

		CldLog.e(TAG, "time:" + time + "...smstype :" + smstype + "...carid :"
				+ carid + "...driverid :" + driverid + "...x :" + x + "...y :"
				+ y + "...addr :" + addr + "...text :" + text
				+ "...voiceduration :" + voiceduration + "...voicedata :"
				+ voicedata);
		DeliveryBusAPI.getInstance().setCarSend(time, smstype, carid, driverid,
				x, y, addr, poiname, text, voiceduration, voicedata,
				new IMtqCarSendListener() {

					@Override
					public void onResult(int errCode) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							if (ProgressDialog.isShowProgress()) {
								ProgressDialog.cancelProgress();
							}

							String content = getResources().getString(
									R.string.dialog_schedule_send_success_text);
							sendResult(content, true);
						} else {
							if (ProgressDialog.isShowProgress()) {
								ProgressDialog.cancelProgress();
							}

							if (errCode == 1004) {
								/**
								 * 邀请状态不是"同意"的，没有权限调度
								 */
								Toast.makeText(ScheduleActivity.this,
										"没有权限调度该司机", Toast.LENGTH_SHORT).show();
							} else {
								String content = getResources()
										.getString(
												R.string.dialog_schedule_send_failed_text);
								sendResult(content, false);
							}
						}
					}
				});
	}

	private void sendResult(String rsult, final boolean success) {
		String sure = getResources().getString(R.string.dialog_know);
		PromptDialog dialog = new PromptDialog(this, rsult, "", "", sure,
				new IPromptListener() {

					@Override
					public void OnCancel() {

					}

					@Override
					public void OnSure() {
						if (success) {
							finish();
						}
					}
				});
		dialog.show();
	}

	@Override
	public void onSendVoiceTime(String mRecordfile, int voicetime) {
		try {
			encodeBase64File = MtqStringUtils.encodeBase64File(mRecordfile);
			voiceduration = voicetime;
			// 设置可以点击
			ll_scedule_voice_play_img.setVisibility(View.VISIBLE);

			initdynamicVoiceWidth();
			mEdit.setVisibility(View.GONE);
			mCount.setVisibility(View.GONE);
			mSend.setAlpha(1f);
			mSend.setClickable(true);
			schedule_sendvoice_text_time.setText(voicetime + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initdynamicVoiceWidth() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				DisplayUtil.getByWidthPosition(200 + voiceduration * 5),
				DisplayUtil.getByWidthPosition(84));
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		schedule_send_voice_bg.setLayoutParams(params);
	}

	@Override
	public void onSendVoiceCancelClick() {
		mModeText.setSelected(true);
		mModeVoice.setSelected(false);
		smstype = 1;
		mEdit.setVisibility(View.VISIBLE);
		mCount.setVisibility(View.VISIBLE);
		ll_scedule_voice_play_img.setVisibility(View.GONE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Editable editable = mEdit.getText();
		if (s.toString().isEmpty()) {
			mSend.setClickable(false);
			mSend.setAlpha(.5f);
			return;
		}
		int len = editable.length();
		// 设置可以点击
		mSend.setClickable(true);
		mSend.setAlpha(1f);
		if (len > MAX_TEXT_NUM) {
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			// 截取新字符串
			String newStr = str.substring(0, MAX_TEXT_NUM);
			mEdit.setText(newStr);
			editable = mEdit.getText();

			// 新字符串的长度
			int newLen = editable.length();
			// 旧光标位置超过字符串长度
			if (selEndIndex > newLen) {
				selEndIndex = editable.length();
			}
			mCount.setTag(newLen + "");
			// 设置新光标所在的位置
			Selection.setSelection(editable, selEndIndex);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		mCount.setText((mEdit.getText() == null ? 0 : mEdit.getText().length())
				+ "/100");

		// 需要去除表情包的输入
		int index = mEdit.getSelectionStart() - 1;
		if (index > 0) {
			if (TextUtil.isEmojiCharacter(s.charAt(index))) {
				Editable edit = mEdit.getText();
				edit.delete(s.length() - 2, s.length());
				Toast.makeText(ScheduleActivity.this,
						getResources().getString(R.string.cannot_edit_emoj),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 失去焦点的时候如果还在播放就关闭
		MtqRecorderUPlayer.getInstance().stopPlay();
	}
}
