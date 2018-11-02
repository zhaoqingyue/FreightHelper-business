package com.mtq.bus.freighthelper.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.api.deliverybus.DeliveryBusAPI;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.me.BusinessActivity;
import com.mtq.bus.freighthelper.ui.activity.me.FeedbackActivity;
import com.mtq.bus.freighthelper.ui.activity.me.MyCarActivity;
import com.mtq.bus.freighthelper.ui.activity.me.MyDriverActivity;
import com.mtq.bus.freighthelper.ui.activity.me.UserInfoActivity;
import com.mtq.bus.freighthelper.ui.activity.me.about.AboutActivity;
import com.mtq.bus.freighthelper.ui.fragment.base.BaseMainFragment;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MeFragment extends BaseMainFragment implements OnClickListener {

	private TextView mTitle;

	public static MeFragment newInstance() {
		Bundle args = new Bundle();
		MeFragment fragment = new MeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_me, container,
				false);
		initViews(view);
		setListener(view);
		initData();
		updateUI();
		return view;
	}

	private void initViews(View view) {
		mTitle = (TextView) view.findViewById(R.id.title_text);
	}

	private void setListener(View view) {
		view.findViewById(R.id.me_userinfo).setOnClickListener(this);
		view.findViewById(R.id.me_bus).setOnClickListener(this);
		view.findViewById(R.id.me_car).setOnClickListener(this);
		view.findViewById(R.id.me_driver).setOnClickListener(this);
		view.findViewById(R.id.me_feedback).setOnClickListener(this);
		view.findViewById(R.id.me_about).setOnClickListener(this);
	}

	private void initData() {
		mTitle.setText(R.string.main_bottom_me);
	}

	private void updateUI() {

	}
	
	@Override
	public void onConnectivityChange() {
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.me_userinfo: {
			Intent intent = new Intent(getActivity(), UserInfoActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.me_bus: {
			if (!DeliveryBusAPI.getInstance().hasBusPermission()) {
				Toast.makeText(getActivity(), "当前账号无业务看板权限", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(getActivity(),
						BusinessActivity.class);
				startActivity(intent);
			}
			break;
		}
		case R.id.me_car: {
			Intent intent = new Intent(getActivity(), MyCarActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.me_driver: {
			Intent intent = new Intent(getActivity(), MyDriverActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.me_feedback: {
			Intent intent = new Intent(getActivity(), FeedbackActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.me_about: {
			Intent intent = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent);
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
}
