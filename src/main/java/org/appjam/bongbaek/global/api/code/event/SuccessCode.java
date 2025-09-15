package org.appjam.bongbaek.global.api.code.event;

import org.appjam.bongbaek.global.api.code.SuccessResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements SuccessResultCode {
	// 200
	EVENT_FOUND(HttpStatus.OK, "경조사 정보 조회가 완료되었습니다."),
	EVENT_UPDATED(HttpStatus.OK, "경조사 정보가 수정되었습니다."),
	EVENT_DELETED(HttpStatus.OK, "경조사 정보가 삭제되었습니다."),
	COST_CALCULATED(HttpStatus.OK, "경조사 비용 계산이 완료되었습니다."),

	// 201
	EVENT_CREATED(HttpStatus.CREATED, "경조사 정보가 생성되었습니다.");

	private final HttpStatus status;
	private final String message;
}
