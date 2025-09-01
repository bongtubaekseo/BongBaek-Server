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
        Header header = Jwts.parser()
                .build()
                .parseSignedClaims(identityToken)
                .getHeader();

        return header.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.valueOf(e.getValue())
                ));
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
