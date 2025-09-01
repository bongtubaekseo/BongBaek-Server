package org.appjam.bongbaek.global.oauth.apple.dto;

import io.jsonwebtoken.Claims;

public record AppleInfoResponse(
        String sub
) {

    public static AppleInfoResponse of(Claims claims) {
        return new AppleInfoResponse(
                claims.getSubject()
        );
    }
}
