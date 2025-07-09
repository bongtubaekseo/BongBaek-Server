package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "인가 코드", example = "카카오에서 발급받은 인가 코드")
        String authorizationCode
) {
}
