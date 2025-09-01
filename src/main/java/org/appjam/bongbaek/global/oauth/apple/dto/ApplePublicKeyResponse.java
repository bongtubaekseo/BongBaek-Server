package org.appjam.bongbaek.global.oauth.apple.dto;

import java.util.List;

public record ApplePublicKeyResponse(List<ApplePublicKey> keys) {
    // NOTE: Apple RSA 공개키 3개 저장 후 맞는 키를 응답
    public ApplePublicKey getMatchedKey(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow();
    }
}
