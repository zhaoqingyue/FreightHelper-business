/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MsgSysAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 系统消息Adapter
 * @author: zhaoqy
 * @date: 2017年6月20日 下午2:52:17
 * @version: V1.0
 */

package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.ui.customview.RedDotTextView;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgSys;

public class MsgSysAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqMsgSys> mMsgSyss;

	public MsgSysAdapter(Context context, List<MtqMsgSys> msgSyss) {
		mContext = context;
		mMsgSyss = msgSyss;
	}

	@Override
	public int getCount() {
		return mMsgSyss.size();
	}

	@Override
	public Object getItem(int position) {
		return mMsgSyss.get(position);
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
					R.layout.item_msg_sys, null);
			holder.title = (RedDotTextView) convertView
					.findViewById(R.id.item_msg_sys_title);
			/*holder.status = (ImageView) convertView
					.findViewById(R.id.item_msg_sys_status);*/
			holder.content = (TextView) convertView
					.findViewById(R.id.item_msg_sys_content);
			holder.time = (TextView) convertView
					.findViewById(R.id.item_msg_sys_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqMsgSys msgSys = mMsgSyss.get(position);
		holder.title.setText(msgSys.title);
		//holder.title.setText("广东省深圳市福田区深南大道创建大厦26楼");
		if (msgSys.readstatus == 0) {
			holder.title.setDotVisibility(View.VISIBLE);
		} else {
			holder.title.setDotVisibility(View.INVISIBLE);
			//holder.title.setVisibility(View.GONE);
		}
		holder.time.setText(TimeUtils.stampToYmdHm(msgSys.time));
		holder.content.setText(msgSys.content);
		return convertView;
	}

	final static class ViewHolder {
		RedDotTextView title;
		//ImageView status;
		TextView content;
		TextView time;
	}
	
	/**
	 * 刷新单个item
	 */
	public void updateView(ListView listview, int itemIndex) {  
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
            
            holder.title = (RedDotTextView) view
					.findViewById(R.id.item_msg_sys_title);
            holder.title.setDotVisibility(View.INVISIBLE);
			/*holder.status = (ImageView) view
					.findViewById(R.id.item_msg_sys_status);
			holder.status.setVisibility(View.GONE);*/
        }         
    }  
}
