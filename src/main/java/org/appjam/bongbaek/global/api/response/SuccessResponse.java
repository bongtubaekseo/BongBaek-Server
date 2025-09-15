package org.appjam.bongbaek.global.api.response;

public record SuccessResponse<T>(
		boolean success,
		int status,
		String message,
		T data
) implements ApiResponse {
}
