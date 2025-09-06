package org.appjam.bongbaek.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record TokenResponse(
        @Schema(description = "access 토큰")
        Token accessToken,
        @Schema(description = "refresh 토큰")
        Token refreshToken
) {

    public static TokenResponse of(
        final Token accessToken,
        final Token refreshToken
    ) {
        return new TokenResponse(accessToken, refreshToken);
    }

    public record Token(
        @Schema(description = "JWT 문자열")
        String token,
        @Schema(description = "만료 시각 (epoch millis)")
        long expiredAt
    ) {

        @JsonProperty("calculatedExpiredAt")
        @Schema(description = "만료까지 남은 초(서버 계산값, 변환 필요 x)")
        public long calculatedExpiredAt() {
            long expSec = expiredAt / 1000;
            long nowSec = Instant.now().getEpochSecond();

            return expSec - nowSec;
        }

        public static Token of(
            final String token,
            final long expiredAt
        ) {
            return new Token(token, expiredAt);
        }
    }
}