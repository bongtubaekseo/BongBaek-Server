package org.appjam.bongbaek.domain.member.service;

import java.util.Optional;

import org.appjam.bongbaek.domain.member.dto.response.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.request.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.request.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.dto.request.WithdrawRequest;
import org.appjam.bongbaek.domain.member.dto.response.MyInfoResponse;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.entity.OAuthProvider;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.domain.member.repository.MemberWithdrawalRepository;
import org.appjam.bongbaek.global.exception.member.MemberAlreadyExistsException;
import org.appjam.bongbaek.global.exception.member.MemberNotAuthenticatedException;
import org.appjam.bongbaek.global.exception.member.MemberNotFoundException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.appjam.bongbaek.global.jwt.JwtBlacklistManager;
import org.appjam.bongbaek.global.jwt.JwtRefreshStore;
import org.appjam.bongbaek.global.jwt.OAuthSignUpStore;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtProvider;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.domain.member.dto.response.TokenResponse;
import org.appjam.bongbaek.global.jwt.dto.TokenInfo;
import org.appjam.bongbaek.global.oauth.OidcOAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

	private final MemberRepository memberRepository;
	private final MemberWithdrawalRepository memberWithdrawalRepository;

	private final OidcOAuthClient oidcOAuthClient;
    private final OAuthSignUpStore oAuthSignUpStore;

	private final JwtProvider jwtProvider;
	private final JwtValidator jwtValidator;
	private final JwtParser jwtParser;
	private final JwtRefreshStore jwtRefreshStore;
	private final JwtBlacklistManager jwtBlacklistManager;

	@Value("${kakao.api-key}")
	private String apiKey;

	@Transactional
	public LoginResponse login(final String oAuthProvider, final String idToken) {
		String oAuthId = oidcOAuthClient.getUserInfo(oAuthProvider, idToken);

		Optional<Member> OptionalMember = memberRepository.findByOauthIdAndOauthProvider(oAuthId,
				OAuthProvider.of(oAuthProvider));

		if (OptionalMember.isEmpty()) {
            oAuthSignUpStore.save(oAuthProvider, oAuthId);

			return LoginResponse.failure(oAuthProvider, oAuthId);
		}

		Member member = OptionalMember.get();

		TokenResponse tokenResponse = generateTokensForMember(member);

		return LoginResponse.success(member, tokenResponse, apiKey);
	}

	@Transactional
	public LoginResponse signUp(
			final SignUpRequest signUpRequest
	) {
        // 최초 로그인에서 검증된 oauthId인지 확인
        if (!oAuthSignUpStore.exists(signUpRequest.oauthProvider(), signUpRequest.oauthId())) {
            throw new MemberNotAuthenticatedException();
        }

        // 이미 가입된 회원인지 확인
		if (isAlreadyExistsMember(signUpRequest)) {
			throw new MemberAlreadyExistsException();
		}

		Member member = memberRepository.save(signUpRequest.toMember());
		TokenResponse tokenResponse = generateTokensForMember(member);

        // 가입 완료 후, redis에서 oauthId 제거
        oAuthSignUpStore.delete(signUpRequest.oauthProvider(), signUpRequest.oauthId());

		log.info("회원가입 완료. {} ID: {}", signUpRequest.oauthProvider(), signUpRequest.oauthId());

		return LoginResponse.success(member, tokenResponse, apiKey);
	}

	@Transactional
	public void logout(final String memberId, final String accessToken) {
		if (!jwtParser.getMemberId(resolveToken(accessToken)).equals(memberId)) {
			throw new MemberNotAuthenticatedException();
		}
		// 유저의 모든 refreshToken 제거
		jwtRefreshStore.deleteAllForUser(memberId);

		// 현재 accessToken 차단
		jwtBlacklistManager.add(resolveToken(accessToken));
	}

	@Transactional
	public TokenResponse reissueTokens(final String refreshToken) {
		jwtValidator.verifyToken(refreshToken);

		// 저장된 refreshToken인지 확인
		if (!jwtRefreshStore.exists(refreshToken)) {
			throw new TokenInvalidException();
		}

		Member member = memberRepository.findById(jwtParser.getMemberId(refreshToken))
				.orElseThrow(MemberNotFoundException::new);

		// 사용한 refreshToken 폐기
		jwtRefreshStore.deleteToken(refreshToken);

		// 새로운 access+refresh 토큰 발급
		return generateTokensForMember(member);
	}

	public MyInfoResponse getMyInfo(final String memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);

		return MyInfoResponse.fromEntity(member);
	}

	/**
	 * 새 access+refresh 토큰 발급
	 * + refreshtoken Redis 저장
	 * */
	private TokenResponse generateTokensForMember(Member member) {
		TokenInfo accessTokenInfo = jwtProvider.generateAccessToken(member);
		TokenInfo refreshTokenInfo = jwtProvider.generateRefreshToken(member);

		long refreshTtlSec = Math.max(1,
				(refreshTokenInfo.expiredAt() - System.currentTimeMillis()) / 1000);

		// refresh token redis에 저장
		jwtRefreshStore.save(member.getMemberId(), refreshTokenInfo.token(), refreshTtlSec);

		return TokenResponse.of(accessTokenInfo, refreshTokenInfo);
	}

	@Transactional
	public void updateProfile(final String memberId, final UpdateMemberRequest request) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);

		member.update(request);
	}

	@Transactional
	public void withdraw(final String memberId, final String accessToken, final WithdrawRequest request) {
		if (!jwtParser.getMemberId(resolveToken(accessToken)).equals(memberId)) {
			throw new MemberNotAuthenticatedException();
		}

		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);

		// 탈퇴 이력 저장
		memberWithdrawalRepository.save(request.toEntity());

		// 회원 정보 삭제
		memberRepository.delete(member);

		// 토큰 무효화 (accessToken 블랙리스트 + refreshToken 전부 삭제)
		jwtBlacklistManager.add(resolveToken(accessToken));
		jwtRefreshStore.deleteAllForUser(memberId);
	}

	private boolean isAlreadyExistsMember(final SignUpRequest signUpRequest) {
		if (signUpRequest.oauthId() != null && !signUpRequest.oauthId().isEmpty()) {
			return memberRepository.existsByOauthIdAndOauthProvider(signUpRequest.oauthId(),
					OAuthProvider.of(signUpRequest.oauthProvider()));
		}

		return false;
	}

	private String resolveToken(String accessTokenWithBearer) {
		return accessTokenWithBearer.substring(ACCESS_TOKEN_PREFIX.length());
	}
}