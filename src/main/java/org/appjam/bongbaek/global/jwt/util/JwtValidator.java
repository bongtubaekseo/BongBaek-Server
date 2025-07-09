package org.appjam.bongbaek.global.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.jwt.enums.JwtValidationType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    public JwtValidationType validateToken(String token) {
        try {
            jwtParser.getBody(token);
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_MALFORMED_JWT);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_EXPIRATION_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_UNSUPPORTED_JWT);
        } catch (IllegalArgumentException ex) {
            return JwtValidationType.EMPTY_JWT;
        } catch (SecurityException ex) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED_MALFORMED_JWT);
        }
    }
}
