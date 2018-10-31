/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: WaybillFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.bus
 * @Description: 运单
 * @author: zhaoqy
 * @date: 2017年8月2日 下午4:09:04
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.fragment.bus;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.manager.HandleErrManager;
import com.mtq.bus.freighthelper.ui.activity.me.SelectTimeActivity;
import com.mtq.bus.freighthelper.ui.adapter.DropDownAdapter;
import com.mtq.bus.freighthelper.ui.customview.BoxCard;
import com.mtq.bus.freighthelper.ui.customview.LinePieView;
import com.mtq.bus.freighthelper.ui.customview.ShadowDrawable;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseFragment;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.mtq.bus.freighthelper.utils.DropDownUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqOrderCountListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqOrderCount;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class WaybillFragment extends BaseFragment implements OnClickListener {

	public static final String TAG = "WaybillFragment";
	public static final int REQUEST_WAYBILL = 1;

	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mLayout;
	private View mTime;
	private TextView mRange;
	private ImageView mArrow;
	private TextView mStartTime;
	private TextView mEndTime;
	private BoxCard mTotal;
	private BoxCard mNotSend;
	private BoxCard mNotBegin;
	private BoxCard mTransport;
	private BoxCard mFinished;
	private BoxCard mAbnormal;
	private LinePieView mPieView;
	private PopupWindow mPop;
	private DropDownAdapter mAdapter;
	private List<DropDown> mDropList;
	private MtqOrderCount mOrderCount;
	private int[] data = new int[4];
	private int[] color = new int[4];
	private int mShadowWidth = 0;
	private boolean mIsLoading = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_bus_waybill;
	}

	@Override
	protected void initViews(View view) {
		mNetAbnormal = (LinearLayout) view
				.findViewById(R.id.waybill_net_abnormal);
		mLoading = (LinearLayout) view.findViewById(R.id.waybill_laoding);
		mLayout = (LinearLayout) view.findViewById(R.id.waybill_layout);
		mTime = view.findViewById(R.id.waybill_time);
		mRange = (TextView) view.findViewById(R.id.waybill_range);
		mArrow = (ImageView) view.findViewById(R.id.waybill_arrow);
		mStartTime = (TextView) view.findViewById(R.id.waybill_starttime);
		mEndTime = (TextView) view.findViewById(R.id.waybill_endtime);
		mTotal = (BoxCard) view.findViewById(R.id.waybill_all);
		mNotSend = (BoxCard) view.findViewById(R.id.waybill_unschedule);
		mNotBegin = (BoxCard) view.findViewById(R.id.waybill_untransport);
		mTransport = (BoxCard) view.findViewById(R.id.waybill_transporting);
		mFinished = (BoxCard) view.findViewById(R.id.waybill_signup);
		mAbnormal = (BoxCard) view.findViewById(R.id.waybill_abnormal);
		mPieView = (LinePieView) view.findViewById(R.id.waybill_pieview);
	}

	@Override
	protected void setListener(View view) {
		view.findViewById(R.id.net_abnormal_refresh).setOnClickListener(this);
		mRange.setOnClickListener(this);
		mArrow.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initData() {
		mDropList = DropDownUtils.getBusDrop();
		mAdapter = new DropDownAdapter(getActivity(), mDropList);
		mShadowWidth = DensityUtils.getDedaultSize(getActivity());
		mIsLoading = true; 

		color[0] = getResources().getColor(R.color.purple);
		color[1] = getResources().getColor(R.color.blue);
		color[2] = getResources().getColor(R.color.green);
		color[3] = getResources().getColor(R.color.light_green);
	}

	@Override
	protected void loadData() {
		if (CldPhoneNet.isNetConnected()) {
			/**
			 * 默认今天
			 */
			String startdate = TimeUtils.getStarttimeForYmd(0);
			String enddate = TimeUtils.getEndtimeForYmd();
			mRange.setText("今天");
			mStartTime.setText(startdate);
			mEndTime.setText(enddate);
			getOrderCount(startdate, enddate, false);
		} else {
			mNetAbnormal.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mIsLoading = false;
		}
	}

	private String mCurStartDate = "";
	private String mCurEndDate = "";

	@Override
	protected void updateUI() {
		if (mOrderCount != null) {
			mTotal.setNum(mOrderCount.total);
			mNotSend.setNum(mOrderCount.notsend);
			mNotBegin.setNum(mOrderCount.notbegin);
			mTransport.setNum(mOrderCount.transport);
			mFinished.setNum(mOrderCount.finished);
			mAbnormal.setNum(mOrderCount.abnormal);

			data[0] = mOrderCount.notsend;
			data[1] = mOrderCount.notbegin;
			data[2] = mOrderCount.transport;
			data[3] = mOrderCount.finished;
			mPieView.setData(data, color);
		}
	}
	
	@Override
	public void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			if (mLoading != null && mIsLoading) {
				mNetAbnormal.setVisibility(View.VISIBLE);
				mLoading.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.net_abnormal_refresh: {
			if (!CldPhoneNet.isNetConnected()) {
				Toast.makeText(getActivity(), R.string.common_network_abnormal,
						Toast.LENGTH_SHORT).show();
			} else {
				mNetAbnormal.setVisibility(View.GONE);
				mLoading.setVisibility(View.VISIBLE);
				loadData();
			}
			break;
		}
		case R.id.waybill_range:
		case R.id.waybill_arrow: {
			dropDown();
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void dropDown() {
		if (mPop == null) {
			ListView listView = new ListView(getActivity());
			listView.setCacheColorHint(0x00000000);
			listView.setBackgroundColor(getActivity().getResources().getColor(
					R.color.white));
			Drawable drawable = getResources().getDrawable(
					R.drawable.divider_bg);
			listView.setDivider(drawable);
			listView.setDividerHeight(1);
			listView.setAdapter(mAdapter);
			ShadowDrawable.Builder.on(listView)
					.bgColor(getResources().getColor(R.color.white))
					.shadowColor(Color.parseColor("#000000"))
					.shadowRange(mShadowWidth).offsetBottom(mShadowWidth)
					.offsetTop(mShadowWidth).offsetRight(mShadowWidth).create();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mPop.dismiss();
					itemClick(position);
				}
			});
			int width = (int) (DensityUtils.getWidth(getActivity()) * 0.40);
			mPop = new PopupWindow(listView, width + mShadowWidth,
					LayoutParams.WRAP_CONTENT, true);
			mPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			mPop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mPop.dismiss();
					mArrow.setImageResource(R.drawable.icon_drop_down);
				}
			});
		}
		mPop.showAsDropDown(mTime, 0, -mShadowWidth);
		mArrow.setImageResource(R.drawable.icon_drop_up);
	}

	protected void itemClick(int position) {
		switch (position) {
		case 0: {
			String startdate = TimeUtils.getStarttimeForYmd(0);
			String enddate = TimeUtils.getEndtimeForYmd();
			mRange.setText("今天");
			mStartTime.setText(startdate);
			mEndTime.setText(enddate);
			getOrderCount(startdate, enddate, false);
			break;
		}
		case 1: {
			/**
			 * 前7天，不包括今天
			 */
			String startdate = TimeUtils.getStarttimeForYmd(7);
			String enddate = TimeUtils.getStarttimeForYmd(1);
			mRange.setText("前7天");
			mStartTime.setText(startdate);
			mEndTime.setText(enddate);
			getOrderCount(startdate, enddate, false);
			break;
		}
		case 2: {
			Intent intent = new Intent(getActivity(), SelectTimeActivity.class);
			intent.putExtra(SelectTimeActivity.SELECT_TIME_FROM,
					SelectTimeActivity.SELECT_TIME_FROM_BUS);
			startActivityForResult(intent, REQUEST_WAYBILL);
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == REQUEST_WAYBILL) {
				Bundle bundle = data.getExtras();
				String startdate = bundle.getString("startdate");
				String enddate = bundle.getString("enddate");
				mRange.setText("自定义");
				mStartTime.setText(startdate);
				mEndTime.setText(enddate);
				getOrderCount(startdate, enddate, false);
			}
		}
	}

	/**
	 * 获取运单统计看板
	 */
	private void getOrderCount(String startdate, String enddate,
			boolean autoRefresh) {
		if (!CldPhoneNet.isNetConnected()) {
			Toast.makeText(getActivity(), R.string.common_network_abnormal,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!autoRefresh) {
			/**
			 * 如果起始日期一样，则不重新获取
			 */
			if (startdate.equals(mCurStartDate) && enddate.equals(mCurEndDate))
				return;
		}

		mCurStartDate = startdate;
		mCurEndDate = enddate;
		DeliveryBusAPI.getInstance().getOrderCount(startdate, enddate,
				new IMtqOrderCountListener() {

					@Override
					public void onResult(int errCode, MtqOrderCount data) {
						mIsLoading = false;
						/**
						 * 帐号被挤出
						 */
						if (errCode == 1008) {
							HandleErrManager.getInstance().handleErrCode(
									errCode);
							return;
						}

						if (errCode == 0 && data != null) {
							mOrderCount = data;
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mLayout.setVisibility(View.VISIBLE);
							updateUI();
						} else {
							Toast.makeText(getActivity(), "获取运单统计看板失败",
									Toast.LENGTH_SHORT).show();
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	public void autoRefreshOrder() {
		if (CldPhoneNet.isNetConnected()) {
			getOrderCount(mCurStartDate, mCurEndDate, true);
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}
}
