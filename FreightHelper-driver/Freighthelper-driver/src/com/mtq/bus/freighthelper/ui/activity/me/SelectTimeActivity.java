package com.mtq.bus.freighthelper.ui.activity.me;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.log.CldLog;
import com.mtq.bus.freighthelper.R;
import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;
import com.mtq.bus.freighthelper.ui.activity.base.BaseActivity;
import com.mtq.bus.freighthelper.ui.adapter.WheelDayAdapter;
import com.mtq.bus.freighthelper.ui.adapter.WheelHourAdapter;
import com.mtq.bus.freighthelper.ui.adapter.WheelMonthAdapter;
import com.mtq.bus.freighthelper.ui.adapter.WheelYearAdapter;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard;
import com.mtq.bus.freighthelper.ui.customview.VerticalCard.IVCListener;
import com.mtq.bus.freighthelper.utils.AnimationUtils;
import com.mtq.bus.freighthelper.utils.DateUtils;
import com.mtq.bus.freighthelper.utils.TimeUtils;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class SelectTimeActivity extends BaseActivity implements
		OnClickListener, IVCListener, OnWheelChangedListener {

	public static final String TAG = "SelectTimeActivity";
	public static final String SELECT_TIME_FROM = "select_time_from";
	public static int SELECT_TIME_FROM_BUS = 1;
	public static int SELECT_TIME_FROM_TRACK = 2;
	public static int SELECT_TYPE_STARTTIME = 3;
	public static int SELECT_TYPE_ENDTIME = 4;

	private ImageView mBack;
	private TextView mTitle;

	private VerticalCard mStart;
	private VerticalCard mEnd;
	private TextView mMaxDay;

	private View mLayout;
	private TextView mSelect;

	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;

	private int mFrom = 0;
	private int mSelectType = 0;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_select_time;
	}

	@Override
	protected void initViews() {
		mBack = (ImageView) findViewById(R.id.title_left_img);
		mTitle = (TextView) findViewById(R.id.title_text);
		mStart = (VerticalCard) findViewById(R.id.select_time_start);
		mEnd = (VerticalCard) findViewById(R.id.select_time_end);
		mMaxDay = (TextView) findViewById(R.id.select_time_max_day);

		mLayout = findViewById(R.id.select_time_layout);
		mSelect = (TextView) findViewById(R.id.select_time_select);
		year = (WheelView) findViewById(R.id.select_time_year);
		month = (WheelView) findViewById(R.id.select_time_month);
		day = (WheelView) findViewById(R.id.select_time_day);
		hour = (WheelView) findViewById(R.id.select_time_hour);
	}

	@Override
	protected void setListener() {
		mBack.setOnClickListener(this);
		mStart.setOnVCListener(this);
		mEnd.setOnVCListener(this);
		findViewById(R.id.select_time_ok).setOnClickListener(this);
		findViewById(R.id.select_time_cancel).setOnClickListener(this);
		findViewById(R.id.select_time_sure).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mTitle.setText(R.string.select_time);
		mBack.setVisibility(View.VISIBLE);
		mFrom = getIntent().getIntExtra(SELECT_TIME_FROM, 0);
		/**
		 * 设置默认时间
		 */
		String starttime = "";
		String endtime = "";
		if (mFrom == SELECT_TIME_FROM_BUS) {
			starttime = TimeUtils.getCurTimeForYmd();
			endtime = starttime;
			mStart.setContent(starttime);
			mEnd.setContent(endtime);
		} else if (mFrom == SELECT_TIME_FROM_TRACK) {
			/**
			 * 默认开始时间：当前时间往前的24小时
			 */
			starttime = TimeUtils.getStartTimeForYmdHm(1);
			endtime = TimeUtils.getCurTimeForYmdHm();
			mStart.setContent(starttime);
			mEnd.setContent(endtime);
		}
	}

	@Override
	protected void loadData() {
		initWheel();
	}

	@Override
	protected void updateUI() {

	}
	
	@Override
	protected void onConnectivityChange() {
		
	}

	@SuppressWarnings("deprecation")
	private void initWheel() {
		year.setVisibleItems(3);
		// 设置背景渐变颜色(正常#FFFFFF)
		year.setShadowColor(0xffFFFFFF, 0xbbFFFFFF, 0x77FFFFFF);
		// 设置分割线颜色
		year.setCenterRectColor(getResources().getColor(R.color.windows_color));
		// 设置背景颜色 Wheel的背景颜色值#FFFFFF
		year.setWheelBackground(R.drawable.wheel_bg_color);
		year.setViewAdapter(new WheelYearAdapter(this));
		year.addChangingListener(this);
		int curYear = DateUtils.getCurYear();
		int curIndex = curYear - DateUtils.START_YEAR;
		year.setCurrentItem(curIndex);

		month.setVisibleItems(3);
		month.setShadowColor(0xffFFFFFF, 0xbbFFFFFF, 0x77FFFFFF);
		month.setCenterRectColor(getResources().getColor(R.color.windows_color));
		month.setWheelBackground(R.drawable.wheel_bg_color);
		month.setViewAdapter(new WheelMonthAdapter(this));
		month.addChangingListener(this);
		int curMonth = DateUtils.getCurMonth();
		month.setCurrentItem(curMonth - 1);

		day.setVisibleItems(3);
		day.setShadowColor(0xffFFFFFF, 0xbbFFFFFF, 0x77FFFFFF);
		day.setCenterRectColor(getResources().getColor(R.color.windows_color));
		day.setWheelBackground(R.drawable.wheel_bg_color);
		CldLog.e(TAG, "curYear: " + curYear + ", curMonth: " + curMonth);
		day.setViewAdapter(new WheelDayAdapter(this, curYear, curMonth));
		int curDay = DateUtils.getCurMonthDay();
		day.setCurrentItem(curDay - 1);

		if (mFrom == SELECT_TIME_FROM_TRACK) {
			hour.setVisibleItems(3);
			hour.setShadowColor(0xffFFFFFF, 0xbbFFFFFF, 0x77FFFFFF);
			hour.setCenterRectColor(getResources().getColor(
					R.color.windows_color));
			hour.setWheelBackground(R.drawable.wheel_bg_color);
			hour.setViewAdapter(new WheelHourAdapter(this));
			hour.setCurrentItem(1);
			hour.setCurrentItem(0);
			hour.setVisibility(View.VISIBLE);
			mMaxDay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_img: {
			finish();
			break;
		}
		case R.id.select_time_ok: {
			String starttime = mStart.getContent();
			String endtime = mEnd.getContent();
			CldLog.e(TAG, "starttime: " + starttime + ", endtime: " + endtime);
			if (mFrom == SELECT_TIME_FROM_BUS) {
				long start = TimeUtils.getStampFromYdh(starttime);
				long end = TimeUtils.getStampFromYdh(endtime);
				long cur = System.currentTimeMillis() / 1000;
				CldLog.e(TAG, "start: " + start);
				CldLog.e(TAG, "end: " + end);
				CldLog.e(TAG, "cur: " + cur);
				
				if (start <= cur) {
					/**
					 * 选择时间只能在最近3个月
					 */
					float diff = cur - start;
					CldLog.e(TAG, "start_diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "start_day: " + day);
					if (day > 3 * TimeUtils.DAY_IN_MONTH) {
						Toast.makeText(this, "选择范围只能在最近3个月", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					Toast.makeText(this, "开始时间不能大于当前时间", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (end <= cur) {
					/**
					 * 选择时间只能在最近3个月
					 */
					float diff = cur - end;
					CldLog.e(TAG, "end_diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "end_day: " + day);
					if (day > 3 * TimeUtils.DAY_IN_MONTH) {
						Toast.makeText(this, "选择范围只能在最近3个月", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					Toast.makeText(this, "结束时间不能大于当前时间", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (start <= end) {
					/**
					 * 如果结束时间比当前时间小一天或以上，则结束时间加一天
					 * (因为通过年-月-日计算时间戳时，是按00:00:00算起的，导致少算了一天)
					 * add 2017-08-15
					 */
					if (cur - end >= TimeUtils.SECONDS_IN_DAY) {
						end += TimeUtils.SECONDS_IN_DAY;
					} else {
						/**
						 * 如果相差小于一天，说明结束时间就是当天
						 */
						end = cur;
					}
					
					/**
					 * 筛选时间不超过31天
					 */
					float diff = end - start;
					CldLog.e(TAG, "diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "day: " + day);
					if (day > TimeUtils.MAX_DAY_IN_MONTH) {
						Toast.makeText(this, "筛选范围不能超过31天", Toast.LENGTH_SHORT)
								.show();
					} else {
						Intent intent = getIntent();
						intent.putExtra("startdate", starttime);
						intent.putExtra("enddate", endtime);
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					Toast.makeText(this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (mFrom == SELECT_TIME_FROM_TRACK) {
				long start = TimeUtils.getStampFromYdhHm(starttime);
				long end = TimeUtils.getStampFromYdhHm(endtime);
				long cur = System.currentTimeMillis() / 1000;
				if (start < cur) {
					/**
					 * 选择时间只能在最近6个月
					 */
					float diff = cur - start;
					CldLog.e(TAG, "start_diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "start_day: " + day);
					if (day > 6 * TimeUtils.DAY_IN_MONTH) {
						Toast.makeText(this, "选择范围只能在最近6个月", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					Toast.makeText(this, "开始时间必须小于当前时间", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (end <= cur) {
					/**
					 * 选择时间只能在最近6个月
					 */
					float diff = cur - end;
					CldLog.e(TAG, "end_diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "end_day: " + day);
					if (day > 6 * TimeUtils.DAY_IN_MONTH) {
						Toast.makeText(this, "选择范围只能在最近6个月", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					Toast.makeText(this, "结束时间不能大于当前时间", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (start < end) {
					/**
					 * 筛选时间不超过5天
					 */
					float diff = end - start;
					CldLog.e(TAG, "diff: " + diff);
					float day = diff / TimeUtils.SECONDS_IN_DAY;
					CldLog.e(TAG, "day: " + day);
					if (day > 5) {
						Toast.makeText(this, "筛选范围不能超过5天", Toast.LENGTH_SHORT)
								.show();
					} else {
						Intent intent = getIntent();
						intent.putExtra("starttime", starttime);
						intent.putExtra("endtime", endtime);
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					Toast.makeText(this, "开始时间必须小于结束时间", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}
		case R.id.select_time_cancel: {
			close();
			break;
		}
		case R.id.select_time_sure: {
			close();
			String time = "";
			/**
			 * 年
			 */
			int curyear = year.getCurrentItem() + DateUtils.START_YEAR;
			String year = curyear + "-";
			time += year;
			/**
			 * 月
			 */
			int curmonth = month.getCurrentItem() + 1;
			String month = curmonth + "-";
			if (curmonth < 10) {
				month = "0" + curmonth + "-";
			}
			time += month;
			/**
			 * 日
			 */
			int curday = day.getCurrentItem() + 1;
			String day = curday + "";
			if (curday < 10) {
				day = "0" + curday + "";
			}
			time += day;

			if (mFrom == SELECT_TIME_FROM_TRACK) {
				/**
				 * 时
				 */
				int curhour = hour.getCurrentItem();
				CharSequence hourStr = ((WheelHourAdapter) hour
						.getViewAdapter()).getItemText(curhour);
				time += " " + hourStr.toString();
			}

			if (mSelectType == SELECT_TYPE_STARTTIME) {
				mStart.setContent(time);
			} else if (mSelectType == SELECT_TYPE_ENDTIME) {
				mEnd.setContent(time);
			}
			break;
		}
		default:
			break;
		}
	}

	private void close() {
		mLayout.setAnimation(AnimationUtils.moveToViewBottom());
		mLayout.setVisibility(View.INVISIBLE);
	}

	private void openTimeSelect(int selectType) {
		mSelectType = selectType;
		if (selectType == SELECT_TYPE_STARTTIME) {
			mSelect.setText(getResources().getString(
					R.string.select_time_start_time));
		} else if (selectType == SELECT_TYPE_ENDTIME) {
			mSelect.setText(getResources().getString(
					R.string.select_time_end_time));
		}
		mLayout.setVisibility(View.VISIBLE);
		mLayout.setAnimation(AnimationUtils.moveToViewLocation());
	}

	@Override
	public void onVcClick(View view) {
		switch (view.getId()) {
		case R.id.select_time_start: {
			openTimeSelect(3);
			break;
		}
		case R.id.select_time_end: {
			openTimeSelect(4);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == year) {
			updateMonths();
		}

		if (wheel == year || wheel == month) {
			updateDays();
		}
	}

	private void updateMonths() {
		month.setViewAdapter(new WheelMonthAdapter(this));
		month.setCurrentItem(0);
	}

	private void updateDays() {
		int curYear = year.getCurrentItem() + DateUtils.START_YEAR;
		int curMonth = month.getCurrentItem() + 1;
		day.setViewAdapter(new WheelDayAdapter(this, curYear, curMonth));
		day.setCurrentItem(0);
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onMessageEvent(BaseEvent event) {
		switch (event.msgId) {
		default:
			break;
		}
	}

}
