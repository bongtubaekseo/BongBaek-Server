package org.appjam.bongbaek.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String kakaoId,
        @Schema(description = "apple ID", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String appleId
) {
    public static LoginResponse ofKakao(final String name, final TokenResponse token, final boolean isCompletedSignUp, final String kakaoId) {
        return new LoginResponse(name, token, isCompletedSignUp, kakaoId, null);
    }

    public static LoginResponse ofApple(final String name, final TokenResponse token, final boolean isCompletedSignUp, final String appleId) {
        return new LoginResponse(name, token, isCompletedSignUp, null, appleId);
    }

    public static LoginResponse ofKakaoLoginSuccess(final String name, final TokenResponse token, final String kakaoId) {
        return new LoginResponse(name, token, true, kakaoId, null);
    }

    public static LoginResponse ofAppleLoginSuccess(final String name, final TokenResponse token, final String appleId) {
        return new LoginResponse(name, token, true, null, appleId);
    }
}
