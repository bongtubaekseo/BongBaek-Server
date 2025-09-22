package org.appjam.bongbaek.global.jwt.components;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;

import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.global.jwt.dto.TokenInfo;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    // TODO: ACCESS 2주,REFRESH 4주일로
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14 * 2;

    private final SecretKey secretKey;
    private final String issuer;

    /**
     * Access Token 생성
     */
    public TokenInfo generateAccessToken(final Member member) {
        return generateToken(member, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    /**
     * Refresh Token 생성
     */
    public TokenInfo generateRefreshToken(final Member member) {
        return generateToken(member, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private TokenInfo generateToken(final Member member, final long expiration) {
        long expiredAt = System.currentTimeMillis() + expiration;
        String token = Jwts.builder()
                .issuer(issue)
                .subject(member.getMemberId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(expiredAt))
                .signWith(secretKey, SIG.HS512)
                .compact();

        return TokenInfo.of(token, expiredAt);
    }
}