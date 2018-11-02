package com.mtq.bus.freighthelper.bean.eventbus;

public class LoginEvent {

	public int msgId;
	public int errCode;
	public String errMsg;
	
	public LoginEvent() {
		this.msgId = -1;
		this.errCode = -1;
		this.errMsg = "";
	}

	public LoginEvent(int msgId, int errCode) {
		this.msgId = msgId;
		this.errCode = errCode;
	}

	public LoginEvent(LoginEvent event) {
		this.msgId = event.msgId;
		this.errCode = event.errCode;
	}
}
