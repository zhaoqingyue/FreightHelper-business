/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: FeedbackActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.me
 * @Description: 反馈吐槽
 * @author: zhaoqy
 * @date: 2017年6月19日 下午11:08:57
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.apitest.activity.DebugTool;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.account.AccountAPI;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.db.DTypeTable;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.AddPicAdapter;
import com.mtq.bus.freighthelper.ui.adapter.DropDownAdapter;
import com.mtq.bus.freighthelper.ui.customview.ShadowDrawable;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.ui.dialog.PhotoPopup;
import com.mtq.bus.freighthelper.ui.dialog.PhotoPopup.OnPopupListener;
import com.mtq.bus.freighthelper.ui.dialog.ProgressDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog;
import com.mtq.bus.freighthelper.ui.dialog.PromptDialog.IPromptListener;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.mtq.bus.freighthelper.utils.DropDownUtils;
import com.mtq.bus.freighthelper.utils.FeedBackUtils;
import com.mtq.bus.freighthelper.utils.FileUtils;
import com.mtq.bus.freighthelper.utils.ImageTools;
import com.mtq.bus.freighthelper.utils.MsgId;
import com.mtq.bus.freighthelper.utils.PermissionUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDeviceDTypeListener;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqUploadAttachPicListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDeviceDType;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FeedbackActivity extends BaseActivity implements OnClickListener,
		IVCListener, OnItemClickListener {

	public static final String TAG = "FeedbackActivity";
	public static final int MAX_LENGTH = 300;
	public static final int IMAGE_CAPTURE = 1; // 拍照
	public static final int IMAGE_SELECT = 2; // 从相册选择
	public static final int POPUP_MODE_FDTYPE = 3;
	public static final int POPUP_MODE_DEVICE_TYPE = 4;

	private Context mContext;
	private LinearLayout mRoot;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mRight;

	private VerticalCard mFeedType;
	private VerticalCard mDeviceType;
	private EditText mFeedTitle;
	private EditText mContent;
	private TextView mCountdown;
	private EditText mMobile;

	private PhotoPopup mPopup;
	private TextView mLimit;
	private GridView mGridView;
	private AddPicAdapter mAdapter;
	private List<String> mPicPathList;
	private List<String> mMediaidList;

	private PopupWindow mPop;
	private DropDownAdapter mDropAdapter;
	private List<DropDown> mTexts;
	private List<DropDown> mFdTypes;
	private List<DropDown> mDeviceTypes;

	private int xoff = 0;
	private int mShadowWidth = 0;
	private int fdtype = 0;
	private int devicetype = 0;
	private int mPopupMode = 0;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_feedback;
	}

	@Override
	protected void initViews() {
		mRoot = (LinearLayout) findViewById(R.id.feedback);
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mRight = (TextView) findViewById(R.id.title_right_text);

		mFeedType = (VerticalCard) findViewById(R.id.feedback_type);
		mDeviceType = (VerticalCard) findViewById(R.id.feedback_device_type);
		mFeedTitle = (EditText) findViewById(R.id.feedback_title);
		mContent = (EditText) findViewById(R.id.feedback_content);
		mCountdown = (TextView) findViewById(R.id.feedback_countdown);
		mMobile = (EditText) findViewById(R.id.feedback_mobile);

		mGridView = (GridView) findViewById(R.id.feedback_add);
		mLimit = (TextView) findViewById(R.id.feedback_limit);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mFeedType.setOnVCListener(this);
		mDeviceType.setOnVCListener(this);
		mGridView.setOnItemClickListener(this);
		setTextChangedListener();
	}

	@Override
	protected void initData() {
		mContext = this;
		mTitle.setText(R.string.me_feedback);
		mBack.setVisibility(View.VISIBLE);
		mRight.setVisibility(View.VISIBLE);
		mRight.setText(R.string.btn_submit);
		mCountdown.setText(0 + "/" + MAX_LENGTH);
		mFeedTitle.requestFocus();

		xoff = (int) (DensityUtils.getWidth(this) * 0.60);
		mShadowWidth = DensityUtils.getDedaultSize(this);

		initAddPic();
		initPopup();
		initDrop();
	}

	private void initAddPic() {
		mMediaidList = new ArrayList<String>();
		mPicPathList = new ArrayList<String>();
		mAdapter = new AddPicAdapter(mContext, mPicPathList, 5);
		mGridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void initPopup() {
		mPopup = new PhotoPopup(this, new OnPopupListener() {

			@TargetApi(23)
			@Override
			public void onTakePhoto() {
				if (PermissionUtils.isNeedPermission(FeedbackActivity.this,
						Manifest.permission.CAMERA, PermissionUtils.CAMERA)) {
					Toast.makeText(mContext, "请打开摄像头权限", Toast.LENGTH_SHORT)
							.show();
				} else {
					captureImage(FileUtils.getTmpCacheFilePath());
				}
			}

			@Override
			public void onChoosePhoto() {
				selectImage();
			}
		});
	}

	private void initDrop() {
		mTexts = new ArrayList<DropDown>();
		mDropAdapter = new DropDownAdapter(this, mTexts);
		mFdTypes = DropDownUtils.getFdTypeDrop();
	}

	@Override
	protected void loadData() {
		if (CldPhoneNet.isNetConnected()) {
			getDeviceDType();
		} else {
			/**
			 * 无网络时，读取数据库的数据
			 */
			updateDeviceType();
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
			finish();
			break;
		}
		case R.id.title_right_text: {
			submit();
			break;
		}
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.feedback_type: {
			if (mFeedType.isExpend()) {
				mTexts.clear();
				if (mFdTypes != null && !mFdTypes.isEmpty()) {
					mTexts.addAll(mFdTypes);
					mDropAdapter.notifyDataSetChanged();
					mPopupMode = POPUP_MODE_FDTYPE;
					dropDown();
				}
			}
			break;
		}
		case R.id.feedback_device_type: {
			if (mDeviceType.isExpend()) {
				mTexts.clear();
				if (mDeviceTypes != null && !mDeviceTypes.isEmpty()) {
					mTexts.addAll(mDeviceTypes);
					mDropAdapter.notifyDataSetChanged();
					mPopupMode = POPUP_MODE_DEVICE_TYPE;
					dropDown();
				}
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == mPicPathList.size()) {
			if (PermissionUtils.isNeedPermissionForStorage(this)) {
				Toast.makeText(this, "请打开储存空间权限", Toast.LENGTH_SHORT).show();
			} else {
				mPopup.showAtLocation(mRoot, Gravity.TOP, 0, 0);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void dropDown() {
		if (mPop == null) {
			ListView listView = new ListView(this);
			listView.setCacheColorHint(0x00000000);
			listView.setBackgroundColor(getResources().getColor(R.color.white));

			Drawable drawable = getResources().getDrawable(
					R.drawable.divider_bg);
			listView.setDivider(drawable);
			listView.setDividerHeight(1);
			listView.setAdapter(mDropAdapter);

			ShadowDrawable.Builder.on(listView)
					.bgColor(getResources().getColor(R.color.white))
					.shadowColor(Color.parseColor("#000000"))
					.shadowRange(mShadowWidth).offsetBottom(mShadowWidth)
					.offsetTop(mShadowWidth).offsetLeft(mShadowWidth).create();

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mPop.dismiss();
					itemClick(position);
				}
			});
			int width = (int) (DensityUtils.getWidth(this) * 0.40);
			mPop = new PopupWindow(listView, width + mShadowWidth,
					LayoutParams.WRAP_CONTENT, true);
			mPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			mPop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mPop.dismiss();
					if (mPopupMode == POPUP_MODE_FDTYPE) {
						mFeedType.closeExpend();
					} else if (mPopupMode == POPUP_MODE_DEVICE_TYPE) {
						mDeviceType.closeExpend();
					}
				}
			});
		}

		if (mPopupMode == POPUP_MODE_FDTYPE) {
			mPop.showAsDropDown(mFeedType, xoff - mShadowWidth, -mShadowWidth);
		} else if (mPopupMode == POPUP_MODE_DEVICE_TYPE) {
			mPop.showAsDropDown(mDeviceType, xoff - mShadowWidth, -mShadowWidth);
		}
	}

	protected void itemClick(int position) {
		if (mPopupMode == POPUP_MODE_FDTYPE) {
			switch (position) {
			case 0: {
				fdtype = 1;
				mDeviceType.setVisibility(View.GONE);
				break;
			}
			case 1: {
				fdtype = 2;
				mDeviceType.setVisibility(View.VISIBLE);
				break;
			}
			default:
				break;
			}
		} else if (mPopupMode == POPUP_MODE_DEVICE_TYPE) {
			switch (position) {
			case 0: {
				devicetype = 2;
				break;
			}
			case 1: {
				devicetype = 3;
				break;
			}
			case 2: {
				devicetype = 4;
				break;
			}
			case 3: {
				devicetype = 5;
				break;
			}
			default:
				break;
			}
		}

		changeUi();
	}

	private void changeUi() {
		String typeStr = FeedBackUtils.getFdType(fdtype);
		String deviceType = FeedBackUtils.getDeviceType(devicetype);
		String title = "";
		if (fdtype == 1) {
			title = typeStr;
		} else if (fdtype == 2) {
			title = typeStr + deviceType;
		} 
		mFeedType.setContent(typeStr);
		mDeviceType.setContent(deviceType);
		mFeedTitle.setText(title);
		mFeedTitle.setSelection(title.length());
	}

	private void submit() {
		String typeStr = FeedBackUtils.getFdType(fdtype);
		String deviceType = FeedBackUtils.getDeviceType(devicetype);
		final String title = /*typeStr + deviceType*/mFeedTitle.getText().toString();
		final String content = mContent.getText().toString();
		if (TextUtils.isEmpty(typeStr)) {
			Toast.makeText(this, "请选择反馈类型", Toast.LENGTH_SHORT).show();
			return;
		} /*
		 * else if (TextUtils.isEmpty(deviceType)) { Toast.makeText(this,
		 * "请选择设备类型", Toast.LENGTH_SHORT).show(); }
		 */else if (TextUtils.isEmpty(title)) {
			Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "请输入反馈内容", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (fdtype == 1) {
				devicetype = 0;
			} else if (fdtype == 2) {
				if (TextUtils.isEmpty(deviceType)) {
					Toast.makeText(this, "请选择设备类型", Toast.LENGTH_SHORT).show();
					return;
				}
			}

			/**
			 * 手机号 (是否要判断手机号是否有效)
			 */
			String mobile = mMobile.getText().toString();
			if (mobile.equals("")) {
				mobile = null;
			} else {
				if (!AccountAPI.getInstance().isPhoneNum(mobile)) {
					Toast.makeText(this, "请输入有效的手机号码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

			/**
			 * 图片文件信息
			 */
			String pics = "";
			if (mMediaidList != null && !mMediaidList.isEmpty()) {
				int len = mMediaidList.size();
				for (int i = 0; i < len; i++) {
					pics += mMediaidList.get(i);
					if (i < len - 1) {
						pics += ";";
					}
				}
			}
			if (pics.equals("")) {
				pics = null;
			}
			setFeedback(fdtype, devicetype, title, content, mobile, pics);
		}
	}

	/**
	 * 获取设备类型
	 */
	private void getDeviceDType() {
		DeliveryBusAPI.getInstance().getDeviceDType(
				new IMtqDeviceDTypeListener() {

					@Override
					public void onResult(int errCode, List<MtqDeviceDType> data) {
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}
						
						updateDeviceType();
						if (errCode == 0 && data != null && !data.isEmpty()) {
							/**
							 * 将最新的设备类型插入到数据库中
							 */
							DTypeTable.getInstance().insert(data);
						}
					}
				});
	}

	/**
	 * 吐槽反馈
	 */
	private void setFeedback(final int fdtype, final int dtype,
			final String title, final String content, final String contact,
			final String pics) {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(this, R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
		} else {
			String str = getResources().getString(R.string.tip_submiting);
			ProgressDialog.showProgress(this, str, null);

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					/**
					 * 吐槽
					 */
					DeliveryBusAPI.getInstance().setFeedback(fdtype, dtype,
							title, content, contact, pics);
				}
			}, 500);
		}
	}

	/**
	 * 拍照
	 */
	public void captureImage(String path) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory("android.intent.category.DEFAULT");
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		Uri uri = Uri.fromFile(new File(path, "image.jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, IMAGE_CAPTURE);
	}

	/**
	 * 从图库中选取图片
	 */
	public void selectImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(intent, IMAGE_SELECT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && resultCode != RESULT_CANCELED) {
			String afterCompressPicPath;
			switch (requestCode) {
			case IMAGE_CAPTURE:
				afterCompressPicPath = ImageTools.compress(
						FeedbackActivity.this, FileUtils.getTmpCacheFilePath()
								+ "/image.jpg");
				if (afterCompressPicPath != null
						&& !TextUtils.isEmpty(afterCompressPicPath)) {
					uploadPic(afterCompressPicPath);
				} else {
					Toast.makeText(mContext, "获取图片失败,请检查设置是否允许储存空间权限",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case IMAGE_SELECT:
				Uri originalUri = data.getData();
				try {
					afterCompressPicPath = null;
					String fp = FileUtils
							.getRealFilePath(mContext, originalUri);
					afterCompressPicPath = ImageTools.compress(
							FeedbackActivity.this, fp);
					if (afterCompressPicPath != null
							&& !TextUtils.isEmpty(afterCompressPicPath)) {
						uploadPic(afterCompressPicPath);
					} else {
						Toast.makeText(mContext, "获取图片失败,请检查设置是否允许储存空间权限",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	public void uploadPic(final String filepath) {
		String str = getResources().getString(R.string.tip_sending);
		ProgressDialog.showProgress(this, str, null);

		String base64_pic = null;
		try {
			byte[] datas = FileUtils.toByteArray(filepath);
			base64_pic = Base64.encodeToString(datas, Base64.DEFAULT);
			DebugTool.saveFile(base64_pic);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}

		int x = 410750200;
		int y = 81353968;
		DeliveryBusAPI.getInstance().uploadAttachPic(x, y, base64_pic,
				new IMtqUploadAttachPicListener() {

					@Override
					public void onResult(int errCode, String mediaid) {
						if (ProgressDialog.isShowProgress()) {
							ProgressDialog.cancelProgress();
						}
						
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0) {
							Toast.makeText(mContext, "图片上传成功",
									Toast.LENGTH_SHORT).show();
							mPicPathList.add(filepath);
							RefreshHint();
							mAdapter.setList(mPicPathList);
							mMediaidList.add(mediaid);
						} else {
							Toast.makeText(mContext, "图片上传失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	protected void RefreshHint() {
		if (mPicPathList.size() > 0)
			mLimit.setVisibility(View.GONE);
		else {
			mLimit.setVisibility(View.VISIBLE);
			mLimit.setText("请您上传图片");
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		case MsgId.MSGID_SET_FEEDBACK_SUCCESS: {
			// Toast.makeText(this, "反馈成功", Toast.LENGTH_SHORT).show();
			if (ProgressDialog.isShowProgress()) {
				ProgressDialog.cancelProgress();
			}

			String content = getResources().getString(
					R.string.dialog_feedback_success);
			feedbackResult(content, true);
			break;
		}
		case MsgId.MSGID_SET_FEEDBACK_FAILED: {
			// Toast.makeText(this, "反馈失败", Toast.LENGTH_SHORT).show();
			if (ProgressDialog.isShowProgress()) {
				ProgressDialog.cancelProgress();
			}

			String content = getResources().getString(
					R.string.dialog_feedback_failed);
			feedbackResult(content, false);
			break;
		}
		default:
			break;
		}
	}

	private void updateDeviceType() {
		mDeviceTypes = new ArrayList<DropDown>();
		List<MtqDeviceDType> dtype = DTypeTable.getInstance().query();
		if (dtype != null && !dtype.isEmpty()) {
			int len = dtype.size();
			for (int i = 0; i < len; i++) {
				DropDown drop = new DropDown();
				drop.id = dtype.get(i).dtype;
				drop.name = dtype.get(i).dname;
				mDeviceTypes.add(drop);
			}
		} else {
			/*
			 * mDeviceTypes.add("北斗双模一体机"); mDeviceTypes.add("凯立德KPND");
			 * mDeviceTypes.add("TD-BOX"); mDeviceTypes.add("TD-PND");
			 */
			mDeviceTypes = DropDownUtils.getDeviceTypeDrop();
		}
	}

	private void feedbackResult(String rsult, final boolean success) {
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

	private void setTextChangedListener() {
		mContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > MAX_LENGTH) {
					s = s.subSequence(0, MAX_LENGTH);
					mContent.setText(s);
					mContent.setSelection(s.length());
				}

				String content = mContent.getText().toString();
				mCountdown.setText(content.length() + "/" + MAX_LENGTH);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
