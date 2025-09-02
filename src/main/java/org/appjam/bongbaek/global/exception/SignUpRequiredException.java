package org.appjam.bongbaek.global.exception;

import lombok.Getter;
import org.appjam.bongbaek.global.common.CommonErrorCode;

@Getter
public class SignUpRequiredException extends CustomException {
    private final String id;

    public SignUpRequiredException(Long kakaoId) {
        super(CommonErrorCode.SIGN_UP_REQUIRED);
        this.id = kakaoId.toString();
    }

    public SignUpRequiredException(String appleId) {
        super(CommonErrorCode.SIGN_UP_REQUIRED);
        this.id = appleId;
    }
}
