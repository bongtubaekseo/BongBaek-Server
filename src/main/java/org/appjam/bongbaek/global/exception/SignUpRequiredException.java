package org.appjam.bongbaek.global.exception;

import lombok.Getter;
import org.appjam.bongbaek.global.common.CommonErrorCode;

@Getter
public class SignUpRequiredException extends CustomException {
    private final String id;

    public SignUpRequiredException(String oauthId) {
        super(CommonErrorCode.SIGN_UP_REQUIRED);
        this.id = oauthId;
    }
}
