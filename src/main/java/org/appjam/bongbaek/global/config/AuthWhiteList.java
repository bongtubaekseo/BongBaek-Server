package org.appjam.bongbaek.global.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthWhiteList {

    public static final String[] WHITE_LIST = {
            // Swagger 관련
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/webjars/**",

            // 기본 리소스
            "/",
            "/favicon.ico",

            // 테스트 및 콜백
            "/test/**",
            "/login/**",
            "/callback",

            // 인증 관련 API
            "/api/v1/oauth/kakao",
            "/api/v1/member/profile",
            "/api/v1/member/reissue",
    };
}
