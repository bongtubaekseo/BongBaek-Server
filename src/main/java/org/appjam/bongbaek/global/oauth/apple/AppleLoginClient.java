package org.appjam.bongbaek.global.oauth.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.oauth.apple.dto.AppleInfoResponse;
import org.appjam.bongbaek.global.oauth.apple.dto.ApplePublicKeyResponse;
import org.appjam.bongbaek.global.oauth.apple.jwt.AppleJwtParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleLoginClient {

    private final AppleJwtParser jwtParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    @Value("${apple.issuer}")
    private String issuer;
    @Value("${apple.client-id}")
    private String clientId;

    public String validateAppleIdentityToken(String identityToken) {

        try {
        Map<String, String> headers = jwtParser.parseHeaders(identityToken);

        ApplePublicKeyResponse suspectPublicKey = getAppleAuthPublicKey();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, suspectPublicKey);

        Claims claims = jwtParser.getTokenClaimsByPublicKey(identityToken, publicKey);

        if (!issuer.equals(claims.getIssuer())) {
            throw new CustomException(CommonErrorCode.VALIDATION_ERROR);
        }
        if (claims.getAudience() == null || !claims.getAudience().contains(clientId)) {
            throw new CustomException(CommonErrorCode.VALIDATION_ERROR);
        }

        AppleInfoResponse userData = AppleInfoResponse.of(claims);

        return userData.id();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    private ApplePublicKeyResponse getAppleAuthPublicKey() {
        RestClient restClient = RestClient.create();

        return restClient.get()
                .uri("https://appleid.apple.com/auth/keys")
                .retrieve()
                .body(ApplePublicKeyResponse.class);
    }
}
