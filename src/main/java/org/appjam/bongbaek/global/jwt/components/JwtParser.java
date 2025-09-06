package org.appjam.bongbaek.global.jwt.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtParser {

    // 나중에 role 도입시 사용
    private static final String CLAIM_ROLE = "role";

    private final SecretKey secretKey;

    /**
     * AccessToken을 파싱하여 Claims를 반환하는 메서드
     */
    public Claims parseClaims(
        final String accessToken
    ) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(CommonErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    /**
     * Access Token을 파싱하여 멤버의 id를 반환하는 메서드
     */
    public String getMemberIdFromAccessToken(String token) {
        Claims claims = parseClaims(token);

        return claims.getSubject();
    }

    public Date getExpire(final String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }
}