package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.CarUtils;
import com.mtq.bus.freighthelper.utils.TestUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarData;

public class MyCarAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqCarData> mMyCars;
	private ItemClickListener mListener;

	public MyCarAdapter(Context context, List<MtqCarData> myCars,
			ItemClickListener listener) {
		mContext = context;
		mMyCars = myCars;
		mListener = listener;
	}
	
	public void updateListView(List<MtqCarData> list) {
		mMyCars = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mMyCars.size();
	}

	@Override
	public Object getItem(int position) {
		return mMyCars.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_mycar, null);
			holder.license = (TextView) convertView
					.findViewById(R.id.item_mycar_license);
			holder.source = (TextView) convertView
					.findViewById(R.id.item_mycar_source);
			holder.driver = (TextView) convertView
					.findViewById(R.id.item_mycar_driver);
			holder.mobile = (TextView) convertView
					.findViewById(R.id.item_mycar_mobile);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqCarData myCar = mMyCars.get(position);
		holder.license.setText(myCar.carlicense);
		holder.source.setText(CarUtils.getSource(myCar.sourceid));
		if (!TextUtils.isEmpty(myCar.driver)) {
			holder.driver.setText(myCar.driver);
		} else {
			holder.driver.setText("未分配司机");
		}
		holder.mobile.setText(myCar.phone);
		holder.mobile.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		holder.mobile.setTag(position);
		holder.mobile.setOnClickListener(mListener);
		return convertView;
	}

	final static class ViewHolder {
		TextView license;
		TextView source;
		TextView driver;
		TextView mobile;
	}
}
