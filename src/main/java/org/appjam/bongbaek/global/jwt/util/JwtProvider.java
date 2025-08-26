package org.appjam.bongbaek.global.jwt.util;

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

    private final JwtParser jwtParser;

    public TokenResponse generateToken(Authentication authentication) {
        TokenResponse.Token accessToken  = generateAccessToken(authentication);
        TokenResponse.Token refreshToken = generateRefreshToken();

        return TokenResponse.of(accessToken, refreshToken);
    }

    /**
     * Access Token 생성
     */
    public TokenResponse.Token generateAccessToken(Authentication authentication) {
        long expiredAt = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME;

        // 권한 CSV (비어있어도 OK)
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        String token = Jwts.builder()
            .subject(authentication.getName())
            .claim("role", authorities)
            .expiration(new Date(expiredAt))
            .signWith(jwtParser.getSigningKey(), SIG.HS256)
            .compact();

        return TokenResponse.Token.of(token, expiredAt);
    }

    /**
     * Refresh Token 생성
     */
    private TokenResponse.Token generateRefreshToken() {
        long expiredAt = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME;

        String token = Jwts.builder()
            .expiration(new Date(expiredAt))
            .signWith(jwtParser.getSigningKey())
            .compact();

        return TokenResponse.Token.of(token, expiredAt);
    }
}