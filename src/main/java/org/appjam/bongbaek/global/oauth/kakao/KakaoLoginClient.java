package org.appjam.bongbaek.global.oauth.kakao;

import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.global.oauth.kakao.dto.KakaoInfoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoLoginClient {

    public Long validateKakaoAccessToken(final String accessToken) {
        try {
            RestClient restClient = RestClient.create();

            KakaoInfoResponse kakaoInfoResponse = restClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .body(KakaoInfoResponse.class);

            if (kakaoInfoResponse == null || kakaoInfoResponse.id() == null) {
                throw new IllegalArgumentException("카카오 사용자 정보를 가져올 수 없습니다.");
            }

            return Long.parseLong(kakaoInfoResponse.id());
        } catch (Exception e) {
            log.error("카카오 Access Token 검증 실패: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 kakao Access Token입니다.");
        }
    } // NOTE: 카카오 Access Token 검증 메서드
}