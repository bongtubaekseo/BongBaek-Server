package org.appjam.bongbaek.global.exception;

import lombok.Getter;
import org.appjam.bongbaek.global.common.CommonErrorCode;

@Getter
public class SignUpRequiredException extends CustomException {
    private final String id;
    private final String provider;

    public SignUpRequiredException(String oauthId, String authProvider) {
        super(CommonErrorCode.SIGN_UP_REQUIRED);
        this.id = oauthId;
        this.provider = authProvider;
    }
}
