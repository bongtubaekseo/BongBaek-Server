package org.appjam.bongbaek.domain.event.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode {

    // 400
    INVALID_EVENT_DATE(false, HttpStatus.BAD_REQUEST, "유효하지 않은 경조사 날짜입니다."),
    INVALID_UUID_FORMAT(false, HttpStatus.BAD_REQUEST, "유효하지 않은 UUID 포멧입니다."),
    INVALID_NOTE_FORMAT(false, HttpStatus.BAD_REQUEST, "메모는 50자를 넘길 수 없습니다."),

    //403
    FORBIDDEN_USER(false, HttpStatus.FORBIDDEN, "해당 경조사의 접근 권한이 없습니다."),
    // 404
    EVENT_NOT_FOUND(false, HttpStatus.NOT_FOUND,  "경조사를 찾을 수 없습니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String successMessage;

    @Override
    public boolean getSuccess() {
        return success;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return successMessage;
    }
}
