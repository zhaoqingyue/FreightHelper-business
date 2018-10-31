/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: TaskFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.bus
 * @Description: 运输计划
 * @author: zhaoqy
 * @date: 2017年6月18日 下午5:17:02
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.fragment.bus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import com.mtq.ols.module.deliverybus.MtqDeliveryBusAPI.IMtqTaskCountListener;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskCount;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class TaskFragment extends BaseFragment implements OnClickListener {

	public static final String TAG = "TaskFragment";
	public static final int REQUEST_TASK = 2;

	private LinearLayout mNetAbnormal;
	private LinearLayout mLoading;
	private LinearLayout mLayout;
	private View mTime;
	private TextView mRange;
	private ImageView mArrow;
	private TextView mStartTime;
	private TextView mEndTime;
	private BoxCard mTotal;
	private BoxCard mNormal;
	private BoxCard mLate;
	private BoxCard mWait;
	private BoxCard mDelay;
	private LinePieView mPieView;
	private PopupWindow mPop;
	private DropDownAdapter mAdapter;
	private List<DropDown> mDropList;
	private MtqTaskCount mTaskCount;
	private boolean isToday = true;
	private int[] data = new int[3];
	private int[] color = new int[3];
	private int mShadowWidth = 0;
	private boolean mIsLoading = false;
	
	/************** 解决getActivity()为null add 2017-11-30 *****************/
	private Context mCtx;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    mCtx = activity;
	}

	@Override
	public void onDetach() {
	    super.onDetach();
	    mCtx = null;
	}
	/************** 解决getActivity()为null add 2017-11-30 *****************/

	@Override
	protected int getLayoutResID() {
		return R.layout.fragment_bus_task;
	}

	@Override
	protected void initViews(View view) {
		mNetAbnormal = (LinearLayout) view.findViewById(R.id.task_net_abnormal);
		mLoading = (LinearLayout) view.findViewById(R.id.task_laoding);
		mLayout = (LinearLayout) view.findViewById(R.id.task_layout);
		mTime = view.findViewById(R.id.task_time);
		mRange = (TextView) view.findViewById(R.id.task_range);
		mArrow = (ImageView) view.findViewById(R.id.task_arrow);
		mStartTime = (TextView) view.findViewById(R.id.task_starttime);
		mEndTime = (TextView) view.findViewById(R.id.task_endtime);
		mTotal = (BoxCard) view.findViewById(R.id.task_all);
		mNormal = (BoxCard) view.findViewById(R.id.task_normal);
		mLate = (BoxCard) view.findViewById(R.id.task_late);
		mWait = (BoxCard) view.findViewById(R.id.task_wait);
		mDelay = (BoxCard) view.findViewById(R.id.task_delay);
		mPieView = (LinePieView) view.findViewById(R.id.task_pieview);
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

		color[0] = getResources().getColor(R.color.blue);
		color[1] = getResources().getColor(R.color.purple);
		color[2] = getResources().getColor(R.color.light_green);
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
			getTaskCount(startdate, enddate, false);
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
		if (mTaskCount != null) {
			/**
			 * 如选择今天，总计划数（全部）=等待发车+正常发车+晚点发车 如不是今天，总计划数（全部）=正常发车+晚点发车
			 */
			if (isToday) {
				mTotal.setNum(mTaskCount.total);
			} else {
				mTotal.setNum(mTaskCount.total - mTaskCount.wait);
			}
			mNormal.setNum(mTaskCount.normal);
			mLate.setNum(mTaskCount.late);
			if (isToday) {
				mWait.setNum(mTaskCount.wait);
			} else {
				mWait.setNum(0);
			}
			mDelay.setNum(mTaskCount.estidelay);

			data[0] = mTaskCount.normal;
			data[1] = mTaskCount.late;
			if (isToday) {
				data[2] = mTaskCount.wait;
			} else {
				data[2] = 0;
			}
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
		case R.id.task_range:
		case R.id.task_arrow: {
			dropDown();
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
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
			isToday = true;
			String startdate = TimeUtils.getStarttimeForYmd(0);
			String enddate = TimeUtils.getEndtimeForYmd();
			mRange.setText("今天");
			mStartTime.setText(startdate);
			mEndTime.setText(enddate);
			getTaskCount(startdate, enddate, false);
			break;
		}
		case 1: {
			/**
			 * 前7天，不包括今天
			 */
			isToday = false;
			String startdate = TimeUtils.getStarttimeForYmd(7);
			String enddate = TimeUtils.getStarttimeForYmd(1);
			mRange.setText("前7天");
			mStartTime.setText(startdate);
			mEndTime.setText(enddate);
			getTaskCount(startdate, enddate, false);
			break;
		}
		case 2: {
			Intent intent = new Intent(getActivity(), SelectTimeActivity.class);
			intent.putExtra(SelectTimeActivity.SELECT_TIME_FROM,
					SelectTimeActivity.SELECT_TIME_FROM_BUS);
			startActivityForResult(intent, REQUEST_TASK);
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
			if (requestCode == REQUEST_TASK) {
				Bundle bundle = data.getExtras();
				String startdate = bundle.getString("startdate");
				String enddate = bundle.getString("enddate");
				if (TimeUtils.isToday(startdate) || TimeUtils.isToday(enddate)) {
					isToday = true;
				} else {
					isToday = false;
				}
				mRange.setText("自定义");
				mStartTime.setText(startdate);
				mEndTime.setText(enddate);
				getTaskCount(startdate, enddate, false);
			}
		}
	}

	/**
	 * 获取运输统计看板
	 */
	private void getTaskCount(String startdate, String enddate,
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
		DeliveryBusAPI.getInstance().getTaskCount(startdate, enddate,
				new IMtqTaskCountListener() {

					@Override
					public void onResult(int errCode, MtqTaskCount data) {
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
							mTaskCount = data;
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mLayout.setVisibility(View.VISIBLE);
							updateUI();
						} else {
							Toast.makeText(/*getActivity()*/mCtx, "获取运输计划统计看板失败",
									Toast.LENGTH_SHORT).show();
							mNetAbnormal.setVisibility(View.GONE);
							mLoading.setVisibility(View.GONE);
							mLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	public void autoRefreshTask() {
		if (CldPhoneNet.isNetConnected()) {
			getTaskCount(mCurStartDate, mCurEndDate, true);
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
