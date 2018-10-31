package com.mtq.bus.freighthelper.utils;

import com.mtq.bus.freighthelper.bean.eventbus.BaseEvent;

import de.greenrobot.event.EventBus;

public class EventUtils {

	public static void createEvent(int msgId, int errCode) {
		BaseEvent event = new BaseEvent();
		event.msgId = msgId;
		event.errCode = errCode;
		EventBus.getDefault().post(new BaseEvent(event));
	}

}
