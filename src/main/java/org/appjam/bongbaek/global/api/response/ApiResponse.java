package org.appjam.bongbaek.global.api.response;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.appjam.bongbaek.global.api.code.SuccessResultCode;

public interface ApiResponse {

	int status();

	String code();

	String message();

	static <T> SuccessResponse<T> success(SuccessResultCode successCode, T data) {
		return SuccessResponse.of(successCode, data);
	}

	static <T> SuccessResponse<T> success(SuccessResultCode successCode) {
		return SuccessResponse.of(successCode);
	}

	static FailureResponse failure(ErrorResultCode errorCode) {
		return FailureResponse.of(errorCode);
	}

	static FailureResponse failure(ErrorResultCode errorCode, String message) {
		return FailureResponse.of(errorCode, message);
	}
}
