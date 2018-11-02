/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DriverListActivity.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.activity.car
 * @Description: 司机列表
 * @author: zhaoqy
 * @date: 2017年7月6日 上午11:59:47
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.activity.car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.Driver;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.db.DriverTable;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.DriverAdapter;
import com.mtq.bus.freighthelper.ui.adapter.ItemClickListener;
import com.mtq.bus.freighthelper.ui.customview.ClearEditText;
import com.mtq.bus.freighthelper.ui.customview.SideBar;
import com.mtq.bus.freighthelper.ui.customview.SideBar.OnTouchingLetterChangedListener;
import com.mtq.bus.freighthelper.utils.CharacterParser;
import com.mtq.bus.freighthelper.utils.PinyinComparator;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqDriverDataListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class DriverListActivity extends BaseActivity implements
		OnClickListener, OnTouchingLetterChangedListener, TextWatcher {

	public static final int GET_DRIVER_ID = 0;
	public static final int PAGESIZE = 200;
	
	public static final int UPDATE_LOADING = 0;
	public static final int UPDATE_NET_ABNORMAL = 1;
	public static final int UPDATE_SUCCESS = 2;
	public static final int UPDATE_FAILED = 3;
	public static final int UPDATE_EMPTY = 4;

	private ImageView mBack;
	private TextView mTitle;
	private TextView mRight;
	private TextView mEmpty;
	private LinearLayout mFailed;
	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mLayout;
	private ClearEditText mClear;
	private ListView mListView;
	private SideBar mSideBar;
	private TextView mSelected;
	private DriverAdapter mAdapter;
	private List<Driver> mCurList;
	private List<Driver> mTotalList;
	private List<MtqDriver> mDriverList;
	private CharacterParser mParser;
	private PinyinComparator mComparator;
	private List<String> mLetters;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_driver_list;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mRight = (TextView) findViewById(R.id.title_right_text);
		mEmpty = (TextView) findViewById(R.id.driver_empty);
		mFailed = (LinearLayout) findViewById(R.id.driver_failed);
		mNetAbnormal = (LinearLayout) findViewById(R.id.driver_net_abnormal);
		mLoading = (LinearLayout) findViewById(R.id.driver_laoding);
		mLayout = (LinearLayout) findViewById(R.id.driver_layout);
		mClear = (ClearEditText) findViewById(R.id.driver_search);
		mListView = (ListView) findViewById(R.id.driver_listview);
		mSideBar = (SideBar) findViewById(R.id.driver_sidrbar);
		mSelected = (TextView) findViewById(R.id.driver_selected);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		findViewById(R.id.failed_refresh).setOnClickListener(this);
		mClear.addTextChangedListener(this);
		mSideBar.setOnTouchingLetterChangedListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.car_driver_list);
		mBack.setVisibility(View.VISIBLE);
		mRight.setVisibility(View.VISIBLE);
		mRight.setText(R.string.btn_save);

		mParser = CharacterParser.getInstance();
		mComparator = new PinyinComparator();
		mSideBar.setTextView(mSelected);
		mTotalList = new ArrayList<Driver>();
		mDriverList = new ArrayList<MtqDriver>();
	}

	@Override
	protected void loadData() {
		/**
		 * 3:表示已同意
		 */
		mDriverList.clear();
		mDriverList = DriverTable.getInstance().queryByStatus(3);
		if (mDriverList != null && !mDriverList.isEmpty()) {
			updateDriver();
		} else {
			if (!CldPhoneNet.isNetConnected()) {
				update(UPDATE_NET_ABNORMAL);
			} else {
				/**
				 * 获取司机列表
				 */
				getDriverDataList(1);
			}
		}
	}

	private void updateDriver() {
		mCurList = transformDriver(mDriverList);
		Collections.sort(mCurList, mComparator);
		mTotalList.clear();
		mTotalList.addAll(mCurList);
		mAdapter = new DriverAdapter(this, mCurList, mListener);
		mListView.setAdapter(mAdapter);
		setLetters();
		update(UPDATE_SUCCESS);
	}

	@Override
	protected void updateUI() {
		
	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	private void setLetters() {
		mLetters = new ArrayList<String>();
		List<String> letter = new ArrayList<String>();
		/**
		 * 获取通讯录首字母
		 */
		int len = mCurList.size();
		for (int i = 0; i < len; i++) {
			Driver contacts = mCurList.get(i);
			letter.add(contacts.letter);
		}

		/**
		 * 去掉重复字母
		 */
		mLetters.add(letter.get(0));
		for (int i = 1; i < len; i++) {
			if (!letter.get(i).equalsIgnoreCase(letter.get(i - 1))) {
				mLetters.add(letter.get(i));
			}
		}
		mSideBar.setLetters(mLetters);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.title_right_text: {
			Driver driver = getSelectedDriver();
			if (driver != null) {
				Intent intent = getIntent();
				intent.putExtra("driverid", driver.driverid);
				intent.putExtra("driver_name", driver.driver_name);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				if (mCurList != null && !mCurList.isEmpty()) {
					String str = getResources().getString(
							R.string.please_selector_driver);
					Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		case R.id.net_abnormal_refresh:
		case R.id.failed_refresh: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(this, R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				update(UPDATE_LOADING);
				getDriverDataList(1);
			}
			break;
		}
		default:
			break;
		}
	}

	private Driver getSelectedDriver() {
		if (mCurList != null && !mCurList.isEmpty()) {
			int len = mCurList.size();
			for (int i = 0; i < len; i++) {
				if (mCurList.get(i).selected) {
					return mCurList.get(i);
				}
			}
		}
		return null;
	}

	private ItemClickListener mListener = new ItemClickListener() {

		@Override
		public void itemOnClick(int position, View v) {
			int len = mCurList.size();
			for (int i = 0; i < len; i++) {
				Driver driver = mCurList.get(i);
				driver.selected = false;
			}

			for (int i = 0; i < len; i++) {
				Driver driver = mCurList.get(i);
				if (i == position) {
					driver.selected = true;
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	};

	private void getDriverDataList(int pageindex) {
		DeliveryBusAPI.getInstance().getDriverDataList(3, null, pageindex,
				PAGESIZE, new IMtqDriverDataListener() {

					@Override
					public void onResult(int errCode, List<MtqDriver> data,
							int tatal) {
						if (errCode == 0) {
							if (data != null && !data.isEmpty()) {
								mDriverList.clear();
								int len = data.size();
								for (int i = 0; i < len; i++) {
									/**
									 * 已同意加入车队的司机
									 */
									if (data.get(i).invitestatus == 3) {
										mDriverList.add(data.get(i));
									}
								}

								if (!mDriverList.isEmpty()) {
									updateDriver();
								} else {
									update(UPDATE_EMPTY);
								}
								/**
								 * 插入到数据库
								 */
								DriverTable.getInstance().insert(data);
							} else {
								update(UPDATE_EMPTY);
							}
						} else {
							update(UPDATE_FAILED);
						}
					}
				});
	}

	protected void update(int type) {
		switch (type) {
		case UPDATE_LOADING: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.VISIBLE);
			mLayout.setVisibility(View.GONE);
			break;
		}
		case UPDATE_NET_ABNORMAL: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mLayout.setVisibility(View.GONE);
			break;
		}
		case UPDATE_SUCCESS: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mLayout.setVisibility(View.VISIBLE);
			break;
		}
		case UPDATE_FAILED: {
			mEmpty.setVisibility(View.GONE);
			mFailed.setVisibility(View.VISIBLE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mLayout.setVisibility(View.GONE);
			break;
		}
		case UPDATE_EMPTY: {
			mEmpty.setVisibility(View.VISIBLE);
			mFailed.setVisibility(View.GONE);
			mNetAbnormal.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mLayout.setVisibility(View.GONE);
			break;
		}
		default:
			break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		search(s.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onTouchingLetterChanged(String s) {
		int position = mAdapter.getPositionForSection(s.charAt(0));
		if (position != -1) {
			mListView.setSelection(position);
		}
	}

	@SuppressLint("DefaultLocale")
	private List<Driver> transformDriver(List<MtqDriver> driverList) {
		List<Driver> list = new ArrayList<Driver>();
		if (driverList != null && !driverList.isEmpty()) {
			int len = driverList.size();
			for (int i = 0; i < len; i++) {
				MtqDriver mtqDriver = driverList.get(i);
				Driver driver = new Driver();
				driver.first = mtqDriver.driver_name.subSequence(0, 1)
						.toString();
				driver.driverid = mtqDriver.driverid;
				driver.driver_name = mtqDriver.driver_name;
				driver.phone = mtqDriver.phone;
				driver.invitestatus = mtqDriver.invitestatus;
				String pinyin = mParser.getSelling(mtqDriver.driver_name);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				if (sortString.matches("[A-Z]")) {
					driver.letter = sortString.toUpperCase();
				} else {
					driver.letter = "#";
				}
				list.add(driver);
			}
		}
		return list;
	}

	private void search(String string) {
		List<Driver> list = new ArrayList<Driver>();
		if (TextUtils.isEmpty(string)) {
			list = mTotalList;
		} else {
			list.clear();
			for (Driver driver : mTotalList) {
				String name = driver.driver_name;
				if (name.indexOf(string.toString()) != -1
						|| mParser.getSelling(name).startsWith(
								string.toString())) {
					list.add(driver);
				}
			}
		}
		mCurList.clear();
		mCurList.addAll(list);
		Collections.sort(mCurList, mComparator);
		mAdapter.notifyDataSetChanged();
	}
}
