package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReissueRequest(
        @Schema(description = "리프레쉬 토큰", example = "토큰 재발급을 위한 리프레쉬 토큰")
        String refreshToken
) {
}

