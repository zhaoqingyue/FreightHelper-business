package com.mtq.bus.freighthelper.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.DateUtils;

public class WheelYearAdapter extends AbstractWheelTextAdapter {

	private List<String> list = new ArrayList<String>();
	
	public WheelYearAdapter(Context context) {
		super(context);
		
		setItemResource(R.layout.item_wheel);
		setItemTextResource(R.id.item_wheel_name);
		list = DateUtils.getYaerList();
	}
	
	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}
	
	@Override
	public int getItemsCount() {
		return list.size();
	}

	@Override
	public CharSequence getItemText(int index) {
		String item = list.get(index);
		if (item != null) {
			return item;
		}
		return null;
	}

	public int getIndexof(String item) {
		return list.indexOf(item);
	}

}
