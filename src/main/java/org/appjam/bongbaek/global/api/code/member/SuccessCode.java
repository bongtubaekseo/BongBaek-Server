package org.appjam.bongbaek.global.api.code.member;

import org.appjam.bongbaek.global.api.code.SuccessResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements SuccessResultCode {
	// 200
	MEMBER_FOUND(HttpStatus.OK, "회원 정보 조회가 완료되었습니다."),
	MEMBER_UPDATED(HttpStatus.OK, "회원 정보가 수정되었습니다."),
	MEMBER_DELETED(HttpStatus.OK, "회원 탈퇴가 완료되었습니다."),
	LOGIN_SUCCESS(HttpStatus.OK, "로그인이 완료되었습니다."),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃이 완료되었습니다."),
	TOKEN_REISSUED(HttpStatus.OK, "토큰 재발급이 완료되었습니다."),

	// 201
	MEMBER_CREATED(HttpStatus.CREATED, "회원가입이 완료되었습니다."),

	// 202
	SIGN_UP_REQUIRED(HttpStatus.ACCEPTED, "회원가입을 진행해주세요.");

	private final HttpStatus status;
	private final String message;
}
