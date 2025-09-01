package org.appjam.bongbaek.global.jwt;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtBlacklistManager {

    private static final String PREFIX = "blackList:";

    private final StringRedisTemplate redis;
    private final JwtParser jwtParser;

    /**
     * 토큰을 블랙리스트에 추가
     * (TTL = 토큰만료까지)
     * */
    public void add(String token) {
        String tokenNoBearer = normalize(token);

        // 초단위로 ttl 계산, 최소 1초
        long ttlSec = Math.max(1, (jwtParser.getExpire(tokenNoBearer).getTime() - System.currentTimeMillis()) / 1000);

        // redis에 "blackList:토큰값" 키 생성
        redis.opsForValue().set(key(tokenNoBearer), "1", ttlSec, TimeUnit.SECONDS);
    }

    /**
     * 블랙리스트에 토큰이 있는지 확인
     * */
    public boolean contains(String authorizationBearer) {
        String token = normalize(authorizationBearer);
        return redis.hasKey(key(token));
    }

    /*
     * redis key 생성 메서드
     */
    private String key(String token) {
        return PREFIX + token;
    }

    /*
     * "Bearer "로 시작하는지 확인 메서드
     */
    private boolean isBearer(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    /*
     * "Bearer " 제거 메서드
     */
    private String normalize(String token) {
        if (token == null) return "";

        return isBearer(token) ? token.substring(7) : token;
    }
}