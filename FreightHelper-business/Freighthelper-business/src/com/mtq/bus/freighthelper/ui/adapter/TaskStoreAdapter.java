/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: TaskStoreAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 当日运单数据Adapter
 * @author: zhaoqy
 * @date: 2017年6月21日 下午3:56:52
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.CarUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskStore;

public class TaskStoreAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqTaskStore> mTaskStores;

	public TaskStoreAdapter(Context context, List<MtqTaskStore> taskStores) {
		mContext = context;
		mTaskStores = taskStores;
	}

	@Override
	public int getCount() {
		return mTaskStores.size();
	}

	@Override
	public Object getItem(int position) {
		return mTaskStores.get(position);
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
					R.layout.item_task_store, null);
			holder.orderid = (TextView) convertView
					.findViewById(R.id.item_task_store_orderid);
			holder.orderstatus = (TextView) convertView
					.findViewById(R.id.item_task_store_orderstatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqTaskStore taskStore = mTaskStores.get(position);
		holder.orderid.setText(taskStore.cut_orderid);
		holder.orderstatus.setText(CarUtils.getOrderStatus(taskStore.orderstatus));
		return convertView;
	}

	final static class ViewHolder {
		TextView orderid;
		TextView orderstatus;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		int totalHeight = 0;
		for (int i = 0; i < getCount(); i++) {
			View listItem = getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (getCount() - 1));
		listView.setLayoutParams(params);
	}
}
