package org.appjam.bongbaek.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appjam.bongbaek.domain.member.dto.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.SignUpRequest;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.entity.MemberToken;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.domain.member.repository.MemberTokenRepository;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.appjam.bongbaek.global.jwt.util.JwtProvider;
import org.appjam.bongbaek.global.oauth.kakao.KakaoLoginClient;
import org.appjam.bongbaek.global.oauth.kakao.dto.KakaoLoginResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final KakaoLoginClient kakaoLoginClient;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponse login(final String accessToken) {
        final Long kakaoId = kakaoLoginClient.validateKakaoAccessToken(accessToken);

        if (memberRepository.existsBykakaoId(kakaoId)) {
            Member member = memberRepository.findBykakaoId(kakaoId);
            TokenResponse tokenResponse = generateTokensForMember(member);

            return LoginResponse.ofLoginSuccess(tokenResponse, kakaoId, null);
        } else {
            return LoginResponse.of(null, false, kakaoId, accessToken);
        }
    } // NOTE: 클라이언트에서 받은 액세스 토큰으로 카카오 사용자 정보 조회

    @Transactional
    public Void logout(final UUID memberId) {
        if (memberTokenRepository.existsByMemberId(memberId)) {
            memberTokenRepository.deleteByMemberId(memberId);
        }
        return null;
    } // TODO: 추후 보완 예정

    @Transactional
    public LoginResponse signUp(final SignUpRequest signUpRequest) {
        Long validatedKakaoId;

        try {
            validatedKakaoId = kakaoLoginClient.validateKakaoAccessToken(signUpRequest.kakaoAccessToken());
        } catch (Exception e) {
            log.error("카카오 Access Token 검증 실패: {}", e.getMessage());
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        if (!validatedKakaoId.equals(signUpRequest.kakaoId())) {
            log.warn("요청된 카카오 ID({}), 검증된 카카오 ID({}) 불일치", signUpRequest.kakaoId(), validatedKakaoId);
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        if (memberRepository.existsBykakaoId(signUpRequest.kakaoId())) {
            throw new CustomException(CommonErrorCode.ALREADY_REGISTERED_MEMBER);
        } // NOTE: 이미 가입된 회원인지 확인

        try {
            IncomeType incomeType = parseIncomeType(signUpRequest.memberIncome());
            LocalDate birthday = LocalDate.parse(signUpRequest.memberBirthday(), DateTimeFormatter.ISO_LOCAL_DATE);

            Member member = signUpRequest.toMember(birthday, incomeType);

            Member savedMember = memberRepository.save(member);

            TokenResponse tokenResponse = generateTokensForMember(savedMember);

            log.info("회원가입 완료. 카카오 ID: {}", signUpRequest.kakaoId());

            return LoginResponse.ofLoginSuccess(tokenResponse, signUpRequest.kakaoId(), signUpRequest.kakaoAccessToken());

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            throw new CustomException(CommonErrorCode.BAD_REQUEST);
        }
    }

    private TokenResponse generateTokensForMember(Member member) {
        final String accessToken = jwtProvider.generateAccessToken(member.getMemberId());
        final String refreshToken = jwtProvider.generateRefreshToken(member.getMemberId());

        if (memberTokenRepository.existsByMemberId(member.getMemberId())) {
            final MemberToken memberToken = memberTokenRepository.findByMemberId(member.getMemberId());
            memberToken.updateRefreshToken(refreshToken);
        } else {
            memberTokenRepository.save(
                    MemberToken.builder()
                            .memberId(member.getMemberId())
                            .refreshToken(refreshToken)
                            .kakaoRefreshToken("")
                            .build());
        }

        return TokenResponse.of(accessToken, refreshToken);
    } // NOTE: 이미 토큰이 있다면 update, 없다면 새로 생성

    private IncomeType parseIncomeType(String incomeString) {
        if ("200만원 이상".equals(incomeString)) {
            return IncomeType.OVER200;
        } else if ("200만원 이하".equals(incomeString)) {
            return IncomeType.UNDER200;
        } else {
            throw new CustomException(CommonErrorCode.BAD_REQUEST);
        }
    }
}
