package org.appjam.bongbaek.global.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "access 토큰")
        String accessToken,
        @Schema(description = "refresh 토큰")
        String refreshToken
) {
    public static TokenResponse of(final String accessToken, final String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}