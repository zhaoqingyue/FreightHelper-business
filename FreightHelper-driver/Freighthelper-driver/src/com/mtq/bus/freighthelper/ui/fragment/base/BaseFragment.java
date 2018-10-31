/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: BaseFragment.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.fragment.base
 * @Description: Fragment 基类
 * @author: zhaoqy
 * @date: 2017年6月19日 下午12:17:48
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.greenrobot.event.EventBus;

public abstract class BaseFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResID(), container,
				false);
		initViews(view);
		setListener(view);
		initData();
		loadData();
		updateUI();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}
	
	protected abstract int getLayoutResID();

	protected abstract void initViews(View view);

	protected abstract void setListener(View view);

	protected abstract void initData();

	protected abstract void loadData();

	protected abstract void updateUI();
	
	public abstract void onConnectivityChange();
}
