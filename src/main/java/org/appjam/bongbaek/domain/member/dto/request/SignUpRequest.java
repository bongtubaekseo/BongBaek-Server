package org.appjam.bongbaek.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.entity.OAuthProvider;

import java.time.LocalDate;

public record SignUpRequest(
        @Schema(description = "소셜로그인 아이디", example = "12345678")
        String oauthId,
        @Schema(description = "소셜로그인 플랫폼 종류")
        String oauthProvider,
        @Schema(description = "회원 이름", example = "김민경")
        String memberName,
        @Schema(description = "회원 생일", example = "2001-02-18")
        LocalDate memberBirthday,
        @Schema(description = "회원 소득", example = "200만원 이상")
        String memberIncome
) {
    public Member toMember() {
        return Member.builder()
                .oauthId(this.oauthId)
                .memberName(this.memberName)
                .memberBirthday(this.memberBirthday)
                .memberIncome(IncomeType.of(this.memberIncome))
                .oauthProvider(OAuthProvider.of(this.oauthProvider))
                .build();
    }
}
