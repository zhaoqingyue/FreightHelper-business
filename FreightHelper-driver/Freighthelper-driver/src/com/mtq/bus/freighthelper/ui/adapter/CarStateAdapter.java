package com.mtq.bus.freighthelper.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.utils.CarUtils;
import com.mtq.bus.freighthelper.utils.GeoCodeUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;
import com.mtq.ols.module.deliverybus.MtqSapDeliveryBusParam.MtqCarState;

public class CarStateAdapter extends BaseAdapter {

	protected static final String TAG = "CarStateAdapter";
	private Context mContext;
	private List<MtqCarState> mCarStates;

	public CarStateAdapter(Context context, List<MtqCarState> carStates) {
		mContext = context;
		mCarStates = carStates;
	}

	@Override
	public int getCount() {
		return mCarStates.size();
	}

	@Override
	public Object getItem(int position) {
		return mCarStates.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_car_state, null);
			holder.carimg = (ImageView) convertView
					.findViewById(R.id.item_carstate_license_img);
			holder.license = (TextView) convertView
					.findViewById(R.id.item_carstate_license);
			holder.status = (TextView) convertView
					.findViewById(R.id.item_carstate_status);
			holder.posimg = (ImageView) convertView
					.findViewById(R.id.item_carstate_pos_img);
			holder.position = (TextView) convertView
					.findViewById(R.id.item_carstate_pos);
			holder.mileage = (TextView) convertView
					.findViewById(R.id.item_carstate_mileage);
			holder.divider = convertView
					.findViewById(R.id.item_carstate_divider);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MtqCarState carState = mCarStates.get(position);
		holder.license.setText(carState.carlicense);
		holder.status.setText(CarUtils.getCarStatus(carState.carstatus));
		int colorid = CarUtils.getStatusColor(carState.carstatus);
		holder.status.setTextColor(mContext.getResources().getColor(colorid));
		/**
		 * 最近定位时间与系统时间相差在5分钟范围内为在线，否则为离线
		 */
		if (TimeUtils.isOnLine(carState.gpstime)) {
			holder.carimg.setImageResource(R.drawable.car_license_highlight);
			holder.posimg.setImageResource(R.drawable.car_pos_highlight);
		} else {
			holder.carimg.setImageResource(R.drawable.car_license_nor);
			holder.posimg.setImageResource(R.drawable.car_pos_nor);
		}

		/*
		 * if (carState.carstatus == 2 || carState.carstatus == 3) {
		 * holder.carimg.setImageResource(R.drawable.car_license_highlight);
		 * holder.posimg.setImageResource(R.drawable.car_pos_highlight); } else
		 * if (carState.carstatus == 1 || carState.carstatus == 20) {
		 * holder.carimg.setImageResource(R.drawable.car_license_nor);
		 * holder.posimg.setImageResource(R.drawable.car_pos_nor); }
		 */

		holder.mileage.setText("今日" + Math.round(carState.mileage / 1000.0) + "公里");
		// holder.mileage.setText("今日" + carState.mileage / 1000 + "公里");
		// holder.position.setText("广东省深圳市福田区深南大道创建大厦26楼");
		holder.position.setTag(carState.carid);
		new GeoCodeUtils(holder.position, carState.carid).setTextByThread(
				carState.x, carState.y, true);
		return convertView;
	}

	final static class ViewHolder {
		ImageView carimg;
		TextView license;
		TextView status;
		ImageView posimg;
		TextView position;
		TextView mileage;
		View divider;
	}
}
