package org.appjam.bongbaek.domain.event.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventSuccessCode implements SuccessCode {
    GET_EVENT(true,HttpStatus.OK, "경조사가 성공적으로 조회됐습니다."),
    UPDATED_EVENT(true, HttpStatus.OK, "경조사가 성공적으로 수정됐습니다."),
    DELETED_EVENT(true, HttpStatus.OK, "경조사가 성공적으로 삭제됐습니다."),
    NO_EVENT(true, HttpStatus.NO_CONTENT, "등록된 경조사가 없습니다.");

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
