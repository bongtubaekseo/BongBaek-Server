package org.appjam.bongbaek.domain.member.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.domain.member.dto.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.SignUpRequest;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.exception.SignUpRequiredException;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtProvider;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.global.oauth.kakao.KakaoLoginClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoLoginClient kakaoLoginClient;

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;

    @Transactional
    public LoginResponse login(
        final String accessToken
    ) {
        final Long kakaoId = kakaoLoginClient.validateKakaoAccessToken(accessToken);

        Member member = memberRepository.findByKakaoId(kakaoId)
            .orElseThrow(() -> new SignUpRequiredException(kakaoId));

        TokenResponse tokenResponse = generateTokensForMember(member);

        return LoginResponse.ofLoginSuccess(member.getMemberName(), tokenResponse, kakaoId);
    }

    @Transactional
    public LoginResponse signUp(
        final SignUpRequest signUpRequest
    ) {
        // 이미 가입된 회원인지 확인
        if (memberRepository.existsByKakaoId(signUpRequest.kakaoId())) {
            throw new CustomException(CommonErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        try {
            IncomeType incomeType = IncomeType.of(signUpRequest.memberIncome());
            Member member = signUpRequest.toMember(incomeType);
            Member savedMember = memberRepository.save(member);

            TokenResponse tokenResponse = generateTokensForMember(savedMember);
            log.info("회원가입 완료. 카카오 ID: {}", signUpRequest.kakaoId());

            return LoginResponse.ofLoginSuccess(member.getMemberName(), tokenResponse, signUpRequest.kakaoId());

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            throw new CustomException(CommonErrorCode.BAD_REQUEST);
        }
    }

    private TokenResponse generateTokensForMember(
        Member member
    ) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            member.getMemberId(),
            "", // credentials 미사용
            Collections.emptyList() // role 미사용
        );

        return jwtProvider.generateToken(authentication);
    }

    @Transactional
    public TokenResponse reissueTokens(
        final String refreshToken
    ) {
        // refresh token 유효성 검사
        jwtValidator.validateToken(refreshToken);

        // refresh 토큰에서 sub(=memberId) 파싱
        String memberId = jwtParser.parseClaims(refreshToken).getSubject();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        // 토큰 재발급
        return generateTokensForMember(member);
    }
}