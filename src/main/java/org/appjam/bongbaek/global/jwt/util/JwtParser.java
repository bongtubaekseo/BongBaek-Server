package org.appjam.bongbaek.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.UUID;

@Component
public class JwtParser {

    private static final String MEMBER_ID = "memberId";

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public Claims getBody(final String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID getUserFromJwt(String token) {
        Claims claims = getBody(token);
        return UUID.fromString(claims.get(MEMBER_ID).toString());
    }

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }
}
