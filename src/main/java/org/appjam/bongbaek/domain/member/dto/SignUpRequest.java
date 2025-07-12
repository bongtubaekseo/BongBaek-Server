package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;

import java.time.LocalDate;

public record SignUpRequest(
        @Schema(description = "kakao Id", example = "12345678")
        Long kakaoId,
        @Schema(description = "apple Id", example = "null")
        String appleId,
        @Schema(description = "회원 이름", example = "김민경")
        String memberName,
        @Schema(description = "회원 생일", example = "2001-02-18")
        String memberBirthday,
        @Schema(description = "회원 소득", example = "200만원 이상")
        String memberIncome
) {
    public Member toMember(LocalDate birthday, IncomeType incomeType) {
        return Member.builder()
                .kakaoId(this.kakaoId)
                .appleId(null)
                .memberName(this.memberName)
                .memberBirthday(birthday)
                .memberIncome(incomeType)
                .build();
    }
}
