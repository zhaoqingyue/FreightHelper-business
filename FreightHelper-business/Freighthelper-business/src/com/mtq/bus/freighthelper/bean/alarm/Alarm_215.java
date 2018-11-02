package com.mtq.bus.freighthelper.bean.alarm;

public class Alarm_215 {
	
	/** 翻车方位（由0：左；1：右；2：底朝天等组成，多个间以逗号分隔） */
	public String turnedoverdirect;
	/** 刹车前车速（公里/时） */
	public float speedbefore;
	/** 翻车时车速（公里/时） */
	public float speedturnedover;
	/** 转弯方向，0：向左侧转弯；1：向右侧转弯  */
	public int turndirect;
	/** 制动距离（米） */
	public int breakdistance;
	
	public Alarm_215() {
		
	}
}
