package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;

public record UpdateMemberRequest(
        @Schema(description = "회원 이름", example = "김민경")
        String memberName,
        @Schema(description = "회원 생일", example = "2001-02-18")
        LocalDate memberBirthday,
        @Schema(description = "회원 소득", example = "200만원 이상")
        String memberIncome
) {
}