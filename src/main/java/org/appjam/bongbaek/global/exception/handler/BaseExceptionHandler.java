package org.appjam.bongbaek.global.exception.handler;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;

public abstract class BaseExceptionHandler {
	protected final ApiResponse buildErrorResponse(ErrorResultCode resultCode) {
		return ApiResponse.failure(resultCode);
	}
}
