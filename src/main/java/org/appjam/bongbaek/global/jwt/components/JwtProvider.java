package org.appjam.bongbaek.global.jwt.components;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    // TODO: ACCESS 2주,REFRESH 4주일로
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14 * 2;

    private final SecretKey secretKey;

    public TokenResponse generateToken(String subject) {
        TokenResponse.Token access  = generateAccessToken(subject);
        TokenResponse.Token refresh = generateRefreshToken(subject);
        return TokenResponse.of(access, refresh);
    }

    /**
     * Access Token 생성
     */
    public TokenResponse.Token generateAccessToken(String subject) {
        long expiredAt = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME;

        String token = Jwts.builder()
            .subject(subject)
            .claim("role", "")
            .expiration(new Date(expiredAt))
            .signWith(secretKey, SIG.HS256)
            .compact();

        return TokenResponse.Token.of(token, expiredAt);
    }

    /**
     * Refresh Token 생성
     */
    private TokenResponse.Token generateRefreshToken(String subject) {
        long expiredAt = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME;

        String token = Jwts.builder()
            .subject(subject)
            .expiration(new Date(expiredAt))
            .signWith(secretKey)
            .compact();

        return TokenResponse.Token.of(token, expiredAt);
    }
}