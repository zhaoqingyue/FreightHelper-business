package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.DropDown;
import com.mtq.bus.freighthelper.utils.DensityUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DropDownAdapter extends BaseAdapter {

	private Context mContext;
	private List<DropDown> mList;
	private int width = 0;
	private int height = 0;

	public DropDownAdapter(Context context, List<DropDown> list) {
		mContext = context;
		mList = list;
		width = (int) (DensityUtils.getWidth(mContext) * 0.40);
		height = (int) (DensityUtils.getHeight(mContext) * 0.08);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mList != null) {
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_dropdown, parent, false);
			holder.title = (TextView) convertView
					.findViewById(R.id.item_dropdown_title);
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

		holder.title.setText(mList.get(position).name);
		return convertView;
	}

	static class ViewHolder {
		public TextView title;
	}
}
