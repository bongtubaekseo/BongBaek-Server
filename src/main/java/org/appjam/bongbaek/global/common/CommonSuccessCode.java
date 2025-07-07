package org.appjam.bongbaek.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {
    // 200 Ok
    OK(true, HttpStatus.OK, "요청이 성공했습니다."),
    GET_EVENT(true, HttpStatus.OK, "경조사가 성공적으로 조회됐습니다."),
    UPDATED_EVENT(true, HttpStatus.OK, "경조사가 성공적으로 수정됐습니다."),
    DELETED_EVENT(true, HttpStatus.OK, "경조사가 성공적으로 삭제됐습니다."),

    // 201 Created
    CREATED(true, HttpStatus.CREATED, "생성되었습니다."),
    CREATED_EVENT(true, HttpStatus.CREATED, "경조사가 성공적으로 생성됐습니다."),

    // 204 No Content
    NO_EVENT(true, HttpStatus.NO_CONTENT,"등록된 경조사가 없습니다.");

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
