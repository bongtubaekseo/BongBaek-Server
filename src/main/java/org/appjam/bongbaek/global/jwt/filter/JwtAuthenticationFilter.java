package org.appjam.bongbaek.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.global.jwt.JwtBlacklistManager;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.global.jwt.data.MemberAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final JwtBlacklistManager jwtBlacklistManager;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = resolveToken(request);

        try {
            if (token != null && !token.isBlank()) {
                // 블랙리스트, 로그아웃된 토큰인지 확인
                if (jwtBlacklistManager.contains("Bearer " + token)) {
                    SecurityContextHolder.clearContext();
                } else if (jwtValidator.validateToken(token)) {
                    String memberId = jwtParser.getMemberIdFromAccessToken(token);
                    MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(memberId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Header에서 Token을 추출
     *
     * @return Token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}