package org.appjam.bongbaek.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.member.dto.LoginRequest;
import org.appjam.bongbaek.domain.member.dto.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.SignUpRequest;
import org.appjam.bongbaek.domain.member.service.MemberService;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonSuccessCode;
import org.appjam.bongbaek.global.jwt.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.")
    @PostMapping("/member/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissueTokens(
            @RequestHeader("refreshToken") final String refreshToken
    ) {
        TokenResponse tokenResponse = memberService.reissueTokens(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.TOKEN_REISSUED, tokenResponse));
    }
}
