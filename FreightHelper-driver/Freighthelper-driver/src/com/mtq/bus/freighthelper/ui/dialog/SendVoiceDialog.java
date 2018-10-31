package com.mtq.bus.freighthelper.ui.dialog;

import java.io.File;
import java.io.IOException;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.audio.MtqAudioErrorCode;
import com.mtq.bus.freighthelper.audio.MtqRecorderUPlayer;
import com.mtq.bus.freighthelper.audio.MtqScheduleAudioFile;
import com.mtq.bus.freighthelper.audio.MtqScheduleAudioRecord;
import com.mtq.bus.freighthelper.utils.DisplayUtil;
import com.mtq.bus.freighthelper.utils.FileUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SendVoiceDialog extends Dialog implements android.view.View.OnClickListener {

	private static final String TAG = "SendVoiceDialog";
	private ImageView dialog_title_img_cancel;
	private LinearLayout dialog_sendvoice_content_container;
	private ImageView dialog_send_voice_bg;
	//播放录音的帧动画
	private ImageView dialog_send_voice_anim;
	private TextView dialog_sendvoice_text_time;
	private ImageView dialog_sendvoice_delete_btn;
	private ImageView dialog_sendvoice_voiceing_img;
	private Button dialog_sendvoice_button_round_bg;
	private Button dialog_sendvoice_btn_submit;
	private RelativeLayout dialog_sendvoice_img;

	private final static int MAX_RECODOR_TIME = 60;
	private final static int FLAG_WAV = 0;
	//amr格式,暂时还不做
	private final static int FLAG_AMR = 1;
	private final static int REFRESSH_CODE = 5;
	private int mState = -1; // -1:没再录制，0：录制wav，1：录制amr
	private int voiceTime;
	private final static int CMD_RECORDFAIL = 2001;
	private final static int CMD_STOP = 2002;
	private int vTime;
	private Handler uiHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			Log.d("MyHandler", "handleMessage......");
			super.handleMessage(msg);
			switch (msg.what) {
			case CMD_RECORDFAIL:
				int vErrorCode = (Integer) msg.obj;
				String vMsg = MtqAudioErrorCode.getErrorInfo(getContext(), vErrorCode);
				// dialog_sendvoice_button_round_bg.setText("录音失败："+vMsg);
				break;
			case REFRESSH_CODE: {
				uiHandler.removeMessages(REFRESSH_CODE);
				vTime++;
				dialog_sendvoice_text_time.setText((vTime >= MAX_RECODOR_TIME ? MAX_RECODOR_TIME : vTime) + "\"");
				removeMessages(REFRESSH_CODE);
				if (vTime >= MAX_RECODOR_TIME) {
					sendStopMsg();
				} else {
					sendEmptyMessageDelayed(REFRESSH_CODE, 1000);
				}
				break;
			}
			case CMD_STOP:
				int vFileType = (Integer) msg.obj;
				switch (vFileType) {
				case FLAG_WAV:
					uiHandler.removeMessages(REFRESSH_CODE);
					voiceTime = vTime;
					MtqScheduleAudioRecord mRecord_1 = MtqScheduleAudioRecord.getInstance();
					mRecord_1.stopRecordAndFile();
					long mSize = mRecord_1.getRecordFileSize();
					if (mSize >= 0) {
						Log.e(TAG, "大小  :"+mSize);
						stopRecorder();
					} else {
						dialog_sendvoice_content_container.setVisibility(View.GONE);
						dialog_sendvoice_button_round_bg
								.setText(getContext().getResources().getString(R.string.dialog_sendvoice_release_save));
						dialog_sendvoice_button_round_bg.setAlpha(1f);
					}
					break;

				}
				break;
			default:
				break;
			}
		}
	};

	private void sendStopMsg() {
		Message msg2 = uiHandler.obtainMessage(CMD_STOP);
		msg2.obj = FLAG_WAV;
		msg2.sendToTarget();
	};

	public SendVoiceDialog(Context context) {
		super(context);
	}

	public SendVoiceDialog(Context context, String title) {
		super(context, R.style.dialog_style);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 点击Dialog以外的区域，Dialog不关闭
		setCanceledOnTouchOutside(false);
		// 设置成系统级别的Dialog，即全局性质的Dialog，在任何界面下都可以弹出来
		// getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		getWindow().setContentView(R.layout.dialog_send_voice);

		initViews();
		setListener();
	}

	private void initViews() {
		// 关闭dialog
		dialog_title_img_cancel = (ImageView) findViewById(R.id.dialog_title_img_cancel);
		// 中间容器
		dialog_sendvoice_content_container = (LinearLayout) findViewById(R.id.dialog_sendvoice_content_container);
		dialog_sendvoice_img = (RelativeLayout) findViewById(R.id.dialog_sendvoice_img);
		dialog_send_voice_bg = (ImageView) findViewById(R.id.dialog_send_voice_bg);
		dialog_send_voice_anim = (ImageView) findViewById(R.id.dialog_send_voice_anim);
		dialog_sendvoice_text_time = (TextView) findViewById(R.id.dialog_sendvoice_text_time);
		dialog_sendvoice_delete_btn = (ImageView) findViewById(R.id.dialog_sendvoice_delete_btn);
		// 下部按钮
		dialog_sendvoice_voiceing_img = (ImageView) findViewById(R.id.dialog_sendvoice_voiceing_img);
		dialog_sendvoice_button_round_bg = (Button) findViewById(R.id.dialog_sendvoice_button_round_bg);
		dialog_sendvoice_btn_submit = (Button) findViewById(R.id.dialog_sendvoice_btn_submit);

		setAudioRecoder();
	}

	private void setListener() {
		dialog_title_img_cancel.setOnClickListener(this);
		dialog_sendvoice_img.setOnClickListener(this);
		dialog_sendvoice_delete_btn.setOnClickListener(this);
		dialog_sendvoice_btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_title_img_cancel: {
			// 如果正在播放 则关闭
			if (mOnSendVoiceDialogListner != null) {
				mOnSendVoiceDialogListner.onSendVoiceCancelClick();
			}
			deleteSendVoiceBtn();
			dialog_sendvoice_content_container.setVisibility(View.GONE);
			dismiss();
			break;
		}
		case R.id.dialog_sendvoice_img: {
			dialog_sendvoice_content_container.setVisibility(View.VISIBLE);
			dynamicVoiceWeith();
			
			MtqRecorderUPlayer.getInstance().startPlay(getContext(), MtqScheduleAudioFile.getWavFilePath());
			break;
		}
		case R.id.dialog_sendvoice_delete_btn: {

			deleteSendVoiceBtn();
			dialog_sendvoice_content_container.setVisibility(View.GONE);
			break;
		}
		case R.id.dialog_sendvoice_btn_submit: {
			dialog_sendvoice_content_container.setVisibility(View.GONE);
			if (MtqScheduleAudioFile.getWavFilePath().isEmpty() || MtqScheduleAudioFile.getWavFilePath() == null) {
				return;
			}
			File file = new File(MtqScheduleAudioFile.getWavFilePath());
			if (file.exists() && voiceTime >= 1) {
				Log.i(TAG, voiceTime + "");
				if (mOnSendVoiceDialogListner != null) {
					mOnSendVoiceDialogListner.onSendVoiceTime(MtqScheduleAudioFile.getWavFilePath(), voiceTime);
				}
				dialog_sendvoice_content_container.setVisibility(View.GONE);
				dismiss();
			} else {
				Toast.makeText(getContext(),
						getContext().getResources().getString(R.string.dialog_sendvoice_please_voice),
						Toast.LENGTH_SHORT).show();

			}

			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// dialog失去焦点就停止播放
		MtqRecorderUPlayer.getInstance().stopPlay();
	}
	//点击了删除
	private void deleteSendVoiceBtn() {
		Log.i(TAG, MtqScheduleAudioFile.getWavFilePath());

		if (MtqScheduleAudioFile.getWavFilePath().isEmpty() || MtqScheduleAudioFile.getWavFilePath() == null) {
			return;
		}
		MtqRecorderUPlayer.getInstance().stopPlay();

		File file = new File(MtqScheduleAudioFile.getWavFilePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileUtils.delete(file);

		// 录音时长制为0
		dialog_sendvoice_text_time.setText(00 + "\"");
	}

	private void setAudioRecoder() {
		
		// Button的touch监听

		dialog_sendvoice_button_round_bg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("", "actioni-->" + event.getAction());
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					dialog_sendvoice_content_container.setVisibility(View.GONE);
					deleteSendVoiceBtn();
					startRecord(FLAG_WAV);
					dialog_sendvoice_button_round_bg
							.setText(getContext().getResources().getString(R.string.dialog_sendvoice_release_save));
					dialog_sendvoice_button_round_bg.setAlpha(0.5f);
					break;
				case MotionEvent.ACTION_MOVE:
					// 超出button界线外的ui处理,暂时还没有交互效果,不做

					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					uiHandler.removeMessages(REFRESSH_CODE);
					sendStopMsg();
					break;
				}
				return true;
			}

		});

	}

	private void stopRecorder() {
		dialog_sendvoice_content_container.setVisibility(View.VISIBLE);
		dynamicVoiceWeith();
		dialog_sendvoice_text_time.setText(getTotalTime());
		dialog_sendvoice_button_round_bg.setAlpha(1f);
		// mAudioRecoderUtils.cancelRecord(); //取消录音（不保存录音文件）
		dialog_sendvoice_button_round_bg
				.setText(getContext().getResources().getString(R.string.dialog_sendvoice_press_audio));
	}

	private void dynamicVoiceWeith() {
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
				DisplayUtil.getByWidthPosition(200+vTime*5),
				DisplayUtil.getByWidthPosition(84));
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.leftMargin=DisplayUtil.getByWidthPosition(70);
		dialog_send_voice_bg.setLayoutParams(params);
	}

	private String getTotalTime() {
		return vTime + "\"";
	}

	public interface OnSendVoiceDialogListner {
		void onSendVoiceTime(String mRecordfile, int voicetime);
		void onSendVoiceCancelClick();
	}

	private OnSendVoiceDialogListner mOnSendVoiceDialogListner;

	public void setOnSendVoiceDialogListner(OnSendVoiceDialogListner onSendVoiceDialogListner) {
		mOnSendVoiceDialogListner = onSendVoiceDialogListner;
	}

	/**
	 * 开始录音,amr格式暂时不做,后台还不支持
	 * 
	 * @param mFlag，0：录制wav格式，1：录音amr格式
	 */
	private void startRecord(int mFlag) {
		vTime = 0;
		if (mState != -1) {
			Message msg = uiHandler.obtainMessage(CMD_RECORDFAIL);
			msg.obj = MtqAudioErrorCode.E_STATE_RECODING;
			msg.sendToTarget();
			return;
		}
		int mResult = -1;
		switch (mFlag) {
		case FLAG_WAV:
			MtqScheduleAudioRecord mRecord_1 = MtqScheduleAudioRecord.getInstance();
			mResult = mRecord_1.startRecordAndFile();
			break;

		}
		if (mResult == MtqAudioErrorCode.SUCCESS) {
			uiHandler.sendEmptyMessage(REFRESSH_CODE);
		} else {
			Message msg = uiHandler.obtainMessage(CMD_RECORDFAIL);
			msg.obj = mResult;
			msg.sendToTarget();
		}
	}

}
