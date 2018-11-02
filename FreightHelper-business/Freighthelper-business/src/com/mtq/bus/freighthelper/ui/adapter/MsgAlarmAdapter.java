/**
 * 
 * Copyright © 2017 mtq. All rights reserved.
 *
 * @Title: MsgAlarmAdapter.java
 * @Prject: MTQBusFreighthelper
 * @Package: com.mtq.bus.freighthelper.ui.adapter
 * @Description: 警情消息Adapter
 * @author: zhaoqy
 * @date: 2017年6月20日 下午2:52:44
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
import com.mtq.bus.freighthelper.utils.MsgUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqMsgAlarm;

public class MsgAlarmAdapter extends BaseAdapter {

	private Context mContext;
	private List<MtqMsgAlarm> mMsgAlarms;

	public MsgAlarmAdapter(Context context, List<MtqMsgAlarm> msgAlarms) {
		mContext = context;
		mMsgAlarms = msgAlarms;
	}

	@Override
	public int getCount() {
		return mMsgAlarms.size();
	}

	@Override
	public Object getItem(int position) {
		return mMsgAlarms.get(position);
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
					R.layout.item_msg_alarm, null);
			holder.license = (RedDotTextView) convertView
					.findViewById(R.id.item_msg_alarm_license);
			/*holder.status = (ImageView) convertView
					.findViewById(R.id.item_msg_alarm_status);*/
			holder.type = (TextView) convertView
					.findViewById(R.id.item_msg_alarm_type);
			holder.time = (TextView) convertView
					.findViewById(R.id.item_msg_alarm_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqMsgAlarm msgAlarm = mMsgAlarms.get(position);
		holder.license.setText(msgAlarm.carlicense);
		if (msgAlarm.readstatus == 0) {
			holder.license.setDotVisibility(View.VISIBLE);
			//holder.status.setVisibility(View.VISIBLE);
		} else {
			holder.license.setDotVisibility(View.INVISIBLE);
			//holder.status.setVisibility(View.GONE);
		}
		holder.time.setText(TimeUtils.stampToYmdHms(msgAlarm.locattime));
		String alarmType = MsgUtils.getAlarmType(msgAlarm.alarmid);
		holder.type.setText(alarmType);
		return convertView;
	}

	final static class ViewHolder {
		RedDotTextView license;
		//ImageView status;
		TextView type;
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
            if (holder != null) {
            	holder.license = (RedDotTextView) view
    					.findViewById(R.id.item_msg_alarm_license);
                 holder.license.setDotVisibility(View.INVISIBLE);
            	/*holder.status = (ImageView) view
    					.findViewById(R.id.item_msg_alarm_status);
    			holder.status.setVisibility(View.GONE);*/
            }
        }         
    }  
}
