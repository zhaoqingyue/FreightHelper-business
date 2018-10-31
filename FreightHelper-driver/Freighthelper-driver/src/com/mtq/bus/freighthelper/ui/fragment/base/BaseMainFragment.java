package com.mtq.bus.freighthelper.ui.fragment.base;

import me.yokeyword.fragmentation.SupportFragment;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.mtq.bus.freighthelper.ui.fragment.main.CarFragment;
import com.mtq.bus.freighthelper.ui.fragment.main.MeFragment;
import com.mtq.bus.freighthelper.ui.fragment.main.MsgFragment;

import de.greenrobot.event.EventBus;

public abstract class BaseMainFragment extends SupportFragment {

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
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

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	public abstract void onConnectivityChange();

	boolean doubleBackToExitPressedOnce = false;

	/**
	 * 处理回退事件
	 */
	@Override
	public boolean onBackPressedSupport() {
		if (getChildFragmentManager().getBackStackEntryCount() > 1) {
			popChild();
		} else {

			if (this instanceof MsgFragment || this instanceof CarFragment
					|| this instanceof MeFragment) {

				if (doubleBackToExitPressedOnce) {
					_mActivity.finish();
					return true;
				}

				this.doubleBackToExitPressedOnce = true;
				Toast.makeText(_mActivity, "再按一次退出货运宝", Toast.LENGTH_SHORT)
						.show();

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						doubleBackToExitPressedOnce = false;
					}
				}, 2000);
			}
		}
		return true;
	}
}
