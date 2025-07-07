package org.appjam.bongbaek.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String MEMBER_ID = "memberId";

    // TODO: ACCESS 2주,REFRESH 4주일로
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14 * 2;

    private final JwtParser jwtParser;

    public TokenResponse reissueToken(UUID memberId) {
        return TokenResponse.of(
                generateAccessToken(memberId),
                generateRefreshToken(memberId)
        );
    } // NOTE: TokenResponse로 반환

    private String generateToken(UUID memberId, Long expirationTime) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .build();

        claims.put(MEMBER_ID, memberId.toString());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(jwtParser.getSigningKey())
                .compact();
    }

    public String generateAccessToken(UUID memberId) {
        return generateToken(memberId, ACCESS_TOKEN_EXPIRATION_TIME);
    } // NOTE: header.payload.signature 형태 생성

    public String generateRefreshToken(UUID memberId) {
        return generateToken(memberId, REFRESH_TOKEN_EXPIRATION_TIME);
    }
}
