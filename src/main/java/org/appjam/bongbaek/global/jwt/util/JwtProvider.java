package org.appjam.bongbaek.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
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

    private String generateToken(UUID memberId, Long expirationTime) {
        final Date now = new Date();

        return Jwts.builder()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .claim(MEMBER_ID, memberId.toString())
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
