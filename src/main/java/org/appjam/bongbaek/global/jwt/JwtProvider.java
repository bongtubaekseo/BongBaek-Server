package org.appjam.bongbaek.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final String MEMBER_ID = "memberId";

    // TODO: ACCESS,REFRESH 모두 2주일로
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public TokenResponse reissueToken(UUID memberId) {
        return TokenResponse.of(
                generateAccessToken(memberId),
                generateRefreshToken(memberId)
        );
    } // NOTE: TokenResponse로 반환

    public String generateAccessToken(UUID memberId) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .build();

        claims.put(MEMBER_ID, memberId.toString());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // Header
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    } // NOTE: header.payload.signature 형태 생성

    public String generateRefreshToken(UUID memberId) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .build();

        claims.put(MEMBER_ID, memberId.toString());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // Header
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public JwtValidationType validateToken(String token) {
        try {
            final Claims claims = getBody(token);
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_MALFORMED_JWT);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_EXPIRATION_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_UNSUPPORTED_JWT);
        } catch (IllegalArgumentException ex) {
            return JwtValidationType.EMPTY_JWT;
        } catch (SecurityException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_MALFORMED_JWT);
        }
    } // NOTE: 토큰 검증 예외

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); // NOTE: JWT_SECRET을 직접 사용하여 HMAC 키 생성
    }

    private Claims getBody(final String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID getUserFromJwt(String token) {
        Claims claims = getBody(token);
        return UUID.fromString(claims.get(MEMBER_ID).toString()); // NOTE: UUID 객체로 변환
    }

}