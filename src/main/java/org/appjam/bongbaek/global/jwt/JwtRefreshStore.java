package org.appjam.bongbaek.global.jwt;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRefreshStore {

    private final StringRedisTemplate redis;

    // redis set 키
    private String memberKey(String memberId)   { return "rt:member:"  + memberId; }

    // redis 단건 키
    private String tokenKey(String refresh)   { return "rt:token:" + refresh; }

    /**
     * redis 세트에 추가/설정
     * */
    public void save(String memberId, String refreshToken, long ttlSeconds) {
        redis.opsForSet().add(memberKey(memberId), refreshToken);
        redis.opsForValue().set(tokenKey(refreshToken), memberId, Math.max(ttlSeconds, 1), TimeUnit.SECONDS);
    }

    /**
     * 해당 키가 존재하는지 확인
     * */
    public boolean exists(String refreshToken) {
        Boolean has = redis.hasKey(tokenKey(refreshToken));
        return Boolean.TRUE.equals(has);
    }

    /**
     * 재발급 후, 이전 refresh 토큰 단일 폐기
     * */
    public void deleteToken(String refreshToken) {
        String tk = tokenKey(refreshToken);
        String memberId = redis.opsForValue().get(tk);
        if (memberId != null) {
            redis.opsForSet().remove(memberKey(memberId), refreshToken);
        }
        redis.delete(tk);
    }

    /**
     * 모든 refresh token 폐기(로그아웃/강제 로그아웃)
     * */
    public void deleteAllForUser(String memberId) {
        String uk = memberKey(memberId);
        Set<String> tokens = redis.opsForSet().members(uk);
        if (tokens != null) {
            for (String t : tokens) redis.delete(tokenKey(t));
        }
        redis.delete(uk);
    }
}
