package org.appjam.bongbaek.global.api.response;

import org.appjam.bongbaek.global.api.code.SuccessResultCode;

public record SuccessResponse<T>(
		int status,
		String code,
		String message,
		T data
) implements ApiResponse {
	public static <T> SuccessResponse<T> of(SuccessResultCode successCode, T data) {
		return new SuccessResponse<>(
				successCode.getStatus().value(),
				successCode.toString(),
				successCode.getMessage(), data);
	}

	public static <T> SuccessResponse<T> of(SuccessResultCode successCode) {
		return new SuccessResponse<>(
				successCode.getStatus().value(),
				successCode.toString(),
				successCode.getMessage(), null);
	}
}
