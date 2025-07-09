package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;

public record LoginResponse(
        @Schema(description = "jwt Token", nullable = true)
        TokenResponse token,
        @Schema(description = "회원 가입 완료 여부")
        boolean isCompletedSignUp,
        @Schema(description = "kakao ID", nullable = true)
        Long kakaoId,
        @Schema(description = "kakao Access Token", nullable = true)
        String kakaoAccessToken
) {
    public static LoginResponse of(final TokenResponse token, final boolean isCompletedSignUp, final Long kakaoId, final String kakaoAccessToken) {
        return new LoginResponse(token, isCompletedSignUp, kakaoId, kakaoAccessToken);
    }

    public static LoginResponse ofLoginSuccess(final TokenResponse token, final Long kakaoId, final String kakaoAccessToken) {
        return new LoginResponse(token, true, kakaoId, kakaoAccessToken);
    }
}