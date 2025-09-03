package org.appjam.bongbaek.global.oauth.apple.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppleJwtParser {

    public Map<String, String> parseHeaders(String token)  {
        // TO DO: 실제 테스트를 위해 예외 처리를 메시지로 처리, 완료 후 수정
        try {
            String[] parts = token.split("\\.");

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            Map<String, Object> header = new ObjectMapper().readValue(headerJson, Map.class);

            return header.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> String.valueOf(e.getValue())
                    ));
        } catch (JsonProcessingException e) {
            throw new  IllegalArgumentException("JWT 헤더가 유효하지 않습니다.");
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
