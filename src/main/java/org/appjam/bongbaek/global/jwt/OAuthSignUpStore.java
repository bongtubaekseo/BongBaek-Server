package org.appjam.bongbaek.global.jwt;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 최초 소셜 로그인 시 OIDC 검증이 끝난 oauthId를
 * 회원가입 직전까지 잠시 보관하는 Redis 저장소.
 * - key 형식: signup:oauth:{provider}:{oauthId}
 *   예) signup:oauth:kakao:1234567890
 */
@Component
@RequiredArgsConstructor
public class OAuthSignUpStore {

    private static final String PREFIX = "signup:oauth:";

    // 최초 로그인 후 회원가입을 진행해야 하는 oauthId 유효 시간(초)
    private static final long DEFAULT_TTL_SECONDS = 10 * 60; // 10분

    private final StringRedisTemplate redis;

    private String key(String provider, String oauthId) {
        return PREFIX + provider + ":" + oauthId;
    }

    /**
     * 기본 TTL(10분)로 oauthId 저장
     */
    public void save(String provider, String oauthId) {
        save(provider, oauthId, DEFAULT_TTL_SECONDS);
    }

    /**
     * 지정한 TTL(초)로 oauthId 저장
     */
    public void save(String provider, String oauthId, long ttlSeconds) {
        redis.opsForValue().set(key(provider, oauthId), "1", Math.max(1, ttlSeconds), TimeUnit.SECONDS);
    }

    /**
     * 해당 provider + oauthId 조합이 최초 로그인에서 검증된 상태로 Redis에 저장돼 있는지 확인
     */
    public boolean exists(String provider, String oauthId) {
        return Boolean.TRUE.equals(redis.hasKey(key(provider, oauthId)));
    }

    /**
     * 회원가입이 정상적으로 끝난 oauthId는 재사용 방지 + 메모리 정리를 위해 제거
     */
    public void delete(String provider, String oauthId) {
        redis.delete(key(provider, oauthId));
    }
}
