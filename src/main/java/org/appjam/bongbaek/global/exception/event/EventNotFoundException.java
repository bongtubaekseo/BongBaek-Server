package org.appjam.bongbaek.global.exception.event;

import org.appjam.bongbaek.global.api.code.event.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class EventNotFoundException extends BaseException {
	public EventNotFoundException() {
		super(ErrorCode.EVENT_NOT_FOUND);
	}
}
