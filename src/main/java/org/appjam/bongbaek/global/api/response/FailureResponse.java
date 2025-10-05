package org.appjam.bongbaek.global.api.response;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;

public record FailureResponse(
		int status,
		String code,
		String message
) implements ApiResponse {
	public static FailureResponse of(ErrorResultCode errorCode) {
		return new FailureResponse(
				errorCode.getStatus().value(),
				errorCode.toString(),
				errorCode.getMessage());
	}
}
