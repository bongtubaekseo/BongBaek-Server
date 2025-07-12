package org.appjam.bongbaek.global.oauth.kakao;

import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.global.oauth.kakao.dto.KakaoAccessTokenResponse;
import org.appjam.bongbaek.global.oauth.kakao.dto.KakaoInfoResponse;
import org.appjam.bongbaek.global.oauth.kakao.dto.KakaoLoginResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class KakaoLoginClient {

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    /**
     * 테스트용 메서드 - 인가코드로 토큰 발급
     * 현재는 클라이언트에서 직접 토큰을 발급받아 전달하므로 사용되지 않음
     */
    @Deprecated
    public KakaoLoginResult login(final String code) {

        RestClient restClient = RestClient.create();
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("code", "{code}")
                .queryParam("client_id", "{client_id}")
                .queryParam("redirect_uri", "{redirect_uri}")
                .queryParam("grant_type", "{grant_type}")
                .encode()
                .buildAndExpand(code, kakaoClientId, kakaoRedirectUrl, "authorization_code")
                .toUri();

        KakaoAccessTokenResponse kakaoAccessTokenResponse = restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(KakaoAccessTokenResponse.class);


        log.info(kakaoAccessTokenResponse.accessToken());

        KakaoInfoResponse kakaoInfoResponse = restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + kakaoAccessTokenResponse.accessToken())
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .body(KakaoInfoResponse.class);

        return KakaoLoginResult.of(Long.parseLong(kakaoInfoResponse.id()), kakaoAccessTokenResponse.accessToken());
    } // NOTE: kakaoId 와 kakaoAccessToken을 반환


    public Long validateKakaoAccessToken(final String accessToken) {
        try {
            RestClient restClient = RestClient.create();

            KakaoInfoResponse kakaoInfoResponse = restClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .body(KakaoInfoResponse.class);

            return Long.parseLong(kakaoInfoResponse.id());
        } catch (Exception e) {
            log.error("카카오 Access Token 검증 실패: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 kakao Access Token입니다.");
        }
    } // NOTE: 카카오 Access Token 검증 메서드
}