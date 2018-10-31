package com.mtq.bus.freighthelper.bean.alarm;

public class Alarm_222 {
	
	/** 系统类型 0：发动机（MIL）；1：变速箱；2：刹车；3：气囊；4：仪表板5：车身控制；6：空调  */
	public int status; 
	/** 故障码个数 */
	public int errcount;
	/** 故障码，多个故障码间以逗号分隔  */
	public String errvalue;
	
	public Alarm_222() {
		
	}
}
