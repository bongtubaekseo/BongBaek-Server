package org.appjam.bongbaek.global.jwt.enums;

public enum JwtValidationType {
    VALID_JWT,
    INVALID_JWT_SIGNATURE,
    INVALID_JWT_TOKEN,
    EXPIRED_JWT_TOKEN,
    UNSUPPORTED_JWT_TOKEN,
    EMPTY_JWT
}
