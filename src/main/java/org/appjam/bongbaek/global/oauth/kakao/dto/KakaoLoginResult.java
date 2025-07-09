package org.appjam.bongbaek.global.oauth.kakao.dto;

public record KakaoLoginResult(
        Long kakaoId,
        String accessToken
) {
    public static KakaoLoginResult of(Long kakaoId, String accessToken) {
        return new KakaoLoginResult(kakaoId, accessToken);
    }
}
