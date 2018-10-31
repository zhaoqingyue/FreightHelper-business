package com.mtq.bus.freighthelper.bean.alarm;

public class Alarm_115 {

	/** 类型（1为圆形区域，2为矩形区域，3为多边形区域，4为路线） */
	public int railtype;
	/** 区域或路段ID */
	public int railid;
	/** 方向（0为进，1为出，2为偏离，3为在内，4为在外） */
	public int raildire;
	/** 持续时间（raildire为3或4才有效） */
	public int duration;

	public Alarm_115() {

	}
}
