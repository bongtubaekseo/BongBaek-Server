package org.appjam.bongbaek.global.exception.common;

import org.appjam.bongbaek.global.api.code.common.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class RequestInvalidException extends BaseException {
	public RequestInvalidException() {
		super(ErrorCode.REQUEST_CONTENT_INVALID);
	}
}
