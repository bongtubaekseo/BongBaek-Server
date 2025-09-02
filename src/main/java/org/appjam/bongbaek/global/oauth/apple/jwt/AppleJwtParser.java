package org.appjam.bongbaek.global.oauth.apple.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppleJwtParser {

    public Map<String, String> parseHeaders(
            String identityToken
    ){
        try {
        Header header = Jwts.parser()
                .build()
                .parseSignedClaims(identityToken)
                .getHeader();

        return header.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.valueOf(e.getValue())
                ));
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 형식의 JWT 토큰입니다", e);
        } catch (JwtException e) {
            throw new IllegalArgumentException("JWT 토큰 파싱 중 오류가 발생했습니다", e);
        }
    }

    public Claims getTokenClaimsByPublicKey(
            String identityToken,
            PublicKey publicKey
    ) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(identityToken);

        return claimsJws.getPayload();
    }
}
