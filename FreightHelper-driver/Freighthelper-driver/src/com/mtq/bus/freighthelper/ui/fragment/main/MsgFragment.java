package com.mtq.bus.freighthelper.ui.fragment.main;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cld.device.CldPhoneNet;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.adapter.FragmentAdapter;
import com.mtq.bus.freighthelper.ui.customview.NoScrollViewPager;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseMainFragment;
import com.mtq.bus.freighthelper.ui.fragment.msg.AlarmFragment;
import com.mtq.bus.freighthelper.ui.fragment.msg.IUpdateMsgCount;
import com.mtq.bus.freighthelper.ui.fragment.msg.SysFragment;
import com.mtq.bus.freighthelper.utils.EventUtils;
import com.mtq.bus.freighthelper.utils.MsgId;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MsgFragment extends BaseMainFragment implements OnClickListener, IUpdateMsgCount {

	public static final String TAG = "MsgFragment";
	private TextView mAlarm;
	private TextView mSys;
	private NoScrollViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private AlarmFragment mAlarmFragment;
	private SysFragment mSysFragment;

	public static MsgFragment newInstance() {
		Bundle args = new Bundle();
		MsgFragment fragment = new MsgFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_msg, container,
				false);
		initViews(view);
		setListener(view);
		initData();
		updateUI();
		return view;
	}

	private void initViews(View view) {
		mAlarm = (TextView) view.findViewById(R.id.msg_alarm);
		mSys = (TextView) view.findViewById(R.id.msg_sys);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.msg_viewpager);
	}

	private void setListener(View view) {
		mAlarm.setOnClickListener(this);
		mSys.setOnClickListener(this);
		view.findViewById(R.id.msg_all).setOnClickListener(this);
	}

	private void initData() {
		initViewPage();
	}

	private void updateUI() {

	}
	
	@Override
	public void onConnectivityChange() {
		if (!CldPhoneNet.isNetConnected()) {
			if (mAlarmFragment != null) {
				mAlarmFragment.onConnectivityChange();
			}
			
			if (mSysFragment != null) {
				mSysFragment.onConnectivityChange();
			}
		}
	}

	private void initViewPage() {
		mFragments = new ArrayList<Fragment>();
		mAlarmFragment = new AlarmFragment();
		mAlarmFragment.setUpdateMsgCountListener(this);
		mFragments.add(mAlarmFragment);
		
		mSysFragment = new SysFragment();
		mSysFragment.setUpdateMsgCountListener(this);
		mFragments.add(mSysFragment);

		mViewPager.setNoScroll(true);
		mViewPager.setAdapter(new FragmentAdapter(getFragmentManager(),
				mFragments));
		mViewPager.setCurrentItem(0);
		mAlarm.setSelected(true);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.msg_all: {
			/**
			 * 全部已读
			 */
			EventUtils.createEvent(MsgId.MSGID_BATCH_SET_ALARM_MSG_READ, 0);
			EventUtils.createEvent(MsgId.MSGID_BATCH_SET_SYS_MSG_READ, 0);
			break;
		}
		case R.id.msg_alarm: {
			if (mViewPager.getCurrentItem() != 0) {
				mViewPager.setCurrentItem(0);
				mAlarm.setSelected(true);
				mSys.setSelected(false);
				/**
				 * 切换页面时，刷新一次
				 */
				mAlarmFragment.pullLatestMsg();
			}
			break;
		}
		case R.id.msg_sys: {
			if (mViewPager.getCurrentItem() != 1) {
				mViewPager.setCurrentItem(1);
				mAlarm.setSelected(false);
				mSys.setSelected(true);
				/**
				 * 切换页面时，刷新一次
				 */
				mSysFragment.pullLatestMsg();
			}
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onUpdateMsgCount() {
		if (mCallback != null) {
			mCallback.onMsgCount();
		} 
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}
	
	private IMsgCount mCallback;

	public interface IMsgCount {
		
		public void onMsgCount();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof IMsgCount)) {
			throw new IllegalStateException(
					"Fragment所在的Activity必须实现Callbacks接口!");
		}
		mCallback = (IMsgCount) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}
}
