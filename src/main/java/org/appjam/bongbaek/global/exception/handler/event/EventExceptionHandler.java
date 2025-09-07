package org.appjam.bongbaek.global.exception.handler.event;

import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.exception.event.EventNotFoundException;
import org.appjam.bongbaek.global.exception.handler.BaseExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventExceptionHandler extends BaseExceptionHandler {
	@ExceptionHandler(EventNotFoundException.class)
	protected ApiResponse handleEventNotFoundException(EventNotFoundException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}
}
