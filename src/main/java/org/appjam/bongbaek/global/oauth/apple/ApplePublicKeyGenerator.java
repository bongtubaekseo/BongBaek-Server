package org.appjam.bongbaek.global.oauth.apple;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.oauth.apple.dto.ApplePublicKey;
import org.appjam.bongbaek.global.oauth.apple.dto.ApplePublicKeyResponse;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {

    public PublicKey generatePublicKey(
            Map<String, String> header,
            ApplePublicKeyResponse applePublicKeys
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        ApplePublicKey publicKey = applePublicKeys.getMatchedKey(
                header.get("kid"),
                header.get("alg")
        );

        return getPublicKey(publicKey);
    }

    private PublicKey getPublicKey(ApplePublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        BigInteger modulus = decodeBase64ToBigInteger(publicKey.n());
        BigInteger exponent = decodeBase64ToBigInteger(publicKey.e());

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());

        return keyFactory.generatePublic(keySpec);
    }

    private BigInteger decodeBase64ToBigInteger(String base64UrlValue) {
        byte[] decoded = Base64.getUrlDecoder().decode(base64UrlValue);
        return new BigInteger(1, decoded); // 1: 양수
    }
}

