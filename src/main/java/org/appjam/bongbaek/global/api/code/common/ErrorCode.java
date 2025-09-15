package org.appjam.bongbaek.global.api.code.common;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorResultCode {
	// 400
	REQUEST_CONTENT_INVALID(HttpStatus.BAD_REQUEST, "올바르지 않은 요청 데이터입니다."),
	REQUIRED_PARAMETER_MISSED(HttpStatus.BAD_REQUEST, "필수 요청값이 존재하지 않습니다."),

	// 404
	REQUEST_PATH_INVALID(HttpStatus.NOT_FOUND, "올바르지 않은 요청 경로입니다."),

	// 405
	HTTP_METHOD_INVALID(HttpStatus.METHOD_NOT_ALLOWED, "올바르지 않은 HTTP 메서드입니다."),

	// 500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생하였습니다.");

	private final HttpStatus status;
	private final String message;
}
