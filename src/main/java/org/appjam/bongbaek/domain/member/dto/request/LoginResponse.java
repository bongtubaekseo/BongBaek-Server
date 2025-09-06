package org.appjam.bongbaek.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;

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
        String appleId,

        @Schema(description = "kakao map api key", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String apiKey
) {
    public static LoginResponse ofKakaoLoginFailure(final String kakaoId) {
        return new LoginResponse(null, null, false, kakaoId, null, null);
    }

    public static LoginResponse ofAppleLoginFailure(final String appleId) {
        return new LoginResponse(null, null, false, null, appleId, null);
    }

    public static LoginResponse ofKakaoLoginSuccess(final String name, final TokenResponse token, final String kakaoId, final String apiKey) {

        return new LoginResponse(name, token, true, kakaoId, null, apiKey);
    }

    public static LoginResponse ofAppleLoginSuccess(final String name, final TokenResponse token, final String appleId, final String apiKey) {
        return new LoginResponse(name, token, true, null, appleId, apiKey);
    }
}
