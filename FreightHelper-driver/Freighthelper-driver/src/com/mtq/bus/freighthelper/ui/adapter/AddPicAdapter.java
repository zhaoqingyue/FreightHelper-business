/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: AddPicAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 反馈界面添加图片Adapter
 * @author: zhaoqy
 * @date: 2017年6月21日 上午11:16:43
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.DensityUtils;
import com.squareup.picasso.Picasso;

public class AddPicAdapter extends BaseAdapter {

	private Context mContext;
	private int picNumLimit = 3;
	private List<String> list = new ArrayList<String>();

	private int width = 0;
	private int height = 0;

	public AddPicAdapter() {
		super();
	}

	/**
	 * 获取列表数据
	 */
	public void setList(List<String> list) {
		this.notifyDataSetChanged();
	}

	public AddPicAdapter(Context mContext, List<String> list, int limit) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.picNumLimit = limit;

		width = (int) (DensityUtils.getWidth(mContext) * 0.166);
		height = width;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 1;
		} else if (list.size() >= picNumLimit) {
			return picNumLimit;
		} else {
			return list.size() + 1;
		}
	}

	@Override
	public String getItem(int position) {
		if (list != null && list.size() == picNumLimit) {
			return list.get(position);
		} else if (list != null && list.size() < picNumLimit) {

			if (position < list.size()) {
				return list.get(position);

			} else
				return null;

		} else if (list != null && list.size() >= picNumLimit) {

			return list.get(position);

		} else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_add_pic, null);
			holder = new ViewHolder();
			holder.item_grida_image = (ImageView) convertView
					.findViewById(R.id.item_add_image);

			/**
			 * 动态设置item的宽高
			 * 
			 * item的layoutparams用GridView.LayoutParams或者
			 * AbsListView.LayoutParams设置，不能用LinearLayout.LayoutParams
			 * convertView.setLayoutParams(new
			 * GridView.LayoutParams(width,height));
			 */
			convertView.setLayoutParams(new AbsListView.LayoutParams(width,
					height));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isShowAddItem(position)) {
			Picasso.with(mContext).load(R.drawable.feedback_add_nor)
					.into(holder.item_grida_image);
		} else {
			if (getItem(position) != null) {
				if (getItem(position).contains(".jpg")) {
					Picasso.with(mContext).load("file://" + getItem(position))
							.into(holder.item_grida_image);
				}
			}
		}
		return convertView;
	}

	/**
	 * 判断当前下标是否是最大值
	 * 
	 * @param position
	 *            当前下标
	 */
	private boolean isShowAddItem(int position) {
		int size = (list == null) ? 0 : list.size();
		return position == size;
	}

	public int getPicNumLimit() {
		return picNumLimit;
	}

	public void setPicNumLimit(int picNumLimit) {
		this.picNumLimit = picNumLimit;
	}

	static class ViewHolder {
		ImageView item_grida_image;
	}
}
