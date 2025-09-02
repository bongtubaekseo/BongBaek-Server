package org.appjam.bongbaek.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.member.dto.request.LoginRequest;
import org.appjam.bongbaek.domain.member.dto.request.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.request.ReissueRequest;
import org.appjam.bongbaek.domain.member.dto.request.SignUpRequest;
import org.appjam.bongbaek.domain.member.dto.request.UpdateMemberRequest;
import org.appjam.bongbaek.domain.member.dto.request.WithdrawRequest;
import org.appjam.bongbaek.domain.member.dto.response.MyInfoResponse;
import org.appjam.bongbaek.domain.member.service.MemberService;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.api.ApiResponse.EmptyBody;
import org.appjam.bongbaek.global.common.CommonSuccessCode;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰으로 로그인합니다.")
    @PostMapping("/oauth/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody final LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = memberService.login(loginRequest.accessToken());

        if (loginResponse.isCompletedSignUp()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CommonSuccessCode.OK, loginResponse));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ApiResponse.success(CommonSuccessCode.ACCEPTED, loginResponse));
        } // NOTE: 기존 회원과 최초 로그인의 응답을 다르게.
    }

    @Operation(summary = "회원가입", description = "추가 정보를 입력하여 회원가입을 완료합니다.")
    @PostMapping("/member/profile")
    public ResponseEntity<ApiResponse<LoginResponse>> profile(
            @RequestBody final SignUpRequest signUpRequest
    ) {
        LoginResponse loginResponse = memberService.signUp(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(CommonSuccessCode.SIGNUP_COMPLETED, loginResponse));
    }

    @Operation(summary = "로그아웃", description = "현재 사용자의 모든 리프레시 토큰을 무효화합니다.")
    @PostMapping("/member/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        @RequestHeader("Authorization") String accessToken
    ) {
        memberService.logout(accessToken);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success(CommonSuccessCode.OK, null));
    }


    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.")
    @PostMapping("/member/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissueTokens(
            @RequestBody final ReissueRequest reissueRequest
    ) {
        TokenResponse tokenResponse = memberService.reissueTokens(reissueRequest.refreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.TOKEN_REISSUED, tokenResponse));
    }

    @Operation(summary = "회원 프로필 수정", description = "이름/생일/소득을 전달하여 프로필을 전체 교체합니다.")
    @PutMapping(path = "/member/profile")
    public ResponseEntity<ApiResponse<EmptyBody>> updateProfile(
        @AuthenticationPrincipal final String memberId,
        @RequestBody @Valid final UpdateMemberRequest request
    ) {
        memberService.updateProfile(memberId, request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(CommonSuccessCode.OK, null));
    }

    @Operation(
        summary = "회원 탈퇴",
        description = """
        사전 정의 사유(INCONVENIENT/PRIVACY_CONCERN/RARELY_USED/BUG_OR_ERROR/NEW_ACCOUNT/OTHER)로 탈퇴합니다.
        - reason: 필수
        - reason=OTHER일 때 detail: 1~50자 필수
        - 그 외 사유일 때 detail: null 필수
        """
    )
    @PostMapping("/member/withdraw")
    public ResponseEntity<ApiResponse<EmptyBody>> withdraw(
        @RequestHeader(value = "Authorization") final String accessToken,
        @AuthenticationPrincipal final String memberId,
        @Valid @RequestBody final WithdrawRequest request
    ) {
        memberService.withdraw(accessToken, memberId, request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(CommonSuccessCode.OK, null));
    }

    @Operation(summary = "마이페이지 회원 정보 조회", description = "마이페이지 속 이름/생일/소득을 조회합니다. 소득 구간 OVER200: 월 소득 200만원 이상, UNDER200: 월 소득 200만원 미만, NONE: 없음")
    @GetMapping(path = "/member/profile")
    public ResponseEntity<ApiResponse<MyInfoResponse>> getMyInfo(
        @AuthenticationPrincipal final String memberId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(CommonSuccessCode.OK, memberService.getMyInfo(memberId)));
    }
}