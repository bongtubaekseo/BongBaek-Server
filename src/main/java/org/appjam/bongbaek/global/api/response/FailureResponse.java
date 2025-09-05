package org.appjam.bongbaek.global.api.response;

public record FailureResponse(
		boolean success,
		int status,
		String message
) implements ApiResponse {
}
