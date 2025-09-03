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
            // 헤더 kid , alg 추출
            Map<String, String> headers = jwtParser.parseHeaders(identityToken);
            // RSA 후보 가져오기
            ApplePublicKeyResponse suspectPublicKey = getAppleAuthPublicKey();
            // Identity Token에 맞는 RSA 찾기
            PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, suspectPublicKey);
            // 사인 검증 및 Claim 추출
            Claims claims = jwtParser.getTokenClaimsByPublicKey(identityToken, publicKey);
            // 발신처, 수신처 확인
            if (claims.getIssuer() == null || !issuer.equals(claims.getIssuer())) {
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
