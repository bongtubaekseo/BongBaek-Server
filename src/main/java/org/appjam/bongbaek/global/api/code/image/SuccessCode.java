package org.appjam.bongbaek.global.api.code.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.api.code.SuccessResultCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements SuccessResultCode {
    // 200
    IMAGE_FOUND(HttpStatus.OK, "이미지 정보 조회가 완료되었습니다."),
    IMAGE_DELETED(HttpStatus.OK, "이미지 정보가 삭제되었습니다."),

    // 201
    IMAGE_CREATED(HttpStatus.CREATED, "이미지 정보가 생성되었습니다.");

    private final HttpStatus status;
    private final String message;
}
