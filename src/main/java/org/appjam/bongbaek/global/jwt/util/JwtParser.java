package org.appjam.bongbaek.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.jwt.data.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtParser {

    // 나중에 role 도입시 사용
    private static final String CLAIM_ROLE = "role";

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    /**
     * AccessToken을 파싱하여 Claims를 반환하는 메서드
     */
    public Claims parseClaims(
        final String accessToken
    ) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(CommonErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    /**
     * Access Token을 파싱하여 Authentication 객체를 반환하는 메서드
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String userId = claims.getSubject();

        // 권한: ROLE_ 접두사 부여 (미도입이면 빈 리스트)
        List<SimpleGrantedAuthority> authorities =
            Stream.ofNullable(claims.get(CLAIM_ROLE, String.class))
                .flatMap(csv -> Arrays.stream(csv.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        // UserDetails 주입
        CustomUserDetails principal = new CustomUserDetails(userId, authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * SecretKey를 생성하는 메소드
     */
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}