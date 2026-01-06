package org.appjam.bongbaek.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "소셜 로그인 아이디 토큰", example = "소셜 로그인 플랫폼으로 부터 발급받은 아이디 토큰")
        String idToken
) {
}
