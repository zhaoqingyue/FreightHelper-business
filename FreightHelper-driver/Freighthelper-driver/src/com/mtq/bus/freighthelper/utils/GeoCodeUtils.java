package com.mtq.bus.freighthelper.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.cld.mapapi.search.geocode.GeoCodeResult;
import com.cld.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.cld.mapapi.search.geocode.ReverseGeoCodeResult;
import com.mtq.bus.freighthelper.api.map.MapViewAPI;

public class GeoCodeUtils {

	private TextView mTextView;
	private int tag;

	public GeoCodeUtils(TextView textview, int tag) {
		mTextView = textview;
		this.tag = tag;
	}
	
	public GeoCodeUtils(TextView textview) {
		mTextView = textview;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				if (mTextView.getTag() == null) {
					mTextView.setText((String) msg.obj);
				} else {
					if (mTextView.getTag().equals(tag)) {
						mTextView.setText((String) msg.obj);
					}
				}
				break;
			}
			case 1: {
				mTextView.setText((String) msg.obj);
				break;
			}
			default:
				break;
			}
		}
	};

	protected void transfer(long x, long y, final boolean isList) {
		if (x == 0 || y == 0) {
			String location = "暂无地址";
			Message message = Message.obtain();
			message.obj = location;
			mHandler.sendMessage(message);
			return;
		}

		MapViewAPI.getInstance().getGeoCodeResult(x, y,
				new OnGetGeoCoderResultListener() {

					@Override
					public void onGetGeoCodeResult(GeoCodeResult arg0) {

					}

					@Override
					public void onGetReverseGeoCodeResult(
							ReverseGeoCodeResult result) {
						String location = "";
						if (result != null) {
							location = result.address;
						} else {
							location = "暂无地址";
						}
						Message msg = Message.obtain();
						if (isList) {
							msg.what= 0;
						} else {
							msg.what= 1;
						}
						msg.obj = location;
						mHandler.sendMessage(msg);
					}
				});
	}

	public void setTextByThread(final int x, final int y, final boolean isList) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				transfer(x, y, isList);
			}
		}).start();
	}
}
