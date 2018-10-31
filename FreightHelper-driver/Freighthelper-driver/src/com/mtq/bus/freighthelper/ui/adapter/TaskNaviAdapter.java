/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: TaskNaviAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 当日行程数据Adapter
 * @author: zhaoqy
 * @date: 2017年6月21日 下午3:55:59
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqTaskNavi;

public class TaskNaviAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqTaskNavi> mTaskNavis;

	public TaskNaviAdapter(Context context, List<MtqTaskNavi> taskNavis) {
		mContext = context;
		mTaskNavis = taskNavis;
	}

	@Override
	public int getCount() {
		return mTaskNavis.size();
	}

	@Override
	public Object getItem(int position) {
		return mTaskNavis.get(position);
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
					R.layout.item_task_navi, null);
			holder.navi = (TextView) convertView
					.findViewById(R.id.item_task_navi);
			holder.mileage = (TextView) convertView
					.findViewById(R.id.item_task_navi_mileage);
			holder.traveltime = (TextView) convertView
					.findViewById(R.id.item_task_navi_traveltime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqTaskNavi taskNavi = mTaskNavis.get(position);
		String starttime = TimeUtils.stampToHm(taskNavi.starttime);
		String endtime = TimeUtils.stampToHm(taskNavi.endtime);
		holder.navi.setText("点火" + starttime + "-" + "熄火" + endtime);
		//DecimalFormat df = new DecimalFormat("0.00");
		//holder.mileage.setText(df.format(taskNavi.mileage) + "公里");
		if (taskNavi.mileage > 0) {
			holder.mileage.setText(taskNavi.mileage + "公里");
		} else {
			holder.mileage.setText("0公里");
		}
		holder.traveltime.setText(taskNavi.traveltime/60 + "分钟");
		return convertView;
	}

	final static class ViewHolder {
		TextView navi;
		TextView mileage;
		TextView traveltime;
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
