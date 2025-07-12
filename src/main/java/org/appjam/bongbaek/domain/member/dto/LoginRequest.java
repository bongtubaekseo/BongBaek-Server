package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "카카오 액세스 토큰", example = "카카오에서 발급받은 액세스 토큰")
        String accessToken
) {
}
