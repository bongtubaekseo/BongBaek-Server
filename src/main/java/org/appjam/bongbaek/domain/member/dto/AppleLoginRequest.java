package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AppleLoginRequest(
        @Schema(description = "애플에서 발급받은 identity token")
        String identityToken
) {
}
