package org.appjam.bongbaek.global.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomJwtAuthenticationEntryPoint(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.resolver = resolver;
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) {

        Object exception = request.getAttribute("exception");
        CustomException custom = (exception instanceof CustomException customException) ? customException
                : new CustomException(CommonErrorCode.UNAUTHORIZED);

        resolver.resolveException(request, response, null, custom);
    }
}