package org.appjam.bongbaek.global.oauth.apple.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record ApplePublicKey(
        @Schema(description = "공개키 종류 / 애플은 항상 RSA")
        String kty,
        @Schema(description = "공개키 식별자 / 이를 통해 세 가지 중에 같은 kid 키로 검증")
        String kid,
        @Schema(description = "JWT 알고리즘")
        String alg,
        @Schema(description = "공개키 계산에 필요한 값")
        String n,
        @Schema(description = "공개키 계산에 필요한 값")
        String e
) {
}
