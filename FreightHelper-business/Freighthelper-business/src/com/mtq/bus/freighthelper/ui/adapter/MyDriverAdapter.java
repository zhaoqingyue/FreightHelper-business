package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.DriverUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqDriver;

public class MyDriverAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqDriver> mMyDrivers;
	private ItemClickListener mListener;

	public MyDriverAdapter(Context context, List<MtqDriver> myDrivers,
			ItemClickListener listener) {
		mContext = context;
		mMyDrivers = myDrivers;
		mListener = listener;
	}

	@Override
	public int getCount() {
		return mMyDrivers.size();
	}

	@Override
	public Object getItem(int position) {
		return mMyDrivers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_mydriver, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_mydriver_name);
			holder.status = (TextView) convertView
					.findViewById(R.id.item_mydriver_status);
			holder.mobile = (TextView) convertView
					.findViewById(R.id.item_mydriver_mobile);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqDriver myDriver = mMyDrivers.get(position);
		holder.name.setText(myDriver.driver_name);
		int invitestatus = myDriver.invitestatus;
		int colorid = DriverUtils.getStatusColor(invitestatus);
		holder.status.setText(DriverUtils.getInviteStatus(invitestatus));
		holder.status.setTextColor(mContext.getResources().getColor(colorid));
		holder.mobile.setText(myDriver.phone);
		holder.mobile.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		holder.mobile.setTag(position);
		holder.mobile.setOnClickListener(mListener);
		return convertView;
	}

	final static class ViewHolder {
		TextView name;
		TextView status;
		TextView mobile;
	}

	/**
	 * 刷新单个item
	 */
	@SuppressWarnings("deprecation")
	public void updateView(ListView listview, int itemIndex, int invitestatus) {
		/**
		 * 得到第一个可显示控件的位置
		 */
		int visiblePosition = listview.getFirstVisiblePosition();
		int headerCount = listview.getHeaderViewsCount();
		/**
		 * 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		 */
		 int index = itemIndex - visiblePosition;
		if (index >= 0) {
			View view = listview.getChildAt(index + headerCount);
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.status = (TextView) view
					.findViewById(R.id.item_mydriver_status);
			int colorid = DriverUtils.getStatusColor(invitestatus);
			holder.status.setText(DriverUtils.getInviteStatus(invitestatus));
			holder.status.setTextColor(mContext.getResources()
					.getColor(colorid));
		}
	}
}
