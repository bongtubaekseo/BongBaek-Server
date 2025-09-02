package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;

public record LoginResponse(
        @Schema(description = "회원 이름")
        String name,
        @Schema(description = "jwt Token", nullable = true)
        TokenResponse token,
        @Schema(description = "회원 가입 완료 여부")
        boolean isCompletedSignUp,
        @Schema(description = "kakao ID", nullable = true)
        String oauthId
) {
    public static LoginResponse of(final String name, final TokenResponse token, final boolean isCompletedSignUp, final String oauthId) {
        return new LoginResponse(name, token, isCompletedSignUp, oauthId);
    }

    public static LoginResponse ofLoginSuccess(final String name, final TokenResponse token, final String oauthId) {
        return new LoginResponse(name, token, true, oauthId);
    }
}
