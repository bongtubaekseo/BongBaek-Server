package org.appjam.bongbaek.domain.member.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.domain.member.dto.request.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.request.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.request.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.dto.request.WithdrawRequest;
import org.appjam.bongbaek.domain.member.dto.response.MyInfoResponse;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.entity.OAuthProvider;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.domain.member.repository.MemberWithdrawalRepository;
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
import org.appjam.bongbaek.global.oauth.kakao.KakaoLoginClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberWithdrawalRepository memberWithdrawalRepository;

    private final KakaoLoginClient kakaoLoginClient;
    private final AppleLoginClient appleLoginClient;

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final JwtRefreshStore jwtRefreshStore;
    private final JwtBlacklistManager jwtBlacklistManager;
    private final EntityManager entityManager;

    @Transactional
    public LoginResponse login(
            OAuthProvider oAuthProvider,
            final String accessToken
    ) {
        if (oAuthProvider.equals(OAuthProvider.KAKAO)) {
            final String kakaoId = kakaoLoginClient.validateKakaoAccessToken(accessToken);

            try {
                Member member = memberRepository.findByKakaoId(kakaoId)
                        .orElseThrow(() -> new SignUpRequiredException(kakaoId));

                TokenResponse tokenResponse = generateTokensForMember(member);

                return LoginResponse.ofKakaoLoginSuccess(member.getMemberName(), tokenResponse, kakaoId);
            } catch(SignUpRequiredException e){
                return LoginResponse.ofKakaoLoginFailure(kakaoId);
            }
        }

        if (oAuthProvider.equals(OAuthProvider.APPLE)) {
            final String appleId = appleLoginClient.validateAppleIdentityToken(accessToken);

            try {
                Member member = memberRepository.findByAppleId(appleId)
                        .orElseThrow(() -> new SignUpRequiredException(appleId));

                TokenResponse tokenResponse = generateTokensForMember(member);

                return LoginResponse.ofAppleLoginSuccess(member.getMemberName(), tokenResponse, appleId);
            } catch(SignUpRequiredException e){
                return LoginResponse.ofAppleLoginFailure(appleId);
            }
        }
        throw new CustomException(CommonErrorCode.UNAUTHORIZED);
    }

    @Transactional
    public LoginResponse signUp(
        final SignUpRequest signUpRequest
    ) {
        if(signUpRequest.kakaoId() != null && !signUpRequest.kakaoId().isEmpty()) {
            // 이미 가입된 회원인지 확인
            if (memberRepository.existsByKakaoId(signUpRequest.kakaoId())) {
                throw new CustomException(CommonErrorCode.ALREADY_REGISTERED_MEMBER);
            }

            Member member = createMember(signUpRequest);
            TokenResponse tokenResponse = generateTokensForMember(member);
            log.info("회원가입 완료. 카카오 ID: {}", signUpRequest.kakaoId());

            return LoginResponse.ofKakaoLoginSuccess(member.getMemberName(), tokenResponse, signUpRequest.kakaoId());
        }

        if(signUpRequest.appleId() != null && !signUpRequest.appleId().isEmpty()) {
            if (memberRepository.existsByAppleId(signUpRequest.appleId())) {
                throw new CustomException(CommonErrorCode.ALREADY_REGISTERED_MEMBER);
            }

            Member member = createMember(signUpRequest);
            TokenResponse tokenResponse = generateTokensForMember(member);
            log.info("회원가입 완료. 애플 ID: {}", signUpRequest.appleId());

            return LoginResponse.ofAppleLoginSuccess(member.getMemberName(), tokenResponse, signUpRequest.appleId());
        }

        throw new CustomException(CommonErrorCode.UNAUTHORIZED);
    }

    @Transactional
    public void logout(final String accessToken) {
        if (!jwtValidator.isBearer(accessToken)) return;

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

    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        return MyInfoResponse.fromEntity(member);
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

    private Member createMember(
            final SignUpRequest signUpRequest
    ) {
        IncomeType incomeType = IncomeType.of(signUpRequest.memberIncome());
        Member member = signUpRequest.toMember(incomeType);

        return memberRepository.save(member);
    }

    @Transactional
    public void withdraw(final String accessToken, final String memberId, final WithdrawRequest request) {
        if (!jwtValidator.isBearer(accessToken)) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        final String accessTokenNoBearer = accessToken.substring(7);

        // 유효성 검증 실패시 예외
        jwtValidator.validateToken(accessTokenNoBearer);

        // 토큰 주체와 사용자 일치 확인
        final String subject = jwtParser.parseClaims(accessTokenNoBearer).getSubject();

        if (!memberId.equals(subject)) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        // 탈퇴 이력 저장
        memberWithdrawalRepository.save(request.toEntity());

        // 회원 정보 삭제
        memberRepository.delete(member);

        entityManager.flush();

        // 토큰 무효화 (accessToken 블랙리스트 + refreshToken 전부 삭제)
        jwtBlacklistManager.add(accessToken);
        jwtRefreshStore.deleteAllForUser(memberId);
    }
}

