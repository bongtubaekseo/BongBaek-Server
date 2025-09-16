package org.appjam.bongbaek.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;

public record LoginResponse(
		@Schema(description = "회원 이름")
		String name,

		@Schema(description = "jwt Token", nullable = true)
		TokenResponse token,

		@Schema(description = "회원 가입 완료 여부")
		boolean isCompletedSignUp,

		@Schema(description = "oauth ID")
		String oauthId,

		@Schema(description = "소셜로그인 플랫폼 종류")
		String oauthProvider,

		@Schema(description = "kakao map api key", nullable = true)
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String apiKey
) {
	public static LoginResponse success(final Member member, final TokenResponse tokenResponse,  final String apiKey) {
		return new LoginResponse(
				member.getMemberName(),
				tokenResponse,
				true,
				member.getOauthId(),
				member.getOauthProvider().getName(),
				apiKey
		);
	}

	public static LoginResponse failure(final String oauthId, final String oauthProvider){
		return new LoginResponse(null, null, false, oauthId, oauthProvider, null);
	}
}
