package org.appjam.bongbaek.domain.member.dto.response;

import java.time.LocalDate;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;

public record MyInfoResponse(
    String memberName,
    LocalDate memberBirthday,
    IncomeType memberIncome
) {
    public static MyInfoResponse fromEntity(Member member) {
        return new MyInfoResponse(
            member.getMemberName(),
            member.getMemberBirthday(),
            member.getMemberIncome()
        );
    }
}