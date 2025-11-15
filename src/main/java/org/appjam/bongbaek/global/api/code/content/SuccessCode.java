package org.appjam.bongbaek.global.api.code.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.api.code.SuccessResultCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements SuccessResultCode {
    // 200
    CONTENT_FOUND(HttpStatus.OK, "경조사 컨텐츠 정보 조회가 완료되었습니다."),
    CONTENT_THUMBNAIL_UPDATED(HttpStatus.OK, "경조사 컨텐츠 썸네일이 수정되었습니다."),
    CONTENT_MAIN_IMAGE_UPLOADED(HttpStatus.OK, "경조사 컨텐츠 메인 이미지가 업로드됐습니다."),
    CONTENT_DELETED(HttpStatus.OK, "경조사 컨텐츠 정보가 삭제되었습니다."),

    // 201
    CONTENT_CREATED(HttpStatus.CREATED, "경조사 컨텐츠 정보가 생성되었습니다.");

    private final HttpStatus status;
    private final String message;
}
