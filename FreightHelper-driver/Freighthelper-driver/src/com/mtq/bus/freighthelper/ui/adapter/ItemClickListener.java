/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: ItemClickListener.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: ListView的Item里面的控件点击事件抽象类
 * @author: zhaoqy
 * @date: 2017年7月4日 下午7:53:10
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class ItemClickListener implements OnClickListener {
	
	@Override
	public void onClick(View v) {
		itemOnClick((Integer) v.getTag(), v);
	}

	public abstract void itemOnClick(int position, View v);
}
