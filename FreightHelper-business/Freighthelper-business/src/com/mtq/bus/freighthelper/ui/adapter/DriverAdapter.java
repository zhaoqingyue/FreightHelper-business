/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: DriverAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 调度消息-司机列表
 * @author: zhaoqy
 * @date: 2017年6月27日 下午2:50:43
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.Driver;

public class DriverAdapter extends BaseAdapter implements SectionIndexer {

	private Context mContext;
	private List<Driver> mDrivers;
	private ItemClickListener mListener;

	public DriverAdapter(Context context, List<Driver> drivers, ItemClickListener listener) {
		mContext = context;
		mDrivers = drivers;
		mListener = listener;
	}

	public void updateListView(List<Driver> list) {
		mDrivers = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDrivers.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrivers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_driver, null);
			holder.letter = (TextView) convertView
					.findViewById(R.id.item_driver_letter);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_driver_img);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_driver_name);
			holder.line = convertView.findViewById(R.id.item_driver_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Driver driver = mDrivers.get(position);
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			holder.letter.setVisibility(View.VISIBLE);
			holder.letter.setText(driver.letter);
			holder.line.setVisibility(View.GONE);
		} else {
			holder.letter.setVisibility(View.GONE);
			holder.line.setVisibility(View.VISIBLE);
		}

		holder.name.setText(driver.driver_name);
		holder.img.setSelected(driver.selected);
		holder.img.setTag(position);
		holder.img.setOnClickListener(mListener);
		return convertView;
	}

	final static class ViewHolder {
		TextView letter;
		ImageView img;
		TextView name;
		View line;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mDrivers.get(i).letter;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return mDrivers.get(position).letter.charAt(0);
	}
}
