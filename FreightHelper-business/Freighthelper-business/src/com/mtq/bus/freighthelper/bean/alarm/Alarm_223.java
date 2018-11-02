package com.mtq.bus.freighthelper.bean.alarm;

public class Alarm_223 {
	
	/** 状态  0：怠速刚超过5秒；1：怠速刚超过3分钟；2：怠速结束（熄火或者车速大于0）  */
	public int status; 
	/** 怠速油耗（升） */
	public float idlefuelcon;
	/** 最小转速（RPM）  */
	public int minspeed; 
	/** 最大转速（RPM）  */
	public int maxspeed; 
	/** 最小节气门（%） */
	public int minthrottle; 
	/** 最大节气门（%） */
	public int maxthrottle; 
	/** 怠速持续时长（秒） */
	public int idleduration; 
	
	public Alarm_223() {
		
	}
}
