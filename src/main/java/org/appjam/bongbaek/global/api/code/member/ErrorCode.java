package org.appjam.bongbaek.global.api.code.member;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorResultCode {
	// 400
	REQUEST_INVALID(HttpStatus.BAD_REQUEST, "올바른 요청값이 아닙니다."),

	// 401
	AUTHENTICATE_FAIL(HttpStatus.UNAUTHORIZED,"인증에 실패했습니다."),
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

	// 403
	AUTHORITY_DENIED(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

	// 404
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다."),

	// 409
	MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다");

	private final HttpStatus status;
	private final String message;
}
