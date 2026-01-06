package org.appjam.bongbaek.domain.member.controller;

import org.appjam.bongbaek.domain.member.dto.request.LoginRequest;
import org.appjam.bongbaek.domain.member.dto.request.ReissueRequest;
import org.appjam.bongbaek.domain.member.dto.request.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.request.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.dto.request.WithdrawRequest;
import org.appjam.bongbaek.global.api.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원", description = "회원 관련 API")
public interface MemberController {
	@Operation(summary = "소셜 로그인", description = "소셜 로그인 플랫폼 정보와 아이디 토큰으로 로그인합니다.")
	ApiResponse login(String oauthProvider, LoginRequest loginRequest);

	@Operation(summary = "회원가입", description = "추가 정보를 입력하여 회원가입을 완료합니다.")
	ApiResponse signUp(SignUpRequest signUpRequest);

	@Operation(summary = "로그아웃", description = "현재 사용자의 모든 리프레시 토큰을 무효화합니다.")
	ApiResponse logout(String memberId, String accessToken);

	@Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.")
	ApiResponse reissueTokens(ReissueRequest reissueRequest);

	@Operation(summary = "회원 프로필 수정", description = "이름/생일/소득을 전달하여 프로필을 전체 교체합니다.")
	ApiResponse updateProfile(String memberId, UpdateMemberRequest request);

	@Operation(
			summary = "회원 탈퇴",
			description = """
					사전 정의 사유(INCONVENIENT/PRIVACY_CONCERN/RARELY_USED/BUG_OR_ERROR/NEW_ACCOUNT/OTHER)로 탈퇴합니다.
					- reason: 필수
					- reason=OTHER일 때 detail: 1~50자 필수
					- 그 외 사유일 때 detail: null 필수
					"""
	)
	ApiResponse withdraw(String memberId, String accessToken, WithdrawRequest request);

	@Operation(summary = "마이페이지 회원 정보 조회", description = "마이페이지 속 이름/생일/소득을 조회합니다. 소득 구간 OVER200: 월 소득 200만원 이상, UNDER200: 월 소득 200만원 미만, NONE: 없음")
	ApiResponse getMyInfo(String memberId);

}
