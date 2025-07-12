package org.appjam.bongbaek.global.oauth;

import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PrincipalHandler {

    private static final String ANONYMOUS_USER = "anonymousUser";

    public static UUID getMemberIdFromPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validatePrincipal(principal);
        return UUID.fromString(principal.toString());
    }

    private static void validatePrincipal(
            final Object principal
    ) {
        if (principal.toString().equals(ANONYMOUS_USER)) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }
    }
}