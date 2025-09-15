package org.appjam.bongbaek.global.api.response;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.appjam.bongbaek.global.api.code.SuccessResultCode;

public interface ApiResponse {

	int status();

	String message();

	static <T> SuccessResponse<T> success(SuccessResultCode successCode, T data) {
		return new SuccessResponse<T>(true, successCode.getStatus().value(), successCode.getMessage(), data);
	}

	static <T> SuccessResponse<T> success(SuccessResultCode successCode) {
		return new SuccessResponse<T>(true, successCode.getStatus().value(), successCode.getMessage(), null);
	}

	static FailureResponse failure(ErrorResultCode errorCode) {
		return new FailureResponse(false, errorCode.getStatus().value(), errorCode.getMessage());
	}
}
