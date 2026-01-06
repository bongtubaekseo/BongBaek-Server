package org.appjam.bongbaek.global.api.code.event;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorResultCode {
	// 404
	EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "경조사 정보가 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;
}
