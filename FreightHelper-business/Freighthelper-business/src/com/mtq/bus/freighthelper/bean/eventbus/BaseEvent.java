package com.mtq.bus.freighthelper.bean.eventbus;

public class BaseEvent {

	public int msgId;
	public int errCode;
	
	public BaseEvent() {
		this.msgId = -1;
		this.errCode = -1;
	}

	public BaseEvent(int msgId, int errCode) {
		this.msgId = msgId;
		this.errCode = errCode;
	}

	public BaseEvent(BaseEvent event) {
		this.msgId = event.msgId;
		this.errCode = event.errCode;
	}
}
