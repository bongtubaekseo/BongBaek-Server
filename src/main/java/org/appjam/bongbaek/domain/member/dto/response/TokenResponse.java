package org.appjam.bongbaek.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import org.appjam.bongbaek.global.jwt.dto.TokenInfo;

public record TokenResponse(
		@Schema(description = "access 토큰 정보")
		TokenInfo accessToken,
		@Schema(description = "refresh 토큰 정보")
		TokenInfo refreshToken
) {

	public static TokenResponse of(
			final TokenInfo accessToken,
			final TokenInfo refreshToken
	) {
		return new TokenResponse(accessToken, refreshToken);
	}
}