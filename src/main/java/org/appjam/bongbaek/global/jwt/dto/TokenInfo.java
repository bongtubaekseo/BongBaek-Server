package org.appjam.bongbaek.global.jwt.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenInfo(
		@Schema(description = "JWT 문자열")
		String token,
		@Schema(description = "만료 시각 (epoch millis)")
		long expiredAt,
		@Schema(description = "만료까지 남은 초(서버 계산값, 변환 필요 x)")
		long calculatedExpiredAt
) {
	public static TokenInfo of(
			final String token,
			final long expiredAt
	) {
		long calculatedExpiredAt = expiredAt- Instant.now().toEpochMilli();

		return new TokenInfo(token, expiredAt, calculatedExpiredAt);
	}
}
