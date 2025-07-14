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
        Long kakaoId
) {
    public static LoginResponse of(final String name, final TokenResponse token, final boolean isCompletedSignUp, final Long kakaoId) {
        return new LoginResponse(name, token, isCompletedSignUp, kakaoId);
    }

    public static LoginResponse ofLoginSuccess(final String name, final TokenResponse token, final Long kakaoId) {
        return new LoginResponse(name, token, true, kakaoId);
    }
}