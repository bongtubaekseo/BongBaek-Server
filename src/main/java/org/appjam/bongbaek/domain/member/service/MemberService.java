package org.appjam.bongbaek.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.domain.member.dto.AppleLoginResponse;
import org.appjam.bongbaek.domain.member.dto.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.exception.SignUpRequiredException;
import org.appjam.bongbaek.global.jwt.JwtBlacklistManager;
import org.appjam.bongbaek.global.jwt.JwtRefreshStore;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtProvider;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.global.oauth.apple.AppleLoginClient;
import org.appjam.bongbaek.global.oauth.apple.dto.AppleInfoResponse;
import org.appjam.bongbaek.global.oauth.kakao.KakaoLoginClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoLoginClient kakaoLoginClient;
    private final AppleLoginClient appleLoginClient;

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final JwtRefreshStore jwtRefreshStore;
    private final JwtBlacklistManager jwtBlacklistManager;

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
    public AppleLoginResponse loginByApple(
            final String identityToken
    ) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        final AppleInfoResponse userData = appleLoginClient.validateAppleIdentityToken(identityToken);
        final String appleId = userData.id();

        Member member = memberRepository.findByAppleId(appleId)
                .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        TokenResponse tokenResponse = generateTokensForMember(member);

        return AppleLoginResponse.ofLoginSuccess(member.getMemberName(), tokenResponse, appleId);
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

    @Transactional
    public AppleLoginResponse signUpByApple(
            final SignUpRequest signUpRequest
    ) {
        // 이미 가입된 회원인지 확인
        if (memberRepository.existsByAppleId(signUpRequest.appleId())) {
            throw new CustomException(CommonErrorCode.ALREADY_REGISTERED_MEMBER);
        }

        try {
            IncomeType incomeType = IncomeType.of(signUpRequest.memberIncome());
            Member member = signUpRequest.toAppleMember(incomeType);
            Member savedMember = memberRepository.save(member);

            TokenResponse tokenResponse = generateTokensForMember(savedMember);
            log.info("회원가입 완료. 애플 ID: {}", signUpRequest.appleId());

            return AppleLoginResponse.ofLoginSuccess(member.getMemberName(), tokenResponse, signUpRequest.appleId());

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            throw new CustomException(CommonErrorCode.BAD_REQUEST);
        }
    }

    @Transactional
    public void logout(final String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) return;

        String accessTokenNoBearer = accessToken.substring("Bearer ".length());
        jwtValidator.validateToken(accessTokenNoBearer);

        String memberId = jwtParser.parseClaims(accessTokenNoBearer).getSubject();

        // 유저의 모든 refreshToken 제거
        jwtRefreshStore.deleteAllForUser(memberId);

        // 현재 accessToken 차단
        jwtBlacklistManager.add(accessToken);
    }

    @Transactional
    public TokenResponse reissueTokens(final String refreshToken) {
        jwtValidator.validateToken(refreshToken);

        // 저장된 refreshToken인지 확인
        if (!jwtRefreshStore.exists(refreshToken)) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        String memberId = jwtParser.parseClaims(refreshToken).getSubject();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        // 사용한 refreshToken 폐기
        jwtRefreshStore.deleteToken(refreshToken);

        // 새로운 access+refresh 토큰 발급
        return generateTokensForMember(member);
    }

    /**
     * 새 access+refresh 토큰 발급
     * + refreshtoken Redis 저장
     * */
    private TokenResponse generateTokensForMember(Member member) {
        TokenResponse tokenResponse = jwtProvider.generateToken(member.getMemberId());

        long refreshTtlSec = Math.max(1, (tokenResponse.refreshToken().expiredAt() - System.currentTimeMillis()) / 1000);

        // refresh token redis에 저장
        jwtRefreshStore.save(member.getMemberId(), tokenResponse.refreshToken().token(), refreshTtlSec);

        return tokenResponse;
    }

    @Transactional
    public void updateProfile(final String memberId, final UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        member.update(request);
    }
}
