package org.appjam.bongbaek.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.oauth.PrincipalHandler;
import org.appjam.bongbaek.domain.member.dto.LoginRequest;
import org.appjam.bongbaek.domain.member.dto.LoginResponse;
import org.appjam.bongbaek.domain.member.dto.SignUpRequest;
import org.appjam.bongbaek.domain.member.service.MemberService;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonSuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인", description = "카카오 OAuth 인증 코드로 로그인합니다.")
    @PostMapping("/oauth/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody final LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = memberService.login(loginRequest.authorizationCode());

        if (loginResponse.isCompletedSignUp()) {
            return ResponseEntity.ok(ApiResponse.success(CommonSuccessCode.OK, loginResponse));
        } else {
            return ResponseEntity
                    .status(202)
                    .body(ApiResponse.success(CommonSuccessCode.ACCEPTED, loginResponse));
        } // NOTE: 기존 회원과 최초 로그인의 응답을 다르게.
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    @PostMapping("/member/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK, memberService.logout(PrincipalHandler.getMemberIdFromPrincipal())));
    } // TODO: 추후 보완 예정

    @Operation(summary = "회원가입", description = "추가 정보를 입력하여 회원가입을 완료합니다.")
    @PostMapping("/member/profile")
    public ResponseEntity<ApiResponse<LoginResponse>> profile(
            @RequestBody final SignUpRequest signUpRequest
    ) {
        LoginResponse loginResponse = memberService.signUp(signUpRequest);

        return ResponseEntity
                .status(201)
                .body(ApiResponse.success(CommonSuccessCode.SIGNUP_COMPLETED, loginResponse));
    }
}
