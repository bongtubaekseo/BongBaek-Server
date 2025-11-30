package org.appjam.bongbaek.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.appjam.bongbaek.domain.member.dto.request.LoginRequest;
import org.appjam.bongbaek.domain.member.dto.response.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.request.ReissueRequest;
import org.appjam.bongbaek.domain.member.dto.request.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.request.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.dto.request.WithdrawRequest;
import org.appjam.bongbaek.domain.member.dto.response.MyInfoResponse;
import org.appjam.bongbaek.domain.member.dto.response.TokenResponse;
import org.appjam.bongbaek.domain.member.service.MemberService;
import org.appjam.bongbaek.global.api.code.member.SuccessCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.api.response.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController {

	private final MemberService memberService;

	@PostMapping("/oauth/{oauthProvider}")
	public SuccessResponse<LoginResponse> login(
			@PathVariable(name = "oauthProvider") final String oauthProvider,
			@RequestBody final LoginRequest loginRequest
	) {
		LoginResponse loginResponse = memberService.login(oauthProvider, loginRequest.idToken());

		if (loginResponse.isCompletedSignUp()) {
			return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, loginResponse);
		} else {
			return ApiResponse.success(SuccessCode.SIGN_UP_REQUIRED, loginResponse);
		} // NOTE: 기존 회원과 최초 로그인의 응답을 다르게.
	}

	@PostMapping("/member/profile")
	public SuccessResponse<LoginResponse> signUp(
			@RequestBody final SignUpRequest signUpRequest
	) {
		return ApiResponse.success(SuccessCode.MEMBER_CREATED, memberService.signUp(signUpRequest));
	}

	@PostMapping("/member/logout")
	public SuccessResponse<Void> logout(
			@AuthenticationPrincipal final String memberId,
			@RequestHeader(name = "Authorization") final String accessToken
	) {
		memberService.logout(memberId, accessToken);

		return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS);
	}

	@PostMapping("/member/reissue")
	public SuccessResponse<TokenResponse> reissueTokens(
			@RequestBody final ReissueRequest reissueRequest
	) {
		return ApiResponse.success(SuccessCode.TOKEN_REISSUED,
				memberService.reissueTokens(reissueRequest.refreshToken()));
	}

	@PutMapping(path = "/member/profile")
	public SuccessResponse<Void> updateProfile(
			@AuthenticationPrincipal final String memberId,
			@RequestBody @Valid final UpdateMemberRequest request
	) {
		memberService.updateProfile(memberId, request);

		return ApiResponse.success(SuccessCode.MEMBER_UPDATED);
	}

	@PostMapping("/member/withdraw")
	public SuccessResponse<Void> withdraw(
			@AuthenticationPrincipal final String memberId,
			@RequestHeader(name = "Authorization") final String accessToken,
			@Valid @RequestBody final WithdrawRequest request
	) {
		memberService.withdraw(memberId, accessToken, request);

		return ApiResponse.success(SuccessCode.MEMBER_DELETED);
	}

	@GetMapping(path = "/member/profile")
	public SuccessResponse<MyInfoResponse> getMyInfo(
			@AuthenticationPrincipal final String memberId
	) {
		return ApiResponse.success(SuccessCode.MEMBER_FOUND, memberService.getMyInfo(memberId));
	}
}
