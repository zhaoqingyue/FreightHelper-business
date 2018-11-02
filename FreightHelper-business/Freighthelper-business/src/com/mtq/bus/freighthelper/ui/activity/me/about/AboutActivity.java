/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AboutActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me.about
 * @Description: 关于界面
 * @author: zhaoqy
 * @date: 2017年6月2日 下午3:51:56
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me.about;

import java.io.File;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.cld.log.CldLog;
import com.cld.net.CldFileDownloader;
import com.cld.net.ICldFileDownloadCallBack;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.center.AppCenterAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.AppVersionManager;
import com.mtq.bus.freighthelper.manager.AppVersionManager.IAppVersionListener;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.ui.dialog.DownloadDialog;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog.ProgressDialogListener;
import com.mtq.bus.freighthelper.ui.dialog.UpgradeDialog;
import com.mtq.bus.freighthelper.ui.dialog.UpgradeDialog.IUpdateListener;
import com.mtq.bus.freighthelper.utils.AppInfo;
import com.mtq.bus.freighthelper.utils.CallUtils;
import com.mtq.bus.freighthelper.utils.FileUtils;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.ols.sap.bean.CldSapKAppParm.MtqAppInfoNew;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AboutActivity extends BaseActivity implements OnClickListener,
		IVCListener {

	protected static final String TAG = "AboutActivity";
	private ImageView mBack;
	private TextView mTitle;
	private TextView mVersion;
	private VerticalCard mUpdate;
	private VerticalCard mService;
	private MtqAppInfoNew mAppParm;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_about;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mVersion = (TextView) findViewById(R.id.about_version);
		mUpdate = (VerticalCard) findViewById(R.id.about_update);
		mService = (VerticalCard) findViewById(R.id.about_customer_service);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
		findViewById(R.id.about_legal_notice).setOnClickListener(this);
		findViewById(R.id.about_user_agreement).setOnClickListener(this);
		findViewById(R.id.about_license_agreement).setOnClickListener(this);
		mService.setOnVCListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.me_about);
		mBack.setVisibility(View.VISIBLE);
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void updateUI() {
		boolean debug = false;
		if (debug) {
			String vername = AppInfo.getVerName(this);
			if (!TextUtils.isEmpty(vername)) {
				mVersion.setText("Android V" + vername);
			}

			boolean hasNew = AppCenterAPI.getInstance().hasNewVersion();
			if (hasNew) {
				/**
				 * 有新版本
				 */
				mHadler.sendEmptyMessage(1);
			} else {
				/**
				 * 已是最新版本
				 */
				mHadler.sendEmptyMessage(0);
			}
		}
	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		String vername = AppInfo.getVerName(this);
		if (!TextUtils.isEmpty(vername)) {
			mVersion.setText("Android V" + vername);
		}
		AppVersionManager.getInstance().checkVersion(new IAppVersionListener() {

			@Override
			public void onResult(int errCode, MtqAppInfoNew result) {
				if (ProgressDialog.isShowProgress()) {
					ProgressDialog.cancelProgress();
				}
				if (errCode == 0) {
					mAppParm = result;
					if (mAppParm != null && mAppParm.version > 0
							&& !TextUtils.isEmpty(mAppParm.filepath)) {
						CldLog.e(TAG, "version: " + mAppParm.version);
						CldLog.e(TAG, "filepath: " + mAppParm.filepath);
						mHadler.sendEmptyMessage(1);
					} else {
						mHadler.sendEmptyMessage(0);
					}
				} else {
					/**
					 * 获取失败
					 */
					CldLog.e(TAG, "checkVersion failed. errCode: " + errCode);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.about_update: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(this, R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
				return;
			}

			boolean hasNew = AppCenterAPI.getInstance().hasNewVersion();
			if (hasNew) {
				mAppParm = AppCenterAPI.getInstance().getMtqAppInfo();
				if (mAppParm != null) {
					onCheckResult();
				} else {
					checkAppVersion();
				}
			} else {
				checkAppVersion();
			}
			break;
		}
		case R.id.about_legal_notice: {
			Intent intent = new Intent(this, LegalNoticeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.about_user_agreement: {
			Intent intent = new Intent(this, UserAgreementActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.about_license_agreement: {
			Intent intent = new Intent(this, LicenseAgreementActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.about_customer_service: {
			String mobile = "400-789-0118";
			CallUtils.makeCall(this, mobile);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case PermissionUtils.CALL_PHONE: {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				String mobile = "400-789-0118";
				CallUtils.call(this, mobile);
			} else {
				/**
				 * 无权限
				 */
				Toast.makeText(this, "请打开拨打电话权限", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			super.onRequestPermissionsResult(requestCode, permissions,
					grantResults);
			break;
		}
	}

	private void checkAppVersion() {
		String str = getResources().getString(
				R.string.common_network_version_update);
		ProgressDialog.showProgress(this, str, new ProgressDialogListener() {

			@Override
			public void onCancel() {

			}
		});

		AppVersionManager.getInstance().checkVersion(new IAppVersionListener() {

			@Override
			public void onResult(int errCode, MtqAppInfoNew result) {
				if (ProgressDialog.isShowProgress()) {
					ProgressDialog.cancelProgress();
				}

				if (errCode == 0) {
					mAppParm = result;
					if (mAppParm != null && mAppParm.version > 0
							&& !TextUtils.isEmpty(mAppParm.filepath)) {

						CldLog.e(TAG, "version: " + mAppParm.version);
						CldLog.e(TAG, "filepath: " + mAppParm.filepath);
						mHadler.sendEmptyMessage(1);
						mHadler.sendEmptyMessage(2);
					} else
						mHadler.sendEmptyMessage(0);
				} else {
					/**
					 * 获取失败
					 */
					CldLog.e(TAG, "checkVersion failed. errCode: " + errCode);
				}
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void onCheckResult() {
		if (mAppParm != null) {
			/**
			 * 有新版本
			 */
			String content = "您使用的版本过低，敬请升级到最新的版本使用;";
			if (!TextUtils.isEmpty(mAppParm.mark)) {
				content += "\n" + mAppParm.mark;
			}
			String title = getResources().getString(
					R.string.about_update_has_new_version);
			String cancel = getResources().getString(R.string.dialog_no_need);
			String sure = getResources().getString(R.string.dialog_update_now);
			if (mAppParm.upgradeflag == 1) {
				/**
				 * 强制升级，只要一个按钮
				 */
				cancel = "";
			}
			UpgradeDialog dialog = new UpgradeDialog(this, title, content,
					cancel, sure, new IUpdateListener() {

						@Override
						public void OnCancel() {
							mHadler.sendEmptyMessage(1);
						}

						@Override
						public void OnSure() {
							updateVersion();
						}
					});
			dialog.show();
		} else {
			mHadler.sendEmptyMessage(0);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHadler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				/**
				 * 已是最新版本
				 */
				if (ProgressDialog.isShowProgress()) {
					ProgressDialog.cancelProgress();
				}

				String update = getResources().getString(
						R.string.about_update_latest);
				mUpdate.setContent(update);
				break;
			}
			case 1: {
				/**
				 * 有新版本
				 */
				String new_version = getResources().getString(
						R.string.about_update_has_new_version);
				mUpdate.setContent(new_version);
				break;
			}
			case 2: {
				onCheckResult();
				break;
			}
			default:
				break;
			}
		};
	};

	DownloadDialog dialog;
	CldFileDownloader mDownloader = null;

	protected void updateVersion() {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			if (PermissionUtils.isNeedPermissionForStorage(this)) {
				Toast.makeText(this, "请打开储存空间权限", Toast.LENGTH_SHORT).show();
				return;
			}

			String downloadUrl = mAppParm.filepath;
			if (!TextUtils.isEmpty(downloadUrl)) {
				if (dialog != null)
					dialog.cancel();

				dialog = new DownloadDialog(this);
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@SuppressWarnings("static-access")
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {

						if (keyCode == KeyEvent.KEYCODE_BACK
								&& event.getRepeatCount() == 0
								&& event.getAction() == KeyEvent.ACTION_UP) {
							if (mDownloader != null) {
								mDownloader.stop();
								mDownloader.resetMonitor();
							}
						}
						return false;
					}
				});

				dialog.show();
				startDownloadApk(downloadUrl);
			} else {
				/**
				 * 下载失败
				 */
				updateFail();
			}
		}
	}

	private void updateFail() {
		String string = "下载失败，请检查网络";
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("static-access")
	private void startDownloadApk(String url) {
		if (mDownloader == null) {
			mDownloader = new CldFileDownloader();
		}
		mDownloader.stop();
		mDownloader.resetMonitor();

		String target = FileUtils.getFileStorePath() + "/update" + ".apk";
		mDownloader.downloadFile(url, target, false, mDownloadCB);
	}

	@SuppressLint("NewApi")
	private boolean hasPermission() {
		String permission = Manifest.permission.CALL_PHONE;
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}

	/**
	 * @Description 安装apk
	 * @author：Wenap
	 * @date：2014-12-31 下午8:32:53
	 * @return void
	 */

	protected void installApk() {
		if (dialog != null)
			dialog.dismiss();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		String target = FileUtils.getFileStorePath() + "/update.apk";
		intent.setDataAndType(Uri.fromFile(new File(target)),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		/**
		 * 清楚版本信息数据
		 */
		AppCenterAPI.getInstance().clearAppVersion();
		// finish();
	}

	/**
	 * 下载回调
	 */
	ICldFileDownloadCallBack mDownloadCB = new ICldFileDownloadCallBack() {

		@Override
		public void onConnecting(boolean bReconnect, String errMsg) {
			Log.e("update", "bReconnect: " + bReconnect + ", msg: "
					+ bReconnect);

			// if(bReconnect == false){
			// updateHandler.sendEmptyMessage(0);
			// }

			if (!CldPhoneNet.isNetConnected()) {
				updateHandler.sendEmptyMessage(5);
			}

		}

		@Override
		public void updateProgress(long down, long total, long rate) {
			int progress = (int) ((down * 1.0 / total) * 100);
			CldLog.i("down: " + down + ", total: " + total + ", rate: " + rate
					+ ",progress: " + progress + "%");
			Message msg = new Message();
			DownProgress bean = new DownProgress(down, total, rate);
			msg.what = 1;
			msg.obj = bean;
			updateHandler.sendMessage(msg);
		}

		@Override
		public void onSuccess(long size, long elapseMs) {
			updateHandler.sendEmptyMessage(2);
		}

		@Override
		public void onFailure(String errMsg) {
			CldLog.i("onFailure!!! errMsg: " + errMsg);
			updateHandler.sendEmptyMessage(0);
		}

		@Override
		public void onCancel() {
			updateHandler.sendEmptyMessage(3);
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler updateHandler = new Handler() {

		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0: // 下载失败
					if (null != dialog) {
						dialog.dismiss();
					}
					if (mDownloader != null) {
						mDownloader.stop();
						mDownloader.resetMonitor();
						// mDownloader.setCB(null);

					}
					updateFail();
					break;
				case 1: // 下载中
					DownProgress bean = (DownProgress) msg.obj;
					if (null == mAppParm) {
						CldLog.e("upgradeInfo is null!");
						return;
					}
					if (null == bean) {
						CldLog.e("DOWN bean is null!");
						return;
					}
					int progress = bean.progress;
					dialog.setProgressBar(progress);
					break;
				case 2: // 下载完成
					dialog.setProgressBar(100);
					postDelayed(new Runnable() {

						@Override
						public void run() {
							installApk();
						}
					}, 500);

					break;
				case 3:// 取消
					break;
				case 5:
					if (null != dialog) {
						dialog.dismiss();
					}

					if (mDownloader != null) {
						mDownloader.setCB(null);
						mDownloader.stop();
						mDownloader.resetMonitor();
					}
					updateFail();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 
	 * 下载进度bean
	 * 
	 * @author Zhouls
	 * @date 2016-3-29 下午6:04:46
	 */
	public class DownProgress {

		public long down;
		public long total;
		public long rate;
		public int progress;

		public DownProgress() {

		}

		public DownProgress(long down, long total, long rate) {
			if (down > 0 && total > 0) {
				this.down = down;
				this.total = total;
				this.rate = rate;
				this.progress = (int) ((down * 1.0 / total) * 100);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (dialog != null) {
			dialog.dismiss();
		}

		if (mDownloader != null) {
			mDownloader.stop();
		}
		super.onDestroy();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onBackPressed() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			if (mDownloader != null) {
				mDownloader.stop();
				mDownloader.resetMonitor();
			}
		} else {
			finish();
		}
	}
}
